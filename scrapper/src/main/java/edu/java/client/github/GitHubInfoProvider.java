package edu.java.client.github;

import edu.java.client.WebClientInfoProvider;
import edu.java.client.dto.LinkInfo;
import java.net.URI;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;

public class GitHubInfoProvider extends WebClientInfoProvider {
    private static final String BASE_API_URL = "https://api.github.com/repos/";
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
        GitHubInfo info = webClient
            .get()
            .uri(url.getPath())
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .bodyToMono(GitHubInfo.class)
            .onErrorReturn(new GitHubInfo(null, null))
            .block();
        if (info == null || info.equals(new GitHubInfo(null, null))) {
            return null;
        }
        return new LinkInfo(url, info.name(), info.update());
    }
}
