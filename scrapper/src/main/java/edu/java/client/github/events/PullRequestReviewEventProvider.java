package edu.java.client.github.events;

import org.springframework.stereotype.Component;

@Component
public class PullRequestReviewEventProvider implements EventProvider {
    @Override
    public String getMessage(GitHubEvent event) {
        String user = event.actor().login();
        String title = event.payload().pullRequest().title();
        String url = event.payload().pullRequest().url();
        return "%s начал ревьюить PullRequest \"%s\" \uD83E\uDD76: ".formatted(
            makeUserText(user),
            makeHyperlink(url, title)
        );
    }

    @Override
    public boolean checkType(String type) {
        return type.equals("PullRequestReviewEvent");
    }
}
