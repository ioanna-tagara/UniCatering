package controller;

import dao.DBConnection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/StudentApplicationServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
                 maxFileSize = 1024 * 1024 * 10,      // 10MB
                 maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class StudentApplicationServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");

        // Πεδία φόρμας
        String fullName = req.getParameter("fullName");
        String department = req.getParameter("department");
        String email = req.getParameter("email");
        String mobile = req.getParameter("mobile");
        String id = req.getParameter("studentId");

        // Αρχεία
        Part photo = req.getPart("photo");
        Part familyCert = req.getPart("familyCertificate");
        Part idCard = req.getPart("idCard");
        Part residenceProof = req.getPart("residenceProof");
        Part parentsTax = req.getPart("parentsTax");
        Part studentTax = req.getPart("studentTax");
        Part additionalDocs = req.getPart("additionalDocs");

        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO applications (full_name, department, email, mobile, photo, family_certificate, id_card, residence_proof, parents_tax, student_tax, additional_docs, student_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, fullName);
            ps.setString(2, department);
            ps.setString(3, email);
            ps.setString(4, mobile);

            // Δυαδικά δεδομένα
            ps.setBinaryStream(5, photo.getInputStream(), (int) photo.getSize());
            ps.setBinaryStream(6, familyCert.getInputStream(), (int) familyCert.getSize());
            ps.setBinaryStream(7, idCard.getInputStream(), (int) idCard.getSize());
            ps.setBinaryStream(8, residenceProof.getInputStream(), (int) residenceProof.getSize());
            ps.setBinaryStream(9, parentsTax.getInputStream(), (int) parentsTax.getSize());
            ps.setBinaryStream(10, studentTax.getInputStream(), (int) studentTax.getSize());
            ps.setBinaryStream(11, additionalDocs.getInputStream(), (int) additionalDocs.getSize());
            
            ps.setString(12,id );

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Database error: " + e.getMessage());
        }

        res.sendRedirect("success.jsp");
    }
}
