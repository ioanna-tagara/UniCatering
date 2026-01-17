<%@ page import="java.sql.*, dao.DBConnection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
   String id = (session != null) ? (String) session.getAttribute("id") : null;

    if (id == null) {
%>
    <div class="info-box">
        <p>Δεν είστε συνδεδεμένος. <a href="login.jsp">Συνδεθείτε</a></p>
    </div>
<%
        return;
    }

    boolean hasApplication = false;
    String status = "", decision = "", fullName = "";

    try (Connection con = DBConnection.getConnection()) {
        PreparedStatement ps = con.prepareStatement("SELECT * FROM applications WHERE student_id = ?");
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            hasApplication = true;
            fullName = rs.getString("full_name");
            decision = rs.getString("decision");
        }
        rs.close();
        ps.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
%>

<!DOCTYPE html>
<html lang="el">
<head>
    <meta charset="UTF-8">
    <title>Η Αίτησή μου</title>
    <style>
        :root {
            --main-bg: #f8f9fa;
            --primary-color: #007bff;
            --primary-dark: #0056b3;
            --success-color: #28a745;
            --text-dark: #333;
            --white: #fff;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: var(--main-bg);
            margin: 0;
            padding: 30px;
        }

        h2 {
            text-align: center;
            color: var(--text-dark);
            margin-bottom: 30px;
        }

        .info-box, form {
            background-color: var(--white);
            padding: 30px;
            border-radius: 12px;
            max-width: 800px;
            margin: 0 auto 40px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
        }

        label {
            display: block;
            margin-top: 15px;
            font-weight: bold;
            color: var(--text-dark);
        }

        input[type="text"],
        input[type="email"],
        input[type="file"] {
            width: 100%;
            padding: 10px;
            margin-top: 6px;
            border-radius: 6px;
            border: 1px solid #ccc;
            font-size: 15px;
        }

        input[type="submit"] {
            background-color: var(--primary-color);
            color: var(--white);
            border: none;
            padding: 14px;
            margin-top: 25px;
            width: 100%;
            border-radius: 6px;
            font-size: 16px;
            cursor: pointer;
        }

        input[type="submit"]:hover {
            background-color: var(--primary-dark);
        }

        a.button {
            display: inline-block;
            background-color: var(--success-color);
            color: white;
            padding: 10px 18px;
            border-radius: 6px;
            text-decoration: none;
            font-weight: bold;
            margin-bottom: 25px;
            transition: background-color 0.3s ease;
        }

        a.button:hover {
            background-color: #218838;
        }

        .section-divider {
            margin: 30px 0;
            border-top: 1px solid #ddd;
        }

        @media (max-width: 600px) {
            form, .info-box {
                padding: 20px;
            }
        }
    </style>
</head>
<body>
<div style="background-color: #333; overflow: hidden; margin: -30px -30px 30px -30px;">
    <form action="LogoutServlet" method="post" style="float: right; margin: 0;">
        <input type="submit" value="🚪 Αποσύνδεση" 
               style="background-color: #d9534f; border: none; color: white; padding: 14px 20px; 
                      cursor: pointer; font-size: 14px; border-radius: 0;">
    </form>
</div>


<h2>Καλωσήρθατε, <%= fullName != null ? fullName : id %></h2>

<% if (hasApplication) { %>
    <div class="info-box">
        <p><strong>Απόφαση:</strong> <%= (decision != null ? decision : "Σε αναμονή") %></p>
        <p>Έχετε ήδη υποβάλει αίτηση. Δεν μπορείτε να υποβάλετε ξανά.</p>
    </div>
<% } else { %>
    <div class="info-box">
        <a class="button" href="instructions.pdf" target="_blank">Δείτε τα απαραίτητα δικαιολογητικά</a>

        <form action="StudentApplicationServlet" method="post" enctype="multipart/form-data">
            <label>Ονοματεπώνυμο:</label>
            <input type="text" name="fullName" required>

            <label>Αριθμός Μητρώου Φοιτητή (ΑΜ):</label>
            <input type="text" name="studentId" required>

            <label>Σχολή / Τμήμα:</label>
            <input type="text" name="department" required>

            <label>Email:</label>
            <input type="email" name="email" required>

            <label>Αριθμός Κινητού:</label>
            <input type="text" name="mobile" required>

          

            <div class="section-divider"></div>

            <label>1. Φωτογραφία φοιτητή/τριας (τύπου ταυτότητας):</label>
            <input type="file" name="photo" accept=".jpg,.jpeg,.png,.pdf" required>

            <label>2. Πιστοποιητικό Οικογενειακής Κατάστασης:</label>
            <input type="file" name="familyCertificate" accept=".pdf" required>

            <label>3. Αντίγραφο Αστυνομικής Ταυτότητας ή Διαβατηρίου:</label>
            <input type="file" name="idCard" accept=".pdf,.jpg,.jpeg,.png" required>

            <label>4. Αποδεικτικό Μόνιμης Κατοικίας:</label>
            <input type="file" name="residenceProof" accept=".pdf" required>

            <label>5. Εκκαθαριστικά Γονέων (ΦΕΦΠ 2023):</label>
            <input type="file" name="parentsTax" accept=".pdf" required>

            <label>6. Εκκαθαριστικό Φοιτητή (ΦΕΦΠ 2023) ή Υπεύθυνη Δήλωση:</label>
            <input type="file" name="studentTax" accept=".pdf" required>

            <label>7. Επιπλέον Δικαιολογητικά (αν απαιτούνται):</label>
            <input type="file" name="additionalDocs" accept=".pdf,.zip">

            <input type="submit" value="Υποβολή Αίτησης">
        </form>
    </div>
<% } %>

</body>
</html>
