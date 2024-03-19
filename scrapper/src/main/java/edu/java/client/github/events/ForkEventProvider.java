package edu.java.client.github.events;

import org.springframework.stereotype.Component;

@Component
public class ForkEventProvider implements EventProvider {
    @Override
    public String getMessage(GitHubEvent event) {
        String user = "Пользователь <b>" + event.actor().login() + "</b>";
        return user + " форкнул репозиторий \uD83E\uDD78: ";
    }

    @Override
    public boolean checkType(String type) {
        return type.equals("ForkEvent");
    }
}
