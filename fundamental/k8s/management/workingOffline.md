# Working Offline
## Using a pre-built node Image
KIND 는 몇 가지 pre-built 이미지를 제공한다. 이 이미지들은 클러스터를 생성하는데 필수적인 것들을 포함하고 오프라인 환경에서 사용할 수 있다.  

[release page](https://github.com/kubernetes-sigs/kind/releases) 에서 사용가능한 이미지 태그들을 찾을 수 있다.  
릴리즈 노트에 있는 이미지에 `@sha256`: [image digest](https://docs.docker.com/engine/reference/commandline/pull/#pull-an-image-by-digest-immutable-identifier) 를 추가해줘야한다.  

네트워크 접속이 가능하거나, 다른 머신에서 이미지를 받아서 타겟 머신으로 옮길 수 있을 경우 다음과 같이 이미지를 받는다.  
`다운 받는 버전은 릴리즈 노트에서 확인`
```
docker pull kindest/node:v1.17.0@sha256:9512edae126da271b66b990b6fff768fbb7cd786c7d39e86bdf55906352fdf62
sha256:9512edae126da271b66b990b6fff768fbb7cd786c7d39e86bdf55906352fdf62: Pulling from kindest/node
cc5a81c29aab: Pull complete 
81c62728355f: Pull complete 
ed9cffdd962a: Pull complete 
6a46f000fce2: Pull complete 
6bd890da28be: Pull complete 
0d88bd219ffe: Pull complete 
af5240f230f0: Pull complete 
Digest: sha256:9512edae126da271b66b990b6fff768fbb7cd786c7d39e86bdf55906352fdf62
Status: Downloaded newer image for kindest/node@sha256:9512edae126da271b66b990b6fff768fbb7cd786c7d39e86bdf55906352fdf62
docker.io/kindest/node:v1.17.0@sha256:9512edae126da271b66b990b6fff768fbb7cd786c7d39e86bdf55906352fdf62
```

[node 이미지](https://docs.docker.com/engine/reference/commandline/save/) 를 `tarball`로 저장한다.

```
docker save -o kind.v1.17.0.tar kindest/node:v1.17.0@sha256:9512edae126da271b66b990b6fff768fbb7cd786c7d39e86bdf55906352fdf62
or
docker save kindest/node:v1.17.0@sha256:9512edae126da271b66b990b6fff768fbb7cd786c7d39e86bdf55906352fdf62 | gzip > kind.v1.17.0.tar.gz
```

타겟 머신으로 tarball 파일을 전송할 때, `docker load` 명령을 통해 노드 이미지를 load할 수 있다.
```
docker load -i kind.v1.17.0.tar
Loaded image ID: sha256:ec6ab22d89efc045f4da4fc862f6a13c64c0670fa7656fbecdec5307380f9cb0

or

docker load -i kind.v1.17.0.tar.gz
Loaded image ID: sha256:ec6ab22d89efc045f4da4fc862f6a13c64c0670fa7656fbecdec5307380f9cb0
```

[태그](https://docs.docker.com/engine/reference/commandline/tag/) 를 생성해 준다.
```
docker image tag kindest/node:v1.17.0@sha256:9512edae126da271b66b990b6fff768fbb7cd786c7d39e86bdf55906352fdf62 kindest/node:v1.17.0
docker image ls kindest/node
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
kindest/node        v1.17.0             ec6ab22d89ef        3 weeks ago
```

최종적으로 `--image`플레그를 이용하여 클러스터를 생성할 수 있다.
```
kind create cluster --image kindest/node:v1.17.0
Creating cluster "kind" ...
 ✓ Ensuring node image (kindest/node:v1.17.0) 🖼
 ✓ Preparing nodes 📦  
 ✓ Writing configuration 📜 
 ✓ Starting control-plane 🕹️ 
 ✓ Installing CNI 🔌 
 ✓ Installing StorageClass 💾 
Set kubectl context to "kind-kind"
You can now use your cluster with:

kubectl cluster-info --context kind-kind

Have a question, bug, or feature request? Let us know! https://kind.sigs.k8s.io/#community 🙂
```

## Building the node image
추가적으로 pre-built 노드 이미를 사용하여, KIND는 쿠버네티스 소스 코드에서 [node image](https://kind.sigs.k8s.io/docs/design/node-image) 를 빌드하는 기능을 제공한다.  

이미지 빌드 과정을 기록하는 것을 추천한다. 왜냐하면 많은 depencies를 다운받아야 하기 때문이다.  
이런한 depencies가 로컬로 다운되도록 온라인에서 한번 이상 빌드하는 것이 좋다. 자세한 것은 [building the node image](https://kind.sigs.k8s.io/docs/user/quick-start/#building-images) 를 참고    

노드 이미지는 차례로 [기본 이미지](https://kind.sigs.k8s.io/docs/design/base-image/) 에서 빌드 된다.
### Prepare Kubernetes source code
쿠버네티스 소스코드를 clone
```
$ mkdir -p $GOPATH/src/k8s.io
$ cd $GOPATH/src/k8s.io
$ git clone https://github.com/kubernetes/kubernetes
```
### Building image
```
$ kind build node-image --image kindest/node:master --kube-root $GOPATH/src/k8s.io/kubernetes
Starting to build Kubernetes
...
Image build completed.
```
이미지가 정상적으로 빌드되면, `--image` 플래그로 클러스터를 생성할 수 있다.
```
$ kind create cluster --image kindest/node:master
  Creating cluster "kind" ...
   ✓ Ensuring node image (kindest/node:master) 🖼
   ✓ Preparing nodes 📦  
   ✓ Writing configuration 📜 
   ✓ Starting control-plane 🕹️ 
   ✓ Installing CNI 🔌 
   ✓ Installing StorageClass 💾 
  Set kubectl context to "kind-kind"
  You can now use your cluster with:
  
  kubectl cluster-info --context kind-kind
  
  Have a question, bug, or feature request? Let us know! https://kind.sigs.k8s.io/#community 🙂
```
## HA cluster
만약 control-plan HA 클러스터를 만들고 싶다면, config 파일을 만들고 클러스터를 시작할 때, 다음과 같이 사용한다.
```
$ cat << EOF | kind create cluster --config=-
  kind: Cluster
  apiVersion: kind.x-k8s.io/v1alpha4
  # 3 control plane node and 1 workers
  nodes:
  - role: control-plane
  - role: control-plane
  - role: control-plane
  - role: worker
  EOF
```

주의할 점은 오프라인 환경에서는 노드 이미지를 준비하는 것 외에도 사전에 HAProxy 이미지를 준비해야 한다.  

현재 사용중인 특정 태그는 [로드밸런서 소스 코드](https://github.com/kubernetes-sigs/kind/blob/master/pkg/cluster/internal/loadbalancer/const.go#L20) 에서 찾을 수 있다.
