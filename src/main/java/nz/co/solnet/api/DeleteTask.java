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

public class DeleteTask  extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(DatabaseHelper.class);
    private static final String DATABASE_URL = "jdbc:derby:applicationdb;create=true";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        try (Connection conn = DriverManager.getConnection(DATABASE_URL)) {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM tasks WHERE id=?");
            statement.setString(1, req.getParameter("id"));
            statement.execute();

            PrintWriter out = resp.getWriter();
            Util.addStyleSheet(out);

            out.println("<h2>Successfully deleted task</h2>");
            Util.endHTML(out);

        } catch (SQLException sqlException) {
            logger.error("Error initialising database connection, or error with adding data", sqlException);
        }    }
}
