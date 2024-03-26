package edu.java.client.github.events;

import org.springframework.stereotype.Component;

@Component
public class IssueCommentEventProvider implements EventProvider {
    @Override
    public String getMessage(GitHubEvent event) {
        String user = event.actor().login();
        String url = event.payload().issue().url();
        String title = event.payload().issue().title();
        return "%s оставил комментарий к \"%s\" \uD83E\uDDD0: ".formatted(
            makeUserText(user),
            makeHyperlink(url, title)
        );
    }

    @Override
    public boolean checkType(String type) {
        return type.equals("IssueCommentEvent");
    }
}
