# LoadBalancer

## kind에서 loadbalancer
> https://kind.sigs.k8s.io/docs/user/loadbalancer/  
> [Metallb](https://metallb.universe.tf/)을 사용하여 kind cluster에 서비스 타입의 LoadBalancer 를 어떻게 동작 시키는지 알아본다.  
> 이 가이드는 metallb 설치 문서를 보완하고 layer2 프로토콜을 사용하여 metallb를 설정한다.  
> 프로토콜에 대한 더 자세한 내용은 metallb [설정 문서](https://metallb.universe.tf/configuration/)를 참고  
>   
> 리눅스에서 Docker를 사용하면, IP space가 docker IP space에 있을 경우, loadbalancer의 외부 IP로 직접적으로 트래픽을 보낼 수 있다.  
>   
> 그외 macOS와 Windows 에서는 도커가 호스트로 도커 네트워크를 노출시킬 수 없다 ... 라는 이야기가 아래에 있음  
> On macOS and Windows, docker does not expose the docker network to the host. Because of this limitation, containers (including kind nodes) are only reachable from the host via port-forwards, however other containers/pods can reach other things running in docker including loadbalancers. You may want to check out the Ingress Guide as a cross-platform workaround. You can also expose pods and services using extra port mappings as shown in the extra port mappings section of the Configuration Guide.


### Installing metallb using default manifests
Create the metallb namespace
```
$ kubectl apply -f https://raw.githubusercontent.com/metallb/metallb/master/manifests/namespace.yaml

namespace/metallb-system created
```
Create the memberlist secrets
```
$ kubectl create secret generic -n metallb-system memberlist --from-literal=secretkey="$(openssl rand -base64 128)" 

secret/memberlist created
```
Apply metallb manifest
```
$ kubectl apply -f https://raw.githubusercontent.com/metallb/metallb/master/manifests/metallb.yaml

podsecuritypolicy.policy/controller created
podsecuritypolicy.policy/speaker created
serviceaccount/controller created
serviceaccount/speaker created
clusterrole.rbac.authorization.k8s.io/metallb-system:controller created
clusterrole.rbac.authorization.k8s.io/metallb-system:speaker created
role.rbac.authorization.k8s.io/config-watcher created
role.rbac.authorization.k8s.io/pod-lister created
clusterrolebinding.rbac.authorization.k8s.io/metallb-system:controller created
clusterrolebinding.rbac.authorization.k8s.io/metallb-system:speaker created
rolebinding.rbac.authorization.k8s.io/config-watcher created
rolebinding.rbac.authorization.k8s.io/pod-lister created
daemonset.apps/speaker created
deployment.apps/controller created

```
metallb pods 가 정상적으로 동작할 때 까지 기다린다
```
$ kubectl get pods -n metallb-system --watch
```

loadbalancer를 사용하여 address pool 설정
layer2 설정을 완료하기 위해서, metallb이 제어(control)하는 IP 주소 범위를  제공해야 한다.  
docker kind network에 이 범위가 있기를 원한다
```
$ docker network inspect -f '{{.IPAM.Config}}' kind

[{172.19.0.0/16  172.19.0.1 map[]} {fc00:f853:ccd:e793::/64  fc00:f853:ccd:e793::1 map[]}]
```
결과값은 172.19.0.0/16과 같은 CIDR을 포함한다. 이 서브클래스로부터 loadbalancer IP 범위를 가지길 바란다.  
예를 들어, configmap을 만들어서 172.19.255.200 부터 172.19.255.250 의 범위를 가지는 metallb 설정을 할 수 있다.
```
$ kubectl apply -f https://kind.sigs.k8s.io/examples/loadbalancer/metallb-configmap.yaml

configmap/config created
```
LoadBalancer 사용하기
아래 예시는 두 http-echo pods ( 하나는 foo, 다른 하나는 bar를 출력하는 서비스 )를 라우트하는 loadbalancer 서비스를 생성하는 것이다.
```
$ kubectl apply -f https://kind.sigs.k8s.io/examples/loadbalancer/usage.yaml
```
외부 IP와 포트로 트래픽을 전송할 때 로드밸런서가 정상 동작하는지 알아본다.
```
LB_IP=$(kubectl get svc/foo-service -o=jsonpath='{.status.loadBalancer.ingress[0].ip}')
```

