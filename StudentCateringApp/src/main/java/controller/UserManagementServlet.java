package controller;

import dao.DBConnection;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/UserManagementServlet")
public class UserManagementServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
    	req.setCharacterEncoding("UTF-8");
    
        String action = req.getParameter("action");
        Connection con = null;

        try {
        	
            con = dao.DBConnection.getConnection();

            if ("προσθεσε".equals(action)) {
                String username = req.getParameter("username");
                String password = req.getParameter("password");
                String fullName = req.getParameter("full_name");
                String email = req.getParameter("email");
                String department = req.getParameter("department");
                
                String hashedPassword = PasswordUtils.hashPassword(password);

                PreparedStatement ps = con.prepareStatement("INSERT INTO students (username, password, full_name, email, department) VALUES (?, ?, ?, ?, ?)");
                ps.setString(1, username);
                ps.setString(2, hashedPassword); // Hash 
                ps.setString(3, fullName);
                ps.setString(4, email);
                ps.setString(5, department);
                ps.executeUpdate();
                ps.close();

            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                PreparedStatement ps = con.prepareStatement("DELETE FROM students WHERE id = ?");
                ps.setInt(1, id);
                ps.executeUpdate();
                ps.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (con != null) con.close(); } catch (Exception ignored) {}
        }

        res.sendRedirect("searchStudent.jsp");

    }
}
