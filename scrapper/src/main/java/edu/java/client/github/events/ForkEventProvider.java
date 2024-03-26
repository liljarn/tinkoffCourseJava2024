package edu.java.client.github.events;

import org.springframework.stereotype.Component;

@Component
public class ForkEventProvider implements EventProvider {
    @Override
    public String getMessage(GitHubEvent event) {
        String user = event.actor().login();
        return "%s форкнул репозиторий \uD83E\uDD78: ".formatted(makeUserText(user));
    }

    @Override
    public boolean checkType(String type) {
        return type.equals("ForkEvent");
    }
}
