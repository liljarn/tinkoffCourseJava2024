package edu.java.bot.service;

import edu.java.bot.dto.request.LinkUpdate;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UpdateServiceImpl implements UpdateService {
    @Override
    public void updateLink(LinkUpdate linkUpdate) {
        log.info("Link was updated");
    }
}
