# Ansible vault
> https://docs.ansible.com/ansible/latest/user_guide/vault.html

## options
```
$ ansible-vault --help
Usage: ansible-vault [create|decrypt|edit|encrypt|encrypt_string|rekey|view] [options] [vaultfile.yml]
encryption/decryption utility for Ansible data files
Options:
  --ask-vault-pass      ask for vault password
  -h, --help            show this help message and exit
  --new-vault-id=NEW_VAULT_ID
                        the new vault identity to use for rekey
  --new-vault-password-file=NEW_VAULT_PASSWORD_FILE
                        new vault password file for rekey
  --vault-id=VAULT_IDS  the vault identity to use
  --vault-password-file=VAULT_PASSWORD_FILES
                        vault password file
  -v, --verbose         verbose mode (-vvv for more, -vvvv to enable
                        connection debugging)
  --version             show program's version number and exit
 See 'ansible-vault <command> --help' for more information on a specific
```
* create / encrypt / decrypt / edit : ansible vault 생성 / 암호화 / 복호화 / 수정
* encrypt_string : ansible vault로 특정 문자열 암호화
* rekey : vault 패스워드 재지정
* view : 파일 읽기
* --ask-vault-pass : prompt로 vault password 입력 요청
* --vault-id : Multiple password 사용할 때, vault id를 통해 하나의 패스워드를 여러개로 관리
* --vault-password-file : vault password 저장소

공식 문서를 봤을 때, 헤깔리는 부분들이 있었기 때문에, 케이스별로 나눠서 정리하도록 함
## 기본 Ansible vault encrypt file
ansible vault를 통해 아무 파일이나 암호화 시킬 수 있다.
```
$ ansible-vault create my-vault-encrypted.yml
New vault password :
Confirm new vault password :
```
새로운 창이 생기고, 원하는 형태의 파일을 작성하면, 암호화된 my-vault.yml 파일을 얻을 수 있다.  
```
$ cat my-vault-encrypted.yml
$ANSIBLE_VAULT;1.1;AES256
61383366323932306663616165376432623838373662636464313136373833313533393961623139
3433326563386161393130626539643763643439656236320a646365363666396562626563333032
34623939363131366337643131653564646437353130666636393033393866346361646666326361
3239633533386466360a653436666665333365346462373935383030326563....
```
위는 암호화된 파일을 조회한 것으로 AES256으로 암호화된 값들을 볼 수 있다.  
이런 식으로 안전하게 암호화된 파일을 생성할 수 있으므로, 패스워드 등을 관리할 때, 사용하면 좋다.

## 암호화된 파일 보기 Ansible vault view
암호화된 파일을 읽기 위해서는 ansible-vault view를 사용한다.  
파일을 암호화할 때 사용하였던, password를 입력하면 암호화된 파일을 읽을 수 있다.
```
$ ansible-vault view my-vault-encrypted.yml
Vault password :
```

## 수정 ansible vault edit encrypted files
암호화된 파일을 수정하기 위해서는 ansible-vault edit를 사용한다.  
파일을 읽는 것과 같게, password를 입력하면 수정할 수 있는 창이 뜬다.
```
$ ansible-vault edit my-vault-encrypted.yml
Vault password :
```
수정하는 로직은 `ansible-vault`는 파일 경로에 임시 파일을 생성하여 편집하고, 편집이 끝나면 `ansible-vault`는 임시 파일을 암호화하고 기존의 파일과 교체한다.  

## password file을 사용 --vault-password-file, --vault-id
ansible vault를 사용하면 매 명령을 보낼 때마다 vault password를 입력해야한다.  
password를 저장하고 사용할 경우 두가지의 옵션을 사용한다.  
> password를 평문으로 저장해서 사용하는 것은 보안상 좋지 않은 방법이다.
> 인가되지않은 유저가 파일을 읽을 수 있기 때문이다.
> 보이지 않는 디렉토리나 파일을 생성하고, 파일 권한을 추가하여 인가되지 않은 유저가 접근하는 것을 방지한다.

```
### password 파일 생성 <PASSWORD> 에는 자신이 원하는 vault 패스워드를 기록
$ echo "<PASSWORD>" > .password_file
$ ansible-vault edit --vault-id .password_file my-vault-encrypted.yml
### 또는
$ ansible-vault edit --vault-password-file .password_file my-vault-encrypted.yml
```
## 존재하고 있는 파일을 암호화 Ansible vault encrypt
이미 존재하고있는 파일을 암호화할 때는 encrypt를 사용한다.
```
$ ansible-vault encrypt existed-file.yml
New vault password :
Confirm new vault password :
Encryption successful
```

