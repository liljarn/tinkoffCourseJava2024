package edu.java.scheduler;

import edu.java.client.ClientInfoProvider;
import edu.java.client.dto.LinkInfo;
import edu.java.dto.ChatLinkResponse;
import edu.java.dto.LinkData;
import edu.java.dto.client.LinkUpdate;
import edu.java.service.link.LinkService;
import edu.java.service.sender.UpdateSender;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
   @Value("${spring.database.check-time-minutes}")
   private int minutesCheckTime;
   private final List<ClientInfoProvider> clientInfoProviders;
   private final UpdateSender updateSender;
   private final LinkService linkService;

   @Scheduled(fixedDelayString = "#{@'app-edu.java.configuration.ApplicationConfig'.scheduler.interval}")
   @Transactional
   public void update() {
       OffsetDateTime time = OffsetDateTime.now();
       time = time.minusMinutes(minutesCheckTime);
       List<ChatLinkResponse> linksToChats = linkService.findAllChatsByLinksFiltered(time);
       log.info(linksToChats);
       for (ChatLinkResponse linkToChats : linksToChats) {
           Long linkId = linkToChats.linkId();
           LinkData data = linkService.getData(linkId);
           for (ClientInfoProvider client : clientInfoProviders) {
               if (client.isValidated(data.url())) {
                   List<LinkInfo> listLinkInfo = client.fetchData(data.url())
                       .stream()
                       .filter(linkInfo -> linkInfo.lastActivityDate().isAfter(data.updateTime()))
                       .sorted(Comparator.comparing(LinkInfo::lastActivityDate))
                       .toList();
                   log.info(listLinkInfo);
                   for (LinkInfo info : listLinkInfo) {
                       LinkUpdate update = new LinkUpdate(
                           linkId,
                           data.url(),
                           info.title(),
                           linkToChats.tgChatIds().stream().toList()
                       );
                       updateSender.sendUpdate(update);
                   }
                   if (!listLinkInfo.isEmpty()) {
                       OffsetDateTime curTime = OffsetDateTime.now();
                       linkService.updateLink(
                           data.url(),
                           curTime,
                           listLinkInfo.get(listLinkInfo.size() - 1).lastActivityDate()
                       );
                   }
               }
           }
       }
   }
}

