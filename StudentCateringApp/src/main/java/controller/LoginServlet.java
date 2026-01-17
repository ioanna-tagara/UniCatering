package controller;

import dao.DBConnection;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

// Servlet υπεύθυνο για τη διαδικασία σύνδεσης φοιτητή
public class LoginServlet extends HttpServlet {

    // Χειρισμός του POST αιτήματος από τη φόρμα login
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Ανάγνωση στοιχείων σύνδεσης από τη φόρμα
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = DBConnection.getConnection()) {

            // SQL ερώτημα για ανάκτηση του χρήστη από τη βάση
            String sql = "SELECT * FROM students WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            // Αν βρέθηκε ο χρήστης στη βάση
            if (rs.next()) {
                // Ανάκτηση αποθηκευμένου κωδικού (hashed password)
                String storedHash = rs.getString("password");  // Πρέπει να είναι hash, όχι απλό κείμενο

                // Έλεγχος αν ο κωδικός που πληκτρολογήθηκε αντιστοιχεί στο αποθηκευμένο hash
                if (PasswordUtils.verifyPassword(password, storedHash)) {
                    // Αντιστοιχία κωδικών -> επιτυχής σύνδεση

                    // Δημιουργία ή ανάκτηση session για τον φοιτητή
                    HttpSession session = request.getSession();

                    // Αποθήκευση του username στο session με το όνομα "id"
                    session.setAttribute("id", username); 

                    // Μεταφορά (forward) στη σελίδα της φόρμας (studentForm.jsp)
                    RequestDispatcher dispatcher = request.getRequestDispatcher("studentForm.jsp");
                    dispatcher.forward(request, response);

                } else {
                    // Ο κωδικός είναι λανθασμένος
                    response.sendRedirect("login.jsp?error=1");
                }
            } else {
                // Ο χρήστης δεν βρέθηκε στη βάση
                response.sendRedirect("login.jsp?error=1");
            }

        } catch (SQLException e) {
            // Αν προκύψει πρόβλημα με τη βάση, το Servlet πετάει εξαίρεση
            throw new ServletException(e);
        }
    }
}
