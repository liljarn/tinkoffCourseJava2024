package edu.java.client.github.events;

import org.springframework.stereotype.Component;

@Component
public class PullRequestEventProvider implements EventProvider {
    @Override
    public String getMessage(GitHubEvent event) {
        String user = "Пользователь <b>" + event.actor().login() + "</b>";
        String title = event.payload().pullRequest().title();
        String url = event.payload().pullRequest().url();
        if (event.payload().action().equals("opened")) {
            return user + " создал PullRequest \"" + makeHyperlink(url, title) + "\" : ";
        } else {
            return  "PullRequest \"" + makeHyperlink(url, title) + "\" был закрыт \uD83E\uDD73: ";
        }
    }

    @Override
    public boolean checkType(String type) {
        return type.equals("PullRequestEvent");
    }
}