## 암호화된 파일 패스워드 변경 Ansible vault rekey
--ask-vault-pass 옵션을 사용하면 prompt로 패스워드를 변경할 수 있다.
```
$ ansible-vault rekey --ask-vault-pass my-vault-encrypted.yml
Vault password:
New Vault password:
Confirm New Vault password:
Rekey successful
```

## 파일 복호화 decrypt
```
$ ansible-vault decrypt --ask-vault-pass my-vault-encrypted.yml
Vault password:
Decryption successful
```

## playbook에서 ansible vault 사용하기
예를 들어 앞서 암호화 하였던 my-vault-encrypted.yml 파일이 다음과 같다고 가정하자.
```
############ my-vault-encrypted.yml 파일
---
- name: This file will be encrypted
  hosts: dev
  tasks:
    - name: Execute command 'date'
      command: date
```
복호화 없이 위 playbook을 실행 시킬 경우 오류가 발생한다.  
암호화된 playbook을 복호화 하려면 두 가지 옵션을 사용한다.
```
$ ansible-playbook --vault-id @prompt my-vault-encrypted.yml
Vault password (default):

##### 또는
$ ansible-playbook --ask-vault-pass my-vault-encrypted.yml
Vault password (default): 
```
## vault password 파일 사용하여 암호화된 파일 생성 및 playbook 실행
```
### password 파일 생성 <PASSWORD> 에는 자신이 원하는 vault 패스워드를 기록
$ echo "<PASSWORD>" > .password_file
$ ansible-vault edit --vault-id .password_file my-vault-encrypted.yml
### 또는
$ ansible-vault edit --vault-password-file .password_file my-vault-encrypted.yml
```
아래와 같이 --vault-password-file 옵션에 암호화 할 때 사용하였던 파일을 입력하면 prompt로 비밀번호를 입력하지 않고 실행 가능하다.
```
$ ansible-playbook my-vault-encrypted.yml --vault-password-file=.password_file
```

## String 암호화 Ansible vault encrypt_string
지금까지 학습한 것과 같이 prompt를 통해 패스워드를 입력 하거나,  
--vault-password-file / --vault-id를 통해 저장된 패스워드를 사용할 수 있다.  
  
### **먼저, prompt를 통해 패스워드를 입력하여 사용하는 것을 알아본다.**
pattern
- <string_to_encrypt> : 암호화 하고자 하는 문자열 ex > 1234
- <string_name_of_variable> : string name (변수의 이름) ex > db_password
`ansible-vault encrypt_string '<string_to_encrypt>' --name '<string_name_of_variable>'`
```
$ ansible-vault encrypt_string '1234' --name 'db_password'
New vault password :
Confirm new vault password :

db_password: !vault |
      $ANSIBLE_VAULT;1.1;AES256
      62313365396662343061393464336163383764373764613633653634306231386433626436623361
      613...
      ...
      6564
```
playboot에서 암호화된 변수 사용하기
```
$ cat secret.yml
---
- name: inline secret variable demonstration
  hosts: dev
  gather_facts: false

  vars:
    db_password: !vault |
          $ANSIBLE_VAULT;1.1;AES256
          62313365396662343061393464336163383764373764613633653634306231386433626436623361
          613...
          ...
          6564

  tasks:
    - name: print the secure variable
      debug:
        var: db_password
```
ansible-playbook
```
$ ansible-playbook --ask-vault-pass secret.yml
Vault password :

PLAY [inline secret variable demonstration] *********************************************************************

TASK [print the secure variable] ********************************************************************************
ok: [dev] => {
    "db_password": "1234"
}

PLAY RECAP ******************************************************************************************************
dev        : ok=1    changed=0    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0
```

### --vault-password-file 를 사용하여 암호화하는 방법을 알아본다.
pattern
- <password_source> : vault password 소스 (prompt, file, or script, with or without a vaultID)
- <string_to_encrypt> : 암호화할 문장 ex > 1234
- <string_name_of_variable> : string 이름 ( 변수의 이름 ) > db_password
`ansible-vault encrypt_string <password_source> '<string_to_encrypt>' --name '<string_name_of_variable>'`
먼저, password_source => vault password 저장소를 사용하는 예제를 본다.
> password_source가 prompt 라면 prompt 에서, 파일 또는 스크립트 등도 가능,
> 패스워드 저장소로 한정하면 안된다.

VAULT_PASSWORD를 저장하는 .passfile을 생성한다.
```
$ echo '<VAULT_PASSWORD>' > .passfile
```

.passfile에 저장되어있는 vault_password를 사용하여 1234를 암호화하여 db_password라는 변수에 저장한다.
```
ansible-vault encrypt_string --vault-password-file .passfile '1234' --name 'db_password'
```

