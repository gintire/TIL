# AWS 네트워크
## VPC 만들기
VPC는 논리적 독립 네트워크를 구성할 수 있게 해주는 AWS 리소스이다.   AWS 계정을 만들면 기본 VPC (default)가 하나 생성되며 이 VPC를 사용해도 상관없다. 하지만 실전에 근접한 실습을 위해 새로운 VPC를 만든다.  

현재 어떤 VPC가 존재하는지 확인
```
aws ec2 describe-vpcs
```
```
{
    "Vpcs": [
        {
            "CidrBlock": "-----------",
            "DhcpOptionsId": "dopt-919a02fa",
            "State": "available",
            "VpcId": "---------",
            "OwnerId": "----------",
            "InstanceTenancy": "default",
            "CidrBlockAssociationSet": [
                {
                    "AssociationId": "------------",
                    "CidrBlock": "----------------",
                    "CidrBlockState": {
                        "State": "associated"
                    }
                }
            ],
            "IsDefault": true
        }
    ]
}
```
VPC 이름과 IPv4 CIDR 블록은 필수다. CIDR 블록 범위는 사설망 대역에서 선택하자.
아래 명령어는 AWS CLI (aws)로  VPC를 생성한 결과에 포함된 VpcId 값을 쿼리한 후 바로 Bash 변수로 담는 표현이다. 다른 명령어를 실행할 때 참조할 값들을 미리 변수에 담아두면 편리하다.
```
export VPC_ID=$(aws ec2 create-vpc \
  --cidr-block 10.1.1.0/24 \
  --output text \
  --query 'Vpc.VpcId')
```
```
echo ${VPC_ID}
```
생성한 VPC에 `Name 태그`를 달아준다.
```
aws ec2 create-tags \
  --resource ${VPC_ID} \
  --tags Key=Name,Value=k8s-by-kubeadm
```
해당 VPC에서 시작된 EC2 인스턴스가 Public IP주소에 해당하는 Public DNS 호스트 이름을 받도록 설정한다.
```
aws ec2 modify-vpc-attribute \
  --vpc-id ${VPC_ID} \
  --enable-dns-hostnames '{"Value": true}'
```

## 서브넷 만들기
VPC만으로는 할 수 있는게 없고 `서브넷(Subnet)`을 만들어야 한다. 즉 CIDR 블록을 가지는 단위로 VPC를 잘게 쪼개서 사용하는 것이다.  
서브넷은 실제로 리소스가 생성되는 물리적인 공간인 가용존과 연결된다.
```
export SUBNET_ID=$(aws ec2 create-subnet \
  --vpc-id ${VPC_ID} \
  --availability-zone us-east-2c \
  --cidr-block 10.1.1.0/26 \
  --output text --query 'Subnet.SubnetId')
```
```
echo ${SUBNET_ID}
```
생성한 서브넷에 Name태그를 달아준다.
```
aws ec2 create-tags \
  --resources ${SUBNET_ID} \
  --tags Key=Name,Value=k8s
```
