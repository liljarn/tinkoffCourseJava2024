package edu.java.client.github.events;

import org.springframework.stereotype.Component;

@Component
public class IssueCommentEventProvider implements EventProvider {
    @Override
    public String getMessage(GitHubEvent event) {
        String user = "Пользователь <b>" + event.actor().login() + "</b>";
        String url = event.payload().issue().url();
        String title = event.payload().issue().title();
        return user + " оставил комментарий к \"" + makeHyperlink(url, title) + "\" \uD83E\uDDD0: ";
    }

    @Override
    public boolean checkType(String type) {
        return type.equals("IssueCommentEvent");
    }
}
