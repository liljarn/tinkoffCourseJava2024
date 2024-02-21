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
    public LinkInfo fetchData(URL url) {
        if (!isSupported(url)) {
            return null;
        }
        GitHubInfo info = webClient
            .get()
            .uri(url.getPath())
            .retrieve()
            .bodyToMono(GitHubInfo.class)
            .block();
        if (info == null) {
            return null;
        }
        return new LinkInfo(url, info.name(), info.update());
    }

    @Override
    public boolean isSupported(URL url) {
        return GITHUB_PATTERN.matcher(url.toString()).matches();
    }
}
