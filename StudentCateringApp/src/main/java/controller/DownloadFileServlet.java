package controller;

import dao.DBConnection;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.io.OutputStream;

@WebServlet("/DownloadFileServlet")
public class DownloadFileServlet extends HttpServlet {

    // Η μέθοδος doGet() καλείται όταν ο client κάνει GET αίτημα για λήψη αρχείου
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        
        // Ανάγνωση των παραμέτρων από το URL: id της αίτησης και το όνομα του πεδίου (π.χ. "idCard")
        int id = Integer.parseInt(req.getParameter("id"));
        String field = req.getParameter("field");

        try (Connection con = DBConnection.getConnection()) {

            // SQL ερώτημα για λήψη του συγκεκριμένου πεδίου αρχείου από τη βάση
            String sql = "SELECT " + field + " FROM applications WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            // Αν υπάρχει αποτέλεσμα
            if (rs.next()) {
                // Ανάγνωση των δεδομένων του αρχείου ως πίνακας byte
                byte[] fileData = rs.getBytes(field);

                // Ορισμός τύπου περιεχομένου (για να ξεκινήσει η λήψη αρχείου)
                res.setContentType("application/octet-stream");

                // Ορισμός header ώστε το αρχείο να «κατεβεί» με συγκεκριμένο όνομα
                res.setHeader("Content-Disposition", "attachment;filename=" + field + "_" + id + ".pdf");

                // Αποστολή των bytes του αρχείου στο response
                OutputStream out = res.getOutputStream();
                out.write(fileData);
                out.flush(); // Βεβαιωνόμαστε πως όλα τα δεδομένα στέλνονται στον client
            }

        } catch (Exception e) {
            // Εκτύπωση σφάλματος στον server log (καλό θα ήταν να υπάρχει logging σύστημα)
            e.printStackTrace();
        }
    }
}
