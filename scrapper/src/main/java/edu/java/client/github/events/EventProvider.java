package edu.java.client.github.events;

public interface EventProvider {
    String getMessage(GitHubEvent event);

    boolean checkType(String type);

    default String makeHyperlink(String url, String title) {
        return "<a href=\"" + url + "\">" + title + "</a>";
    }

    default String makeUserText(String user) {
        return "Пользователь <b>%s</b>".formatted(user);
    }
}
