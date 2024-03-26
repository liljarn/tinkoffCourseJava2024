package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.ClientInfoProvider;
import edu.java.client.dto.LinkInfo;
import edu.java.client.github.GitHubInfoProvider;
import edu.java.client.github.events.EventProvider;
import edu.java.client.github.events.PushEventProvider;
import edu.java.exceptions.LinkNotSupportedException;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class GitHubInfoProviderTest {
    private static final String API_LINK = "/repos/liljarn/tinkoffCourseJava2023/events";
    private static final String LINK = "https://github.com/repos/liljarn/tinkoffCourseJava2023";
    private static final String NOT_GITHUB_LINK = "https://youtube.com";
    private final List<EventProvider> providers = List.of(new PushEventProvider());
    private WireMockServer server;

    //Arrange
    @BeforeEach
    public void setUp() {
        server = new WireMockServer(wireMockConfig().dynamicPort());
        server.stubFor(get(urlPathMatching(API_LINK))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    [
                      {
                        "type": "PushEvent",
                        "payload": {
                          "ref": "refs/heads/master"
                        },
                        "actor": {
                          "login": "liljarn"
                        },
                        "created_at": "2024-03-16T19:22:17Z"
                      }
                    ]""")));
        server.stubFor(get(urlPathMatching("/repos/aboba/abobus/events"))
            .willReturn(aResponse()
                .withStatus(404)));
        server.start();
    }

    @Test
    @DisplayName("Existing GitHub repository link test")
    public void fetchData_shouldReturnCorrectData_whenRepositoryExists() {
        //Arrange
        ClientInfoProvider client = new GitHubInfoProvider(server.baseUrl(), providers);
        URI url = URI.create(LINK);
        String title =
            "Пользователь <b>liljarn</b> запушил в репозиторий новые коммиты в ветку \"/master\" \uD83E\uDD70: ";
        //Act
        List<LinkInfo> info = client.fetchData(url);
        //Assert
        assertThat(info.get(0)).extracting(LinkInfo::url, LinkInfo::title)
            .contains(url, title);
    }

    @Test
    @DisplayName("Not updated GitHub repository link test")
    public void fetchData_shouldEmptyList_whenRepositoryWasNotUpdated() {
        //Arrange
        String oldApiLinkEvents = "/repos/liljarn/tinkoff/events";
        String oldApiLink = "/repos/liljarn/tinkoff";
        String oldLink = "https://github.com/repos/liljarn/tinkoff";
        server.stubFor(get(urlPathMatching(oldApiLinkEvents))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    [

                    ]""")));
        server.stubFor(get(urlPathMatching(oldApiLink))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "text": "test"
                    }""")));
        ClientInfoProvider client = new GitHubInfoProvider(server.baseUrl(), providers);
        URI url = URI.create(oldLink);
        //Act
        List<LinkInfo> info = client.fetchData(url);
        //Assert
        assertThat(info).isEmpty();
    }

    @Test
    @DisplayName("Nonexistent GitHub repository link test")
    public void fetchData_shouldThrowLinkNotSupportedException_whenRepositoryDoesNotExist() {
        //Arrange
        ClientInfoProvider client = new GitHubInfoProvider(server.baseUrl(), providers);
        URI url = URI.create("https://github.com/repos/aboba/abobus");
        //Expect
        assertThatThrownBy(() -> client.fetchData(url))
            .isInstanceOf(LinkNotSupportedException.class);
    }

    @Test
    @DisplayName("Not GitHub link test")
    public void fetchData_shouldReturnNull_whenLinkDoesNotSupport() {
        //Arrange
        ClientInfoProvider client = new GitHubInfoProvider(server.baseUrl(), providers);
        //Act
        List<LinkInfo> info = client.fetchData(URI.create(NOT_GITHUB_LINK));
        //Assert
        assertThat(info).isNull();
    }

    @Test
    @DisplayName("GitHub repository link test")
    public void isValidate_shouldReturnTrue_whenLinkIsValidated() {
        //Arrange
        ClientInfoProvider client = new GitHubInfoProvider(server.baseUrl(), providers);
        //Act
        boolean response = client.isValidated(URI.create(LINK));
        //Assert
        assertThat(response).isTrue();
    }

    @Test
    @DisplayName("Not GitHub repository link test")
    public void isValidate_shouldReturnFalse_whenLinkIsNotValidated() {
        //Arrange
        ClientInfoProvider client = new GitHubInfoProvider(server.baseUrl(), providers);
        //Act
        boolean response = client.isValidated(URI.create(NOT_GITHUB_LINK));
        //Assert
        assertThat(response).isFalse();
    }

    @AfterEach
    public void shutdown() {
        server.stop();
    }
}