결과
```
db_password: !vault |
      $ANSIBLE_VAULT;1.1;AES256
      62313365396662343061393464336163383764373764613633653634306231386433626436623361
      6...
      ...
      6564
```

playbook 내부에서 위 변수를 사용하려면, playbook을 다음과 같이 설정한다.
```
$ cat secret.yml
---
- name: inline secret variable demonstration
  hosts: dev
  gather_facts: false

  vars:
    db_password: !vault |
          $ANSIBLE_VAULT;1.1;AES256
          62313365396662343061393464336163383764373764613633653634306231386433626436623361
          6...
          ...
          6564

  tasks:
    - name: print the secure variable
      debug:
        var: db_password
```
ansible-playbook에 --vault-password-file 옵션으로 vault_password가 저장되어있는 파일을 넘겨준다.
```
$ ansible-playbook --vault-password-file .passfile secret.yml

PLAY [inline secret variable demonstration] *********************************************************************

TASK [print the secure variable] ********************************************************************************
ok: [dev] => {
    "db_password": "1234"
}

PLAY RECAP ******************************************************************************************************
dev        : ok=1    changed=0    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0
```

### --vault-id를 사용하여 암호화하는 방법을 알아본다.
--vault-id를 사용하는 것은 동일한 변수에 다양한 값 ( multiple passwords를 관리하기 위해서 사용한다. )  
--vault-password-file과 비슷하지만, --vault-password-file은 하나의 변수에 하나의 값만 사용할 수 있도록 지원한다.  

예를 들면, 다양한 profile (dev, operation, local 등..)이 있을 경우, 같은 변수에 다른 값을 지정해줄 수 있다.  
--vault-id를 사용하는 것은 위에서 사용한 모든 명령어에서 동일하게 사용될 수 있다.
`ansible-vault create --vault-id dev@prompt secret.yml`  
`ansible-vault encrypt_string --vault-id operation@.passfile`  
위와 같이 사용할 수 있다. 이때 @prompt는 password_source로 prompt창을 사용하는 것이다.  
만약 .passfile이 password_source로 전달된다면, .passfile에 있는 값을 vault password로 가진다.  

예시로 string을 암호화 하는 예시를 들어본다.  
여기서 .passfile은 vault password가 저장되어있는 파일이다.
```
$ cat .passfile
<VAULT_PASSWORD>
```
String 암호화
```
$ ansible-vault encrypt_string --vault-id dev@.passfile '1234' --name 'db_password'
db_password: !vault |
          $ANSIBLE_VAULT;1.2;AES256;dev
          30613233633461343837653833666333643061636561303338373661313838333565653635353162
          326...
          ...
          6330
```
playbook에서 암호화된 vars 사용하기
```
$ cat secret.yml
---
- name: inline secret variable demonstration
  hosts: dev
  gather_facts: false

  vars:
    db_password: !vault |
          $ANSIBLE_VAULT;1.1;AES256
          32373330386330643061613237393466393363333031303965383063316338393261616134353233
          636...
          ...
          6335

  tasks:
    - name: print the secure variable
      debug:
        var: db_password
```
playbook 실행
```
$ ansible-playbook --vault-id dev@.passfile secret.yml

PLAY [inline secret variable demonstration] *********************************************************************

TASK [print the secure variable] ********************************************************************************
ok: [dev] => {
    "db_password": "1234"
}

PLAY RECAP ******************************************************************************************************
dev        : ok=1    changed=0    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0
```

## 실제 사용했던 방법 🎁🎁🎁
시나리오
- 운영, 개발 host가 inventory에 존재
- host 서버에서 sudo 권한을 얻기 위하여, ansible_sudo_password 를 넘겨줘야함
- ansible_sudo_password를 inventory 혹은 playbook에 평문으로 저장하거나, --extra-vars를 통해 전송하기에 보안상 이슈가 있음
- ansible_sudo_password를 ansible vault로 암호화

> 암호화된 vars를 넘기는 방법은 두가지로 사용한다.
> 1. ansible vault의 encrypt_string을 사용하여, 패스워드를 암호화하고, inventory 혹은 playbook에 vars: <ENCRYPTED_PASSWORD>로 전달
> 2. ansible vault의 create를 통해 vars_files를 생성하여 playbook에 vars_files: <path to vars_files> 로 넘겨줌

