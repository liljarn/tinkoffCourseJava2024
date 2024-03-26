package edu.java.client.github.events;

import org.springframework.stereotype.Component;

@Component
public class IssuesEventProvider implements EventProvider {
    @Override
    public String getMessage(GitHubEvent event) {
        String user = event.actor().login();
        String url = event.payload().issue().url();
        String title = event.payload().issue().title();
        if (event.payload().action().equals("opened")) {
            return  "%s создал новый issue \"%s\" \uD83D\uDC80: ".formatted(
                makeUserText(user),
                makeHyperlink(url, title)
            );
        } else if (event.payload().action().equals("closed")) {
            return "Был решён issue \"%s\" \uD83D\uDE0E: ".formatted(makeHyperlink(url, title));
        } else {
            return  "Произошли изменения issue \"%s\": ".formatted(makeHyperlink(url, title));
        }
    }

    @Override
    public boolean checkType(String type) {
        return type.equals("IssuesEvent");
    }
}
