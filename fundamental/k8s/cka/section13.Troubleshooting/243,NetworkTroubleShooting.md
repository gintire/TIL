# Network Troubleshooting
## Network Plugin in kubernetes
kubelet은 kubelet 설정에 아래 설정에 설정된 파라미터에 따른 CNI 플러그인을 사용한다.
* -cni-bin-dir : 쿠버네티스는 시작시 플러그인에 대해 이 디렉터리를 조사한다.
* -network-plugin : cni-bin-dir에서 사용하는 네트워크 플러그인. 반드시 플러그인 디렉터리에서 검색된 플러그인과 이름이 같아야만 한다.

### 1. Weave Net

쿠버네티스 도큐먼트에서 유일하게 언급된 플러그인

설치방법 : `kubectl apply -f "https://cloud.weave.works/k8s/net?k8s-version=$(kubectl version | base64 | tr -d '\n')"`

참고 : https://kubernetes.io/docs/setup/production-environment/tools/kubeadm/high-availability/

### 2. Flannel

설치 방법 : `kubectl apply -f ㅗttps://raw.githubusercontent.com/coreos/flannel/2140ac876ef134e0ed5af15c65e414cf26827915/Documentation/kube-flannel.yml`

Note: As of now flannel does not support kubernetes network policies.

### 3. Calico

설치 방법 : `curl https://docs.projectcalico.org/manifests/calico.yaml -O`

Apply the manifest using the following command : `kubectl apply -f calico.yaml`

   Calico is said to have most advanced cni network plugin.



### Tip
**Note: If there are multiple CNI configuration files in the directory, the kubelet uses the configuration file that comes first by name in lexicographic order.**

## DNS in Kubernetes
쿠버네티스는 CoreDNS를 사용한다. CoreDNS는 쿠버네티스 클러스터 DNS 역할을 할 수 있는 유연하고 확장 가능한 DNS 서버이다.

### Memory and Pods
큰 규모의 쿠버네티스 클러스터에서, CoreDNS의 메모리 사용은 클러스터의 파드와 서비스의 수에 주로 영향을 받는다.

다른 요인으로는 채워진 DNS 응답 캐시의 크기와 CoreDNS 인스턴스 당 수신된 쿼리 속도(QPS)가 있다.

쿠버네티스의 coreDNS 자원들:
1. coreDNS라는 서비스 계정
2. coredns와 kube-dns라는 cluster-roles
3. coredns와 kube-dns라는 clusterrolebindings
4. coredns라는 deployment
5. coredns라는 configmap
6. kube-dns 서비스

coreDNS deployment를 분석하는 동안 `corefile플러그인`이 `configmap`으로 정의된 중요한 구성으로 구성되어 있음을 알 수 있다.

포트 53은 DNS 솔루션을 위해 사용된다.

```
    kubernetes cluster.local in-addr.arpa ip6.arpa {
       pods insecure
       fallthrough in-addr.arpa ip6.arpa
       ttl 30
    }
```

### k8s backend - cluster.local and reverse domains

`proxy . /etc/resolve.conf`

클러스터 도메인 외부에서 권한있는 DNS 서버로 직접 전달한다.

