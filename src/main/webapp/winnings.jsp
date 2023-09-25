<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.lang.*,com.kenneth.lotto.model.*,java.util.*" %>

<html>
<head>
    <title>Winning Numbers</title>
    <link type="text/css" href="../styles.css" rel="stylesheet" />
</head>
<body>
    <table>
        <thead>
            <tr>
                <th>Id</th>
                <th>Winning Numbers</th>
                <th>Prize Pool</th>
            </tr>
        </thead>
        <tbody>
        <%

            List<WinningNumber> allWins =
                    (List<WinningNumber>)request.getAttribute("allWins");
            for(WinningNumber c : allWins){
                String row = String.format("<tr><td>%d</td><td>%s</td><td>%d</td></tr>",
                        c.getId(),
                        c.getPicksString(),
                        c.getPrizePool()
                );
                out.println(row);
            }
        %>
        </tbody>
    </table>
</body>
</html>
