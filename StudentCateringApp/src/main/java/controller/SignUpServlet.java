package controller;

import dao.DBConnection;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class SignUpServlet extends HttpServlet {

    // Η doPost() χειρίζεται την υποβολή της φόρμας εγγραφής
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Ανάκτηση τιμών από τη φόρμα εγγραφής
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("full_name");
        String email = request.getParameter("email");
        String department = request.getParameter("department");

        // Βασικός έλεγχος εγκυρότητας email με regex
        if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            // Επιστροφή στη φόρμα με μήνυμα λάθους αν το email δεν είναι έγκυρο
            response.sendRedirect("signUp.jsp?error=1&message=Invalid+email+format");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {

            // Έλεγχος αν το username υπάρχει ήδη
            String checkUsernameSql = "SELECT COUNT(*) FROM students WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkUsernameSql);
            checkStmt.setString(1, username);
            ResultSet resultSet = checkStmt.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                // Αν το username υπάρχει ήδη, επιστροφή με μήνυμα
                response.sendRedirect("signUp.jsp?error=1&message=Username+already+exists");
                return;
            }

            // Κρυπτογράφηση του κωδικού πριν την αποθήκευση
            String hashedPassword = PasswordUtils.hashPassword(password);

            // Εισαγωγή νέου χρήστη στη βάση δεδομένων
            String sql = "INSERT INTO students (username, password, full_name, email, department) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);  // Αποθηκεύεται ο hash του password
            stmt.setString(3, fullName);
            stmt.setString(4, email);
            stmt.setString(5, department);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Αν η εγγραφή είναι επιτυχής, ανακατεύθυνση με success
                response.sendRedirect("signUp.jsp?success=1");
            } else {
                // Αν όχι, ανακατεύθυνση με αποτυχία
                response.sendRedirect("signUp.jsp?error=1&message=Registration+failed+unexpectedly");
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Καταγραφή στο server log

            // Επιστροφή με μήνυμα λάθους URL-encoded
            String errorMessage = e.getMessage().replace(" ", "+");
            response.sendRedirect("signUp.jsp?error=1&message=" + errorMessage);
        }
    }
}
