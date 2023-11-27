package com.kenneth.lotto;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import com.kenneth.lotto.model.*;
import com.kenneth.lotto.service.*;
import com.kenneth.lotto.service.LottoService.*;

@SpringBootTest
class LottoApplicationTests {
    private static AdminService adminService;
    private static ClientService clientService;
    private static Path csvInputPath;
    private static final List<Client> clients = new ArrayList<>();
    private static final List<WinningNumber> winnings = new ArrayList<>();

    @BeforeAll
    private static void setup(){
        adminService = new AdminService();
        clientService = new ClientService();
        String filePath = "./src/test/entries_sample.csv";
        csvInputPath = Path.of(filePath);
// create test clients and winning number
        int[]
                arr1 = {1,20,2,4,45,5},
                arr2 = {1,10,3,4,5,6},
                arr3 = {3,20,10,44,34,6},
                arr4 = {6,23,15,2,45,3},
                arr5 = {3,4,1,2,5,6};
        Client c1 = new Client("c1",arr1),
                c2 = new Client("c2",arr2),
                c3 = new Client("c3",arr3),
                c4 = new Client("c4",arr4),
                c5 = new Client("c5",arr5);
        clients.addAll(List.of(c1,c2,c3,c4,c5));
        int[] win1 = {1,2,3,4,5,6};
        WinningNumber winning = new WinningNumber();
        winning.setPicks(win1);
        winnings.add(winning);
    }

    @Test
    void contextLoads(){
    }

    @ParameterizedTest
    @MethodSource("clients")
    @DisplayName("Test Client Class Constructor")
    public void clientCtorTest(String name, int[] picks) {
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
        assertEquals(prize,adminService.checkPrizeTest(client,winning));
    }
    private static Arguments[] clientsAndWinnings() {
        return new Arguments[]{
                Arguments.of(clients.get(0),winnings.get(0),Prize.SECOND),
                Arguments.of(clients.get(1),winnings.get(0),Prize.FIRST),
                Arguments.of(clients.get(2),winnings.get(0),Prize.NONE),
                Arguments.of(clients.get(3),winnings.get(0),Prize.THIRD),
                Arguments.of(clients.get(4),winnings.get(0),Prize.GRAND)
        };
    }

    @Test
    public void parseCsvTest() throws IOException {
        Reader r = new FileReader(
                new File(csvInputPath.toUri())
        );
        final int lineLength = 64;
        char[] buffer = new char[lineLength];
        int read;
        StringBuilder sb = new StringBuilder();
        while( (read=r.read(buffer)) > 0){
            sb.append(
                    String.valueOf(buffer,0,read));
        }
        int result = clientService.parseCsvTest(sb.toString());

        assertEquals(5,result);
    }

    @Test
    public void getWinnersFor1PicksTest(){
        WinningNumber wn = winnings.get(0);
        var result = adminService.getWinnersFor1PicksTest(wn,clients);
        System.out.println("Winning picks are: " + wn.getPicksString());
        System.out.println("Winners are:");
        for(var cpe : result.getValue().entrySet()){
            System.out.printf(
                    "\t%s with %s\n",
                    cpe.getKey(),cpe.getValue()
            );
        }

        assertEquals(4,result.getValue().size());
    }
}
