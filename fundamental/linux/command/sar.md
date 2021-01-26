# sar
## 개요
- sar 명령어는 솔라리스, 유닉스, 리눅스 등에서 유용하게 쓰는 시스템 모니터링 프로그램이다.
- 모니터링 대상이 상당히 넓은 편이며 CPU 활동에 대한 통계를 출력한다.
- 각종 활동에 대한 통계를 다른 프로그램을 이용하여 파일로 저장하고 통계치를 리포팅 하는 기능을 제공한다.
- sadc에서 생성한 daily activity 파일을 읽어서 보고서를 작성하기도 하고 시스템의 활동 상황을 수집할 수도 있다.

## sar 명령어로 모니터링 가능한 항목
- I/O 전송량
- 페이징
- 프로세스 생성 숫자
- 블락 디바이스 활동
- 인터럽트
- 네트워크 통계
- run 큐 및 시스템 부하 평균
- 메모리와 스왑 공간 활용 통계
- 메모리 통계
- CPU 이용도
- 특정 프로세스에 대한 CPU 이용도
- inode, 파일, 기타 커널 테이블에 대한 상태
- 시스템 스위칭 활동(context switch)
- 스와핑 통계
- 특정 프로세스 통계
- 특정 프로세스의 자식 프로세스 통계
- TTY 디바이스 활동

## sar 패키지 설치
ubuntu
```
$ apt list | grep sysstat
$ apt-get install sysstat
```
centos
```
# rpm -qa | grep syssta
# yum install sysstat
```

sar 은 10분 간격으로 데이터를 수집하기 때문에 설치 직후 데이터가 없어서 나오는 에러가 발생

## sar 명령어 활용
- 기본 정보 : CPU 사용 정보 출력
```
$ sar
Linux 5.4.0-64-generic (test)   01/26/2021      _x86_64_        (12 CPU)

04:21:34 PM     CPU     %user     %nice   %system   %iowait    %steal     %idle
04:21:37 PM     all      1.59      0.00      1.22      0.17      0.00     97.02
```
  * %user : user mode 에서 작동한 CPU 가동률
  * %sys : kernel mode에서 작동한 CPU 가동률
  * %idle : idle 상태로 있었던 CPU 대기율
  * %iowait : io wait 상태로 있었던 CPU 대기율

- 실시간 정보 보기
sar [간격][인터벌] 형식으로 입력

```
## 3초 간격으로 10개의 데이터 값을 출력
$ sar 3 10
```

- 버퍼의 액티비티 측정
I/O 와 transfer의 통계를 백분율로 출력
```
$ sar -b 3 10
Linux 5.4.0-64-generic (test)   01/26/2021      _x86_64_        (12 CPU)

04:23:02 PM       tps      rtps      wtps      dtps   bread/s   bwrtn/s   bdscd/s
04:23:05 PM     15.67      0.00     15.67      0.00      0.00    157.33      0.00
04:23:08 PM      8.33      0.00      8.33      0.00      0.00     96.00      0.00
```
  * tps : 디스크에서 발생되어진 초당 전송량. 즉 디스크에 요청한 I/O양.
  * rtps : 디스크로부터 발생된 초당 읽기 총 요청 횟수
  * bread/s : 드라이브 안의 블럭에서 초당 읽은 데이터의 총합.
  * bwrtn/s : 드라이브 안의 블럭에서 초당 쓰여진 데이터의 총합.

- 페이징 통계를 출력
```
$ sar -B 3 10
Linux 5.4.0-64-generic (test)   01/26/2021      _x86_64_        (12 CPU)

04:24:10 PM  pgpgin/s pgpgout/s   fault/s  majflt/s  pgfree/s pgscank/s pgscand/s pgsteal/s    %vmeff
04:24:13 PM      0.00     70.67    581.33      0.00    309.00      0.00      0.00      0.00      0.00
04:24:16 PM      0.00     76.00    380.33      0.00    156.33      0.00      0.00      0.00      0.00
04:24:19 PM      0.00     60.00   1059.00      0.00    622.67      0.00      0.00      0.00      0.00
```
  * pgpgin/s : 디스크로부터 초당 paged in 된 page의 총 수
  * pgpgout/s : 디스크로부터 초당 paged out 된 page의 총 수
- 지정한 시간까지만 정보 출력
```
$ sar -e 03:00:00
```

- 특정 날짜의 sar 정보를 출력
sar -f /var/spool/sa/sa26 (26일자 sar 정보 출력)
```
sar -f /var/spool/sa/sa26
```

- 실행 대기 큐에 있는 프로세스를 보여줍니다. 시스템의 load avarage를 나타냅니다.
```
$ sar -q 3 10
Linux 5.4.0-64-generic (test)   01/26/2021      _x86_64_        (12 CPU)

04:27:25 PM   runq-sz  plist-sz   ldavg-1   ldavg-5  ldavg-15   blocked
04:27:28 PM         0      1161      0.18      0.35      0.36         0
04:27:31 PM         0      1161      0.18      0.35      0.36         0
04:27:34 PM         1      1161      0.17      0.34      0.36         0
```
  * runq-sz plist-sz ldavg-1 ldavg-5 ldavg-15 순으로 보여주며 각 프로세스 대기 시간과 전체 프로세스 수,
  * 1분전, 5분전 평균 작업 부하 정보를 보여 줍니다.
