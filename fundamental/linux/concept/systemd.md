# Systemd
## Service vs Systemctl


### Service
init.d에서 서비스 관리 시 사용하는 명령으로 이전 리눅스 배포 버전 (ex, CentOS6)에서 사용하는 명령어이며 최근 리눅스 배포판에서 service 명령을 사용시 systemctl로 전달된다.

### Systemctl
최근 리눅스 배포 버전 (ex, CentOS7)에서 서비스 유닛 (서비스 이름.service)을 관리하는데 사용하는 명령어로 /usr/lib/systemd 또는 /lib/systemd/system 디렉토리 아래에 확장자가 .service인 파일을 만들면 , systemctl 명령을 통해 서비스를 제어할 수 있다.



## 서비스 유닛 파일 저장 위치
### /lib/systemd/system
유닛파일들의 시스템 copy는 /lib/systemd/system 디렉터리에 저장된다. 소프트웨어가 유닛 파일을 시스템에 설치하면 이 위치에 기본적으로 저장되게 된다.

여기에 저장된 유닛파일들은 세션동안 필요로할 때 시작하거나, 중지시킬수 있다.  여기에 저장되어있는 파일들은 수정하지 말아야한다.

만약 오버라이드가 필요로한다면 다른 경로의 디렉터리에서 수정해라

### /etc/systemd/system
유닛의 기능을 변경하고자 하다면, /etc/systemd/system 디렉터리에서 수정을 하는 것이 가장 좋다. 이 디렉터리에 있는 유닛파일들은 파일 시스템의 다른 위치보다 우선된다. 시스템의 단위 파일 사본을 수정해야하는 경우이 디렉토리에 대체 파일을 배치하는 것이 가장 안전하고 융통성있는 방법입니다.

시스템 단위 파일의 특정 지시문 만 재정의하려는 경우 실제로 하위 디렉토리 내에 단위 파일 snippet을 제공 할 수 있습니다. 이렇게하면 시스템 사본의 지시문을 추가하거나 수정하여 변경할 옵션 만 지정할 수 있습니다.

이를 수행하는 올바른 방법은 끝에 .d가 추가 된 단위 파일의 이름을 따서 명명 된 디렉토리를 만드는 것입니다. 따라서 example.service라는 단위의 경우 example.service.d라는 하위 디렉토리를 만들 수 있습니다. 이 디렉토리 내에서 .conf로 끝나는 파일을 사용하여 시스템 단위 파일의 속성을 재정의하거나 확장 할 수 있습니다.

### /run/systemd/system
런타인 유닛 정의는 /run/systemd/system에 저장된다. 여기에 있는 유닛 파일은 /etc/systemd.system 과 /lib/systemd/system 사이의 우선순위를 가진다. 

systemd 프로세스 자체는 런타임에 생성 된 동적으로 생성 된 단위 파일에이 위치를 사용합니다.

이 디렉토리는 세션 기간 동안 시스템의 장치 동작을 변경하는 데 사용할 수 있습니다. 이 디렉토리의 모든 변경 사항은 서버가 재부팅 될 때 손실됩니다.



## 유닛의 타입
systemd는 리소스의 타입에 따라 유닛을 카테고리화 한다. 타입을 정의하는 가장 쉬운 방법은 type suffix를 통해 정의하는 것이다. 

systemd에서 사용가능한 유닛의 타입은 아래와 같다.

* service: A service unit describes how to manage a service or application on the server. This will include how to start or stop the service, under which circumstances it should be automatically started, and the dependency and ordering information for related software.
* socket: A socket unit file describes a network or IPC socket, or a FIFO buffer that systemd uses for socket-based activation. These always have an associated .service file that will be started when activity is seen on the socket that this unit defines.
* device: A unit that describes a device that has been designated as needing systemd management by udev or the sysfs filesystem. Not all devices will have .device files. Some scenarios where .device units may be necessary are for ordering, mounting, and accessing the devices.
* mount: This unit defines a mountpoint on the system to be managed by systemd. These are named after the mount path, with slashes changed to dashes. Entries within /etc/fstab can have units created automatically.
* automount: An .automount unit configures a mountpoint that will be automatically mounted. These must be named after the mount point they refer to and must have a matching .mount unit to define the specifics of the mount.
* swap: This unit describes swap space on the system. The name of these units must reflect the device or file path of the space.
* target: A target unit is used to provide synchronization points for other units when booting up or changing states. They also can be used to bring the system to a new state. Other units specify their relation to targets to become tied to the target’s operations.
* path: This unit defines a path that can be used for path-based activation. By default, a .service unit of the same base name will be started when the path reaches the specified state. This uses inotify to monitor the path for changes.
* timer: A .timer unit defines a timer that will be managed by systemd, similar to a cron job for delayed or scheduled activation. A matching unit will be started when the timer is reached.
* snapshot: A .snapshot unit is created automatically by the systemctl snapshot command. It allows you to reconstruct the current state of the system after making changes. Snapshots do not survive across sessions and are used to roll back temporary states.
* slice: A .slice unit is associated with Linux Control Group nodes, allowing resources to be restricted or assigned to any processes associated with the slice. The name reflects its hierarchical position within the cgroup tree. Units are placed in certain slices by default depending on their type.
* scope: Scope units are created automatically by systemd from information received from its bus interfaces. These are used to manage sets of system processes that are created externally.


