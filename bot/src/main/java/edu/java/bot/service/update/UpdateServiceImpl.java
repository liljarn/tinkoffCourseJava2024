package edu.java.bot.service.update;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dto.request.LinkUpdate;
import edu.java.bot.sender.Sender;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateServiceImpl implements UpdateService {
    private final Sender sender;

    @Override
    public void updateLink(LinkUpdate linkUpdate) {
        List<Long> tgChatIds = linkUpdate.tgChatIds();
        for (long chatId : tgChatIds) {
            sender.sendMessage(new SendMessage(chatId, linkUpdate.description() + linkUpdate.url()));
        }
    }
}
