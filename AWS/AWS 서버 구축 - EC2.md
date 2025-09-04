
웹 애플리케이션 서버와 클라이언트가 통신하기 위해서는 서버를 물리 서버나 클라우드 환경에 배포해야 한다. 이 글에서는 **AWS EC2**를 활용해 클라우드 서버를 구축하고, Spring 프로젝트를 배포하는 방법을 정리한다.

# EC2 (Elastic Compute Cloud)


**EC2**는 AWS에서 제공하는 가상 클라우드 컴퓨터 서비스이다. 사용자는 성능, 용량, 비용에 맞게 맞춤형 인스턴스를 선택하여 활용할 수 있다.


본 글에서는 Spring 프로젝트를 배포할 클라우드 서버를 구축한다.

주요 학습 목표는 다음과 같다.

1. 자바 실행 파일(JAR)을 업로드하는 방법
2. EC2 인스턴스를 생성하는 방법

![](https://velog.velcdn.com/images/hariaus/post/6d37ad3d-3c84-438e-bb72-a49be676b74a/image.png)


## 0. IAM 계정 생성



**IAM 계정**은 루트 계정 보안을 강화하기 위해 AWS 리소스 접근 권한을 분리하여 제공하는 계정이다.


S3에 실행 파일을 올리고 EC2에서 접근하기 위해 IAM 계정을 생성한다.

![](https://velog.velcdn.com/images/hariaus/post/692cdab1-24de-43ab-b287-fb309b9427b3/image.png)


IAM 콘솔에서 사용자 생성을 클릭한다.

![](https://velog.velcdn.com/images/hariaus/post/9e8e15a4-dd0b-4dc5-bbd0-792add68509b/image.png)


권한 정책에서 `AmazonS3FullAccess`를 선택한다.

![](https://velog.velcdn.com/images/hariaus/post/12dad067-ca3c-41cd-aab2-e6f622f31257/image.png)


![](https://velog.velcdn.com/images/hariaus/post/a2517230-e75d-4fac-b765-e9881a392a1c/image.png)

![](https://velog.velcdn.com/images/hariaus/post/40ead5eb-c185-4450-96fc-60885ca0173c/image.png)


사용자 생성 후 액세스 키를 발급받고 `.csv` 파일로 다운로드한다. (이후에는 다시 확인할 수 없으므로 반드시 저장한다.)

## 1. EC2 인스턴스 생성하기


AWS 콘솔에 로그인한 후 지역이 서울(ap-northeast-2)로 설정되어 있는지 확인한다.

![](https://velog.velcdn.com/images/hariaus/post/41d6e723-f5b8-443a-b4fb-47e32128042b/image.png)


EC2 서버를 구축하게 되면 네트워크 설정을 하게 된다. 네트워크 설정은 간단히 말해서 어떤 컴퓨터가 내 서버에 접근가능한지를 설정하는 것이다. 

이를 보안 그룹을 지정해두면 편리하게 쓸 수 있다. Spring Boot 서버 실행을 위해 **HTTP, TCP**, 그리고 원격 접속을 위해 **SSH**를 허용한다.

![](https://velog.velcdn.com/images/hariaus/post/13521231-1765-4e44-9a0e-5041ae978e9a/image.png)


EC2 대시보드에서 **인스턴스 시작** 버튼을 클릭한다.

![](https://velog.velcdn.com/images/hariaus/post/31295b06-9ba4-4455-bf2a-370e17bd81e5/image.png)


OS는 **Ubuntu**를 선택한다. Ubuntu는 경량화되어 있으며, 오픈소스라 추가 비용이 들지 않는다.

인스턴스 유형은 프리 티어 범위인 **t2.micro**를 선택한다.

SSH 접속을 위한 키 페어를 생성한다.

![](https://velog.velcdn.com/images/hariaus/post/186406ee-568a-4d5d-920c-2f2f09a8bd91/image.png)


퍼블릭 IP는 비활성화하고 보안 그룹은 전에 만들어두었던 것을 사용한다. 우린 탄력적 IP를 사용할 것이다. 

- 퍼블릭 IP : EC2를 종료하고 다시 시작하거나 하면 IP 주소가 바뀐다.
- 탄력적 IP : EC2를 종료하더라도 IP 주소는 변하지 않는다. DNS 설정이 쉽다.

나머지 설정은 기본으로 두고, 제일 아래에 있는 주황색 버튼(인스턴스 시작)을 누르면 된다. 

![](https://velog.velcdn.com/images/hariaus/post/367a2e41-397d-4377-b80d-fc94aee655e2/image.png)


퍼블릭 IP를 비활성화 했으므로 탄력적 IP를 인스턴스에 부여해야한다. 위에 있는 탄력적 IP 주소 할당을 누른 뒤, 그대로 할당하면 된다. 

![](https://velog.velcdn.com/images/hariaus/post/c71c103d-7734-484f-a30a-47c29aaa41c2/image.png)


탄력적 IP 주소 연결 버튼을 통해 방금 만든 EC2 인스턴스와 탄력적 IP를 연결한다. EC2를 지우게 되면 탄력적 IP 주소 릴리스를 하도록 하자. 하지 않으면 요금부과가 된다고 한다. 

![](https://velog.velcdn.com/images/hariaus/post/6147150e-3b5c-4e5a-a6c7-9c484a33e6cc/image.png)


이제 S3에서 EC2로 파일 전송이 가능하도록, S3를 만들고 스프링 실행 파일을 넣도록 하자. 버킷 만들기를 누른 후 기본 설정에서 바꾸지 않고 만들도록 한다. 

![](https://velog.velcdn.com/images/hariaus/post/9a2f87fe-30e9-403c-ab4d-37ff48b439cf/image.png)


버킷 생성 후에 버킷에 들어가 업로드 버튼을 눌러 스프링 실행 파일을 추가한다. 

## 2. EC2 접속


```bash
$ ssh -i {YOUR_KEY_PAIR_FILE.pem} {USER_NAME}@{AWS_PUBLIC_DNS_}
```

EC2 인스턴스가 준비되면 SSH로 접속한다.

```bash
aws configure

AWS Access Key ID [None]:  액세스 키 ID 입력
AWS Secret Access Key [None]:  비밀 액세스 키 입력
Default region name [None]:  ap-northeast-2 
Default output format [None]:  생략
```

이후 AWS CLI를 설정한다. 

정상적으로 설정되면 `aws s3 ls` 명령어를 통해 버킷 목록이 출력된다.

```bash
// aws s3 cp "ec2 폴더 경로" "s3 폴더 경로"
// --recursive 옵션은 하위 폴더 전체를 포함한다는 옵션

aws s3 cp --recursive ./ s3://community-test
```

이제 S3에서 EC2로 파일을 복사한다.

## 2. 파일 실행하기


EC2에 복사된 JAR 파일을 실행한다.

```bash
java -jar myFile.jar
```

이후 Postman 등을 통해 API 요청을 보내면, 서버가 정상적으로 동작하는 것을 확인할 수 있다.

![](https://velog.velcdn.com/images/hariaus/post/b80229f6-b28a-4531-b00b-41088cd0d0c4/image.png)


# 참고 자료


[[AWS] EC2에서  S3로 파일 복제하기](https://velog.io/@wngud4950/AWS-EC2%EC%97%90%EC%84%9C-S3%EB%A1%9C-%ED%8C%8C%EC%9D%BC-%EB%B3%B5%EC%A0%9C%ED%95%98%EA%B3%A0-%EC%9D%B4%EB%AF%B8%EC%A7%80-%EC%A0%80%EC%9E%A5%ED%95%98%EA%B8%B0-1)