## 유닛 파일의 구조
### 일반적인 유닛파일의 특성
service 파일을 크게  Unit, Service, Install 3가지의 섹션으로 구성된다.

섹션 이름은 잘 정의되어 있으며 대소문자를 구분한다.  

유닛의 행위나 메타데이터가 키-벨류 형태로 선언된다.
```
[Section]
Directive1=value
Directive2=value
...
```

### Unit 세션 지시어
유닛에 대한 메타데이터가 정의되고 다른 유닛과 연관되는 설정을 한다.
* Description=: This directive can be used to describe the name and basic functionality of the unit. It is returned by various systemd tools, so it is good to set this to something short, specific, and informative.
* Documentation=: This directive provides a location for a list of URIs for documentation. These can be either internally available man pages or web accessible URLs. The systemctl status command will expose this information, allowing for easy discoverability.
* Requires=: This directive lists any units upon which this unit essentially depends. If the current unit is activated, the units listed here must successfully activate as well, else this unit will fail. These units are started in parallel with the current unit by default.
* Wants=: This directive is similar to Requires=, but less strict. Systemd will attempt to start any units listed here when this unit is activated. If these units are not found or fail to start, the current unit will continue to function. This is the recommended way to configure most dependency relationships. Again, this implies a parallel activation unless modified by other directives.
* BindsTo=: This directive is similar to Requires=, but also causes the current unit to stop when the associated unit terminates.
* Before=: The units listed in this directive will not be started until the current unit is marked as started if they are activated at the same time. This does not imply a dependency relationship and must be used in conjunction with one of the above directives if this is desired.
* After=: The units listed in this directive will be started before starting the current unit. This does not imply a dependency relationship and one must be established through the above directives if this is required.
* Conflicts=: This can be used to list units that cannot be run at the same time as the current unit. Starting a unit with this relationship will cause the other units to be stopped.
* Condition...=: There are a number of directives that start with Condition which allow the administrator to test certain conditions prior to starting the unit. This can be used to provide a generic unit file that will only be run when on appropriate systems. If the condition is not met, the unit is gracefully skipped.
* Assert...=: Similar to the directives that start with Condition, these directives check for different aspects of the running environment to decide whether the unit should activate. However, unlike the Condition directives, a negative result causes a failure with this directive.


### service 파일 예제
example.service
```
# Sample init script for eazyBI
[Unit]
Description=eazyBI service
After=syslog.target network.target
 
[Service]
Type=simple
# Run the service as this user and group
User=eazybi
Group=eazybi
TimeoutSec=30
# Directory where eazyBI is installed
WorkingDirectory=/opt/atlassian/eazybi_private
ExecStart=/opt/atlassian/eazybi_private/bin/start.sh
Restart=on-failure
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=eazybi
 
[Install]
WantedBy=multi-user.target
```
### Service 세션
[Service] 섹션은 서비스에만 적용되는 구성을 제공하는 데 사용됩니다.

기본적으로 Service 섹션에서 정의되어야 하는 것은 Type 이다. 이는 프로세스 및 데몬 화 동작에 따라 서비스를 분류합니다.

이것이 중요한 점이 system가 어떻게 서비스를 관리하고 상태를 확인하게하는지 알려준다.

Type 지시어

