package com.gintire.test.assertj;

import com.gintire.test.domain.Player;
import com.gintire.test.domain.PlayerAssert;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Project: spring-boot-test
 * Package: com.gintire.springboottest.assertj
 * <p>
 * User: jin36
 * Date: 2021-01-12
 * Time: 오후 9:52
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class PlayerTest {

    // mvn assertj:generate-assertions 선행
    @Test
    void playerTest() {
        PlayerAssert.assertThat(new Player(
                "Michael Jordan",
                "Chicago Bulls",
                List.of("Scottie Pippen", "Tony Kukoc")))
                .hasName("Michael Jordan")
                .hasTeam("Chicago Bulls")
                .hasTeamMates("Scottie Pippen", "Tony Kukoc");
    }
}
