package edu.java.scrapper;

import edu.java.scrapper.IntegrationTest;
import org.junit.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;

public class MigrationTest extends IntegrationTest {

    @Test
    public void chatTableTest() throws SQLException {
        try (Connection connection = POSTGRES.createConnection("")) {
            PreparedStatement sqlQuery = connection.prepareStatement("SELECT * FROM chat");
            String actualResult = sqlQuery.executeQuery().getMetaData().getColumnName(1);
            assertEquals(actualResult, "chat_id");
        }
    }

    @Test
    public void linkTableTest() throws SQLException {
        try (Connection connection = POSTGRES.createConnection("")) {
            PreparedStatement sqlQuery = connection.prepareStatement("SELECT * FROM link");
            String firstColumn = sqlQuery.executeQuery().getMetaData().getColumnName(1);
            String secondColumn = sqlQuery.executeQuery().getMetaData().getColumnName(2);
            String thirdColumn = sqlQuery.executeQuery().getMetaData().getColumnName(3);
            assertEquals(firstColumn, "link_id");
            assertEquals(secondColumn, "url");
            assertEquals(thirdColumn, "update_at");
        }
    }

    @Test
    public void connectionTableTest() throws SQLException {
        try (Connection connection = POSTGRES.createConnection("")) {
            PreparedStatement sqlQuery = connection.prepareStatement("SELECT * FROM chat_link");
            String firstColumn = sqlQuery.executeQuery().getMetaData().getColumnName(1);
            String secondColumn = sqlQuery.executeQuery().getMetaData().getColumnName(2);
            assertEquals(firstColumn, "chat_id");
            assertEquals(secondColumn, "link_id");
        }
    }
}
