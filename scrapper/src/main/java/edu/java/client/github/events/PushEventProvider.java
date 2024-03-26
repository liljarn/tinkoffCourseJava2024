package edu.java.client.github.events;

import org.springframework.stereotype.Component;

@Component
public class PushEventProvider implements EventProvider {
    @Override
    public String getMessage(GitHubEvent event) {
        String user = event.actor().login();
        String branch = event.payload().ref();
        branch = branch.substring(branch.lastIndexOf("/"));
        return "%s запушил в репозиторий новые коммиты в ветку \"%s\" \uD83E\uDD70: ".formatted(
            makeUserText(user),
            branch
        );
    }

    @Override
    public boolean checkType(String type) {
        return type.equals("PushEvent");
    }
}
