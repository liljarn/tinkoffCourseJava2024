package edu.java.scrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DatabaseMigrationTest extends IntegrationEnvironment {
    @Test
    @DisplayName("Chat table test")
    @SneakyThrows
    public void migrationChatTable_shouldWorkCorrectly() {
        //Arrange
        Connection connection = POSTGRES.createConnection("");
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM chat");
        //Act
        ResultSet resultSet = statement.executeQuery();
        //Assert
        String columnName = resultSet.getMetaData().getColumnName(1);
        assertThat(columnName).isEqualTo("chat_id");
    }

    @Test
    @DisplayName("Link table test")
    @SneakyThrows
    public void migrationLinkTable_shouldWorkCorrectly() {
        //Arrange
        Connection connection = POSTGRES.createConnection("");
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM link");
        //Act
        var meta = statement.executeQuery().getMetaData();
        //Assert
        assertThat(meta.getColumnName(1)).isEqualTo("link_id");
        assertThat(meta.getColumnName(2)).isEqualTo("last_update_time");
        assertThat(meta.getColumnName(3)).isEqualTo("name");
        assertThat(meta.getColumnName(4)).isEqualTo("url");
    }
}
