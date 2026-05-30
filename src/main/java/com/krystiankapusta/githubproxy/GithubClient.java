package com.krystiankapusta.githubproxy;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
class GithubClient {
    private final RestClient restClient;

    GithubClient(RestClient.Builder builder, @Value("${github.base-url:https://api.github.com}") String baseUrl) {
        this.restClient = builder
                .baseUrl(baseUrl)
                .defaultHeader("Accept", "application/vnd.github.v3+json")
                .build();
    }

    List<GithubRepositoryDTO> getRepositories(String username) {
        
        return restClient.get().uri("/users/{username}/repos", username).retrieve().body(new ParameterizedTypeReference<List<GithubRepositoryDTO>>() {});
        
    }

    List<GithubBranchDTO> getBranches(String owner, String repo){

        return restClient.get().uri("/repos/{owner}/{repo}/branches", owner, repo).retrieve().body(new ParameterizedTypeReference<List<GithubBranchDTO>>() {});
    }

}
