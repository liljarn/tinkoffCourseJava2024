package edu.java.client.github.events;

import org.springframework.stereotype.Component;

@Component
public class PullRequestReviewEventProvider implements EventProvider {
    @Override
    public String getMessage(GitHubEvent event) {
        String user = "Пользователь <b>" + event.actor().login() + "</b>";
        String title = event.payload().pullRequest().title();
        String url = event.payload().pullRequest().url();
        return user + " начал ревьюить PullRequest \"" + makeHyperlink(url, title) + "\" \uD83E\uDD76: ";
    }

    @Override
    public boolean checkType(String type) {
        return type.equals("PullRequestReviewEvent");
    }
}
