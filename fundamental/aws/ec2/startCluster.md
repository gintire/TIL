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

