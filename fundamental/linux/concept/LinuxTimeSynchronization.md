# Linux time synchronization: NTP, Chrony, and systemd-timesynd
> https://prog.world/linux-time-synchronization-ntp-chrony-and-systemd-timesyncd/
## Device keep track of time
리눅스 호스트는 System time과 RTC (Real Time Clock) time이 있는 것을 알아야 한다.

하드웨어의 시간은 컴퓨터가 꺼지더라도 메인보드에 있는 베터리를 이용하여 계속해서 흐르게 된다.  
### RTC time
RTC의 주된 기능은 time server가 접속 불가 상태일 때, 시간을 저장하는 용도이다.  
인터넷을 통해 time server 에 연결할 수 없었던 때, 각 컴퓨터는 내부에 정확한 시계가 있어야 했다.  
운영체제는 부팅할 때 RTC에 접근해야 했으며, 사용자는 하드웨어의 BIOS 설정에서 시간이 정확한지 확인을 했어야 했다.

하지만, 하드웨어는 time zone 이라는 개념을 이해하지 못한다. RTC는 그저 시간을 저장할 뿐, time zone이나 UTC를 반영하지 못한다. 

툴을 이용해서 RTC를 설치한다. 툴에 대한 내용은 아래에서 이제 언급할 예정이다. 

### System time
System time은 desktop에 GUI로 보여주는 시간을 나타낸다. date 명령어의 결과물이나, log의 timestamp 등등이다.

## What does NTP have?
전세계의 컴퓨터는 NTP (Network Time Protocol)을 사용하여, NTP 서버의 계층 구조를 사용하여 인터넷을 통한 표준 시간에 동기화 한다.  

주 시간 서버는 level1에 위치하고, 위성, 라디오 또는 전화선을 통한 모뎀을 통해 레벨 0의 다양한 국가 시간 서비스에 직접 연결된다.  
 
레벨 0의 시간 서비스는 원자 시계, 원자 시계가 전송하는 신호에 맞춰 조정된 무선 수신기 또는 GPS 위성이 전송하는 고정밀 시계 신호를 사용하는 GPS 수신기 일 수 있다.

대부분의 참조 서버에서 수천 개의 공용 NTP 계층 2서버가 열려 있으며 모든 사람이 사용할 수 있다.  
NTP 서버가 필요한 호스트가 많은 조직과 사용자는 자체 시간 서버를 설치하는 것을 선호하므로 하나의 로컬 호스트만 계층 2 또는 3으로 처리한다.  
그런 다음 로컬 시간 서버를 사용하도록 네트워크의 나머지 노드를 구성한다. 

우리 집의 네트워크의 경우 레벨 3이다.

### 다양한 NTP 구현
NTP의 초기 구현체는 **ntpd**이다. 그 후, 두가지의 새로운 것이 등장하였다.  
**chronyd** 와 **systemd-timesyncd** 이다.  
위 세개 모두 로컬 호스트의 시간을 NTP time server의 시간과 동기화 시킨다.

**systemd-timesyncd** 는 **chronyd** 만큼 신뢰성이 높지 않다. 그러나 대부분의 목적에 충분하다.  

만약 RTC가 동기화되지 않는다면, 로컬 시스템 시간이 약간 변경되면 점차적으로 시스템 시간을 조정하여 NTP 서버와 동기화 할 수 있다.
systemd-timesync 서비스는 시간 서버로 사용할 수 없다.

### Chrony
Chrony는 두개의 프로그램을 포함하는 NTP 구현체이다. **chronyd** daemon과 **chronyc**라고 불리는 command line 인터페이스이다.   
Chrony는 많은 경우 단순히 대체 할 수 없는 몇 가지 특징을 가진다.
* Chrony는 예전의 ntpd service에 비해 빠르게 서버를 동기화 시킨다.

### 
