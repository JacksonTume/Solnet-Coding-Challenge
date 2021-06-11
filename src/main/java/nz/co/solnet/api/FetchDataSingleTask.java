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

public class FetchDataSingleTask extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(DatabaseHelper.class);
    private static final String DATABASE_URL = "jdbc:derby:applicationdb;create=true";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        try (Connection conn = DriverManager.getConnection(DATABASE_URL)) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM tasks ORDER BY id ASC");

            PrintWriter out = resp.getWriter();
            Util.addStyleSheet(out);
            out.println("<table>");

            Util.addHTMLHeaders(out);
            while(resultSet.next()){
                String id = req.getParameter("id");
                String idCompare = resultSet.getString("id");
                if(id.equals(idCompare)){
                    Util.addHTMLTable(resultSet, out);
                }
            }
            out.println("</table>");

            Util.addGoBack(out);
            out.println("</div>");
            out.close();

        } catch (SQLException sqlException) {
            logger.error("Error initialising database connection, or error with adding data", sqlException);
        }
    }
}
