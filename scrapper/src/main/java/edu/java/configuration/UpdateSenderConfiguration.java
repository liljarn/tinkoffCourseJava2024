package edu.java.configuration;

import edu.java.client.bot.BotClient;
import edu.java.dto.client.LinkUpdate;
import edu.java.service.sender.BotUpdateSender;
import edu.java.service.sender.KafkaUpdateSender;
import edu.java.service.sender.UpdateSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class UpdateSenderConfiguration {
    @Bean
    public UpdateSender updateSender(
        ApplicationConfig config,
        KafkaTemplate<String, LinkUpdate> kafka,
        BotClient client
    ) {
        return (config.useQueue())
            ? new KafkaUpdateSender(kafka, config)
            : new BotUpdateSender(client);
    }
}
