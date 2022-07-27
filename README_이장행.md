![image](https://github.com/jaehyunkim12/hw4-msa-capstone-project/blob/main/images/airbnb.jpg)

# 숙소예약(AirBnB)

# 분석/설계

## AS-IS 조직 (Horizontally-Aligned)
  ![image](https://github.com/jaehyunkim12/hw4-msa-capstone-project/blob/main/images/asis.png)

## TO-BE 조직 (Vertically-Aligned)  
  ![image](https://github.com/jaehyunkim12/hw4-msa-capstone-project/blob/main/images/tobe.png)


## 모델링 결과
* MSAEz 로 모델링한 결과:  https://labs.msaez.io/#/storming/bjjILDCdbDV6g79R6na93SWK29E3/e52b0637612b3f8e2c55366db1eacf6e


### 완성된 모형

![image](https://github.com/jaehyunkim12/hw4-msa-capstone-project/blob/main/images/%EC%A0%84%EC%B2%B4%20%EA%B7%B8%EB%A6%BC-1.png)

      - 도메인 서열 분리 
        - Core Domain:  reservation, room : 없어서는 안될 핵심 서비스이며, 연간 Up-time SLA 수준을 99.999% 목표, 배포주기는 reservation 의 경우 1주일 1회 미만, room 의 경우 1개월 1회 미만
        - Supporting Domain:   message, viewpage : 경쟁력을 내기위한 서비스이며, SLA 수준은 연간 60% 이상 uptime 목표, 배포주기는 각 팀의 자율이나 표준 스프린트 주기가 1주일 이므로 1주일 1회 이상을 기준으로 함.
        - General Domain:   payment : 결제서비스로 3rd Party 외부 서비스를 사용하는 것이 경쟁력이 높음 


### 완성본에 대한 기능적/비기능적 요구사항을 커버하는지 검증

![image](https://github.com/jaehyunkim12/hw4-msa-capstone-project/blob/main/images/%EC%A0%84%EC%B2%B4%20%EA%B7%B8%EB%A6%BC-2.png)

※ 완성된 모델은 모든 요구사항을 커버함.
1. 호스트가 숙소 등록/수정/삭제한다.  (남색)
2. 고객이 숙소 선택 및 예약한다.        (진한빨강)
4. 호스트가 고객 예약을 accept 한다.   (녹색)
5. 고객이 숙소 결제한다.                   (갈색)
6. 결제가 되면 결제 내역(Message)이 전달된다. (갈색)
7. 고객이 예약을 취소할 수 있다. (하늘)
8. 호스트가 예약을 취소할 수 있다. (주황)
9. 예약이 취소되면 취소 내역(Message)이 전달된다.
10. 고객이 숙소 후기(review)를 남길 수 있다.   (회색)
11. 호스트가 고객 후기(review)를 남길 수 있다. (회색)
12. 숙소 정보/예약날짜/인원수/예약상태 등을 한 화면에서 확인할 수 있다.(viewpage)



### 비기능 요구사항에 대한 검증

![image](https://github.com/jaehyunkim12/hw4-msa-capstone-project/blob/main/images/%EC%A0%84%EC%B2%B4%20%EA%B7%B8%EB%A6%BC-4.png)

- 마이크로 서비스를 넘나드는 시나리오에 대한 트랜잭션 처리
- 호스트 예약 accept 시 결제 처리 : 호스트가 예약 accept 하지 않은 예약은 받지 않는다고 결정하여 Request-Response 방식 처리
- 결제 완료시 Host 연결 및 예약처리: reservation 에서 room 마이크로서비스로 예약요청이 전달되는 과정에 있어서 room 마이크로 서비스가 별도의 배포주기를 가지기 때문에 Eventual Consistency 방식으로 트랜잭션 처리함.
- 나머지 모든 inter-microservice 트랜잭션: 예약상태, 후기처리 등 모든 이벤트에 대해 데이터 일관성의 시점이 크리티컬하지 않은 모든 경우가 대부분이라 판단, Eventual Consistency 를 기본으로 채택함.
