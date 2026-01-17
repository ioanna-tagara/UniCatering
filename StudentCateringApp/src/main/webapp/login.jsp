<%@ page contentType="text/html;charset=UTF-8" %>
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
    .login-container p {
        text-align: center;
        margin-top: 15px;
    }
    .error-message {
        color: red;
        text-align: center;
        margin-top: 10px;
    }
</style>

<div class="login-container">
    <h2>Login</h2>
    <form action="login" method="post">
        <input type="text" name="username" placeholder="Username" required />
        <input type="password" name="password" placeholder="Password" required />
        <input type="submit" value="Login" />
    </form>
    <p>Don't have an account? <a href="signUp.jsp">Sign up here</a>.</p>
    
    <form action="admin-login.jsp" method="get">
    <input type="submit" value="Σύνδεση Διαχειριστή">
</form>

    <% if (request.getParameter("error") != null) { %>
        <div class="error-message">Invalid username or password!</div>
    <% } %>
</div>
