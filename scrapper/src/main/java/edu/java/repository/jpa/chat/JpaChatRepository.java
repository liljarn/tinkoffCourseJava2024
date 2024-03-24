package edu.java.repository.jpa.chat;

import edu.java.repository.jpa.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRepository extends JpaRepository<ChatEntity, Long> {
}
