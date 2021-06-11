package nz.co.solnet.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Util {

    public static void addGoBack(PrintWriter out){
        out.println("<a href=\"javascript:history.back()\">Go Back</a>");
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

    public static void addHTMLHeaders(PrintWriter out) {
        out.println("<tr><th>ID</th><th>Title</th><th>Description</th><th>Due Date</th><th>Status</th><th>Creation Date</th></tr>");
    }

    public static void addParametersToPreparedStatement(PreparedStatement statement, HttpServletRequest req) throws SQLException {
        statement.setString(1, req.getParameter("title"));
        statement.setString(2, req.getParameter("description"));
        statement.setDate(3, Date.valueOf(req.getParameter("due_date")));
        statement.setString(4, req.getParameter("status"));
        statement.setDate(5, Date.valueOf(java.time.LocalDate.now()));
    }
}
