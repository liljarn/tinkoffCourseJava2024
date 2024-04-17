package edu.java.bot.service.update;

import edu.java.bot.dto.request.LinkUpdate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
@ConditionalOnProperty(name = "app.use-queue", havingValue = "true")
public class KafkaUpdateConsumer {
    private final UpdateService updateService;

    @SneakyThrows
    @RetryableTopic(
        attempts = "1",
        dltStrategy = DltStrategy.FAIL_ON_ERROR,
        dltTopicSuffix = "_dlq")
    @KafkaListener(topics = "${app.topic-name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(LinkUpdate update) {
        log.info(String.format("#### -> Consumed message -> %s", update));
        updateService.updateLink(update);
    }

    @DltHandler
    public void handleDltPayment(
        LinkUpdate linkUpdate, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Event on dlt topic={}, payload={}", topic, linkUpdate);
    }
}
