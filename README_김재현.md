
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
