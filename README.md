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

# 구현:

분석/설계 단계에서 도출된 헥사고날 아키텍처에 따라, 각 BC별로 대변되는 마이크로 서비스들을 스프링부트로 구현

구현한 각 서비스를 로컬에서 실행하는 방법은 아래와 같다 (각자의 포트넘버는 8081 ~ 808n 이다)

```
   mvn spring-boot:run
```

## SAGA Pattern
쓰기에 있어 Pub-Sub의 Saga 패턴이 사용

방 등록 -> 예약 요청(고객) -> 예약 승인(집주인) -> 결제 진행 -> 예약 확인 -> 방 예약 완료

![image](https://user-images.githubusercontent.com/37835544/181160084-e0ea78b2-3e61-4ae1-8515-7c2923fd8f3c.png)


## CQRS Pattern

집주인 및 고객이 현황을 조회 할 수 있도록 viewPage를 CQRS 로 구현
- room, reservation, payment 개별 Aggregate 데이터 조회가 가능
- 비동기식으로 처리되어 발행된 이벤트 기반 Kafka 를 통해 수신/처리 되어 별도 Table 에 관리
- Table 모델링 (ROOMVIEW)

- viewpage MSA ViewHandler 를 통해 구현 ("RoomRegistered" 이벤트 발생 시, Pub/Sub 기반으로 별도 Roomview 테이블에 저장)

![image](https://user-images.githubusercontent.com/37835544/181169708-77fa4897-7f56-4731-94a9-c3f5a0be3905.png)

- Event별로 Table 데이터 변경

![image](https://user-images.githubusercontent.com/37835544/181169941-100c3f8d-b7ec-40a4-808a-d1b6b33fb5d8.png)



## Gateway
      - gateway 스프링부트 App을 추가 후 application.yaml내에 각 마이크로 서비스의 routes 를 추가하고 각 서버의 URI 작성
       
          - application.yaml 예시
            ```
		spring:
		  profiles: default
		  cloud:
		    gateway:
		      routes:
			- id: room
			  uri: http://localhost:8081
			  predicates:
			    - Path=/rooms/**, /reviews/** 
			- id: payment
			  uri: http://localhost:8082
			  predicates:
			    - Path=/payments/** 
			- id: reservation
			  uri: http://localhost:8083
			  predicates:
			    - Path=/reservations/** 
			- id: Message
			  uri: http://localhost:8084
			  predicates:
			    - Path=/messages/** 
			- id: viewpage
			  uri: http://localhost:8085
			  predicates:
			    - Path= /roomviews/**
			- id: frontend
			  uri: http://localhost:8080
			  predicates:
			    - Path=/**
	    
            ```


# Correlation/Compensation(Unique Key)

PolicyHandler에서 처리 시 어떤 건에 대한 처리인지를 구별하기 위한 Correlation-key 구현을 
이벤트 클래스 안의 변수로 전달받아 서비스간 연관된 처리를 구현

예약(Reservation)을 하면 동시에 연관된 방(Room), 결제(Payment) 등의 서비스의 상태가 적당하게 변경이 되고,
예약건의 취소를 수행하면 다시 연관된 방(Room), 결제(Payment) 등의 서비스의 상태값 등의 데이터가 적당한 상태로 변경


예약 전 - 방 상태(status = true)

![image](https://user-images.githubusercontent.com/37835544/181166258-2c4b0d4e-0d5a-44ec-87eb-4ef4551bf410.png)


예약 후 - 방 상태(status = false)

![image](https://user-images.githubusercontent.com/37835544/181166377-340c6fe8-0c62-49ef-abc9-92c0d19b576b.png)


예약 후 - 결제 상태(payments/1 존재)

![image](https://user-images.githubusercontent.com/37835544/181166457-c2a9ee2f-13de-4137-83b3-f193b1ac267f.png)


예약 취소 - 방 상태(status = true)

![image](https://user-images.githubusercontent.com/37835544/181167738-7e592ddd-62f2-42c7-bae3-488c23cae1e2.png)


예약 취소 - 결제 상태(payment 삭제)

![image](https://user-images.githubusercontent.com/37835544/181166867-88016341-0ed6-43f4-8ef2-ffb8bb3ba062.png)



## Request/Response(Feign Client / Sync.Async)

ReservationAccepted 시 approvePayment 호출은 Req/Res 방식을 이용하였고, FeignClient 를 이용하여 처리

```
# PaymentService.java

@FeignClient(name = "payment", url = "${api.url.payment}")
public interface PaymentService {
    @RequestMapping(method = RequestMethod.POST, path = "/payments")
    public void approvePayment(@RequestBody Payment payment);
    // keep

}


```

- 예약 승인을 받은 직후 (@PostUpdate) 가능상태 확인 및 결제를 동기(Sync)로 요청하도록 처리
```
# Reservation.java (Entity)

    @PostPersist
    public void onPostUpdate(){
    
            ReservationAccepted reservationAccepted = new ReservationAccepted(this);
            reservationAccepted.publishAfterCommit();

            msaneil.external.Payment payment = new msaneil.external.Payment();
            // mappings goes here
            payment.setRsvId(this.getRsvId());
            payment.setRoomId(this.getRoomId());
            payment.setStatus(true);
            payment.setPayId(this.getRsvId());

            ReservationApplication.applicationContext
                .getBean(msaneil.external.PaymentService.class)
                .approvePayment(payment);
    }
```


- 결제 (payment) 서비스 중단 시 결과

![image](https://user-images.githubusercontent.com/37835544/181162844-f2ec96d1-2ab0-48f3-97f5-9a6c962e240b.png)

- 결제 서비스 실행 후 결과

![image](https://user-images.githubusercontent.com/37835544/181163106-417a5721-55c6-4347-b454-8583fe66bb5e.png)


## Circuit Breaker

* Circuit Breaker 프레임워크 : istio 사용

예약(reservation)--> 룸(room) 시의 연결이 RESTful Request/Response 로 연동되어있고, 예약 요청이 과도할 경우 Circuit Breaker 발생

- DestinationRule 를 생성하여 circuit break 가 발생할 수 있도록 MAX 값을 1로 설정
```
# destination-rule.yml
apiVersion: networking.istio.io/v1alpha3
kind: DestinationRule
metadata:
  name: dr-room
  namespace: airbnb
spec:
  host: room
  trafficPolicy:
    connectionPool:
      http:
        http1MaxPendingRequests: 1
        maxRequestsPerConnection: 1
```

* istio-injection 활성화 및 room pod container 확인

```
kubectl get ns -L istio-injection
kubectl label namespace airbnb istio-injection=enabled 
```

* 부하테스터 siege 툴을 통한 서킷 브레이커 동작 확인:

siege 실행

```
kubectl run siege --image=apexacme/siege-nginx -n airbnb
kubectl exec -it siege -c siege -n airbnb -- /bin/bash
```


- 동시사용자 1명 테스트 시 모두 201 정상 응답
```
siege -c1 -t10S -v --content-type "application/json" 'http://room:8080/rooms POST {"desc": "Oceㅁn View"}'

** SIEGE 4.0.4
** Preparing 1 concurrent users for battle.
The server is now under siege...
HTTP/1.1 201     0.49 secs:     254 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.05 secs:     254 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.02 secs:     254 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.03 secs:     254 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.02 secs:     254 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.02 secs:     254 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.03 secs:     254 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.03 secs:     254 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.03 secs:     254 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.03 secs:     256 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.03 secs:     256 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.02 secs:     256 bytes ==> POST http://room:8080/rooms
```

- 동시사용자 2명 테스트 시 503 에러 116개 발생
```
siege -c2 -t10S -v --content-type "application/json" 'http://room:8080/rooms POST {"desc": "Ocean View"}'

** SIEGE 4.0.4
** Preparing 2 concurrent users for battle.
The server is now under siege...
HTTP/1.1 201     0.04 secs:     258 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.02 secs:     258 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.02 secs:     258 bytes ==> POST http://room:8080/rooms
HTTP/1.1 503     0.10 secs:      81 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.04 secs:     258 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.05 secs:     258 bytes ==> POST http://room:8080/rooms
HTTP/1.1 503     0.01 secs:      81 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.22 secs:     258 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.08 secs:     258 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.07 secs:     258 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.01 secs:     258 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.03 secs:     258 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.02 secs:     258 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.01 secs:     258 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.02 secs:     258 bytes ==> POST http://room:8080/rooms
HTTP/1.1 503     0.01 secs:      81 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.01 secs:     258 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.02 secs:     258 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.02 secs:     258 bytes ==> POST http://room:8080/rooms
HTTP/1.1 201     0.02 secs:     258 bytes ==> POST http://room:8080/rooms
HTTP/1.1 503     0.00 secs:      81 bytes ==> POST http://room:8080/rooms

Lifting the server siege...
Transactions:                   1264 hits
Availability:                  92.02 %
Elapsed time:                   8.86 secs
Data transferred:               0.48 MB
Response time:                  0.01 secs
Transaction rate:             164.12 trans/sec
Throughput:                     0.04 MB/sec
Concurrency:                    1.98
Successful transactions:        1264
Failed transactions:             116
Longest transaction:            0.03
Shortest transaction:           0.00
```

- 운영시스템은 죽지 않고 지속적으로 CB 에 의하여 적절히 회로가 열림과 닫힘 발생

## Autoscale(HPA)
```
room deployment.yml 파일에 resources 설정 추가
```
![image](https://user-images.githubusercontent.com/109929530/181161525-332c15db-2260-4c41-9b9f-4d675d4179ee.png)

Autoscale 설정
cpu-percent=50 : Pod 들의 평균 CPU 사용율
(Pod의 평균 CPU 사용율이 100 milli-cores(50%)를 넘게되면 HPA 발생)
```
kubectl autoscale deployment php-apache --cpu-percent=50 --min=1 --max=10
```

부하 테스트 (동시사용자 : 100명 / 시간 : 50초)
```
siege -c50 -t50S -v --content-type "application/json" 'http://room:8080/rooms POST {"desc": "Ocean View"}'
```
siege 로그 확인 : 성공률 100% 달성
```
Lifting the server siege...
Transactions:                  9032 hits
Availability:                 100.00 %
Elapsed time:                  45.22 secs
Data transferred:               2.80 MB
Response time:                  0.34 secs
Transaction rate:             135.70 trans/sec
Throughput:                     0.07 MB/sec
Concurrency:                   67.42
Successful transactions:        9032
Failed transactions:               0
Longest transaction:            2.34
Shortest transaction:           0.01
```

## Zero-Downtime Deploy(Readiness Probe)

- Zero-Downtime 배포 테스트를 위해 Autoscaler / CB 설정 삭제
```
kubectl delete destinationrules dr-room -n airbnb
kubectl label namespace airbnb istio-injection-
kubectl delete hpa room -n airbnb
```

- 새버전으로의 배포 시작
```
kubectl set image ...
```

- seige로 배포 시 Availability 100% -> 81% 하락 확인

```
siege -c100 -t60S -r10 -v --content-type "application/json" 'http://room:8080/rooms POST {"desc": "Ocean View"}'


Transactions:                   3927 hits
Availability:                  81.64 %
Elapsed time:                  11.24 secs
Data transferred:               0.98 MB
Response time:                  0.12 secs
Transaction rate:             396.14 trans/sec
Throughput:                     0.08 MB/sec
Concurrency:                   65.21
Successful transactions:        3927
Failed transactions:             609
Longest transaction:            0.88
Shortest transaction:           0.00

```
- deployment.yaml 의 readiness probe 설정

![image](https://user-images.githubusercontent.com/109929530/181144376-c53b08e9-0c35-4cef-9a38-85c2c7ac24ad.png)


- 재배포 시 Availability 확인: 100% 달성
```
Lifting the server siege...
Transactions:                  13654 hits
Availability:                 100.00 %
Elapsed time:                  46.38 secs
Data transferred:               4.18 MB
Response time:                  0.16 secs
Transaction rate:             325.33 trans/sec
Throughput:                     0.11 MB/sec
Concurrency:                   99.20
Successful transactions:       13654
Failed transactions:               0
Longest transaction:            1.10
Shortest transaction:           0.00

```
