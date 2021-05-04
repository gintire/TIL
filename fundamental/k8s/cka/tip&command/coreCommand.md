# 명령어 모음
### 배포되어 있는 컴포넌트 yaml로 생성하기
deployment : `kubectl get -n <namespace> deployment. <name> -o yaml > <name>.yaml`

### yaml 파일 생성
pods : `kubectl run pods <name> --dry-run=client -o yaml --image=<image_name> > <name>.yaml`


