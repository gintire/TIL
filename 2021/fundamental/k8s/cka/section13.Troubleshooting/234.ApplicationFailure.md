# Application Failure

웹과 데이터베이스 서버가 있는 2계층 어플리케이션을 살펴본다.

데이터베이스 서버는 데이터베이스를 호스팅하고 웹 서버는 데이터베이스 서버를 사용하여 유저에게 웹 서비스를 제공한다.

User - Web-service - Web - DB-Service - DB


## 시나리오 - 유저가 어플리케이션 접근이 되지않는다고 이슈
### check 1
처음으로, 프론트엔드 어플리케이션부터 확인한다. 표준적인 방법으로 이를 확인한다. curl을 사용해서 노드포트의 IP에 접근 가능한지 확인한다.

### check 2
다음으로 service를 확인한다. web pod의 엔드포인트를 발견하고있는지 확인한다.  
```
$ kubectl describe service web-service
...
Endpoints:      10.32.0.6:8080
...
```
위 예시는 엔드포인트를 잘 찾고있는 예시이다.

하지만 만약 서비스가 파드를 제대로 찾지 못하고있다면, 파드와 서비스에 설정된 `selectors`를 확인해라.

### check 3
다음 파드 상태와 running 상태인지 확인한다.
```
$ kubectl get pod
```

이벤트 확인
```
$ kubectl describe pod web
```

상태에 대한 로그 확인
```
$ kubectl logs web
```
실패한 뒤 로그를 확인하면, 현재 상태의 컨테이너에 대한 로그만 나와서 이전 컨테이너에서 실패한 로그를 확인할 수 없다.

그러므로 다시 실패가 떨어질 때까지 `-f` 옵션을 사용해서 대기한다.

### check 4
DB 서비스 확인, DB pod 상태 확인

DB 로그 확인
