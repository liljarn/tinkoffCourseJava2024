package edu.java.scrapper.kafka;

import edu.java.configuration.ApplicationConfig;
import edu.java.dto.client.LinkUpdate;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.service.sender.KafkaUpdateSender;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
public class KafkaUpdateSenderTest extends IntegrationEnvironment {
    @Autowired
    private KafkaTemplate<String, LinkUpdate> kafkaTemplate;

    @Autowired
    private ApplicationConfig config;

    @Test
    @DisplayName("Тестирование KafkaLinkUpdateSender#sendUpdate")
    public void sendUpdateShouldSendUpdateInKafka() {
        //Arrange
        var linkUpdateSender = new KafkaUpdateSender(kafkaTemplate, config);
        var linkUpdate = new LinkUpdate(
            1L,
            URI.create("http://google.com"),
            "test",
            List.of(1L)
        );
        var kafkaConsumer = new KafkaConsumer<String, LinkUpdate>(
            Map.of(
                "bootstrap.servers", KAFKA.getBootstrapServers(),
                "group.id", "scrapper",
                "key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer",
                "value.deserializer", "org.springframework.kafka.support.serializer.JsonDeserializer",
                "properties.spring.json.trusted.packages", "*",
                "spring.json.value.default.type", "edu.java.dto.client.LinkUpdate",
                "auto.offset.reset", "earliest"
            )
        );
        kafkaConsumer.subscribe(List.of(config.topic()));
        //Act
        linkUpdateSender.sendUpdate(linkUpdate);
        //Assert
        await()
            .pollInterval(Duration.ofMillis(100))
            .atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> {
                var records = kafkaConsumer.poll(Duration.ofMillis(100));
                assertThat(records).hasSize(1);
                assertThat(records.iterator().next().value()).isEqualTo(linkUpdate);
            });
    }
}
