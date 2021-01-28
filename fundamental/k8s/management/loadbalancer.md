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
```
