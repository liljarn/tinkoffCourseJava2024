package edu.java.repository.link;

import edu.java.dto.LinkData;
import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import static edu.java.repository.jooq.tables.ChatLink.CHAT_LINK;
import static edu.java.repository.jooq.tables.Link.LINK;

@RequiredArgsConstructor
@Component
public class JooqLinkRepository implements LinkRepository {
    private final DSLContext context;

    @Override
    public ListLinksResponse findAll(Long chatId) {
        List<LinkResponse> list = context.select(LINK.LINK_ID, LINK.URL)
            .from(LINK)
            .join(CHAT_LINK)
            .on(LINK.LINK_ID.eq(CHAT_LINK.LINK_ID))
            .where(CHAT_LINK.CHAT_ID.eq(chatId))
            .fetchInto(LinkResponse.class);
        return new ListLinksResponse(list, list.size());
    }

    @Override
    public LinkResponse add(AddLinkRequest addLinkRequest) {
        Long linkId =
            context.insertInto(LINK, LINK.URL).values(addLinkRequest.link().toString()).returning(LINK.LINK_ID)
                .fetchOne(LINK.LINK_ID);
        return new LinkResponse(linkId, addLinkRequest.link());
    }

    @Override
    public LinkResponse remove(RemoveLinkRequest removeLinkRequest) {
        Long linkId = removeLinkRequest.id();
        String url = context.deleteFrom(LINK).where(LINK.LINK_ID.eq(linkId)).returning(LINK.URL).fetchOne(LINK.URL);
        return new LinkResponse(linkId, URI.create(url));
    }

    @Override
    public LinkData getData(Long linkId) {
        return context.select(LINK.LAST_UPDATE_TIME, LINK.URL).from(LINK).where(LINK.LINK_ID.eq(linkId))
            .fetchAny().into(LinkData.class);
    }

    @Override
    public void updateLink(URI url, OffsetDateTime checkTime, OffsetDateTime lastUpdateTime) {
        context.update(LINK)
            .set(LINK.LAST_UPDATE_TIME, lastUpdateTime)
            .set(LINK.CHECKED_AT, checkTime)
            .where(LINK.URL.eq(url.toString()))
            .execute();
    }

    @Override
    public Long getLinkId(String url) {
        boolean isInTable = context.selectCount()
                                .from(LINK)
                                .where(LINK.URL.eq(url))
                                .fetchOne(0, int.class) > 0;

        if (isInTable) {
            Long linkId = context.select(LINK.LINK_ID)
                .from(LINK)
                .where(LINK.URL.eq(url))
                .fetchOneInto(Long.class);

            return linkId;
        }
        return 0L;
    }
}
