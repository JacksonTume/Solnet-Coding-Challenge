package nz.co.solnet.api;

import nz.co.solnet.helper.DatabaseHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Util {
    private static final Logger logger = LogManager.getLogger(DatabaseHelper.class);

    public static void endHTML(PrintWriter out){
        out.println("<a href=\"javascript:history.back()\">Go Back</a>");
        out.println("</div>");
        out.close();
    }

    public static void addStyleSheet(PrintWriter out){
        out.println("<head>\n" +
                "\t<link rel=\"stylesheet\" href=\"markdown.css\">" +
                "</head>\n");
        out.println("<div class=\"markdown-body\">\n");
    }

    public static void addHTMLTable(ResultSet resultSet, PrintWriter out) throws SQLException {
        out.println("<tr>");

        out.println("<td>" + resultSet.getString("id") + "</td>");
        out.println("<td>" + resultSet.getString("title") + "</td>");
        out.println("<td>" + resultSet.getString("description") + "</td>");
        out.println("<td>" + resultSet.getDate("due_date") + "</td>");
        out.println("<td>" + resultSet.getString("status") + "</td>");
        out.println("<td>" + resultSet.getDate("creation_date") + "</td>");

        out.println("</tr>");
    }

    public static void addHTMLTable(HttpServletRequest req, PrintWriter out) throws SQLException {
        out.println("<tr><th>Title</th><th>Description</th><th>Due Date</th><th>Status</th><th>Creation Date</th></tr>");
        out.println("<tr>");

        out.println("<td>" + req.getParameter("title") + "</td>");
        out.println("<td>" + req.getParameter("description") + "</td>");
        out.println("<td>" + Date.valueOf(req.getParameter("due_date")) + "</td>");
        out.println("<td>" + req.getParameter("status") + "</td>");
        out.println("<td>" + Date.valueOf(java.time.LocalDate.now()) + "</td>");

        out.println("</tr>");
    }

    public static void addHTMLTableHeaders(PrintWriter out) {
        out.println("<tr><th>ID</th><th>Title</th><th>Description</th><th>Due Date</th><th>Status</th><th>Creation Date</th></tr>");
    }

    public static void addParametersToPreparedStatement(PreparedStatement statement, HttpServletRequest req) throws SQLException {
        statement.setString(1, req.getParameter("title"));
        statement.setString(2, req.getParameter("description"));
        statement.setDate(3, Date.valueOf(req.getParameter("due_date")));
        statement.setString(4, req.getParameter("status"));
        statement.setDate(5, Date.valueOf(java.time.LocalDate.now()));
    }

    public static boolean isDateValid(Date date){
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            dateFormat.parse(String.valueOf(date));
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isTitleValid(String title){
        return title.length() <= 256;
    }

    public static boolean isDescriptionValid(String description){
        return description.length() <= 1024;
    }

    public static boolean isStatusValid(String status){
        return status.length() <= 10;
    }

    public static boolean validateInput(HttpServletRequest req) {
        if(!Util.isTitleValid(req.getParameter("title"))) {
            logger.error("Title too long!");
            return false;
        }

        if(!Util.isDescriptionValid(req.getParameter("description"))) {
            logger.error("Description too long!");
            return false;
        }

        if(!Util.isStatusValid(req.getParameter("status"))) {
            logger.error("Status too long!");
            return false;
        }

        if(!Util.isDateValid(Date.valueOf(req.getParameter("due_date")))){
            logger.error("Date format incorrect! make sure to use format yyyy-mm-dd");
            return false;
        }
        return true;
    }
}
