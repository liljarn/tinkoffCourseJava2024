package edu.java.client.github.events;

import org.springframework.stereotype.Component;

@Component
public class IssuesEventProvider implements EventProvider {
    @Override
    public String getMessage(GitHubEvent event) {
        String user = "Пользователь <b>" + event.actor().login() + "</b>";
        String url = event.payload().issue().url();
        String title = event.payload().issue().title();
        if (event.payload().action().equals("opened")) {
            return  user + " создал новый issue \"" + makeHyperlink(url, title) + "\" \uD83D\uDC80: ";
        } else if (event.payload().action().equals("closed")) {
            return "Был решён issue \"" + makeHyperlink(url, title) + "\" \uD83D\uDE0E: ";
        } else {
            return  "Произошли изменения issue \"" + makeHyperlink(url, title) + "\": ";
        }
    }

    @Override
    public boolean checkType(String type) {
        return type.equals("IssuesEvent");
    }
}
