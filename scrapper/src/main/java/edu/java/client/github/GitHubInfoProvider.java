package edu.java.client.github;

import edu.java.client.WebClientInfoProvider;
import edu.java.client.dto.LinkInfo;
import edu.java.exceptions.LinkNotSupportedException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

@Log4j2
public class GitHubInfoProvider extends WebClientInfoProvider {
    private static final String BASE_API_URL = "https://api.github.com/repos";
    private static final String EVENTS_ENDPOINT = "/events?per_page=10";
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

    @SuppressWarnings("checkstyle:MultipleStringLiterals") @Override
    public List<LinkInfo> fetchData(URI url) {
        if (!isValidated(url)) {
            return null;
        }
        GitHubEvent[] info = webClient
            .get()
            .uri(url.getPath() + EVENTS_ENDPOINT)
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .bodyToMono(GitHubEvent[].class)
            .block();
        if (info == null || info.length == 0) {
            throw new LinkNotSupportedException(url);
        }
        List<GitHubEvent> events = Arrays.stream(info).toList().stream()
            .filter(event -> event.type().equals("IssuesEvent") || event.type().equals("PushEvent"))
            .toList();
        return events.stream()
            .map(event -> new LinkInfo(url, getDescription(event.type(), event), event.updateTime()))
            .toList();
    }

    private String getDescription(String eventType, GitHubEvent event) {
        String message;
        if (eventType.equals("IssuesEvent")) {
            String title = event.payload().issue().title();
            if (event.payload().action().equals("opened")) {
                message = "В репозитории был создан новый issue \"" + title + "\" \uD83D\uDC80: ";
            } else if (event.payload().action().equals("closed")) {
                message = "В репозитории был решён issue \"" + title + "\" \uD83D\uDE0E: ";
            } else {
                message = "В репозитории произошли изменения issue \"" + title + "\": ";
            }
        } else {
            String branch = event.payload().ref();
            branch = branch.substring(branch.lastIndexOf("/"));
            message = "В ветку \"" + branch + "\" репозитория были запушены новые коммиты: ";
        }
        return message;
    }
}
