# Ingress

## kind 내용
> https://kind.sigs.k8s.io/docs/user/ingress/  
> kind cluster에서 ingress를 설정하는 방법에 대해 알아본다.

클러스터를 생성 할 때 KIND의 extraPortMapping 구성 옵션을 활용하여 호스트에서 노드에서 실행중인 ingress 컨트롤러로 포트를 전달할 수 있다.  
또한, Ingress 컨트롤러 `nodeSelector`에서 사용할 kubeadm `InitConfiguration`의 node-labels를 통해 커스텀 노드 레이블을 설정할 수 있다.  

1. 클러스터를 생성
2. Ingress controller 배포한다. Ingress Controller 다음과 같은 역할을 한다.
   * Ambassador
   * Contour
   * Ingress NGINX

### 클러스터 생성
Kind 클러스터를 생성한다. `extraPortMappings`와  `node-labels` 이용
* `extraPortMappings` :  localhost가 80 443을 통해 Ingress controller 요청을 생성할 수 있게 한다.
* `node-labels` : label selector와 일치하는 특정 노드에서만 수신 컨트롤러를 실행하도록 허용한다.

기존에 있는 cluster를 먼저 삭제
```
$ kind delete cluster
```

```
cat <<EOF | kind create cluster --config=-
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
nodes:
- role: control-plane
  kubeadmConfigPatches:
  - |
    kind: InitConfiguration
    nodeRegistration:
      kubeletExtraArgs:
        node-labels: "ingress-ready=true"
  extraPortMappings:
  - containerPort: 80
    hostPort: 80
    protocol: TCP
  - containerPort: 443
    hostPort: 443
    protocol: TCP
EOF
```

### Ambassador
Ambassador 는 Ambassador operator를 통해 설치 된다.  
설치 🎹🎹  
`CRDs 설치`  
`$ kubectl apply -f https://github.com/datawire/ambassador-operator/releases/latest/download/ambassador-operator-crds.yaml`
```
$ kubectl apply -f https://github.com/datawire/ambassador-operator/releases/latest/download/ambassador-operator-crds.yaml
Warning: apiextensions.k8s.io/v1beta1 CustomResourceDefinition is deprecated in v1.16+, unavailable in v1.22+; use apiextensions.k8s.io/v1 CustomResourceDefinition
customresourcedefinition.apiextensions.k8s.io/ambassadorinstallations.getambassador.io unchanged
```
`ambassador` namesapce 에서 operator를 사용하여 Ambassador 를 설치하기 위한 kind 특화 manifest를 설치한다. 

`$ kubectl apply -n ambassador -f https://github.com/datawire/ambassador-operator/releases/latest/download/ambassador-operator-kind.yaml`  
`$ kubectl wait --timeout=180s -n ambassador --for=condition=deployed ambassadorinstallations/ambassador`
```
$ kubectl apply -n ambassador -f https://github.com/datawire/ambassador-operator/releases/latest/download/ambassador-operator-kind.yaml
namespace/ambassador created
configmap/static-helm-values created
serviceaccount/ambassador-operator created
clusterrole.rbac.authorization.k8s.io/ambassador-operator-cluster created
clusterrolebinding.rbac.authorization.k8s.io/ambassador-operator-cluster created
role.rbac.authorization.k8s.io/ambassador-operator created
rolebinding.rbac.authorization.k8s.io/ambassador-operator created
deployment.apps/ambassador-operator created
ambassadorinstallation.getambassador.io/ambassador created

$ kubectl wait --timeout=180s -n ambassador --for=condition=deployed ambassadorinstallations/ambassador
ambassador
ambassadorinstallation.getambassador.io/ambassador condition met
```

