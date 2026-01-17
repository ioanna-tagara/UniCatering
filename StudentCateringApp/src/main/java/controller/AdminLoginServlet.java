package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

// Δηλώνουμε τη διαδρομή που θα ενεργοποιεί αυτό το servlet
@WebServlet("/AdminLoginServlet")
public class AdminLoginServlet extends HttpServlet {

    // Η μέθοδος doPost() καλείται όταν γίνει POST αίτημα (φόρμα login)
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        // Λήψη δεδομένων από τη φόρμα σύνδεσης
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // Χρήση try-with-resources για αυτόματο κλείσιμο της σύνδεσης
        try (Connection con = dao.DBConnection.getConnection()) {

            // SQL ερώτημα για έλεγχο στοιχείων διαχειριστή
            String sql = "SELECT * FROM admin_users WHERE username = ? AND password = ?";

            // Χρήση PreparedStatement για αποφυγή SQL injection
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            // Εκτέλεση του ερωτήματος
            ResultSet rs = ps.executeQuery();

            // Αν υπάρχει αντιστοιχία στα στοιχεία
            if (rs.next()) {
                // Δημιουργία συνεδρίας και αποθήκευση του διαχειριστή
                HttpSession session = req.getSession();
                session.setAttribute("adminUser", username);

                // Ανακατεύθυνση στον πίνακα διαχειριστή
                res.sendRedirect("adminPanel.jsp");
            } else {
                // Αν αποτύχει η σύνδεση, εμφάνιση μηνύματος λάθους
                req.setAttribute("error", "Λανθασμένα στοιχεία");
                req.getRequestDispatcher("admin-login.jsp").forward(req, res);
            }

        } catch (Exception e) {
            // Αν προκύψει σφάλμα, το πετάμε για να καταγραφεί
            throw new ServletException("Login error", e);
        }
    }
}
