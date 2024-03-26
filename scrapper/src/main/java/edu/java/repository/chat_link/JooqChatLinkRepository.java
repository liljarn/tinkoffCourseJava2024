package edu.java.repository.chat_link;

import edu.java.dto.ChatLinkResponse;
import edu.java.dto.response.LinkResponse;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Value;
import static edu.java.repository.jooq.tables.ChatLink.CHAT_LINK;
import static edu.java.repository.jooq.tables.Link.LINK;

@Log4j2
@RequiredArgsConstructor
public class JooqChatLinkRepository implements ChatLinkRepository {
    private final DSLContext context;
    @Value("${spring.database.limit}")
    private int limit;

    @Override
    public List<ChatLinkResponse> findAllFiltered(OffsetDateTime time) {
        return context
            .select(LINK.LINK_ID, CHAT_LINK.CHAT_ID)
            .from(LINK)
            .join(CHAT_LINK).on(LINK.LINK_ID.eq(CHAT_LINK.LINK_ID))
            .where(LINK.CHECKED_AT.lt(time))
            .limit(limit)
            .fetch()
            .intoGroups(LINK.LINK_ID, rec -> rec.get(CHAT_LINK.CHAT_ID))
            .entrySet()
            .stream()
            .map(entry -> new ChatLinkResponse(entry.getKey(), new HashSet<>(entry.getValue())))
            .toList();
    }

    @Override
    public void add(Long chatId, Long linkId) {
        context.insertInto(CHAT_LINK, CHAT_LINK.CHAT_ID, CHAT_LINK.LINK_ID).values(chatId, linkId).execute();
    }

    @Override
    public LinkResponse remove(Long chatId, Long linkId) {
        LinkResponse response = context.select(LINK.LINK_ID, LINK.URL)
            .from(LINK)
            .join(CHAT_LINK)
            .on(CHAT_LINK.LINK_ID.eq(LINK.LINK_ID))
            .where(CHAT_LINK.CHAT_ID.eq(chatId).and(LINK.LINK_ID.eq(linkId)))
            .fetchOneInto(LinkResponse.class);
        context.deleteFrom(CHAT_LINK).where(CHAT_LINK.LINK_ID.eq(linkId).and(CHAT_LINK.CHAT_ID.eq(chatId))).execute();
        return response;
    }

    @Override
    public boolean isTracked(Long chatId, Long linkId) {
        long count = context.fetchCount(CHAT_LINK, CHAT_LINK.LINK_ID.eq(linkId).and(CHAT_LINK.CHAT_ID.eq(chatId)));
        return count > 0;
    }

    @Override
    public boolean hasChats(Long linkId) {
        long count = context.fetchCount(CHAT_LINK, CHAT_LINK.LINK_ID.eq(linkId));
        return count > 0;
    }
}
