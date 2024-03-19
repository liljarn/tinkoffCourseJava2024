package edu.java.client.github.events;

import org.springframework.stereotype.Component;

@Component
public class PushEventProvider implements EventProvider {
    @Override
    public String getMessage(GitHubEvent event) {
        String user = "Пользователь <b>" + event.actor().login() + "</b>";
        String branch = event.payload().ref();
        branch = branch.substring(branch.lastIndexOf("/"));
        return user + " запушил в репозиторий новые коммиты в ветку " + "\"" + branch + "\" \uD83E\uDD70: ";
    }

    @Override
    public boolean checkType(String type) {
        return type.equals("PushEvent");
    }
}
