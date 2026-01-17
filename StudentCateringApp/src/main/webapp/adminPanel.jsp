<%@ page import="java.sql.*, dao.DBConnection" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Αξιολόγηση Αιτήσεων</title>
    <style>
        body { font-family: Arial; background-color: #f2f2f2; padding: 20px; }
        h2 { text-align: center; }
        .application-box {
            border: 1px solid #ccc;
            background-color: #fff;
            padding: 20px;
            margin: 20px auto;
            max-width: 800px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.05);
        }
        h3 { margin-top: 0; }
        ul { list-style-type: none; padding: 0; }
        ul li { margin-bottom: 5px; }
        label { font-weight: bold; }
        input[type="text"],
        input[type="number"] {
            width: 100%;
            padding: 8px;
            margin-top: 5px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        input[type="submit"] {
            background-color: #007bff;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #0056b3;
        }
        .no-applications {
            text-align: center;
            font-size: 18px;
            margin-top: 40px;
            color: #888;
        }
    </style>
</head>
<body>
<div style="background-color: #333; overflow: hidden; margin-bottom: 20px;">
    <a href="searchStudent.jsp" style="float: left; display: block; color: white; text-align: center; padding: 14px 20px; text-decoration: none;">👥 Διαχείριση Χρηστών</a>
    <a href="adminPanel.jsp" style="float: left; display: block; color: white; text-align: center; padding: 14px 20px; text-decoration: none;">📄 Αξιολόγηση Αιτήσεων</a>
    <form action="LogoutServlet" method="post" style="float: right; margin: 0;">
        <input type="submit" value="🚪 Αποσύνδεση" style="background-color: #d9534f; border: none; color: white; padding: 14px 20px; cursor: pointer; font-size: 14px;">
    </form>
</div>


<h2>Αιτήσεις Φοιτητών - Πίνακας Αξιολόγησης</h2>

<%
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    boolean found = false;

    try {
        con = DBConnection.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT * FROM applications WHERE decision IS NULL ORDER BY id DESC");

        while (rs.next()) {
            found = true;
            int id = rs.getInt("id");
            String fullName = rs.getString("full_name");
            String department = rs.getString("department");
            String email = rs.getString("email");
%>

<div class="application-box">
    <h3><%= fullName %> - <%= department %></h3>
    <p><strong>Email:</strong> <%= email %></p>

    <h4>📄 Έγγραφα:</h4>
    <ul>
        <li><a href="DownloadFileServlet?id=<%=id%>&field=photo">Φωτογραφία</a></li>
        <li><a href="DownloadFileServlet?id=<%=id%>&field=family_certificate">Οικογ. Κατάσταση</a></li>
        <li><a href="DownloadFileServlet?id=<%=id%>&field=id_card">Ταυτότητα</a></li>
        <li><a href="DownloadFileServlet?id=<%=id%>&field=residence_proof">Απόδειξη Κατοικίας</a></li>
        <li><a href="DownloadFileServlet?id=<%=id%>&field=parents_tax">Φορολ. Γονέων</a></li>
        <li><a href="DownloadFileServlet?id=<%=id%>&field=student_tax">Φορολ. Φοιτητή</a></li>
        <li><a href="DownloadFileServlet?id=<%=id%>&field=additional_docs">Άλλα</a></li>
    </ul>

    <h4>📋 Αξιολόγηση</h4>
    <form method="post" action="ReviewServlet">
        <input type="hidden" name="applicationId" value="<%= id %>">

        <label>Αριθμός Μητρώου (ΑΜ):</label>
        <input type="text" name="studentId" required>

        <label>Εισόδημα (€):</label>
        <input type="number" name="income" required>

        <label>Μέλη Οικογένειας:</label>
        <input type="number" name="familySize" required>

        <label>Απόσταση από ΑΕΙ (χλμ):</label>
        <input type="number" name="distance" required>

        <input type="submit" value="Αξιολόγηση & Αποθήκευση">
    </form>
</div>

<%
        } // end while
        if (!found) {
%>
    <div class="no-applications">Δεν υπάρχουν αιτήσεις προς αξιολόγηση.</div>
<%
        }
    } catch (Exception e) {
        out.println("<p>Σφάλμα: " + e.getMessage() + "</p>");
        e.printStackTrace();
    } finally {
        if (rs != null) rs.close();
        if (stmt != null) stmt.close();
        if (con != null) con.close();
    }
%>

</body>
</html>
