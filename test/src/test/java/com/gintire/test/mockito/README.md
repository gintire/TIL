# Mockito

Mockito는 JUnit 위에서 동작하며 Mocking과 Verification을 도와주는 프레임워크이다.
### 차별점
1. 테스트 그 자체에 집중
2. 테스트 스텁을 만드는 것과 검증을 분리
3. Mock 만드는 방법을 단일화
4. 테스트 스텁을 만들기 쉬움
5. API가 간단
6. 프레임워크가 지원해주지 않으면 안되는 코드를 최대한 배제
7. 실패 시에 발생하는 에러추적이 깔끔

### 기본 사용법
Mockito는 Stub 작성과 Verify가 중심을 이루며 다음과 같은 순서로 진행
1. CreateMock : 인터페이스에 해당하는 Mock 객체를 만든다.
2. Stub : 테스트에 필요한 Mock 객체의 동작을 지정
3. Exercise : 테스트 메소드 내에서 Mock 객체를 사용한다.
4. Verify : 메소드가 예상대로 호출됐는지 검증

### 주요 요소
* mock() / @Mock : mock을 생성
  * 선택적으로 Answer / MockSettings를 통해 작동 방식을 지정
  * mock이 어떻게 동작하는지 when() / given()을 통해 지정
  * 제공된 answers이 필요에 맞지 않으면, Answer 인터페이스를 확장해서 직접 작성
* spy() / @Spy : 특정 mocking, 실제 메서드가 호출되지만, verified와 stubbed을 사용할 수 있음
* InjectMocks : @Spy 또는 @Mock 주석이 달린 mocks / spied 필드 자동 삽입
* verify() :  주어진 인수로 메서드가 호출되었는지 확인
  * 유연한 인수 일치를 사용할 수 있음. 예를 들어 any()를 통해 any 표현을 사용 가능
  * 또는 대신 @Captor를 사용하여 호출 된 인수 캡처
* BDD Mockito를 통해 Behavior-Driven development 문법 사용

### void가 리턴값이 mock 테스트
doNothing()은 Mockito의 void 메서드에 대한 기본 설정이다.
- doAnswer() : void를 반환하는 모의 객체 메서드가 호출 될 때이를 사용하여 일부 작업을 수행 할 수 있습니다. 
- doThrow() : 예외를 발생시키는 void 메서드를 stub하려면 doThrow ()를 사용할 수 있습니다.
