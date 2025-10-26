<%@ page import="com.game.repository.PlayerRepositoryDB" %>
<%@ page import="com.game.entity.Player" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%
    PlayerRepositoryDB repository = new PlayerRepositoryDB();
    List<Player> players = repository.getAll(0, 10);
    int totalCount = repository.getAllCount();
%>

<!DOCTYPE html>
<html>
<head>
    <title>Players</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { border-collapse: collapse; width: 100%; margin: 20px 0; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
<h1>Players (<%= totalCount %> total)</h1>

<table>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Title</th>
        <th>Race</th>
        <th>Profession</th>
        <th>Level</th>
        <th>Birthday</th>
        <th>Banned</th>
    </tr>
    <% for (Player player : players) { %>
    <tr>
        <td><%= player.getId() %></td>
        <td><%= player.getName() %></td>
        <td><%= player.getTitle() %></td>
        <td><%= player.getRace() %></td>
        <td><%= player.getProfession() %></td>
        <td><%= player.getLevel() %></td>
        <td><%= player.getBirthdate() != null ? player.getBirthdate() : "N/A" %></td>
        <td><%= player.getBanned() %></td>
    </tr>
    <% } %>
</table>
<br>
<a href="html/my.html">Full Management Interface</a> |
<a href="index.jsp">Back to Home</a>
</body>
</html>