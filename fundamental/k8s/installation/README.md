![kind](./kind_logo.png)
# local k8s Installation
Install k8s cluster on Ubuntu 20.04 with kubeadm

## Environment
**OS** : Ubuntu 20.04
**Tool** : [kind](https://kind.sigs.k8s.io/)

## Prev Installation
### Install Docker
필수 패키지 설치
```
sudo apt-get install apt-transport-https ca-certificates curl gnupg-agent software-properties-common
```
GPG Key 인증
```
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
```
docker repository 등록 ( x86_64 경우 )
```
sudo add-apt-repository \
"deb [arch=amd64] https://download.docker.com/linux/ubuntu \
$(lsb_release -cs) \
stable"
```
apt docker 설치
```
sudo apt-get update && sudo apt-get install docker-ce docker-ce-cli containerd.io
```
시스템 부팅시 docker가 시작되도록 설정 & 실행
```
sudo systemctl enable docker && service docker start
```
상태 확인
```
systemctl status docker.service
```
## Installation
> https://kind.sigs.k8s.io/

**`Linux`**
** swapoff **
```
sudo swapoff -a
```
Install `kind`
```
curl -Lo ./kind https://kind.sigs.k8s.io/dl/v0.9.0/kind-linux-amd64
chmod +x ./kind
```
** aliasing **
```
$vi ~/.bash_aliases
alias kind=/app/k8s/bin/kind

$mkdir -p /app/k8s/bin
$mv ./kind /app/k8s/bin/kind

$source ~/.bash_aliases
```

Creating a Cluster  
k8s cluster를 생성하기 위해서는 간단하게 `kind create cluster`를 이용하면 된다.

위 명령은 이미 빌드된 [node image](https://kind.sigs.k8s.io/docs/design/node-image)로 쿠버네티스 클러스터를 생성한다.  
> 자세한 사항은 공식 문서 참조
> https://kind.sigs.k8s.io/docs/user/quick-start/

Interacting With Your Cluster
```
$kind create cluster # Default cluster context name is `kind`.
...
$kind create cluster --name kind-2
```

```
$kind get clusters
kind
kind-2
```
cluster에 대한 상세 내용 조회
```
$kubectl cluster-info --context kind-kind
$kubectl cluster-info --context kind-kind-2
```

kubectl 설치
```
$ curl -LO https://storage.googleapis.com/kubernetes-release/release/`curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt`/bin/linux/amd64/kubectl % Total % Received % Xferd Average Speed Time Time Time Current Dload Upload Total Spent Left Speed 100 41.9M 100 41.9M 0 0 2506k 0 0:00:17 0:00:17 --:--:-- 8128k # chmod +x ./kubectl
$ chmod +x ./kubectl
```
** aliasing **
```
$vi ~/.bash_aliases
alias kind=/app/k8s/bin/kind
alias kubectl=/app/k8s/bin/kubectl

$mv ./kubectl /app/k8s/bin/kubectl

$source ~/.bash_aliases
```
Cluster 삭제
```
$ kind delete cluster
```

Docker Image Cluster에 로드
```
$ kind load docker-image my-custom-image:unique-tag
$ docker ps
```
로드된 이미지 확인 
```
$ docker exec -it kind-control-plane crictl images
```

멀티 노드 cluster 설정
Configuration
- [configuration](kind-example-config.yaml)

```
kind create cluster --config kind-example-config.yaml
```
클러스터 로그 확인
```
$ kind export logs
```

Nginx 로드
```
### docker pull nginx:<VERSION>
$ docker pull nginx:1.19.6

### load a image to a cluster
$ kind load docker-image nginx:1.19.6

### check
~$ docker exec -it kind-control-plane crictl images
IMAGE                                      TAG                  IMAGE ID            SIZE
docker.io/kindest/kindnetd                 v20200725-4d6bea59   b77790820d015       119MB
docker.io/library/nginx                    1.19.6               f6d0b4767a6c4       137MB
...
...
```


## Known Issues
이것 때문에 고생 + 시간 낭비 ...  
**Snap으로 도커를 설치할 경우 오류**  
snap으로 도커를 설치했을 경우, `$TMPDIR` 접근하지 못하므로 kind에서 사용하는 temp 디렉터리를 사용하는 명령어를 사용하지 못한다.  

**snap으로 설치된 도커 삭제**
```
snap remove docker
```
