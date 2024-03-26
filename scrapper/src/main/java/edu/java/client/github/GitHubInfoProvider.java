package edu.java.client.github;

import edu.java.client.WebClientInfoProvider;
import edu.java.client.dto.LinkInfo;
import edu.java.client.github.events.EventProvider;
import edu.java.client.github.events.GitHubEvent;
import edu.java.exceptions.LinkNotSupportedException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

@Log4j2
public class GitHubInfoProvider extends WebClientInfoProvider {
    private static final String BASE_API_URL = "https://api.github.com/repos";
    private static final String EVENTS_ENDPOINT = "/events?per_page=10";
    private static final Pattern GITHUB_PATTERN = Pattern.compile("https://github.com/(.+)/(.+)");

    private final List<EventProvider> eventProviders;

    @Value("${client.github.token}")
    private String token;

    public GitHubInfoProvider(String apiUrl, List<EventProvider> eventProviders) {
        super(apiUrl);
        this.eventProviders = eventProviders;
    }

    public GitHubInfoProvider(List<EventProvider> eventProviders) {
        this(BASE_API_URL, eventProviders);
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
            .onErrorReturn(new GitHubEvent[0])
            .block();
        if (info == null || info.length == 0) {
            String response = webClient
                .get()
                .uri(url.getPath())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorReturn("")
                .block();
            if (response.isEmpty()) {
                throw new LinkNotSupportedException(url);
            }
            return Collections.emptyList();
        }
        return Arrays.stream(info)
            .flatMap(event -> eventProviders.stream()
                .filter(provider -> provider.checkType(event.type()))
                .map(provider -> new LinkInfo(url, provider.getMessage(event), event.updateTime()))
            )
            .toList();
    }
}
