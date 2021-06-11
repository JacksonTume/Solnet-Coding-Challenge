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

public class AddData extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(DatabaseHelper.class);
    private static final String DATABASE_URL = "jdbc:derby:applicationdb;create=true";


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        // validate input
        if(!Util.validateInput(req)){
            PrintWriter out = resp.getWriter();
            Util.addStyleSheet(out);
            out.println("<h2>Incorrect input while updating task - check output to see exact problem</h2>");
            Util.endHTML(out);
            return;
        }

        try (Connection conn = DriverManager.getConnection(DATABASE_URL)) {
            PrintWriter out = resp.getWriter();
            Util.addStyleSheet(out);

            PreparedStatement statement = conn.prepareStatement("INSERT INTO tasks VALUES (DEFAULT, ?, ?, ?, ?, ?)");

            Util.addParametersToPreparedStatement(statement, req);

            statement.execute();

            out.println("<h2>Successfully added data to table</h2><table>");
            Util.addHTMLTable(req, out);
            out.println("</table>");

            Util.endHTML(out);

        } catch (SQLException sqlException) {
            logger.error("Error initialising database connection, or error with adding data", sqlException);
        }
    }
}
