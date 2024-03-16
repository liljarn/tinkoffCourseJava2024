//package edu.java.scrapper.client;
//
//import com.github.tomakehurst.wiremock.WireMockServer;
//import edu.java.client.ClientInfoProvider;
//import edu.java.client.dto.LinkInfo;
//import edu.java.client.stackoverflow.StackOverflowInfoProvider;
//import java.net.URI;
//import edu.java.exceptions.LinkNotSupportedException;
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
//import static com.github.tomakehurst.wiremock.client.WireMock.get;
//import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
//import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//public class StackOverflowInfoProviderTest {
//    private static final String API_LINK = "/questions/53579112*";
//    private static final String LINK =
//        "https://stackoverflow.com/questions/53579112/inject-list-of-all-beans-with-a-certain-interface";
//    private static final String NOT_STACKOVERFLOW_LINK = "https://youtube.com";
//
//    private WireMockServer server;
//
//    //Arrange
//    @BeforeEach
//    public void setUp() {
//        server = new WireMockServer(wireMockConfig().dynamicPort());
//        server.stubFor(get(urlPathMatching(API_LINK))
//            .willReturn(aResponse()
//                .withStatus(200)
//                .withHeader("Content-Type", "application/json")
//                .withBody("""
//                    {"items":[{"tags":["java","spring","dependency-injection"],"owner":{"account_id":2381622,"reputation":21432,"user_id":2083523,"user_type":"registered","accept_rate":68,"profile_image":"https://www.gravatar.com/avatar/2ce2d086884e247c69544a7bec5b79a2?s=256&d=identicon&r=PG&f=y&so-version=2","display_name":"Avi","link":"https://stackoverflow.com/users/2083523/avi"},"is_answered":true,"view_count":25154,"accepted_answer_id":53579193,"answer_count":1,"score":28,"last_activity_date":1543744914,"creation_date":1543744254,"question_id":53579112,"content_license":"CC BY-SA 4.0","link":"https://stackoverflow.com/questions/53579112/inject-list-of-all-beans-with-a-certain-interface","title":"Inject list of all beans with a certain interface"}],"has_more":false,"quota_max":300,"quota_remaining":251}""")));
//        server.stubFor(get(urlPathMatching("/questions/1000000000000000*"))
//            .willReturn(aResponse()
//                .withStatus(404)));
//        server.start();
//    }
//
//    @Test
//    @DisplayName("Existing StackOverflow question link test")
//    @SneakyThrows
//    public void fetchData_shouldReturnCorrectData_whenQuestionExists() {
//        //Arrange
//        ClientInfoProvider client = new StackOverflowInfoProvider(server.baseUrl());
//        //Act
//        LinkInfo info = client.fetchData(URI.create(LINK));
//        //Assert
//        assertThat(info).extracting(LinkInfo::url, LinkInfo::title)
//            .contains(URI.create(LINK), "Inject list of all beans with a certain interface");
//    }
//
//    @Test
//    @DisplayName("Nonexistent StackOverflow question link test")
//    @SneakyThrows
//    public void fetchData_shouldThrowLinkNotSupportedException_whenQuestionDoesNotExist() {
//        //Arrange
//        ClientInfoProvider client = new StackOverflowInfoProvider(server.baseUrl());
//        URI url = URI.create("https://stackoverflow.com/questions/10000000000000000/aboba");
//        //Expected
//        assertThatThrownBy(()->client.fetchData(url)).isInstanceOf(LinkNotSupportedException.class);
//    }
//
//    @Test
//    @DisplayName("Not StackOverflow link test")
//    @SneakyThrows
//    public void fetchData_shouldReturnNull_whenLinkDoesNotSupport() {
//        //Arrange
//        ClientInfoProvider client = new StackOverflowInfoProvider(server.baseUrl());
//        //Act
//        LinkInfo info = client.fetchData(URI.create(NOT_STACKOVERFLOW_LINK));
//        //Assert
//        assertThat(info).isNull();
//    }
//
//    @Test
//    @DisplayName("StackOverflow question link test")
//    @SneakyThrows
//    public void isValidate_shouldReturnTrue_whenLinkIsValidated() {
//        //Arrange
//        ClientInfoProvider client = new StackOverflowInfoProvider(server.baseUrl());
//        //Act
//        boolean response = client.isValidated(URI.create(LINK));
//        //Assert
//        assertThat(response).isTrue();
//    }
//
//    @Test
//    @DisplayName("Not StackOverflow question link test")
//    @SneakyThrows
//    public void isValidate_shouldReturnFalse_whenLinkIsNotValidated() {
//        //Arrange
//        ClientInfoProvider client = new StackOverflowInfoProvider(server.baseUrl());
//        //Act
//        boolean response = client.isValidated(URI.create(NOT_STACKOVERFLOW_LINK));
//        //Assert
//        assertThat(response).isFalse();
//    }
//
//    @AfterEach
//    public void shutdown() {
//        server.stop();
//    }
//}
//
