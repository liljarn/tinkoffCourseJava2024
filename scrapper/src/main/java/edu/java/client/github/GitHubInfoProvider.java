package edu.java.client.github;

import edu.java.client.WebClientInfoProvider;
import edu.java.client.dto.LinkInfo;
import edu.java.exceptions.LinkNotSupportedException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;

public class GitHubInfoProvider extends WebClientInfoProvider {
    private static final String BASE_API_URL = "https://api.github.com/repos/";
    private static final String EVENTS_ENDPOINT = "/events";
    private static final Pattern GITHUB_PATTERN = Pattern.compile("https://github.com/(.+)/(.+)");

    @Value("${client.github.token}")
    private String token;

    public GitHubInfoProvider(String apiUrl) {
        super(apiUrl);
    }

    public GitHubInfoProvider() {
        this(BASE_API_URL);
    }

    @Override
    public boolean isValidated(URI url) {
        return GITHUB_PATTERN.matcher(url.toString()).matches();
    }

    @Override
    public LinkInfo fetchData(URI url) {
        if (!isValidated(url)) {
            return null;
        }
        GitHubEvent[] info = webClient
            .get()
            .uri(url.getPath() + EVENTS_ENDPOINT)
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .bodyToMono(GitHubEvent[].class)
            .onErrorReturn(new GitHubEvent[0])
            .block();
        if (info == null || info.length == 0) {
            throw new LinkNotSupportedException(url);
        }
        GitHubEvent githubEvent = Arrays.stream(info).toList().stream()
            .filter(event -> event.type().equals("IssuesEvent") || event.type().equals("PushEvent")).findFirst().get();
        return new LinkInfo(url, getDescription(githubEvent.type(), githubEvent), githubEvent.updateTime());
        //return new LinkInfo(url, info.name(), info.update());
    }

    private String getDescription(String eventType, GitHubEvent event) {
        String message = "";
        if (eventType.equals("IssuesEvent")) {
            String title = event.payload().issue().title();
            if (event.payload().action().equals("opened")) {
                message = "В репозитории был создан новый issue \"" + title + "\" \uD83D\uDC80:";
            } else if (event.payload().action().equals("closed")) {
                message = "В репозитории был решён issue \"" + title + "\" \uD83D\uDE0E:";
            } else {
                message = "В репозитории произошли изменения issue \"" + title + "\":";
            }
        } else {
            String branch = event.payload().ref();
            branch = branch.substring(branch.lastIndexOf("/"));
            message = "В ветку \"" + branch + "\" репозитория были запушены новые коммиты: ";
        }
        return message;
    }
}
