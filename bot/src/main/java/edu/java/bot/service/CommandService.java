package edu.java.bot.service;

import edu.java.bot.model.Link;
import java.util.List;
import java.util.UUID;

public interface CommandService {
    void registration(long userId);

    void track(long userId, String url);

    void untrack(long userId, UUID linkId);

    List<Link> list(long userId);
}
