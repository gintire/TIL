# Kubeadm으로 쿠버네티스 클러스터 만들기
문서 참고 : https://jonnung.dev/kubernetes/2020/03/07/create-kubernetes-cluster-using-kubeadm-on-aws-ec2-part2/

참고 : [요소 및 용어 정리](../../k8s/study/용어정리.md)  

## 컨테이너 런타임 환경 갖추기 ( Docker ...)
##  Kubeadm 설치하기
### 서버 요구 사항
* CPU 2코어, RAM 2GB 이상
* 클러스터 내 모든 노드간 네트워크 통신 가능
* 고유한 Hostname, MAC 주소, product_uuid
  * `ip link`
  * `sudo cat /sys/class/dmi/id/product_uuid`
* swap을 사용하지 않는다.
  * `swapoff -a` : swap 기능 끔
  * `echo 0 > /proc/sys/vm/swappiness` : 커널 속성을 변경해 swap을 disable
  * `sed -e '/swap/ s/^#*/#/' -i /etc/fstab` : Swap 을 하는 파일 시스템을 찾아 주석 처리
* 쿠버네티스 구성 요소가 사용하는 포트에 대한 방화벽 오픈
  * 컨트롤 플레인
    * TCP - Inbound - 6443: Kubernetes API Server (used by All)
    * TCP - Inbound - 2379~2380: Etcd server client API (used by kube-apiserver, etcd)
    * TCP - Inbound - 10250: Kubelet API (used by Self, Control plane)
    * TCP - Inbound - 10251: kube-scheduler (used by Self)
    * TCP - Inbound - 10252: kube-controller-manager (used by Self)
  * 워커 노드
    * TCP - Inbound - 10250: Kubelet API (used by Self, Control plane)
    * TCP - Inbound - 30000~32767: NodePort Services (used by All)

## Kubeadm, Kuebelet, Kubectl 설치
ssh로 접속
```
## 마스터 노드
ssh -i "k8s.id_rsa" ubuntu@<instance_id>.compute.amazonaws.com
```

```
sudo apt-get update && sudo apt-get install -y apt-transport-https curl

curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -

cat <<EOF | sudo tee /etc/apt/sources.list.d/kubernetes.list
deb https://apt.kubernetes.io/ kubernetes-xenial main
EOF

sudo apt-get update
sudo apt-get install -y kubelet kubeadm kubectl

# 패키지 버전 홀드 ( 업데이트에서 제외 )
sudo apt-mark hold kubelet kubeadm kubectl 
```
설치가 완료 되었다면 `kubeadm`, `kubectl` 버전을 확인해본다.
```
$ kubeadm version
kubeadm version: &version.Info{Major:"1", Minor:"17", GitVersion:"v1.17.3", GitCommit:"06ad960bfd03b39c8310aaf92d1e7c12ce618213", GitTreeState:"clean", BuildDate:"2020-02-11T18:12:12Z", GoVersion:"go1.13.6", Compiler:"gc", Platform:"linux/amd64"}

$ kubectl version
Client Version: version.Info{Major:"1", Minor:"17", GitVersion:"v1.17.3", GitCommit:"06ad960bfd03b39c8310aaf92d1e7c12ce618213", GitTreeState:"clean", BuildDate:"2020-02-11T18:14:22Z", GoVersion:"go1.13.6", Compiler:"gc", Platform:"linux/amd64"}
```

