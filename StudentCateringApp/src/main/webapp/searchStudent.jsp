<%@ page import="java.sql.*, dao.DBConnection" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
	<meta charset="UTF-8">
	
    <title>Διαχείριση Χρηστών</title>
    <style>
        body { font-family: Arial; padding: 20px; background-color: #f4f4f4; }
        h2 { text-align: center; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ccc; padding: 10px; text-align: left; }
        th { background-color: #eee; }
        form.inline { display: inline; }
        .add-form { background: #fff; padding: 20px; margin-top: 20px; border-radius: 8px; }
        input[type="text"], input[type="password"], input[type="email"] {
            width: calc(100% - 20px); padding: 8px; margin: 5px 0;
        }
        input[type="submit"] {
            padding: 10px 15px; background-color: #28a745;
            color: white; border: none; border-radius: 4px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #218838;
        }
    </style>
</head>
<body>
<div style="background-color: #333; overflow: hidden; margin-bottom: 20px;">
    <a href="searchStudent.jsp" style="float: left; display: block; color: white; text-align: center; padding: 14px 20px; text-decoration: none;">👥 Διαχείριση Χρηστών</a>
    <a href="adminPanel.jsp" style="float: left; display: block; color: white; text-align: center; padding: 14px 20px; text-decoration: none;">📄 Αξιολόγηση Αιτήσεων</a>
    <form action="LogoutServlet" method="post" style="float: right; margin: 0;">
        <input type="submit" value="🚪 Αποσύνδεση" style="background-color: #d9534f; border: none; color: white; padding: 14px 20px; cursor: pointer; font-size: 14px;">
    </form>
</div>
<h2>🧑‍💼 Διαχείριση Χρηστών</h2>

<!-- Φόρμα προσθήκης -->
<div class="add-form">
    <h3>Προσθήκη Χρήστη</h3>
    <form method="post" action="UserManagementServlet">
        <input type="hidden" name="action" value="προσθεσε">
        <label>Όνομα Χρήστη:</label><br>
        <input type="text" name="username" required><br>
        <label>Κωδικός:</label><br>
        <input type="password" name="password" required><br>
        <label>Ονοματεπώνυμο:</label><br>
        <input type="text" name="full_name" required><br>
        <label>Email:</label><br>
        <input type="email" name="email" required><br>
        <label>Τμήμα:</label><br>
        <input type="text" name="department" required><br><br>
        <input type="submit" value="Προσθήκη">
    </form>
</div>

<!-- Λίστα χρηστών -->
<h3>Λίστα Χρηστών</h3>
<table>
    <tr>
        <th>ID</th>
        <th>Όνομα Χρήστη</th>
        <th>Ονοματεπώνυμο</th>
        <th>Email</th>
        <th>Τμήμα</th>
        <th>Ενέργειες</th>
    </tr>

<%
    Connection con = DBConnection.getConnection();
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM students ORDER BY id");

    while (rs.next()) {
%>
    <tr>
        <td><%= rs.getInt("id") %></td>
        <td><%= rs.getString("username") %></td>
        <td><%= rs.getString("full_name") %></td>
        <td><%= rs.getString("email") %></td>
        <td><%= rs.getString("department") %></td>
        <td>
            <form method="post" action="UserManagementServlet" class="inline">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" value="<%= rs.getInt("id") %>">
                <input type="submit" value="Διαγραφή" onclick="return confirm('Σίγουρα;');">
            </form>
        </td>
    </tr>
<%
    }
    rs.close(); 
    stmt.close();
    con.close();
%>
</table>

</body>
</html>
