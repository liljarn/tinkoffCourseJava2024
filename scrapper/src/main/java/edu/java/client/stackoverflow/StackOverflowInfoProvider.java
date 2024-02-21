package edu.java.client.stackoverflow;

import edu.java.client.WebClientInfoProvider;
import edu.java.client.dto.LinkInfo;
import java.net.URL;
import java.util.regex.Pattern;

public class StackOverflowInfoProvider extends WebClientInfoProvider {
    private static final String BASE_API_URL = "https://api.stackexchange.com/2.3";
    private static final Pattern STACKOVERFLOW_PATTERN =
        Pattern.compile("https://stackoverflow.com/questions/(\\d+).*");

    public StackOverflowInfoProvider(String apiUrl) {
        super(apiUrl);
    }

    public StackOverflowInfoProvider() {
        this(BASE_API_URL);
    }

    @Override
    public LinkInfo fetchData(URL url) {
        if (!isSupported(url)) {
            return null;
        }
        String questionId = STACKOVERFLOW_PATTERN.matcher(url.toString()).group(1);
        StackOverflowResponse info = webClient
            .get()
            .uri("/questions/ " + questionId + "?site=stackoverflow")
            .retrieve()
            .bodyToMono(StackOverflowResponse.class)
            .block();
        if (info == null || info.items().length == 0) {
            return null;
        }
        return new LinkInfo(url, info.items()[0].title(), info.items()[0].lastActivityDate());
    }

    @Override
    public boolean isSupported(URL url) {
        return STACKOVERFLOW_PATTERN.matcher(url.toString()).matches();
    }
}
