
# RDS




RDS(Amazon Relational Database Service)는 AWS에서 제공하는 관계형 데이터베이스 서비스이다. 데이터베이스 관리 작업을 자동화해주는 서비스이다. 



기존에는 EC2 인스턴스를 생성하고 데이터베이스를 Docker로 build, run하여 사용했었는데, 따로 관리하는 것이 더 효율적일 것 같아 RDS를 사용하게 되었다.

## EC2-RDS 연동 이유


처음에는 EC2에 데이터베이스까지 함께 두었기 때문에 왜 RDS로 따로 관리해야 하는지 잘 알지 못했다. 그러나 RDS를 사용하는 이유는 다음과 같다.

### 1. 복잡한 관리 작업 대체

**RDS**는 데이터베이스를 위한 **완전 관리형 서비스**다. 데이터베이스 설치와 패치, 백업 및 복구와 같은 복잡하고 반복적인 작업을 AWS가 알아서 처리한다. EC2에 직접 데이터베이스를 구축하면 이 모든 과정을 개발자가 수동으로 관리해야 한다. 덕분에 개발자는 핵심 애플리케이션 개발에만 집중할 수 있다.

---

### 2. 고가용성

서비스 중단은 비즈니스에 치명적인 영향을 준다. RDS는 **다중 AZ(Availability Zone) 배포** 기능을 제공하여, 주 인스턴스에 문제가 발생하면 자동으로 다른 가용 영역의 보조 인스턴스로 전환된다. EC2에 직접 데이터베이스를 구성할 경우 이러한 고가용성 환경을 직접 구축하고 관리해야 하는 큰 부담이 있다.

→ **프리티어는 RDS 단일 인스턴스만 제공하므로, 다중 AZ와 같은 다중 인스턴스 구성 시 요금이 발생한다.**

---

### 3. 스토리지 용량을 자동으로 확장

데이터의 양은 시간이 지나면서 계속 증가한다. RDS는 **자동 확장 기능**을 통해 데이터 증가에 맞춰 스토리지 용량을 자동으로 늘려준다. 따라서 용량 부족으로 인해 서비스가 중단될 위험을 미리 방지할 수 있다.

**→ RDS 설정 시 추가할 수 있지만, 요금 부과 위험이 있어 이 글에서는 다루지 않았다.**

---

### 4. 강력한 보안 기능과 쉽게 연동

데이터베이스는 민감한 정보를 다루기 때문에 보안이 매우 중요하다. RDS는 **AWS의 보안 그룹 및 IAM 정책**과 쉽게 통합되어 강력한 접근 제어를 제공한다. 또한 데이터베이스의 엔드포인트를 외부에 노출하지 않고 EC2와 같은 내부 리소스만 접근을 허용하도록 설정하여 보안을 한층 더 강화할 수 있다.

## EC2 인스턴스 생성

---

RDS와 함께 사용할 EC2 인스턴스가 필요하므로, 먼저 EC2 인스턴스를 생성한다.

