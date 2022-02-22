# Advanced Kubectl commands (json etc)

## Examples
```aidl
## Get cluster nodes as json
$ kubectl get nodes -o json
## Get cluster nodes name
$ kubectl get nodes -o=jsonpath='{.items[*].metadata.name}'
## Get cluster nodes osImage 
$ kubectl get nodes -o=jsonpath='{.items[*].status.nodeInfo.osImage}'
## Get users in kubeconfig 
$ kubectl config view --kubeconfig=/root/my-kube-config -o=jsonpath='{.users[*].name}'
## Get PVs sorted by capacity
$ kubectl get pv --sort-by=.spec.capacity.storage
## Get PVs sorted by capacity ( Using custom-columes - NAME, CAPACITY )
$ kubectl get pv --sort-by=.spec.capacity.storage -o=custom-columns=NAME:.metadata.name,CAPACITY:.spec.capacity.storage
## Get the context configured for the 'aws-user' in the my-kube-config 
$ kubectl config view --kubeconfig=/root/my-kube-config -o=jsonpath="{.contexts[?(@.context.user=='aws-user').name]}"
```
