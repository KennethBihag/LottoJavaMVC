package com.kenneth.lotto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import com.kenneth.lotto.model.*;

@SpringBootTest
class LottoApplicationTests {

    @Test
    void contextLoads() {
    }

    @ParameterizedTest
    @MethodSource("clients")
    @DisplayName("Test Client Class Constructor")
    public void ClientCtorTest(String name, int[] picks) {
        Client client = null;
        try {
            client = new Client(name, picks);
            assertNotNull(client);
        } catch (IllegalArgumentException ex) {
            assertNull(client);
        }
    }

    private static Arguments[] clients() {
        return new Arguments[]{
                Arguments.of(null, null),
                Arguments.of("", new int[]{}),
                Arguments.of("user", new int[]{0}),
                Arguments.of("nextuser", new int[6]),
                Arguments.of("otheruser", new int[]{1, 2, 45, 30, 20})
        };
    }

}
