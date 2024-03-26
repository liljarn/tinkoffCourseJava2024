package edu.java.configuration;

import edu.java.client.ClientInfoProvider;
import edu.java.repository.chat.JdbcChatRepository;
import edu.java.repository.chat.JooqChatRepository;
import edu.java.repository.chat_link.JdbcChatLinkRepository;
import edu.java.repository.chat_link.JooqChatLinkRepository;
import edu.java.repository.jpa.chat.JpaChatRepository;
import edu.java.repository.jpa.link.JpaLinkRepository;
import edu.java.repository.link.JdbcLinkRepository;
import edu.java.repository.link.JooqLinkRepository;
import edu.java.service.chat.ChatService;
import edu.java.service.chat.DefaultChatService;
import edu.java.service.chat.JpaChatService;
import edu.java.service.link.DefaultLinkService;
import edu.java.service.link.JpaLinkService;
import edu.java.service.link.LinkService;
import java.util.List;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
    public ChatService jdbcChatService(JdbcChatRepository jdbcChatRepository) {
        return new DefaultChatService(jdbcChatRepository);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
    public LinkService jdbcLinkService(
        JdbcLinkRepository linkRepository,
        JdbcChatLinkRepository chatLinkRepository,
        JdbcChatRepository chatRepository,
        List<ClientInfoProvider> clients
    ) {
        return new DefaultLinkService(linkRepository, chatLinkRepository, chatRepository, clients);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
    public ChatService jooqChatService(JooqChatRepository chatRepository) {
        return new DefaultChatService(chatRepository);
    }

    @Bean
    public DefaultConfigurationCustomizer postgresJooqCustomizer() {
        return (DefaultConfiguration c) -> c.settings()
            .withRenderSchema(false)
            .withRenderFormatted(true)
            .withRenderQuotedNames(RenderQuotedNames.NEVER);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
    public LinkService jooqLinkService(
        JooqLinkRepository linkRepository,
        JooqChatLinkRepository chatLinkRepository,
        JooqChatRepository chatRepository,
        List<ClientInfoProvider> clients
    ) {
        return new DefaultLinkService(linkRepository, chatLinkRepository, chatRepository, clients);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
    public ChatService jpaChatService(JpaChatRepository chatRepository) {
        return new JpaChatService(chatRepository);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
    public LinkService jpaLinkService(
        JpaChatRepository chatRepository,
        JpaLinkRepository linkRepository,
        List<ClientInfoProvider> clients
    ) {
        return new JpaLinkService(linkRepository, chatRepository, clients);
    }
}
