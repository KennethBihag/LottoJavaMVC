
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.kenneth.lotto.model.*" %>
<%@ page import="java.lang.*,java.util.*" %>
<html>
<head>
    <title>Winning Numbers</title>
    <link type="text/css" href="/styles.css" rel="stylesheet" />
    <%
        List<Winner> allWinners =
                (List<Winner>)request.getAttribute("allWinners");
        StringBuilder toShow = new StringBuilder();
        if(allWinners.size()<1) {
            toShow.append("<p>NO WINNERS YET</p><p style='font-weight: bold'>:(</p>");
        } else{
            toShow.append("<table><thead><tr>");
            toShow.append("<th>Id</th><th>Client Id</th><th>Winning Number Id</th><th>Prize</th>");
            toShow.append("</tr></thead><tbody>");
            for(Winner w : allWinners){
                String row = String.format("<tr><td>%d</td><td>%d</td><td>%d</td><td>%d</td></tr>",
                        w.getId(),
                        w.getClient().getId(),
                        w.getWinningNumber().getId(),
                        w.getPrize()
                );
                toShow.append(row);
            }
            toShow.append("</tbody></table>");
        }
        session.setAttribute("toShow",toShow.toString());
    %>
</head>
<body>
    ${toShow}
</body>
</html>
