package edu.java.service.sender;

import edu.java.configuration.ApplicationConfig;
import edu.java.dto.client.LinkUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class KafkaUpdateSender implements UpdateSender {
    private final KafkaTemplate<String, LinkUpdate> kafkaTemplate;
    private final ApplicationConfig config;

    @Override
    public void sendUpdate(LinkUpdate update) {
        kafkaTemplate.send(config.topic(), update);
    }
}
