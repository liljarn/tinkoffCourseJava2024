package edu.java.scrapper.repository;

import edu.java.exceptions.ChatAlreadyRegisteredException;
import edu.java.exceptions.ChatNotFoundException;
import edu.java.repository.chat.ChatRepository;
import edu.java.repository.chat.JdbcChatRepository;
import edu.java.scrapper.IntegrationEnvironment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class JdbcChatRepositoryTest extends IntegrationEnvironment {
    private static JdbcTemplate jdbcTemplate;

    //Arrange
    @BeforeAll
    public static void setup() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(POSTGRES.getDriverClassName());
        dataSource.setUrl(POSTGRES.getJdbcUrl());
        dataSource.setUsername(POSTGRES.getUsername());
        dataSource.setPassword(POSTGRES.getPassword());
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    public void add_shouldCorrectlyAddIdInChatTable_whenIdIsNotInTable() {
        //Arrange
        ChatRepository chatRepository = new JdbcChatRepository(jdbcTemplate);
        Long chatId = 1L;
        Connection connection = POSTGRES.createConnection("");
        PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM chat WHERE chat_id = (?)");
        statement.setLong(1, chatId);
        //Act
        chatRepository.add(chatId);
        //Assert
        ResultSet rs = statement.executeQuery();
        long idCount = -1;
        if (rs.next()) {
            idCount = rs.getLong(1);
        }
        assertThat(idCount).isEqualTo(1L);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    public void remove_shouldDeleteIdFromChatTable_whenIdExists() {
        //Arrange
        ChatRepository chatRepository = new JdbcChatRepository(jdbcTemplate);
        Connection connection = POSTGRES.createConnection("");
        long chatId = 1879L;
        PreparedStatement statement = connection.prepareStatement("INSERT INTO chat (chat_id) VALUES (?)");
        statement.setLong(1, chatId);
        statement.executeUpdate();
        //Act
        chatRepository.remove(chatId);
        //Assert
        statement = connection.prepareStatement("SELECT COUNT(*) FROM chat WHERE chat_id = (?)");
        statement.setLong(1, chatId);
        ResultSet rs = statement.executeQuery();
        long idCount = -1;
        if (rs.next()) {
            idCount = rs.getLong(1);
        }
        assertThat(idCount).isEqualTo(0L);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    public void isInTable_shouldReturnTrue_whenChatIdExists() {
        //Arrange
        ChatRepository chatRepository = new JdbcChatRepository(jdbcTemplate);
        Connection connection = POSTGRES.createConnection("");
        long chatId = 2000L;
        PreparedStatement statement = connection.prepareStatement("INSERT INTO chat (chat_id) VALUES (?)");
        statement.setLong(1, chatId);
        statement.executeUpdate();
        //Act
        boolean inTable = chatRepository.isInTable(chatId);
        //Assert
        assertThat(inTable).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    public void isInTable_shouldReturnFalse_whenChatIdDoesNotExists() {
        //Arrange
        ChatRepository chatRepository = new JdbcChatRepository(jdbcTemplate);
        long chatId = 123L;
        //Act
        boolean inTable = chatRepository.isInTable(chatId);
        //Assert
        assertThat(inTable).isFalse();
    }

    @AfterAll
    public static void teardown() {
        POSTGRES.close();
    }
}
