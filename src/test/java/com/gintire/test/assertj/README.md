# Assertj

## 기본


## assertions generator
https://joel-costigliola.github.io/assertj/assertj-assertions-generator-maven-plugin.html
https://joel-costigliola.github.io/assertj/assertj-assertions-generator.html
dependency  추가
```
<!-- https://mvnrepository.com/artifact/org.assertj/assertj-assertions-generator-maven-plugin -->
        <dependency>
            <groupId>org.assertj</groupId>
            <artifactId>assertj-assertions-generator-maven-plugin</artifactId>
            <version>2.1.0</version>
        </dependency>
```
plugin 추가
```
<plugin>
                <groupId>org.assertj</groupId>
                <artifactId>assertj-assertions-generator-maven-plugin</artifactId>
                <version>2.1.0</version>
                <configuration>
                    <classes>
                        <param>com.gintire.test.domain.Player</param>
                    </classes>
                </configuration>
            </plugin>
```

mvn 실행
```
mvn assertj:generate-assertions
```

target.generated-test-sources.assertj-assertions.com.gintire.test.domain아래에   
<class명>Assert라는 클래스가 생성된다.  

해당 클래스 내부에 assertThat문이 있고, Abstract<클래스명>Assert를 보면, 프로퍼티에 따라 메서드들이 생성되어있다.  
> 개인적인 생각 :왜 만들었는지 잘모르겠다 .....
