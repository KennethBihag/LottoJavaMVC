package com.kenneth.lotto;

import static org.junit.jupiter.api.Assertions.*;

import com.kenneth.lotto.service.AdminService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import com.kenneth.lotto.service.LottoService.*;
import com.kenneth.lotto.model.*;

@SpringBootTest
class LottoApplicationTests {
    private static AdminService adminService;
    @BeforeAll
    private static void setup(){
        adminService = new AdminService();
    }

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
    @ParameterizedTest
    @MethodSource("clientsAndWinnings")
    public void checkPrizeTest(Client client, WinningNumber winning, Prize prize){
        assertEquals(prize,adminService.checkPrize(client,winning));
    }

    private static Arguments[] clientsAndWinnings() {
        int[]
                arr1 = {1,2,2,4,5,5},
                arr2 = {1,10,3,4,5,6},
                arr3 = {3,20,10,44,34,6},
                arr4 = {6,23,15,2,45,3};
        Client c1 = new Client("c1",arr1),
                c2 = new Client("c2",arr2),
                c3 = new Client("c3",arr3),
                c4 = new Client("c4",arr4);
        int[] win1 = {1,2,3,4,5,6};
        WinningNumber winning = new WinningNumber();
        winning.setPicks(win1);
        return new Arguments[]{
                Arguments.of(c1,winning,Prize.SECOND),
                Arguments.of(c2,winning,Prize.FIRST),
                Arguments.of(c3,winning,Prize.NONE),
                Arguments.of(c4,winning,Prize.THIRD)
        };
    }
}
