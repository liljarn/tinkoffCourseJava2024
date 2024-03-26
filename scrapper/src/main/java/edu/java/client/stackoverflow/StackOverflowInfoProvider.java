package edu.java.client.stackoverflow;

import edu.java.client.WebClientInfoProvider;
import edu.java.client.dto.LinkInfo;
import edu.java.exceptions.LinkNotSupportedException;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.log4j.Log4j2;

@Log4j2
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
    public boolean isValidated(URI url) {
        return STACKOVERFLOW_PATTERN.matcher(url.toString()).matches();
    }

    @Override
    public LinkInfo fetchData(URI url) {
        Matcher matcher = STACKOVERFLOW_PATTERN.matcher(url.toString());
        if (!matcher.matches()) {
            return null;
        }
        String questionId = matcher.group(1);
        log.info(questionId);
        StackOverflowResponse info = webClient
            .get()
            .uri("/questions/" + questionId + "?site=stackoverflow")
            .retrieve()
            .bodyToMono(StackOverflowResponse.class)
            .onErrorReturn(new StackOverflowResponse(null))
            .block();
        if (info == null || info.equals(new StackOverflowResponse(null)) || info.items().length == 0) {
            throw new LinkNotSupportedException(url);
        }
        return new LinkInfo(url, info.items()[0].title(), info.items()[0].lastActivityDate());
    }
}
