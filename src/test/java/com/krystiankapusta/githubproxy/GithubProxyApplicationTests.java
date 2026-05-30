package com.krystiankapusta.githubproxy;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class GithubProxyApplicationTests {

    private static WireMockServer wireMockServer;

    @Autowired
    private RestTestClient restTestClient;

    @DynamicPropertySource
    static void configureWireMock(DynamicPropertyRegistry registry) {
        registry.add("github.base-url", () -> "http://localhost:" + wireMockServer.port());
    }

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(0);
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    static void stopWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    void shouldReturnOnlyNotForkRepositoriesWithBranches(){

        WireMock.stubFor(WireMock.get(WireMock.urlPathMatching("/users/octocat/repos"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    [
                    {"name":"hello-world","owner":{"login":"octocat"},"fork":false},
                    {"name":"fork-repo","owner":{"login":"octocat"},"fork":true}
                    ]
                """)
            )
        );

        WireMock.stubFor(WireMock.get(WireMock.urlPathMatching("/repos/octocat/hello-world/branches"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    [
                    {"name":"main","commit":{"sha":"a1b2c3d4e5f6"}},
                    {"name":"feature/test","commit":{"sha":"111222333"}}
                    ]
                """)
            )
        );
        
        restTestClient.get()
            .uri("/users/octocat/repos")
            .exchange()
            .expectStatus().isOk()
            .expectBody(List.class)
            .value(body -> {
                assertThat(body).hasSize(1);
            });
    }

    @Test
    void shouldReturnCodeNotFoundWhenUserDoesNotExist(){
        WireMock.stubFor(WireMock.get(WireMock.urlPathMatching("/users/non-existing-user/repos"))
            .willReturn(WireMock.aResponse().withStatus(404)));
        
        restTestClient.get()
            .uri("/users/non-existing-user/repos")
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorResponse.class)
            .value(error -> {
                assertThat(error.status()).isEqualTo(404);
                assertThat(error.message()).isEqualTo("User not found");
            });
    }
}