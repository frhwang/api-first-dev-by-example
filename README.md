# 예제로 살펴보는 API First 개발

> 본 자료를 가지고 진행한 [스크린캐스트 영상](https://youtu.be/1sTYV1hbzVU)이 공개되었으니 참고하시기 바랍니다.

## 왜 API First 개발인가?

- 공급자 입장보다 사용자(Client) 입장에서 API를 설계해 사용자에게 편리하고 좋은 설계를 할 가능성을 높인다.
- API 추상화 수준을 적절히 유지하는 데 도움을 준다.
  - 내부 구현 후 설계된 API는 내부 추상화 수준이 노출되거나 종속되기 쉬움
  - API를 먼저 설계하면 내부 추상화 수준과 절연된 추상화 수준을 갖기에 유리
- 좀 더 이른 시점에 사용자와 API 스펙을 논의하고 변경하기가 용이하다.
  - 내부 구현 후 만들어진 API를 변경하려면 비용이 훨씬 비쌈
- 구체적인 내부 구현에 대한 고민을 좀 더 미룰 수 있다.
  - 개발 시 머릿속의 추상화 수준을 뒤섞지 않고 단순하게 유지하기 좋음
  - 좋은 아키텍쳐의 근간

## API First로 간단한 샘플 앱 개발해보기

### 1. 요구사항 검토

> 아주 간단한 할 일(Todo) 관리 API 서버 애플리케이션

- 할 일 등록 : 내용과 함께 할 일을 등록할 수 있음
- 할 일 목록 조회 : 등록된 할 일을 모두 조회할 수 있음
- 할 일 삭제 : 특정 할 일 항목 하나를 삭제할 수 있음

### 2. 개념적인 모델링 및 필요 API 도출

#### 개념적인 모델링

- 개념적(혹은 멘탈) 모델링을 통해 구현할 모델에 대한 다소 추상적이지만 핵심적인 큰 그림을 잡아본다.
- 샘플 앱은 굉장히 단순한 할 일(Todo) 모델이 하나 존재

#### 필요한 API를 대략적으로 정리

- 큰 그림이 그려지면 어떤 API들이 필요할지 대략적으로 정리해본다.
- 샘플 앱
  - 할 일 등록 : `POST /todos`
  - 할 일 목록 조회 : `GET /todos`
  - 할 일 삭제 : `DELETE /todos/{id}`

### 3. API 디자인 및 문서화

- 실제로 노출할 API를 디자인하고 `Controller`를 구현해본다.
- WebMvcTest와 Spring REST Docs를 이용해 API 테스트를 작성하고 문서화도 함께 진행한다.
- 이 때 협력하는 `Service`는 Mock(또는 Fake) 객체를 이용해 인터페이스만 발견하고 실제 구현을 미룬다.
- 최종적으로 asciidoctor gradle task(`./gradlew asciidoctor`)를 실행하면 완전한 API 문서(`build/asciidoc/html5/index.html`)가 생성된다.

### 4. 핵심 도메인 모델 구현

- 핵심적인 도메인 모델과 수반되는 저장소(Repository)를 구현한다.
- 이 단계에서는 도메인 모델의 필요한 기능을 다 구현해 보는 것이 아니라 아주 기본적인 골격만 구성해 본다는 느낌으로 접근한다.
- 샘플 앱에서는 아주 단순한 도메인 모델 하나(Todo)만 존재하고 Spring Data의 `JpaRepository`가 제공하는 기본 인터페이스만 이용하므로 테스트를 작성하는 것이 큰 의미가 없어 보일 수도 있다.
- 하지만 도메인 모델이 좀 더 복잡하다면 모델의 Entity Graph가 의도대로 잘 영속되고 조회 가능한지 테스트로 살펴보는 것은 모델링을 검증하는데 꽤 유용할 수 있다.
- Repository까지 포함한 테스트를 작성할 때는 `@SpringBootTest` 보다는 `@DataJpaTest`를 이용해 영속성을 테스트하는 데 필요한 Spring TestContext만 이용하는 것이 좀 더 효율적이다.

### 5. 응용 서비스 구현 

- 응용 서비스(Application Service)는 도메인 객체들과 협력해 사용 사례(Use case)를 구현한다. (도메인 로직이 응용 서비스에 직접 구현되지 않게 주의)
- 응용 서비스의 역할은 도메인 객체들의 행위를 `Orchestration`하는 것에 가깝기 때문에 협력 객체들을 Mock Object로 대체해 인터페이스를 발견하면서 구현할 수 있다. (통합 테스트가 아님에 주의)
- 이 단계에서 새롭게 발견된 도메인 모델의 인터페이스 중 테스트해 볼만한 가치가 있는 것들은 단위 테스트를 추가하거나 4번 단계의 Repository 테스트에 추가해본다.

## 참고

- [msbaek/atdd-example](https://github.com/msbaek/atdd-example)
- [Spring REST Docs](https://spring.io/projects/spring-restdocs)
- [springdoc-openapi](https://springdoc.org)
- [Spring REST Docs vs OpenAPI](https://www.baeldung.com/spring-rest-docs-vs-openapi)
- [Swagger와 Spring Restdocs의 우아한 조합](https://taetaetae.github.io/posts/a-combination-of-swagger-and-spring-restdocs/)
- [Spring Auto REST Docs](https://github.com/ScaCap/spring-auto-restdocs)
- [우아한형제들 기술 블로그 - Spring Rest Docs 적용](https://techblog.woowahan.com/2597/)
- [우아한형제들 기술 블로그 - Spring REST Docs에 날개를](https://techblog.woowahan.com/2678/)
