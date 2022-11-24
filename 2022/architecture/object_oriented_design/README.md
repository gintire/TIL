*** MUST GO Tucker의 Go언어 프로그래밍 27장 객체지향 설계 원칙 SOLID 내용을 정리한 것입니다 ***
# 객체지향 설계 5가지 원칙 SOLID
SOLID란 객체지향 설계 5가지 원칙의 영문명 앞글자를 따서 만든 용어이다.
* 단일 책임 원칙 (Single responsibility principle, SRP)
* 개방-폐쇄 원칙 (Open-closed principle, OCP)
* 리스코프 치환 원칙 (Liskov substitution principle, LSP)
* 인터페이스 분리 원칙 (Interface segregation principle, ISP)
* 의존 관계 역전 원칙 (Dependency inversion principle, DIP)

SOLID 5가지 원칙은 반드시 지켜야 하는 의무사항은 아니지만 이 원칙들에 입각해서 설계를 하면 더 좋은 설계를 할 수 있다.

좋은 설계란 *** 상호 결합도가 낮고 응집도가 높은 설계 *** 이다.

## 단일 책임 원칙 (Single responsibility principle, SRP)
### 정의
모든 객체는 책임을 하나만 져야 한다.

### 이점
코드 재사용성을 높여준다.

### 단일 책임 원칙을 위배한 코드
```
type FinanceReport struct {
    report string
}

func (r *FinanceReport) SendReport(email string) {

}
```
FinanceReport는 회계 보고서 객체인데, 이메일로 전송하는 SendReport() 메서드를 가지고 있다. FinanceRport는 회계 보고서라는 책임을 가지고 있는데, 보고서를 전송하는 책임까지 지고 있다. 책임이 두 개가 되므로 단일 책임 원칙을 위배하였다.  
이럴 경우 다른 보고서 객체를 만들 때마다 동일한 코드를 재작성 해야하며, 추후 SendReport()에 변경이 생길 때 모든 메서드에 변경을 적용시켜야한다.

단일 책임 원칙에 입각한 설계를 하면 FinanceReport는 Report 인터페이스를 구현하고, ReportSender는 Report 인터페이스를 이용하는 관계를 형성한다.

### 단일 책임 원칙을 지킨 코드
[예시코드](./srp_ex.go)

## 개방-폐쇄 원칙 (Open-closed principle, OCP)
개방-폐쇄 원칙 정의는 다음과 같다.
### 정의
확장에는 열려 있고, 변경에는 닫혀 있다.
### 이점
상호 결합도를 줄여 새 기능을 추가할 때 기존 구현을 변경하지 않아도 된다.

### 개방-폐쇄 원칙을 위배한 코드
```
func SendReport(r *Report, method SendType, receiver string) {
    switch method {
    case Email:
        // 이메일 전송
    case Fax:
        // 팩스 전송
    case PDF:
        // pdf 파일 생성
    ...
    }
}
```
흔히 보는 코드로, 전송 방식을 추가하려면 새로운 case를 만들어 구현을 추가해주면 된다. 즉 기존 SendReport() 함수 구현을 변경하게 되는 것이다. 따라서 개방-폐쇄 원칙에 위배된다. 이 SendType에 따른 switch문이 한 곳만 있다면 그나마 다행이지만 코드 여러 곳에 퍼져있다면 변경 범위가 늘어나게 된다.

### 개방-폐쇄 원칙을 지킨 코드
[예시코드](./ocp_ex.go)


EmailSender와 FaxSender는 모두 ReportSender라는 인터페이스를 구현한 객체이다. 여기에 새로운 전송 방식을 추가하면 어떻게 될까?

ReportSender를 구현한 새로운 객체를 추가해주면 된다. 새 기능을 추가했지만, 기존 구현을 변경하지 않아도 된다.

## 리스코프 치환 원칙
### 정의
q(x)를 타입 T의 객체 x에 대해 증명할 수 있는 속성이라 하자. 그렇다면 S가 T의 하위 타입이라면 q(y)는 타입 S의 객체 y에 대해 증명할 수 있어야 한다.
### 이점
예상치 못한 작동을 예방할 수 있다.

```
type T interface {      // Something() 메소드를 포함한 인터페이스
    Something()
}

type S struct {
}

func (s *S) Something() {   // T 인터페이스 구현
}

type U struct {
}

func (u *U) Something() {   // T 인터페이스 구현
}

func q(t T) {
    ...
}

var y = &S{}                // S 타입 y
var u = &U{}                // U 타입 u
q(y)
q(u)
```

1. T 인터페이스가 존재
2. S 객체와 U객체가 구현하고 있다
3. q()는 인터페이스 T를 인수로 받는다.
4. q() 함수는 S 객체 인스턴스인 y와 U 객체 인스턴스인 u 모두에 대해서 잘 동작해야 한다.

### 리스코프 치환 원칙을 위배한 코드
```
type Report interface {
    Report() string
}
type MarketingReport {
}
func (m *MarketingReport) Report() string {
    ...
}

func SendReport(r Report) {
    if _, ok := r.(*MarketingReport); ok {      // r이 마케팅 보고서일 경우 패닉
        panic("Can't send MarketingReport")
    }
    ...
}

var report = &MarketingReport{}
SendReport(report)      // 패닉 발생
```