## 마스터 노드 생성
AWS 프리티어를 이용해서 `t2.micro` 타입의 EC2 인스턴슬르 만들었다.  
`t2.micro` 인스턴스 타입은 vCPU 1이기 때문에 클러스터 최소 요구 사항을 만족하지 않는다. 하지만 Production 환경이 아니기 때문에 조금 무리해서라도 해보기로 한다.  
쿠버네티스 마스터 노드를 초기화 하는 명령어를 실행한다.  
`--apiserver-advertise-address` 파라미터는 다른 노드가 마스터 노드에 접근할 수 있는 IP 주소를 명시한다.  
`--pod-network-cide` 파라미터는 쿠버네티스에서 사용할 컨테이너의 네트워크 대역을 지정한다. 실제 서버에 할당된 IP와 중복되지 않도록 해야 한다.  
다음 단계에서 진행할 네트워크 플러그인 설치 과정에서 Calico를 설치할 계획이라 CIDR 범위를 `192.168.0.0/16`로 지정했다.  
만약 Flannel을 사용한다면 `10.244.0.0/16`을 사용해야 한다.  
`--apiserver-cert-extra-sans` 파라미터도 중요하다. 이 값에는 쿠버네티스가 생성한 TLS 인증서에 적용할 IP 또는 도메인을 명시할 수 있다.  
만약 개발자 로컬 환경에서 `kubectl`을 통해 이 클러스터에 접근하려면 `kube-apiserver`와 통신할 수 있어야 하기 때문에 마스터 노드가 실행되고 있는 EC2 인스턴스의 퍼블릭 IP 주소를 추가해야 한다.
```
kubeadm init \
    --apiserver-advertise-address=0.0.0.0 \
    --pod-network-cidr=192.168.0.0/16 \
    --apiserver-cert-extra-sans=10.1.1.10,3.**<실제 퍼블릭 IP>.68.159
```
위 명령어를 실행하면 아래와 같은 WARNING 과 ERROR 가 보일 것이다.
```
[init] Using Kubernetes version: v1.20.2-agent     software-properties-commonubuntu/gpg | sudo apt-key add -
[preflight] Running pre-flight checks
        [WARNING IsDockerSystemdCheck]: detected "cgroupfs" as the Docker cgroup driver. The recommended driver is "systemd". Please follow the guide at https://kubernetes.io/docs/setup/cri/
        [WARNING SystemVerification]: this Docker version is not on the list of validated versions: 20.10.3. Latest validated version: 19.03
error execution phase preflight: [preflight] Some fatal errors occurred:
        [ERROR NumCPU]: the number of available CPUs 1 is less than the required 2
        [ERROR Mem]: the system RAM (978 MB) is less than the minimum 1700 MB
[preflight] If you know what you are doing, you can make a check non-fatal with `--ignore-preflight-errors=...`
To see the stack trace of this error execute with --v=5 or higher
```
WARNING 메시지에서 etected "cgroupfs" as the Docker cgroup driver. The recommended driver is "systemd". 라며 Docker가 사용하는 Cgroup(Control Group) 드라이버를 systemd로 바꾸는 것을 권장하고 있다.  
Cgroup은 프로세스에 할당된 리소스를 제한하는데 사용된다. Ubuntu는 init 시스템으로 systemd를 사용하고 있고 systemd가 Cgroup관리자로써 작동하게 된다.  
그런데 Docker 가 사용하는 Cgroup 관리자가 `cgroupfs`인 경우 리소스가 부족할 때 시스템이 불안정해지는 경우가 있다고 한다. 단일 Cgroup관리자가 일관성 있게 리소스를 관리하도록 단순화 하는 것이 좋다고 한다.
자세한 내용은 [쿠버네티스 공식 문서](https://kubernetes.io/ko/docs/setup/production-environment/#cgroup-%EB%93%9C%EB%9D%BC%EC%9D%B4%EB%B2%84)를 확인

```
# Docker가 사용하는 Cgroup driver 확인하기
$ docker info | grep -i cgroup
WARNING: No swap limit support
 Cgroup Driver: cgroupfs
```
Docker 설정에 Cgroup driver를 바꾸려면 `/lib/systemd/system/docker.service`을 열어서 아래 구문을 찾아 `--exec-opt native.cgroupdriver=systemd` 파라미터를 추가한 뒤 저장한다.
```
ExecStart=/usr/bin/dockerd -H fd:// --containerd=/run/containerd/containerd.sock --exec-opt native.cgroupdriver=systemd
```
systemd를 리로드하고 도커를 재시작한다.
```
systemctl daemon-reload
systemctl restart docker
```
ERROR 메시지에서는 쿠버네티스가 권장 CPU 개수 2개보다 현재 시스템이 가진 CPU 개수가 적어서 발생한 에러다. 실습을 위해 AWS 프리 티어를 이용해 만든 EC2(t2.micro)라서 어쩔 수 없다. 이 오류를 무시하는 옵션을 추가한다.
```
kubeadm init \
    --apiserver-advertise-address=0.0.0.0 \
    --pod-network-cidr=192.168.0.0/16 \
    --apiserver-cert-extra-sans=10.1.1.10,3.**<실제 퍼블릭 IP>.68.159 \
    --ignore-preflight-errors=NumCPU
```

`t2.micro`로 EC2 인스턴스를 생성하면 k8s cluster를 만드는데 메모리가 부족할 수 있다. 다음은 위 명령어를 사용했을 때 발생할 수 있는, 에러메시지이다.
```
[init] Using Kubernetes version: v1.20.2
[preflight] Running pre-flight checks
        [WARNING NumCPU]: the number of available CPUs 1 is less than the required 2
        [WARNING SystemVerification]: this Docker version is not on the list of validated versions: 20.10.3. Latest validated version: 19.03
error execution phase preflight: [preflight] Some fatal errors occurred:
        [ERROR Mem]: the system RAM (978 MB) is less than the minimum 1700 MB
[preflight] If you know what you are doing, you can make a check non-fatal with `--ignore-preflight-errors=...`
To see the stack trace of this error execute with --v=5 or higher
```
