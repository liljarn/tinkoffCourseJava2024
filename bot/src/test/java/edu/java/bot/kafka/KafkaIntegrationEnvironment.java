package edu.java.bot.kafka;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class KafkaIntegrationEnvironment {
    public static KafkaContainer KAFKA;
    public static String TOPIC_NAME = "test";

    static {
        KAFKA = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.0"));
        KAFKA.start();
    }

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
        registry.add("spring.kafka.consumer.bootstrap-servers", KAFKA::getBootstrapServers);
        registry.add("spring.kafka.producer.bootstrap-servers", KAFKA::getBootstrapServers);
        registry.add("app.use-queue", () -> true);
        registry.add("app.topic-name", () -> TOPIC_NAME);
    }
}
