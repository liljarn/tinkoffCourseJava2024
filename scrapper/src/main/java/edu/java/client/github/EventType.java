package edu.java.client.github;

import lombok.Getter;

@Getter
@SuppressWarnings("checkstyle:MultipleStringLiterals")
public enum EventType {
    PUSH_EVENT("PushEvent"),
    ISSUES_EVENT("IssuesEvent"),
    ISSUE_COMMENT_EVENT("IssueCommentEvent"),
    PULL_REQUEST_EVENT("PullRequestEvent"),
    PULL_REQUEST_REVIEW_EVENT("PullRequestReviewEvent"),
    FORK_EVENT("ForkEvent");

    private final String type;

    EventType(String type) {
        this.type = type;
    }

    public static String getTypeMessage(GitHubEvent event) {
        String user = "Пользователь <b>" + event.actor().login() + "</b>";
        return switch (event.type()) {
            case "PushEvent" -> {
                String branch = event.payload().ref();
                branch = branch.substring(branch.lastIndexOf("/"));
                yield user + " запушил в репозиторий новые коммиты в ветку " + "\"" + branch + "\" \uD83E\uDD70: ";
            }
            case "IssuesEvent" -> {
                String url = event.payload().issue().url();
                String title = event.payload().issue().title();
                if (event.payload().action().equals("opened")) {
                    yield "Был создан новый issue \"" + makeHyperlink(url, title) + "\" \uD83D\uDC80: ";
                } else if (event.payload().action().equals("closed")) {
                    yield "Был решён issue \"" + makeHyperlink(url, title) + "\" \uD83D\uDE0E: ";
                } else {
                    yield "Произошли изменения issue \"" + makeHyperlink(url, title) + "\": ";
                }
            }
            case "IssueCommentEvent" -> {
                String url = event.payload().issue().url();
                String title = event.payload().issue().title();
                yield user + " оставил комментарий к \"" + makeHyperlink(url, title) + "\" \uD83E\uDDD0: ";
            }
            case "PullRequestEvent" -> {
                if (event.payload().action().equals("opened")) {
                    yield user + " создал PullRequest \"" + event.payload().pullRequest().title()
                          + "\" : ";
                } else {
                    String title = event.payload().pullRequest().title();
                    String url = event.payload().pullRequest().url();
                    yield "PullRequest \"" + makeHyperlink(url, title) + "\" был закрыт \uD83E\uDD73: ";
                }
            }
            case "PullRequestReviewEvent" -> {
                String title = event.payload().pullRequest().title();
                String url = event.payload().pullRequest().url();
                yield user + " начал ревьюить PullRequest \"" + makeHyperlink(url, title) + "\" \uD83E\uDD76: ";
            }
            case "ForkEvent" -> user + " форкнул репозиторий \uD83E\uDD78: ";
            default -> "Unknown event type";
        };
    }

    private static String makeHyperlink(String url, String title) {
        return "<a href=\"" + url + "\">" + title + "</a>";
    }
}
