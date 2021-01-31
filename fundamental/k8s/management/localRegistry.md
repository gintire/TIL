# Local Registry ( Local 이미지 저장소 )

## Create A Cluster And Registry
local docker registry와 kind cluster를 만드는 스크립트
[/app/k8s/bin/registry/local/kind-with-registry.sh](../example/registry/local/kind-with-registry.sh)

## Using The Registry
registry는 다음과 같이 사용할 수 있다.  
1. `docker pull gcr.io/google-samples/hello-app:1.0` 이미지를 pull 한다.
2. 이미지가 local registry를 사용할 수 있도록 tag 해준다. `docker tag gcr.io/google-samples/hello-app:1.0 localhost:5000/hello-app:1.0`
3. registry로 이것을 push 한다. `docker push localhost:5000/hello-app:1.0`
4. 이미지를 사용할 수 있다. `kubectl create deployment hello-server --image=localhost:5000/hello-app:1.0`

개인적으로 사용할 이미지를 만들었다면, `localhsot:5000/image:foo` 처럼 이미지에 태그를 하고 `localhost:5000/image:fee`처럼 쿠버네티스에서 사용할 수 있다.