Ambassador 를 사용할 준비가 되었다. Ingress [사용법 예시](https://kind.sigs.k8s.io/docs/user/ingress/#using-ingress)를 실행할 수 있다. 하지만 Ambassador는 정의된 `Ingress`를 자동으로 로드하지는 않는다.  
`Ingress` 리소스는 Ambassador를 인지하기 위하여 `kubernetes.io/ingress.class: ambassador` 어노테이션을 반드시 포함해야한다 ( 그러지 않으면 무시된다 ).  
그래서 예시가 로드되면 어노테이션을 추가해준다.
```
kubectl annotate ingress example-ingress kubernetes.io/ingress.class=ambassador
``` 
그러면 Ambassoador는 Ingress를 노출해야한다. 추가적인 내용은 [여기](https://www.getambassador.io/docs/latest/)에서 확인

### Contour
[Contour](https://projectcontour.io/) 는 k8s를 위한 high performance ingress controller 이다.    
> Countour는 Envoy edge와 서비스 프록시를 제공하기 위한 오픈소스 쿠버네티스 인그레스 컨트롤러이다.
> Countour는 가벼운 profile를 유지하면서 동적 설정 업데이트 및 다중 팀 ingress 위임 ( delegation ) 을 즉시 지원한다.  


[Contour components](https://projectcontour.io/quickstart/contour.yaml)를 배포한다.
```
$ kubectl apply -f https://projectcontour.io/quickstart/contour.yaml

namespace/projectcontour created
serviceaccount/contour created
serviceaccount/envoy created
configmap/contour created
customresourcedefinition.apiextensions.k8s.io/extensionservices.projectcontour.io created
customresourcedefinition.apiextensions.k8s.io/httpproxies.projectcontour.io created
customresourcedefinition.apiextensions.k8s.io/tlscertificatedelegations.projectcontour.io created
serviceaccount/contour-certgen created
rolebinding.rbac.authorization.k8s.io/contour created
role.rbac.authorization.k8s.io/contour-certgen created
job.batch/contour-certgen-v1.11.0 created
clusterrolebinding.rbac.authorization.k8s.io/contour created
clusterrole.rbac.authorization.k8s.io/contour created
service/contour created
service/envoy created
deployment.apps/contour created
daemonset.apps/envoy created
```
kind 특정 패치를 적용하여 hostPort를 ingress controller 로 전달하고, taint tolerations을 설정하고 커스텀 레이블 노드를 스케쥴 한다.
```
{
  "spec": {
    "template": {
      "spec": {
        "nodeSelector": {
          "ingress-ready": "true"
        },
        "tolerations": [
        {
          "key": "node-role.kubernetes.io/master",
          "operator": "Equal",
          "effect": "NoSchedule"
        }
        ]
      }
    }
  }
}
```
위를 아래 명령어로 적용시켜준다.
```
kubectl patch daemonsets -n projectcontour envoy -p '{"spec":{"template":{"spec":{"nodeSelector":{"ingress-ready":"true"},"tolerations":[{"key":"node-role.kubernetes.io/master","operator":"Equal","effect":"NoSchedule"}]}}}}'
```

Contour를 사용할 준비가 되었다. [Using Ingress 기본 예제](https://kind.sigs.k8s.io/docs/user/ingress/#using-ingress)를 참고  
추가적인 Contour의 정보 :  [projectcontour.io](https://projectcontour.io/)

### Ingress NGINX
```
$ kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/provider/kind/deploy.yaml

namespace/ingress-nginx created
serviceaccount/ingress-nginx created
configmap/ingress-nginx-controller created
clusterrole.rbac.authorization.k8s.io/ingress-nginx created
clusterrolebinding.rbac.authorization.k8s.io/ingress-nginx created
role.rbac.authorization.k8s.io/ingress-nginx created
rolebinding.rbac.authorization.k8s.io/ingress-nginx created
service/ingress-nginx-controller-admission created
service/ingress-nginx-controller created
deployment.apps/ingress-nginx-controller created
validatingwebhookconfiguration.admissionregistration.k8s.io/ingress-nginx-admission created
serviceaccount/ingress-nginx-admission created
clusterrole.rbac.authorization.k8s.io/ingress-nginx-admission created
clusterrolebinding.rbac.authorization.k8s.io/ingress-nginx-admission created
role.rbac.authorization.k8s.io/ingress-nginx-admission created
rolebinding.rbac.authorization.k8s.io/ingress-nginx-admission created
job.batch/ingress-nginx-admission-create created
job.batch/ingress-nginx-admission-patch created
```
menifest는 hostPorts를 ingress controller 로 전달하기 위한 kind에 특화된 패치를 포함하고, taint tolerations을 설정하고 커스텀 labelled 노드에 스케쥴을 한다.    

Ingress가 모두 설정되었다. process 요청이 동작할 때 까지 기다린다.
```
$ kubectl wait --namespace ingress-nginx \
    --for=condition=ready pod \
    --selector=app.kubernetes.io/component=controller \
    --timeout=90s
```
