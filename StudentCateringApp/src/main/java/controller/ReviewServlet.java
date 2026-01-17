package controller;

import dao.DBConnection;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

/**
 * Servlet για την αξιολόγηση αιτήσεων φοιτητών βάσει κοινωνικοοικονομικών κριτηρίων.
 */
@WebServlet("/ReviewServlet")
public class ReviewServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        try {
            // Λήψη παραμέτρων από τη φόρμα
            String studentId = request.getParameter("studentId");
            int income = Integer.parseInt(request.getParameter("income"));           // Ετήσιο εισόδημα
            int familySize = Integer.parseInt(request.getParameter("familySize"));   // Μέγεθος οικογένειας
            int distance = Integer.parseInt(request.getParameter("distance"));       // Απόσταση κατοικίας (σε χλμ)

            // Λογική έγκρισης: Χαμηλό εισόδημα, μεγάλη οικογένεια, μεγάλη απόσταση
            boolean approved = (income < 10000 && familySize >= 4 && distance > 30);

            // Σύνδεση στη βάση και ενημέρωση της απόφασης για τον φοιτητή
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "UPDATE applications SET decision = ? WHERE student_id = ?"
            );
            ps.setString(1, approved ? "ΕΓΚΡΙΝΕΤΑΙ" : "ΑΠΟΡΡΙΠΤΕΤΑΙ");
            ps.setString(2, studentId);
            ps.executeUpdate();

            // Ανακατεύθυνση πίσω στον πίνακα διαχειριστή
            response.sendRedirect("adminPanel.jsp");

        } catch (Exception e) {
            // Αν προκύψει πρόβλημα, εμφανίζεται σφάλμα
            throw new ServletException("Αποτυχία αξιολόγησης: " + e.getMessage());
        }
    }
}
