<style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f5f5f5;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
    }
    .signup-container {
        background-color: white;
        padding: 30px;
        border-radius: 8px;
        box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        width: 350px;
    }
    .signup-container h2 {
        text-align: center;
        margin-bottom: 20px;
    }
    .signup-container input[type="text"],
    .signup-container input[type="password"],
    .signup-container input[type="email"] {
        width: 100%;
        padding: 10px;
        margin: 8px 0;
        border: 1px solid #ccc;
        border-radius: 4px;
    }
    .signup-container input[type="submit"] {
        width: 100%;
        padding: 10px;
        background-color: #4CAF50;
        border: none;
        color: white;
        border-radius: 4px;
        cursor: pointer;
        margin-top: 10px;
    }
    .signup-container input[type="submit"]:hover {
        background-color: #45a049;
    }
    .signup-container p {
        text-align: center;
        margin-top: 15px;
    }
    .success-message {
        color: green;
        text-align: center;
        margin-bottom: 10px;
    }
    .error-message {
        color: red;
        text-align: center;
        margin-bottom: 10px;
    }
</style>

<div class="signup-container">
    <h2>Sign Up</h2>

    <% 
        String success = request.getParameter("success");
        String error = request.getParameter("error");
        String message = request.getParameter("message");

        if ("1".equals(success)) { 
    %>
        <div class="success-message">Sign up successful!</div>
    <% 
        } else if ("1".equals(error)) { 
            String errorMessage = message != null ? message : "Error during sign up. Please try again."; 
    %>
        <div class="error-message"><%= errorMessage %></div>
    <% 
        } 
    %>

    <form action="SignUpServlet" method="post">
        <input type="text" id="username" name="username" placeholder="Username" required>
        <input type="password" id="password" name="password" placeholder="Password" required>
        <input type="text" id="full_name" name="full_name" placeholder="Full Name" required>
        <input type="email" id="email" name="email" placeholder="Email" required>
        <input type="text" id="department" name="department" placeholder="Department" required>
        <input type="submit" value="Sign Up">
    </form>

    <p>Already have an account? <a href="login.jsp">Login here</a></p>
</div>
