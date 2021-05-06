# Control Plane Failure
클러스터의 노드의 상태를 확인한다.

## Check Node Status
클러스터에 동작하고있는 pod의 상태를 확인한다.
```
kubectl get nodes
```

```
kubectl get pods
```

## Check Controlplane Services
kube-apiserver, controller-manager, scheduler가 서비스로 동작하고 있다면 서비스 상태를 확인한다.
```
$ service kube-apiserver status

$ service kube-controller-manager status

$ service kube-scheduler status
```

그 후 워커노드에서 `kubelet`, `kube-proxy`도 확인한다.
```
$ service kubelet status

$ service kube-proxy status
```

## Check Service Logs
```
$ kubectl logs kube-apiserver-master
```

kube-apiserver 로그를 확인하기위해서 journalctl 유틸리티를 사용해도 된다.
```
sudo journalctl -u kube-apiserver
```

## 문제
control plane에 오류가 났을 경우 kube-system의 controlplane 먼저 확인
```
$ kubectl get pods -n kube-system
```

서비스 설정을 확인
```
$ cat /etc/systemd/system/kubelet.service.d/10-kubeadm.conf
Environment="KUBELET_CONFIG_ARGS=--config=/var/lib/kubelet/config.yaml"
```

공식 문서에서 `static pod`를 [검색](https://kubernetes.io/docs/tasks/configure-pod-container/static-pod/)
> Manifests are standard Pod definitions in JSON or YAML format in a specific directory. Use the staticPodPath: <the directory> field in the kubelet configuration file, which periodically scans the directory and creates/deletes static Pods as YAML/JSON files appear/disappear there. Note that the kubelet will ignore files starting with dots when scanning the specified directory.

**staticPodPath: <the directory>** [kubelet configuration file](https://kubernetes.io/docs/reference/config-api/kubelet-config.v1beta1/)

staticPodPath에 static pod의 설정 경로가 명시된다.

```
$ grep -i staticPodPath /var/lib/kubelet/config.yaml
staticPodPath: /etc/kubernetes/manifests
```

```
vi /etc/kubernetes/manifests/kube-scheduler.yaml
```

### deployment scale up
```
kubectl scale deployment app --replicas=2
``` 