SendReport() 호출자 입장에서는 당연히 MarketingReport 인스턴스도 전송이 잘 될 거라 예상하지만 실제로는 패닉이 발생한다.

상위 Report에 대해서 작동하는 SendReport() 함수는 하위 타입인 MarketingReport에 대해서도 똑같이 작동해야 하지만 이 코드는 그렇지 못하기 때문에 리스코프 치환 원칙을 위배한 코드가 된다.

리스코프 치환 원칙에 입각한 코드는 함수 계약 관계를 준수하는 코드를 말한다.

## 인터페이스 분리 원칙 (Interface segregation principle, ISP)
### 정의
클라이언트는 자신이 이용하지 않는 메서드에 의존하지 않아야 한다
### 이점
인터페이스를 분리하면 불필요한 메서드들과 의존 관계가 끊어져 더 가볍게 인터페이스를 이용할 수 있다.

```
type Report interface {
    Report() string
    Pages() int
    Author() string
    WrittenDate() time.Time
}

func SendReport(r Report) {
    send(r.Report())
}
```

Report 인터페이스는 SendReport 에서 사용하지 않는 불필요한 메서드들을 포함하고 있다. 이는 인터페이스 분리 원칙을 위반한다.

ISP에 입각하여 코드를 수정한다.

```
type Report interface {
    Report() string
}

type WrittenInfo interface {
    Pages() int
    Author() string
    WrittenDate() time.Time
}

func SendReport(r Report) {
    send(r.Report())
}
```

Report 인터페이스는 메서드 하나만 가지고 있다. 이제 SendReport()는 함수가 필요한 유일한 메서드인 Report를 포함한 인터페이스와 관계를 맺고 불필요한 메서드와는 관계 맺지 않는다.

즉, 많은 메서드를 포함하는 커다란 인터페이스 보다는 적은 수의 메서드를 가진 인터페이스 여러 개로 이뤄진 객체가 더 좋다.

## 의존 관계 역전 원칙 (Dependency inversion principle, DIP)
### 정의
상위 계층이 하위 계층에 의존하는 전통적인 의존 관계를 반적(역전) 시킴으로써 상위 계층이 하위 계층의 구현으로 독립되게 할 수 있다.
### 원칙
* 원칙 1 : "상위 모듈은 하위 모듈에 의존해서는 안 된다. 둘 다 추상 모듈에 의존해야 한다"
* 원칙 2 : "추상 모듈은 구체화된 모듈에 의존해서는 안 된다. 구체화된 모듈은 추상 모듈에 의존해야 한다.
### 이점
* 구체화된 모듈이 아닌 추상 모듈에 의존함으로써 확장성이 증가한다.
* 상호 결합도가 낮아져서 다른 프로그램으로 이식성이 증가한다.

### 원칙 1 뜯어보기
> "상위 모듈은 하위 모듈에 의존해서는 안 된다. 둘 다 추상 모듈에 의존해야 한다."

<<interface 입력>> ← 전송 → <<interface 출력>>
 ⇡                              ⇡
 키보드                         네트워크
의존 관계를 떨어뜨리면 각 모듈은 본연의 기능에 충실 할 수 있다.

### 원칙 2 뜯어보기
> "추상 모듈은 구체화된 모듈에 의존해서는 안된다. 구체화된 모듈은 추상 모듈에 의존해야 한다."
구체화된 모듈 간의 의존 관계를 끊고 추상 모듈을 통해서 의존해야 한다는 원칙을 살펴본다. 예를 들어 메인이 수신되면 알람을 울린다고 가정한다. 메일이라는 모듈과 알람이라는 모듈이 서로 관계 맺고 있는 코드를 살펴 본다.

```
type Mail struct {
    alarm   Alarm
}

type Alarm struct {
}

func (m *Mail) OnRecv() {   // OnRecv() 메서드는 메일 수신 시 호출된다.
    m.alarm.Alarm()         // 알람을 울린다.
}
```

메일 객체는 알람 객체를 소유하고 있고, 메일 수신 시 호출되는 OnRecv() 메서드에서 소유한 알람 객체를 사용해 알람을 울린다. 클래스 다이어그램으로 다음과 같이 나타난다.

메일 ← 알람

메일이라는 구체화된 모듈이 알람이라는 구체화된 모듈에 의존하고 있어 의존 관계 역전 원칙에 위배된다.

메일이 알람에 직접 의존하지 않고 인터페이스를 통해 의존하도록 변경해야 한다.

<<interface Event>> ← <<interface EventListener>>
 ⇡                              ⇡
메일                            알람

메일은 Event라는 인터페이스를 구현하고 알람은 EventListener라는 인터페이스를 구현하고 있다. 그리고 EventListener를 통해서 관계 맺고 있다. 이렇게 변경하면 메일이라는 구체화된 객체는 알람이라는 구체화된 객체와 관계를 맺고 있지 않고, 추상화된 객체인 Event와 EventListener를 통해서 관계 맺고 있는다.

어떤 구체화된 모듈도 구체화된 모듈에 의존적이지 않고, 추상화된 모듈 역시 구체화된 모듈에 의존적이지 않기 때문에 의존 관계 역전 원칙의 두 번째 원칙에 입각한 설계가 된다.

### 의존 관계 역전 원칙을 따른 코드
[예제코드](./dip_ex.go)