# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션
## 실습 단위테스트 작성
### 기능 요구사항
- [x] 구간 단위 테스트 (LineTest) 작성
- [x] 구간 서비스 단위 테스트 without Mock (LineServiceTest) 작성
- [x] 구간 서비스 단위 테스트 with Mock (LineServiceMockTest) 작성
- [x] 단위 테스트를 기반으로 비즈니스 로직을 리팩터링 하세요.

### 프로그래밍 요구사항
#### 단위 테스트 코드 작성하기
- [x] 기존 기능에 대한 테스트 작성이기 때문에 테스트 작성 시 바로 테스트가 성공해야 함 
#### 비즈니스 로직 리팩터링
- [x] 비즈니스 로직을 도메인 클래스(Line)으로 옮기기
  - 구간 추가/삭제 기능에 대한 비즈니스 로직은 현재 LineService에 대부분 위치하고 있음
- [x] 리팩터링 시 LineTest의 테스트 메서드를 활용하여 TDD 사이클로 리팩터링을 진행
  - 리팩터링 과정에서 Line 이외 추가적인 클래스가 생겨도 좋음
  - 구간 관리에 대한 책임을 Line 외 별도의 도메인 객체가 가지게 할 수 있음

## 1단계 - 지하철 구간 추가 기능 개선
### 기능 요구사항
- [x] 기존 구간에 새로운 구간 추가
  - [x] 상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없음
  - 사이에 추가되는 경우 : A-C > A-B B-C -> 새롭게 추가된 구간의 길이를 뺀 나머지를 새로운 역과 기존 역의 거리로 함.
  - 상행 종점을 추가하는 경우 : 새로운 구간의 하행역이 기존 노선의 상행 종점이어야 함.
  - 하행 종점을 추가하는 경우 : 새로운 구간의 상행역이 기존 노선의 하행 종점이어야 함.
  - [x] 역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없음
  - [x] 상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없음
- [x] 역 목록 조회
  - 구간이 추가된 순서가 아닌, 상행 종점에서 하행 종점 순서로 역 목록이 조회되어야 함.

### 프로그래밍 요구사항
- [x] 요구사항 설명을 참고하여 인수 조건을 정의
- [ ] 인수 조건을 검증하는 인수 테스트 작성
- [x] 인수 테스트를 충족하는 기능 구현
- [x] 인수 조건은 인수 테스트 메서드 상단에 주석으로 작성.
- [x] 인수 테스트 이후 기능 구현은 TDD로 진행하세요.
- [x] 도메인 레이어 테스트는 필수
- [ ] 서비스 레이어 테스트는 선택
- [x] 예외 케이스 검증 테스트 필수