[AWS 서버 구축 - EC2](https://velog.io/@hariaus/AWS-%EC%84%9C%EB%B2%84-%EA%B5%AC%EC%B6%95-EC2)

### MySQL 설치

```python
sudo apt-get install mysql-client -y
```

나는 Ubuntu 서버를 사용했으며, 위 명령어로 mysql-client를 설치하였다.

## RDS 인스턴스 생성


AWS 프리 티어를 사용하고 있어 그에 맞게 설정하였다.

### 데이터베이스 종류

![](https://velog.velcdn.com/images/hariaus/post/50ee1723-e83f-410e-9909-1098b92c7cc1/image.png)

![](https://velog.velcdn.com/images/hariaus/post/7abfa955-4647-4ea6-ba64-c1eccf770afe/image.png)


---

### 템플릿

![](https://velog.velcdn.com/images/hariaus/post/7d8b778f-70ce-4f5c-8291-82920c09fa78/image.png)


---

### 데이터베이스 암호

마스터 사용자 이름과 암호는 MySQL 접속 시 필요하므로 따로 저장해두어야 한다.

![](https://velog.velcdn.com/images/hariaus/post/c7c85784-2fea-47d3-b1f7-3bacf0566adb/image.png)

---

### 스토리지

프로비저닝된 SSD는 과금이 발생할 수 있으니 주의해야 한다. 프리 티어에서는 RDS 스토리지를 최대 20GB까지 사용할 수 있다.

![](https://velog.velcdn.com/images/hariaus/post/e5fb475d-db54-4521-9156-85fcd1f3076d/image.png)


![](https://velog.velcdn.com/images/hariaus/post/777df302-275c-4779-9acf-b6fe6c3cb9d4/image.png)


스토리지 자동 조정 기능은 임계값을 초과하면 스토리지가 늘어나 과금될 가능성이 있으므로 체크를 해제하는 것이 좋다.

---

### 보안 그룹

`EC2 컴퓨팅 리소스에 연결`을 선택하면 퍼블릭 액세스를 활성화하지 못하므로 `EC2 컴퓨팅 리소스에 연결 안 함`을 선택해준다. 나중에 VPC 설정을 통해서 EC2에서 RDS에 접속할 수 있도록 설정할 수 있다.
![](https://velog.velcdn.com/images/hariaus/post/044f033a-5853-437e-a113-0f3f5cdb3b5b/image.png)


보안 그룹은 EC2-RDS 연동 과정에서 자동으로 추가되므로 따로 지정할 필요가 없다.

![](https://velog.velcdn.com/images/hariaus/post/e7a3ec55-6aae-4bfb-a459-57d8764225a4/image.png)


또한 서울 Region에는 ap-northeast-2a, ap-northeast-2c 두 개의 가용영역(AZ)이 존재한다. EC2와 연동하려면 EC2와 동일한 가용영역을 선택하는 것이 좋다.

---

### 추가 설정

그 외 옵션은 상황에 맞게 선택하면 된다. 나는 나머지 옵션은 기본값으로 두었다.

## EC2 - RDS 연동


연동 과정은 보안 그룹에 서로의 보안 그룹을 추가해주는 방식으로 진행된다. 인바운드 규칙을 수정해도 되지만, AWS에서 제공하는 기능을 사용하였다.

### 자동 보안그룹 설정

![](https://velog.velcdn.com/images/hariaus/post/a64cdf07-a1ac-4dc9-95bc-4f96e9014c02/image.png)


![](https://velog.velcdn.com/images/hariaus/post/c5a0076d-62b0-4d76-b8e5-f775c618a66c/image.png)


EC2 연결 설정을 통해 RDS와 EC2를 연동할 수 있다.

![](https://velog.velcdn.com/images/hariaus/post/676281e8-390c-47e9-832f-7a1d935ba899/image.png)


설정을 완료하면 EC2-RDS 연동이 성공적으로 이루어진 것을 확인할 수 있다.

> RDS가 완전히 생성된 후 연동을 진행하는 것이 좋다. 생성 도중에는 보안 그룹이 할당되지 않는 경우가 발생할 수 있다. 이 경우 보안 그룹에 서로의 보안 그룹을 수동으로 추가하면 된다.
> 

---

### 연동 확인

EC2에 mysql-client를 설치했다면, 다음 명령어로 연동을 확인할 수 있다.

```bash
mysql -u [사용자 이름] -p -h [엔드포인트]
```

사용자 이름은 RDS 데이터베이스 생성 시 입력한 이름이고, 엔드포인트는 데이터베이스 상세 정보에서 확인할 수 있다.

---

이후 애플리케이션의 데이터베이스 주소를 RDS 엔드포인트로 변경한 후 배포하면 EC2-RDS 연동이 완료된다.

## 참고 자료



[EC2 & RDS 인스턴스 생성 및 연동](https://velog.io/@ncookie/EC2-RDS-%EC%9D%B8%EC%8A%A4%ED%84%B4%EC%8A%A4-%EC%83%9D%EC%84%B1-%EB%B0%8F-%EC%97%B0%EB%8F%99#ec2%EC%97%90%EC%84%9C-%EC%A0%91%EC%86%8D)

[AWS 2편: RDS 생성 후 EC2 와 연동](https://bcp0109.tistory.com/357)