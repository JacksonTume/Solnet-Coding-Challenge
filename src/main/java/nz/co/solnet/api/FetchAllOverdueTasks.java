package nz.co.solnet.api;

import nz.co.solnet.helper.DatabaseHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class FetchAllOverdueTasks extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(DatabaseHelper.class);
    private static final String DATABASE_URL = "jdbc:derby:applicationdb;create=true";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL)) {

            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM tasks ORDER BY id ASC");

            PrintWriter out = resp.getWriter();
            out.println("<head>\n" +
                    "\t<link rel=\"stylesheet\" href=\"markdown.css\">" +
                    "</head>\n");
            out.println("<table>");
            out.println("<tr><th>ID</th><th>Title</th><th>Description</th><th>Due Date</th><th>Status</th><th>Creation Date</th></tr>");

            while(resultSet.next()){
                Date dueDate = resultSet.getDate("due_date");
                Date creationDate = resultSet.getDate("creation_date");

                if(dueDate.compareTo(creationDate) == -1){
                    out.println("<tr>");

                    out.println("<td>" + resultSet.getString("id") + "</td>");
                    out.println("<td>" + resultSet.getString("title") + "</td>");
                    out.println("<td>" + resultSet.getString("description") + "</td>");
                    out.println("<td>" + resultSet.getDate("due_date") + "</td>");
                    out.println("<td>" + resultSet.getString("status") + "</td>");
                    out.println("<td>" + resultSet.getDate("creation_date") + "</td>");


                    out.println("</tr>");
                }
            }
            out.println("</table>");


        } catch (SQLException sqlException) {
            logger.error("Error initialising database connection, or error with adding data", sqlException);

        }
    }
}
