package edu.java.scheduler;

import edu.java.client.ClientInfoProvider;
import edu.java.client.bot.BotClient;
import edu.java.client.dto.LinkInfo;
import edu.java.dto.ChatLinkResponse;
import edu.java.dto.client.LinkUpdate;
import edu.java.repository.chat_link.ChatLinkRepository;
import edu.java.repository.link.LinkRepository;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final ClientInfoProvider stackOverflowInfoProvider;
    private final ClientInfoProvider gitHubInfoProvider;
    private final BotClient botClient;
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;

    @Scheduled(fixedDelayString = "#{@'app-edu.java.configuration.ApplicationConfig'.scheduler.interval}")
    @SneakyThrows
    @Transactional
    public void update() {
        //LinkInfo info = null;
        List<ChatLinkResponse> linksToChats = chatLinkRepository.findAll();
        for (ChatLinkResponse linkToChats : linksToChats) {
            Long linkId = linkToChats.linkId();
            URL url = linkRepository.findLink(linkId).toURL();
            OffsetDateTime updateTime = linkRepository.getDate(linkId);
            LinkUpdate update = new LinkUpdate(linkId, url.toURI(), "", linkToChats.tgChatIds());
//            if (stackOverflowInfoProvider.isValidated(url)) {
//                info = stackOverflowInfoProvider.fetchData(url);
//            } else if (gitHubInfoProvider.isValidated(url)) {
//                info = gitHubInfoProvider.fetchData(url);
//                if (info.lastActivityDate().isBefore(updateTime)) {
//                    LinkUpdate update = new LinkUpdate(linkId, url.toURI(),  "", linkToChats.tgChatIds());
//                    linkRepository.updateLink(info);
//                    botClient.sendUpdate(update);
//                }
//            }
            sendUpdate(stackOverflowInfoProvider, url, updateTime, update);
            sendUpdate(gitHubInfoProvider, url, updateTime, update);
        }
        log.info(chatLinkRepository.findAll());
    }

    @SneakyThrows
    private void sendUpdate(ClientInfoProvider client, URL url, OffsetDateTime updateTime, LinkUpdate update) {
        if (client.isValidated(url)) {
            LinkInfo info = client.fetchData(url);
            if (info.lastActivityDate().isBefore(updateTime)) {
                linkRepository.updateLink(info);
                botClient.sendUpdate(update);
            }
        }
    }
}
