package edu.java.client.stackoverflow;

import edu.java.client.WebClientInfoProvider;
import edu.java.client.dto.LinkInfo;
import edu.java.configuration.RetryConfiguration;
import edu.java.exceptions.LinkNotSupportedException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

@Log4j2
public class StackOverflowInfoProvider extends WebClientInfoProvider {
    private static final String BASE_API_URL = "https://api.stackexchange.com/2.3";
    private static final Pattern STACKOVERFLOW_PATTERN =
        Pattern.compile("https://stackoverflow.com/questions/(\\d+).*");

    @Value("${client.stackoverflow.access-token}")
    private String accessToken;
    @Value("${client.stackoverflow.key}")
    private String key;

    public StackOverflowInfoProvider(String apiUrl, RetryConfiguration config) {
        super(apiUrl, config, "stackoverflow");
    }

    public StackOverflowInfoProvider(RetryConfiguration config) {
        this(BASE_API_URL, config);
    }

    @Override
    public boolean isValidated(URI url) {
        return STACKOVERFLOW_PATTERN.matcher(url.toString()).matches();
    }

    @Override
    public List<LinkInfo> fetchData(URI url) {
        Matcher matcher = STACKOVERFLOW_PATTERN.matcher(url.toString());
        if (!matcher.matches()) {
            return null;
        }
        String questionId = matcher.group(1);
        StackOverflowResponse info = webClient
            .get()
            .uri("/questions/" + questionId
                 + "?site=stackoverflow&access_token=" + accessToken + "&key=" + key)
            .retrieve()
            .bodyToMono(StackOverflowResponse.class)
            .onErrorReturn(new StackOverflowResponse(null))
            .block();
        if (info == null || info.equals(new StackOverflowResponse(null)) || info.items().length == 0) {
            throw new LinkNotSupportedException(url);
        }
        List<LinkInfo> listInfo = new ArrayList<>();
        listInfo.add(new LinkInfo(
            url,
            "Был обновлён вопрос \"" + info.items()[0].title() + "\": ",
            info.items()[0].lastActivityDate()
        ));
        return listInfo;
    }
}