* simple: The main process of the service is specified in the start line. This is the default if the Type= and Busname= directives are not set, but the ExecStart= is set. Any communication should be handled outside of the unit through a second unit of the appropriate type (like through a .socket unit if this unit must communicate using sockets).
* forking: This service type is used when the service forks a child process, exiting the parent process almost immediately. This tells systemd that the process is still running even though the parent exited.
* oneshot: This type indicates that the process will be short-lived and that systemd should wait for the process to exit before continuing on with other units. This is the default Type= and ExecStart= are not set. It is used for one-off tasks.
* dbus: This indicates that unit will take a name on the D-Bus bus. When this happens, systemd will continue to process the next unit.
* notify: This indicates that the service will issue a notification when it has finished starting up. The systemd process will wait for this to happen before proceeding to other units.
* idle: This indicates that the service will not be run until all jobs are dispatched.

특정 서비스 타입을 사용할 때 사용되는 추가적인 지시어들
* RemainAfterExit=: This directive is commonly used with the oneshot type. It indicates that the service should be considered active even after the process exits.
* PIDFile=: If the service type is marked as “forking”, this directive is used to set the path of the file that should contain the process ID number of the main child that should be monitored.
* BusName=: This directive should be set to the D-Bus bus name that the service will attempt to acquire when using the “dbus” service type.
* NotifyAccess=: This specifies access to the socket that should be used to listen for notifications when the “notify” service type is selected This can be “none”, “main”, or “all. The default, "none”, ignores all status messages. The “main” option will listen to messages from the main process and the “all” option will cause all members of the service’s control group to be processed.


지금까지 몇 가지 필수 정보에 대해 논의했지만 실제로 서비스 관리 방법을 정의하지 않았다.

* ExecStart=: This specifies the full path and the arguments of the command to be executed to start the process. This may only be specified once (except for “oneshot” services). If the path to the command is preceded by a dash “-” character, non-zero exit statuses will be accepted without marking the unit activation as failed.
* ExecStartPre=: This can be used to provide additional commands that should be executed before the main process is started. This can be used multiple times. Again, commands must specify a full path and they can be preceded by “-” to indicate that the failure of the command will be tolerated.
* ExecStartPost=: This has the same exact qualities as ExecStartPre= except that it specifies commands that will be run after the main process is started.
* ExecReload=: This optional directive indicates the command necessary to reload the configuration of the service if available.
* ExecStop=: This indicates the command needed to stop the service. If this is not given, the process will be killed immediately when the service is stopped.
* ExecStopPost=: This can be used to specify commands to execute following the stop command.
* RestartSec=: If automatically restarting the service is enabled, this specifies the amount of time to wait before attempting to restart the service.
* Restart=: This indicates the circumstances under which systemd will attempt to automatically restart the service. This can be set to values like “always”, “on-success”, “on-failure”, “on-abnormal”, “on-abort”, or “on-watchdog”. These will trigger a restart according to the way that the service was stopped.
* TimeoutSec=: This configures the amount of time that systemd will wait when stopping or stopping the service before marking it as failed or forcefully killing it. You can set separate timeouts with TimeoutStartSec= and TimeoutStopSec= as well.

### Install 세션
[Install] 섹션에 대해서 이해를 하고 넘어가자면 systemctl enable [service name] 으로 VM 구동시 서비스가 자동으로 구동되도록 할 때 이용하는 섹션

* WantedBy=: The WantedBy= directive is the most common way to specify how a unit should be enabled. This directive allows you to specify a dependency relationship in a similar way to the Wants= directive does in the [Unit] section. The difference is that this directive is included in the ancillary unit allowing the primary unit listed to remain relatively clean. When a unit with this directive is enabled, a directory will be created within /etc/systemd/system named after the specified unit with .wants appended to the end. Within this, a symbolic link to the current unit will be created, creating the dependency. For instance, if the current unit has WantedBy=multi-user.target, a directory called multi-user.target.wants will be created within /etc/systemd/system (if not already available) and a symbolic link to the current unit will be placed within. Disabling this unit removes the link and removes the dependency relationship.
* RequiredBy=: This directive is very similar to the WantedBy= directive, but instead specifies a required dependency that will cause the activation to fail if not met. When enabled, a unit with this directive will create a directory ending with .requires.
* Alias=: This directive allows the unit to be enabled under another name as well. Among other uses, this allows multiple providers of a function to be available, so that related units can look for any provider of the common aliased name.
* Also=: This directive allows units to be enabled or disabled as a set. Supporting units that should always be available when this unit is active can be listed here. They will be managed as a group for installation tasks.
* DefaultInstance=: For template units (covered later) which can produce unit instances with unpredictable names, this can be used as a fallback value for the name if an appropriate name is not provided.

[나머지는 공식 문서 참고]( https://www.digitalocean.com/community/tutorials/understanding-systemd-units-and-unit-files)
