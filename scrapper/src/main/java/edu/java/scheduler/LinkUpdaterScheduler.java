package edu.java.scheduler;

import edu.java.client.ClientInfoProvider;
import edu.java.client.bot.BotClient;
import edu.java.client.dto.LinkInfo;
import edu.java.dto.ChatLinkResponse;
import edu.java.dto.LinkData;
import edu.java.dto.client.LinkUpdate;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.exceptions.LinkNotSupportedException;
import edu.java.repository.chat_link.ChatLinkRepository;
import edu.java.repository.link.LinkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final List<ClientInfoProvider> clientInfoProviders;
    private final BotClient botClient;
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;

    @Scheduled(fixedDelayString = "#{@'app-edu.java.configuration.ApplicationConfig'.scheduler.interval}")
    @Transactional
    public void update() {
        List<ChatLinkResponse> linksToChats = chatLinkRepository.findAll();
        for (ChatLinkResponse linkToChats : linksToChats) {
            Long linkId = linkToChats.linkId();
            LinkData data = linkRepository.getData(linkId);
            LinkUpdate update = new LinkUpdate(
                linkId,
                data.url(),
                "Ссылка была обновлена: ",
                linkToChats.tgChatIds()
            );
            for (ClientInfoProvider client : clientInfoProviders) {
                if (client.isValidated(data.url())) {
                    LinkInfo info = client.fetchData(data.url());
                    if (info.lastActivityDate().isAfter(data.updateTime())) {
                        linkRepository.updateLink(info);
                        botClient.sendUpdate(update);
                    }
                }
            }
        }
    }
}

