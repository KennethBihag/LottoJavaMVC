
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.lang.*,com.kenneth.lotto.model.*,java.util.*" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Collections" %>
<html>
<head>
    <title>All Entries</title>
</head>
<body>
    <table>
        <thead>
            <tr>
                <th>Client</th>
                <th>Id</th>
                <th>picks</th>
            </tr>
        </thead>
        <tbody>
        <%

            List<Client> allEntries =
                    (List<Client>)request.getAttribute("allEntries");
            for(Client c : allEntries){
                String row = String.format("<tr><td>%s</td><td>%d</td><td>%s</td></tr>",
                        c.getName(),
                        c.getId(),
                        c.getPicksString());
                out.println(row);
            }
        %>
        </tbody>
    </table>
</body>
</html>
