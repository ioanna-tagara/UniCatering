<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="el">
<head>
    <title>Σύνδεση Διαχειριστή</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .login-container {
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            width: 300px;
        }

        .login-container h2 {
            text-align: center;
            margin-bottom: 20px;
        }

        .login-container input[type="text"],
        .login-container input[type="password"] {
            width: 100%;
            padding: 10px;
            margin: 8px 0;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .login-container input[type="submit"] {
            width: 100%;
            padding: 10px;
            background-color: #4CAF50;
            border: none;
            color: white;
            border-radius: 4px;
            cursor: pointer;
            margin-top: 10px;
        }

        .login-container input[type="submit"]:hover {
            background-color: #45a049;
        }

        .error-message {
            color: red;
            text-align: center;
            margin-top: 10px;
        }

        .back-link {
            text-align: center;
            margin-top: 15px;
            font-size: 14px;
        }
    </style>
</head>
<body>

<div class="login-container">
    <h2>Είσοδος Διαχειριστή</h2>
    <form action="AdminLoginServlet" method="post">
        <input type="text" name="username" placeholder="Όνομα Χρήστη" required />
        <input type="password" name="password" placeholder="Κωδικός" required />
        <input type="submit" value="Σύνδεση" />
    </form>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error-message"><%= request.getAttribute("error") %></div>
    <% } %>

    <div class="back-link">
        <a href="login.jsp">🔙 Επιστροφή στην Αρχική</a>
    </div>
</div>

</body>
</html>
