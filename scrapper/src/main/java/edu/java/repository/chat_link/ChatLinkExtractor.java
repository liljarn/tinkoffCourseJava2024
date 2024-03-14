package edu.java.repository.chat_link;

import edu.java.dto.ChatLinkResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.ResultSetExtractor;

public class ChatLinkExtractor implements ResultSetExtractor<List<ChatLinkResponse>> {
    @Override
    public List<ChatLinkResponse> extractData(ResultSet resultSet) throws SQLException {
        Map<Long, ChatLinkResponse> chatLinkMap = new HashMap<>();
        List<ChatLinkResponse> result = new ArrayList<>();
        while (resultSet.next()) {
            Long linkId = resultSet.getLong("link_id");
            Long tgChatId = resultSet.getLong("chat_id");
            ChatLinkResponse chatLinkResponse = chatLinkMap.computeIfAbsent(
                linkId,
                k -> new ChatLinkResponse(linkId, new HashSet<>())
            );
            chatLinkResponse.tgChatIds().add(tgChatId);
            if (!result.contains(chatLinkResponse)) {
                result.add(chatLinkResponse);
            }
        }
        return result;
    }
}
