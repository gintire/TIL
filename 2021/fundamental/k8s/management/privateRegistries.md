# Private Registries
목표 : 인증을 요구하는 이미지 저장소를 kind로 어떻게 사용하는지 연구
## 목록
1. Use ImagePullSecrets
2. Pull to the Host and Side-Load
3. Add Credentials to the Nodes
    * Mount a Config File to Each Node

## Use ImagePullSecrets
쿠버네티스는 이미지를 pulling하기 위하여 `imagePullSecrets`를 사용하여 configuring pods를 지원한다.  
가능하다면, 제일 선호되고 가장 이식 가능한 경로이다.  

이에 대한 [업스트림 쿠버네티스 문서](https://kubernetes.io/docs/concepts/containers/images/#specifying-imagepullsecrets-on-a-pod) 는 링크를 확인, kind는 이를 대해 특별한 처리는 없다.  

구성 파일이 이미 로컬에 있지만 여전히 보안 secrets를 사용할며녀, 쿠버네티스 문서에서 [파일에서 secret를 만드는 방법](https://kubernetes.io/docs/tasks/configure-pod-container/pull-image-private-registry/#registry-secret-existing-credentials) 을 읽어보기  

## Pull to the Host and Side-Load
kind는 `kind load ...`명령어로  host로 부터 [이미지를 불러올 수 있다](https://kind.sigs.k8s.io/docs/user/quick-start/#loading-an-image-into-your-cluster) . 만약 자격 증명을 사용하여 호스트를 구성하여 원하는 이미지를 가져온 다음 노드에 로드하면 노드에서 인증할 필요가 없다.

## Add Credentials to the Nodes
일반적으로 [private 저장소를 사용](https://kubernetes.io/docs/concepts/containers/images/#using-a-private-registry) 에 대한 업스트림 문서가 적용되면, kind에는 두가지 옵션이 있다.

### Mount a Config File to Each Node
만약 이미 생성된 docker config.json 이 host에 대한 자격 증명을 포함하고 있다면, 이것을 각 kind 노드에 마운트 시킬 수 있다.  
파일이 만약 `/path/to/my/secret.json`에 있다면, [kind config](../example/registry/private/private_config.yml) 는 아래와 같을 것이다.
```
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
nodes:
- role: control-plane
  extraMounts:
  - containerPath: /var/lib/kubelet/config.json
    hostPath: /path/to/my/secret.json
```
### Use an Access Token
자격 증명이 프로그래밍적으로 런타임에 노드로 추가될 수 있다.  
만약 이렇ㄱ게 한다면, kubelet은 각 노드가 새로운 자격증명을 불러오게 하기 위해 재시작 되어야 한다.  

액세스 토큰을 사용하여 호스트머신에서 [gcr.io](https://cloud.google.com/container-registry/) cred 파일을 생성하기 위한 예제 [shell snippet](../example/registry/private/kind-gcr.sh)

#### Use a Service Account
Access tokens는 금방 소멸된다. 그래서 Service Account나 keyfile를 대신해서 사용하는 것을 선호한다.  
먼저, console을 통해 키를 다운받거나 gcloud를 사용하여 새로운 것을 생성한다.
  
```
gcloud iam service-accounts keys create <output.json> --iam-account <account email>
```
[access token snippet](../example/registry/private/kind-gcr.sh) 에서 `gcloud auth print-access-token | ...` 해당 라인을 아래와 같이 바꿔준다.
```
cat <output.json> | docker login -u _json_key --password-stdin https://gcr.io
```

자세한 사항은 key file 인증에 대한 구글의 [upstream docs](https://cloud.google.com/container-registry/docs/advanced-authentication#json_key_file) 문서를 참조

#### Use a Certificate
인증서를 통한 저장소가 있고 인증서와 인증서 키가 host 폴더에 있는 경우 예와 같이 기본 설정을 패치하는 하기 위해 `containerd` 플러그인에 이것을 마운트하고 사용할 수 있다.
클라우드 생성 예제, [private_config_certificate.yml](../example/registry/private/private_config_certificate.yml)

