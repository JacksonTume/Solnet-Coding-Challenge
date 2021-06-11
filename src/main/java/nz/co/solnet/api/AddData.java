package nz.co.solnet.api;

import nz.co.solnet.helper.DatabaseHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

public class AddData extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(DatabaseHelper.class);
    private static final String DATABASE_URL = "jdbc:derby:applicationdb;create=true";


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL)) {

            PreparedStatement statement = conn.prepareStatement("INSERT INTO tasks VALUES (DEFAULT, ?, ?, ?, ?, ?)");
            statement.setString(1, req.getParameter("title"));
            statement.setString(2, req.getParameter("description"));
            statement.setDate(3, Date.valueOf(req.getParameter("due_date")));
            statement.setString(4, req.getParameter("status"));
            statement.setDate(5, Date.valueOf(java.time.LocalDate.now()));

            statement.execute();

            String addedTask = Json.createObjectBuilder()
                    .add("title", req.getParameter("title"))
                    .add("description", req.getParameter("description"))
                    .add("due_date", req.getParameter("due_date"))
                    .add("status", req.getParameter("status"))
                    .add("current_date", String.valueOf(java.time.LocalDate.now()))
                    .build().toString();
            logger.info("Added new task:\n" + addedTask);

        } catch (SQLException sqlException) {
            logger.error("Error initialising database connection, or error with adding data", sqlException);
        }
    }
}
