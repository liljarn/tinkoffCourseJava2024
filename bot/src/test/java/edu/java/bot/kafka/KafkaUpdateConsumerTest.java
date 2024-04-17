package edu.java.bot.kafka;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.dto.request.LinkUpdate;
import edu.java.bot.service.update.UpdateService;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.kafka.consumer.auto-offset-reset=earliest"
})
public class KafkaUpdateConsumerTest extends KafkaIntegrationEnvironment {
    @MockBean
    private TelegramBot telegramBot;

    @MockBean
    private UpdateService updateService;

    @Autowired
    private KafkaTemplate<String, LinkUpdate> kafkaTemplate;

    @Autowired
    private KafkaProperties kafkaProperties;

    @Test
    @DisplayName("Valid message consume test")
    public void consume_shouldCatchUpdate_whenMessageIsValid() {
        //Arrange
        LinkUpdate linkUpdate = new LinkUpdate(
            1L,
            URI.create("https://google.com"),
            "test",
            List.of(1L)
        );
        //Act
        kafkaTemplate.send(TOPIC_NAME, linkUpdate);
        //Assert
        await()
            .pollInterval(Duration.ofMillis(100))
            .atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> Mockito.verify(updateService, Mockito.times(1))
                .updateLink(linkUpdate));
    }

    @Test
    @DisplayName("consume with thrown in process exception test")
    public void consume_shouldProduceInDlq_whenUpdateNotConsumed() {
        //Arrange
        LinkUpdate linkUpdate = new LinkUpdate(
            1L,
            URI.create("https://google.com"),
            "test",
            List.of(1L)
        );
        Mockito.doThrow(RuntimeException.class).when(updateService).updateLink(linkUpdate);
        KafkaConsumer<String, LinkUpdate> dlqKafkaConsumer = new KafkaConsumer<>(
            kafkaProperties.buildConsumerProperties(null)
        );
        dlqKafkaConsumer.subscribe(List.of(TOPIC_NAME + "_dlq"));
        //Act
        kafkaTemplate.send(TOPIC_NAME, linkUpdate);
        //Assert
        await()
            .pollInterval(Duration.ofMillis(100))
            .atMost(Duration.ofSeconds(10))
            .untilAsserted(() -> {
                var values = dlqKafkaConsumer.poll(Duration.ofMillis(100));
                assertThat(values).hasSize(1);
                assertThat(values.iterator().next().value()).isEqualTo(linkUpdate);
                Mockito.verify(updateService).updateLink(linkUpdate);
            });
    }
}
