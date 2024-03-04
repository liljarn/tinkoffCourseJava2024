package edu.java.client.github;

import edu.java.client.WebClientInfoProvider;
import edu.java.client.dto.LinkInfo;
import java.net.URL;
import java.util.regex.Pattern;

public class GitHubInfoProvider extends WebClientInfoProvider {
    private static final String BASE_API_URL = "https://api.github.com/repos/";
    private static final Pattern GITHUB_PATTERN = Pattern.compile("https://github.com/(.+)/(.+)");

    public GitHubInfoProvider(String apiUrl) {
        super(apiUrl);
    }

    public GitHubInfoProvider() {
        this(BASE_API_URL);
    }

    @Override
    public boolean isValidated(URL url) {
        return GITHUB_PATTERN.matcher(url.toString()).matches();
    }

    @Override
    public LinkInfo fetchData(URL url) {
        if (!isValidated(url)) {
            return null;
        }
        GitHubInfo info = webClient
            .get()
            .uri(url.getPath())
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
