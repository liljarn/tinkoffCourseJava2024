package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.ClientInfoProvider;
import edu.java.client.dto.LinkInfo;
import edu.java.client.github.GitHubInfoProvider;
import java.net.URL;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

public class GitHubInfoProviderTest {
    private static final String API_LINK = "/repos/liljarn/tinkoffCourseJava2023";
    private static final String LINK = "https://github.com/repos/liljarn/tinkoffCourseJava2023";
    private static final String NOT_GITHUB_LINK = "https://youtube.com";

    private WireMockServer server;

    @BeforeEach
    public void setUp() {
        server = new WireMockServer(wireMockConfig().dynamicPort());
        server.stubFor(get(urlPathMatching(API_LINK))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                       "id": 699502365,
                       "name": "tinkoffCourseJava2023",
                       "full_name": "liljarn/tinkoffCourseJava2023",
                       "private": false,
                       "description": null,
                       "created_at": "2023-10-02T19:01:04Z",
                       "updated_at": "2023-10-02T19:07:33Z",
                       "pushed_at": "2024-01-10T15:06:43Z"
                     }""")));
        server.stubFor(get(urlPathMatching("/repos/aboba/abobus"))
            .willReturn(aResponse()
                .withStatus(404)));
        server.start();
    }

    @Test
    @DisplayName("Existing GitHub repository link test")
    @SneakyThrows
    public void fetchData_shouldReturnCorrectData_whenRepositoryExists() {
        ClientInfoProvider client = new GitHubInfoProvider(server.baseUrl());
        LinkInfo info = client.fetchData(new URL(LINK));
        assertThat(info).extracting(LinkInfo::url, LinkInfo::title).contains(new URL(LINK), "tinkoffCourseJava2023");
    }

    @Test
    @DisplayName("Nonexistent GitHub repository link test")
    @SneakyThrows
    public void fetchData_shouldReturnNull_whenRepositoryDoesNotExist() {
        ClientInfoProvider client = new GitHubInfoProvider(server.baseUrl());
        LinkInfo info = client.fetchData(new URL("https://github.com/repos/aboba/abobus"));
        assertThat(info).isNull();
    }

    @Test
    @DisplayName("Not GitHub link test")
    @SneakyThrows
    public void fetchData_shouldReturnNull_whenLinkDoesNotSupport() {
        ClientInfoProvider client = new GitHubInfoProvider(server.baseUrl());
        LinkInfo info = client.fetchData(new URL(NOT_GITHUB_LINK));
        assertThat(info).isNull();
    }

    @Test
    @DisplayName("GitHub repository link test")
    @SneakyThrows
    public void isValidate_shouldReturnTrue_whenLinkIsValidated() {
        ClientInfoProvider client = new GitHubInfoProvider(server.baseUrl());
        assertThat(client.isValidated(new URL(LINK))).isTrue();
    }

    @Test
    @DisplayName("Not GitHub repository link test")
    @SneakyThrows
    public void isValidate_shouldReturnFalse_whenLinkIsNotValidated() {
        ClientInfoProvider client = new GitHubInfoProvider(server.baseUrl());
        assertThat(client.isValidated(new URL(NOT_GITHUB_LINK))).isFalse();
    }

    @AfterEach
    public void shutdown() {
        server.stop();
    }
}