**inventory**
inventory.yml
oper / dev 서버 그룹이 있으며, host에는 동일한 계정을 사용한다.  
아래는 평문으로 저장된 상태를 표현하며, ansible_sudo_pass가 평문상태이다.
```
---
all:
  children:
    oper:
      children:
        oper_server1:
          hosts: foo.example.com
        oper_server2:
          hosts: bar.example.com
      vars:
        ansible_sudo_pass: <VERY_IMPORTANT_PASS_BUT_CAN_SEE_OPER>
    dev:
      children:
        dev_server1:
          hosts: foofoo.example.com
        dev_server2:
          hosts: barbar.example.com
      vars:
        ansible_sudo_pass: <VERY_IMPORTANT_PASS_BUT_CAN_SEE_DEV>
```
**playbook**
예제는 변수로 입력 받은 유저의 패스워드를 변경하는 playbook이다.  
path : playbook/user_password_change.yml
```
---
- name: Change passwd
  become: true
  hosts: "{{ variable_host | default('dev')}}"
  remote_user: devops
  tasks:
  - name: Change password of existing user
    become: true
    become_method: sudo
    user: name={{ user_name }} update_password=always password={{ new_password|password_hash('sha512') }}
    register: result
  - debug:
      var: result
```
**ansible-playbook**
```
$ ansible-playbook -i inventory.yml playbook/user_password_change.yml --extra-vars "user_name=test variable_host=dev new_password=test123"
```

위의 과정에서 평문의 패스워드를 ansible vault를 사용하여 암호화하는 예시를 든다.
🔊🔊🔊 중요 중요
먼저, encrypt_string을 사용하여 평문 패스워드를 암호화 한다.  
이때, password store를 사용한다.
```
$ echo 'votmdnjem123' > .passfile_dev
$ echo 'votmdnjem432' > .passfile_operation
$ ansible-vault encrypt_string --vault-id dev@.passfile_dev 'roqkf123' --name 'ansible_sudo_password'
ansible_sudo_password: !vault |
           $ANSIBLE_VAULT;1.2;AES256;dev
           306asdasdasdasdasd666333643061636561303338373661313838333565653635353162
           326...
           ...
           6330
$ ansible-vault encrypt_string --vault-id operation@.passfile_operation 'roqkf432' --name 'ansible_sudo_password'
ansible_sudo_password: !vault |
           $ANSIBLE_VAULT;1.2;AES256;operation
           332425ssdasdasdasd666333643061636561303338373661313838333565653635353162
           326...
           ...
           6330
```

생성된 암호화된 패스워드를 inventory에 저장한다.
```
---
all:
  children:
    oper:
      children:
        oper_server1:
          hosts: foo.example.com
        oper_server2:
          hosts: bar.example.com
      vars:
        ansible_sudo_pass: !vault |
                  $ANSIBLE_VAULT;1.2;AES256;dev
                  306asdasdasdasdasd666333643061636561303338373661313838333565653635353162
                  326...
                  ...
                  6330
    dev:
      children:
        dev_server1:
          hosts: foofoo.example.com
        dev_server2:
          hosts: barbar.example.com
      vars:
        ansible_sudo_pass: !vault |
                  $ANSIBLE_VAULT;1.2;AES256;operation
                  332425ssdasdasdasd666333643061636561303338373661313838333565653635353162
                  326...
                  ...
                  6330
```
이제, 개발 환경에서 playbook을 실행한다.
```
$ ansible-playbook -i inventory.yml playbook/user_password_change.yml --vault-id dev@.passfile_dev --extra-vars "user_name=test variable_host=dev new_password=test123"
```

**inventory에서 vars를 넘겨는 것 이외에 playbook에서 vars_files를 넘겨주는 방법을 알아본다**
먼저 vars_file을 다음과 같이 생성해준다.
```
$ ansible-vault create --vault-id dev@.passfile_dev vars_store
ansivle_sudo_pass: "votmdnjem123"
```
inventory.yml에서 vars 값을 삭제
```
---
all:
  children:
    oper:
      children:
        oper_server1:
          hosts: foo.example.com
        oper_server2:
          hosts: bar.example.com
    dev:
      children:
        dev_server1:
          hosts: foofoo.example.com
        dev_server2:
          hosts: barbar.example.com
```
playbook에 vars_files를 통해 vars_store를 넘겨준다.  
path : playbook/user_password_change.yml
```
---
- name: Change passwd
  become: true
  hosts: "{{ variable_host | default('dev')}}"
  remote_user: devops
  tasks:
  - name: Change password of existing user
    become: true
    become_method: sudo
    user: name={{ user_name }} update_password=always password={{ new_password|password_hash('sha512') }}
    register: result
  - debug:
      var: result
  vars_files:
    - /app/ansible/vars_store
```
ansible-playbook
```
$ ansible-playbook -i inventory.yml playbook/user_password_change.yml --vault-id dev@.passfile_dev --extra-vars "user_name=test variable_host=dev new_password=test123"
```
