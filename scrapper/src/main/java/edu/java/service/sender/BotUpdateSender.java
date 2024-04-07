package edu.java.service.sender;

import edu.java.client.bot.BotClient;
import edu.java.dto.client.LinkUpdate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BotUpdateSender implements UpdateSender {
    private final BotClient botClient;

    @Override
    public void sendUpdate(LinkUpdate update) {
        botClient.sendUpdate(update);
    }
}