- 메모리, 스왑 공간의 이용 통계를 출력합니다.
```
$ sar -r
Linux 5.4.0-64-generic (test)   01/26/2021      _x86_64_        (12 CPU)

04:14:25 PM  LINUX RESTART      (12 CPU)

04:15:01 PM kbmemfree   kbavail kbmemused  %memused kbbuffers  kbcached  kbcommit   %commit  kbactive   kbinact   kbdirty
04:25:01 PM  20858944  22822660   1231472      4.99    397248   1622784   8184584     24.77   1693336   1342212       468
Average:     20858944  22822660   1231472      4.99    397248   1622784   8184584     24.77   1693336   1342212       468
```
  * kbmemfree : 사용가능한 총 메모리의 양(k/bytes)
  * kbavail : 
  * kbmemused : 사용중인 총 메모리의 양(k/bytes), 커널에서 사용중인 메모리는 제외
  * %memused : 사용된 메모리의 %양
  * kbbuffers : 커널에서 buffer 메모리로 총 사용된 메모리의 양(k/bytes)
  * kbcached : 커널에서 cache data로 사용된 총 메모리의 양(k/bytes)
  * kbcommit: 
  * %commit : 
  * kbactive :
  * kbinact :
  * kbdirty :
  
- 커널 테이블 & 파일에서 inode의 상태 출력
```
$ sar -v
Linux 5.4.0-64-generic (test)   01/26/2021      _x86_64_        (12 CPU)

04:14:25 PM  LINUX RESTART      (12 CPU)

04:15:01 PM dentunusd   file-nr  inode-nr    pty-nr
04:25:01 PM     67292      2592     90802         5
```
  * dentunusd : Directory cache 에서 사용되고있지 않은 cache entries
  * file-nr : file handles 의 사용양
  * inode-nr : inode handles 의 사용양

- 소켓 정보 출력
```
$ sar -n SOCK 3 10
Linux 5.4.0-64-generic (test)   01/26/2021      _x86_64_        (12 CPU)

04:25:35 PM    totsck    tcpsck    udpsck    rawsck   ip-frag    tcp-tw
04:25:38 PM       181         5         2         0         0         0
```
  * tcp-tw : tcp time_wait 소켓 사용양

## Error
시스템 모니터링을 위해 `sysstat` 관련 명령어를 사용하였을 때 다음과 같은 에러가 발생하는 경우가 있음
```
$ sar
Cannot open /var/log/sysstat/sa26: No such file or directory
Please check if data collecting is enabled
```
이는 현재 `sysstat` 서비스에서 진단 데이터를 기록하지 않고 있기 때문에 진단 가능한 데이터가 없어서 발생하는 문제  

```
# vi /etc/default/sysstat
#
# Default settings for /etc/init.d/sysstat, /etc/cron.d/sysstat
# and /etc/cron.daily/sysstat files
#

# Should sadc collect system activity informations? Valid values
# are "true" and "false". Please do not put other values, they
# will be overwritten by debconf!
ENABLED="false"

```
false 값을 true로 변경  

`sysstat` 서비스 재시작
```
# service sysstat restart

or

# systemctl restart sysstat
```

```
$ sar -q
Linux 5.4.0-64-generic (test)   01/26/2021      _x86_64_        (12 CPU)

04:14:25 PM  LINUX RESTART      (12 CPU)
```

## 비고
```
$ sar --help
Usage: sar [ options ] [ <interval> [ <count> ] ]
Main options and reports (report name between square brackets):
        -B      Paging statistics [A_PAGE]
        -b      I/O and transfer rate statistics [A_IO]
        -d      Block devices statistics [A_DISK]
        -F [ MOUNT ]
                Filesystems statistics [A_FS]
        -H      Hugepages utilization statistics [A_HUGE]
        -I { <int_list> | SUM | ALL }
                Interrupts statistics [A_IRQ]
        -m { <keyword> [,...] | ALL }
                Power management statistics [A_PWR_...]
                Keywords are:
                CPU     CPU instantaneous clock frequency
                FAN     Fans speed
                FREQ    CPU average clock frequency
                IN      Voltage inputs
                TEMP    Devices temperature
                USB     USB devices plugged into the system
        -n { <keyword> [,...] | ALL }
                Network statistics [A_NET_...]
                Keywords are:
                DEV     Network interfaces
                EDEV    Network interfaces (errors)
                NFS     NFS client
                NFSD    NFS server
                SOCK    Sockets (v4)
                IP      IP traffic      (v4)
                EIP     IP traffic      (v4) (errors)
                ICMP    ICMP traffic    (v4)
                EICMP   ICMP traffic    (v4) (errors)
                TCP     TCP traffic     (v4)
                ETCP    TCP traffic     (v4) (errors)
                UDP     UDP traffic     (v4)
                SOCK6   Sockets (v6)
                IP6     IP traffic      (v6)
                EIP6    IP traffic      (v6) (errors)
                ICMP6   ICMP traffic    (v6)
                EICMP6  ICMP traffic    (v6) (errors)
                UDP6    UDP traffic     (v6)
                FC      Fibre channel HBAs
                SOFT    Software-based network processing
        -q      Queue length and load average statistics [A_QUEUE]
        -r [ ALL ]
                Memory utilization statistics [A_MEMORY]
        -S      Swap space utilization statistics [A_MEMORY]
        -u [ ALL ]
                CPU utilization statistics [A_CPU]
        -v      Kernel tables statistics [A_KTABLES]
        -W      Swapping statistics [A_SWAP]
        -w      Task creation and system switching statistics [A_PCSW]
        -y      TTY devices statistics [A_SERIAL]
```
