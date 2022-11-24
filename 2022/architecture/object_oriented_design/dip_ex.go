/**
	Event 인터페이스는 Register() 메서드를 가지고 있고,
	1. Mail 객체는 이를 구현하여 Register() 메서드가 호출되면 EventListener를 등록한다.
	2. OnRecv() 메서드가 호출되면 등록된 EventListener 객체의 OnFire() 메서드를 호출한다.
	3. Alarm 객체는 EventListener 인터페이스를 구현하여 OnFire() 메서드가 호출될 때 알람이 울리도록 구현한다.
	4. Mail 인스턴스에 Alarm 인스턴스를 등록하면 메일 수신 시 알람이 울리게 된다.
**/
type Event interface {
	Register(EventListener)
}
type EventListener interface {
	OnFire()
}

type Mail struct {
	listener EventListener
}

func (m *Mail) Register(listener EventListener) { // 1. Event 인터페이스 구현
	m.listener = listener
}

func (m *Mail) OnRecv() {
	m.listener.Onfire() // 2. 등록된 listener의 OnFire() 호출
}

type Alarm struct {
}

func (a *Alarm) OnFire() { // 3. EventListener 인터페이스 구현
	// 알람
	fmt.Println("알람! 알람!")
}

var mail = &Mail{}
var listener EventListener = &Alarm{}

mail.Register(listener)
mail.OnRecv()		//4. 알람이 울리게 된다.
