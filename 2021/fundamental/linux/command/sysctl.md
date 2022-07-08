# sysctl
sysctl 명령어는 런타임 (Runtime) 중에 /proc/sys 하위 디렉토리에 대한 커널 매개변수 값을 변경할 때 사용하는 명령어  
리눅스 커널에 대한 설정값 변경 및 조회가 가능하며 커널 튜닝을 위한 명령어  
시스템 관리자라면 sysctl 명령어를 알아둬야 하며 DDos 방어를 위하여 네트워크 트래픽 관련된 커널 매개변수들은 필수로 숙지  
  
/proc/sys 하위 디렉토리에 존재하는 디렉토리를 구분하는 '/'는 sysctl 명령어 이용시 '.'으로 대체함  

## 사용법
일시적인 커널 매개변수 값 변경
예시 > ping을 응답하지 않도록 설정
```
echo 1 > /proc/sys/net/ipv4/icmp_echo_ignore_all
```
혹은 sysctl명령어로 변경
```
$ sysctl -w net.ipv4.icmp_echo_ignore_all=1
```

영구적으로 커널 매개변수 값 변경
```
## /etc/sysctl.conf파일 변경
# vi /etc/sysctl.conf
net.ipv4.icmp_echo_ignore_all = 1 
```

```
$ sysctl -h

Usage:
 sysctl [options] [variable[=value] ...]

Options:
  -a, --all            display all variables
  -A                   alias of -a
  -X                   alias of -a
      --deprecated     include deprecated parameters to listing
  -b, --binary         print value without new line
  -e, --ignore         ignore unknown variables errors
  -N, --names          print variable names without values
  -n, --values         print only values of the given variable(s)
  -p, --load[=<file>]  read values from file
  -f                   alias of -p
      --system         read values from all system directories
  -r, --pattern <expression>
                       select setting that match expression
  -q, --quiet          do not echo variable set
  -w, --write          enable writing a value to variable
  -o                   does nothing
  -x                   does nothing
  -d                   alias of -h

 -h, --help     display this help and exit
 -V, --version  output version information and exit

For more details see sysctl(8).
```