## coreDNS와 연관된 Troubleshooting issues 
1. Pending 상태의 `CoreDNS`파드를 발견하면 먼저 네트워크 플러그인이 설치되었는지 확인한다.
2. coredns 파드는 `CrashLoopBackOff` 또는 `Error` 상태를 가진다.
예전 버전의 도커를 사용하는 SELinux에 노드를 운영할 경우, coredns 파드가 동작하지 않는 것을 경험할 것이다.
이것을 해결하기 위해서 아래중 하나를 수행한다.
    1. 도커 버전을 업그레이드 한다.
    2. SELinux를  disable처리한다.
    3. coredns deployment 에서 `allowPrivilegeEscalation`을  true로 설정한다.
    ```
        kubectl -n kube-system get deployment coredns -o yaml | \
         sed 's/allowPrivilegeEscalation: false/allowPrivilegeEscalation: true/g' | \
         kubectl apply -f -
    ```
    4. 또 다른 이유는 CoreDNS가 `CrashLoopBackOff`로 되는 이유는 Kubernetes에 배포된 CoreDNS포드가 루프를 감지하는 경우이다.  
    이를 해결하는 방법에는 여러가지가 있는데 일부는 여기에 있다.
    * 다음을 kubelet config yaml에 추가한다. : `resolveConf: <path-to-your-real-resolv-conf-file`  
    이 flag는 `kubelet`에 대체의 `resulv.conf`를 pod에 넘겨줘라고 명령한다.   
    시스템에서 `systemd-resolved, /run/systemd/resolve/resolv.conf` 가 보통 "real" `resolv.conf`에 위치한다.
    * 호스트 노드의 local DNS 캐시 사용을 disable 처리한다. 그리고 `/etc/resolv.conf`를 원본으로 복원시킨다.
    * 빠르게 고치는 방법은 Corefile을 수정하는 방법이다. `/etc/resolv.conf` DNS 업스트림의 IP주소를 수정한다. 예를 들면 `8.8.8.8`  
    하지만 이것은 CoreDNS에서만 이슈를 수정하는 것이다. 기본 dnsPolicy를 가진 Pods의 `kubelet`은 계속해서 부정확한 `resolv.conf`를 바라보게 된다.
3. `coreDNS` 파드와 `kube-dns`서비스가 잘 동작하고 있다면, `kube-dns` 서비스가 적절한 `endpoints`를 가지고 있는지 확인한다.
    ```
    kubectl -n kube-system get ep kube-dns
    ```
    서비스의 endpoint가 없을 경우, service를 inspect하여 적절한 selector와 port를 가지는지 확인한다.

## Kube-proxy
kube-proxy는 클러스터에서 각 노드에서 동작중인 네트워크 프록시이다. `kube-proxy`는 노드에서 네트워크 룰을 유지한다.  
이 네트워크 룰은 클러스터의 내 외부의 네트워크 세션에서 Pod로 네트워크 통신을 허용한다.

`kubeadm`으로 설정된 클러스터에서 `kube-proxy`를 `daemonset`으로 찾을 수 있다.

`kubeproxy`는 서비스와 각 서비스에 연관된 endpoint를 바라본다. 클라이언트가 virtualIP를 사용하여 서버에 접속하려하면 `kubeproxy`는 실제 pod로 트래픽을 보내주는 역할을 한다.

만약 `kubectl describe ds kube-proxy -n kube-system` 명령을 하면 `kube-proxy` 바이너리가 `kube-proxy conatainer`내부에서 다음 명령으로 동작하는 것을 볼 수 있다.
```
 Command:
      /usr/local/bin/kube-proxy
      --config=/var/lib/kube-proxy/config.conf
      --hostname-override=$(NODE_NAME)
```

그래서 구성파일 (예를 들면 > `/var/lib/kube-proxy/config.conf`) 에서 구성을 가져오며 포드가 실행중인 노드 이름으로 호스트 이름을 재정의할 수 있다.

config file에서 다음을 정의한다. clusterCIDR, kubeproxy mode, ipvs, iptables, bindaddress, kube-config etc.

## Kube-proxy와 연관된 이슈에 대한 트러블 슈팅
1. kube-system 네임스페이스의 `kube-proxy` 파드를 확인한다.
2. `kube-proxy`로그를 확인한다.
3. `configmap`이 제대로 정의되었고, `kube-proxy`바이너리를 실행하기 위한 config file이 올바른지 확인한다.
4. `kube-config`는 `config map`에 정의되어 있다.
5. `kube-proxy`가 컨테이너 내부에 동작중인지 확인
```
# netstat -plan | grep kube-proxy
tcp        0      0 0.0.0.0:30081           0.0.0.0:*               LISTEN      1/kube-proxy
tcp        0      0 127.0.0.1:10249         0.0.0.0:*               LISTEN      1/kube-proxy
tcp        0      0 172.17.0.12:33706       172.17.0.12:6443        ESTABLISHED 1/kube-proxy
tcp6       0      0 :::10256                :::* 
```

참고자료
* 서비스 : https://kubernetes.io/docs/tasks/debug-application-cluster/debug-service/
* DNS Troubleshooting: https://kubernetes.io/docs/tasks/administer-cluster/dns-debugging-resolution/
