package edu.java.repository.jpa.link;

import edu.java.repository.jpa.entity.LinkEntity;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaLinkRepository extends JpaRepository<LinkEntity, Long> {
    Optional<LinkEntity> findByUrl(String url);

    Set<LinkEntity> findAllByCheckedAtBefore(OffsetDateTime checkedAt);
}
