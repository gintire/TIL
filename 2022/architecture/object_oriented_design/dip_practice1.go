// Need to fix
type Player struct {
	name string
}

type Monster struct {
	hp int
}

func (p *Player) Name() string {
	return p.name
}

func (p *Player) Attack(m *Monster) {
	m.DealDamage(p, 100)
}

func (m *Monster) DealDamage(attacker *Player, damage int) {
	m.hp -= damage
	if m.hp < 0 {
		fmt.Println(attacker.Name(), "가 나를 죽였다")
	}
}

/**
* Player라는 구체화된 모듀링 Monster라는 구체화된 모듈에 의존하고 있어 의존 관계 역전 원칙에 위배된다.
**/

// My code
type Event interface {
	Register(EventListener)
}

type EventListener interface {
	OnDealDamage(attacker *player, damage int)
}

type Player struct {
	name     string
	listener EventListener
}

type Monster struct {
	hp int
}

func (p *Player) Register(name string, listener EventListener) {
	p.name = name
	p.listener = listener
}

func (p *Player) Attack() {
	p.listener.OnDealDamage(p, 1000)
}

func (m *Monster) OnDealDamage(attacker *Player, damage int) {
	m.hp -= damage
	if m.hp < 0 {
		fmt.Println(attacker.Name(), "가 나를 죽였다")
	}
}

/**
* Solution
**/
type Attacker interface {
	Name() string
}
type DamageTaker interface {
	DealDamage(att Attacker, damage int)
}

type Player struct {
	name string
}

type Monster struct {
	hp int
}

func (p *Plyaer) Name() string {
	return p.name
}

func (p *Player) Attack(dt DamageTaker) {
	dt.DealDamage(p, 100)
}

func (m *Monster) DealDamage(att Attacker, damage int) {
	m.hp -= damage
	if m.hp < 0 {
		fmt.Println(attacker.Name(), "가 나를 죽였다")
	}
}