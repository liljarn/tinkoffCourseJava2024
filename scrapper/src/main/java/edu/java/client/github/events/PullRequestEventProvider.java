package edu.java.client.github.events;

import org.springframework.stereotype.Component;

@Component
public class PullRequestEventProvider implements EventProvider {
    @Override
    public String getMessage(GitHubEvent event) {
        String user = event.actor().login();
        String title = event.payload().pullRequest().title();
        String url = event.payload().pullRequest().url();
        if (event.payload().action().equals("opened")) {
            return "%s создал PullRequest \"%s\" : ".formatted(
                makeUserText(user),
                makeHyperlink(url, title)
            );
        } else {
            return "PullRequest \"%s\" был закрыт \uD83E\uDD73: ".formatted(makeHyperlink(url, title));
        }
    }

    @Override
    public boolean checkType(String type) {
        return type.equals("PullRequestEvent");
    }
}
