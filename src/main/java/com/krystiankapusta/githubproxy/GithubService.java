package com.krystiankapusta.githubproxy;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
class GithubService {

    private final GithubClient githubClient;

    GithubService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    List<Repository> getUserRepositories(String username) {

        List<GithubRepositoryDTO> githubRepositoryDTOs = githubClient.getRepositories(username);

        return githubRepositoryDTOs
            .stream()
            .filter(repo -> !repo.fork())
            .map(this::mapToRepository)
            .toList();
    }

    private Repository mapToRepository(GithubRepositoryDTO githubRepositoryDTO) {
        List<GithubBranchDTO> githubBranches = githubClient.getBranches(
            githubRepositoryDTO.owner().login(),
            githubRepositoryDTO.name()
        );

        List<Branch> branches = githubBranches
            .stream()
            .map(this::mapToBranch)
            .toList();
        
        return new Repository(
            githubRepositoryDTO.name(),
            githubRepositoryDTO.owner().login(),
            branches
        );
    }

    private Branch mapToBranch(GithubBranchDTO githubBranchDTO){

        String lastCommitSha = (githubBranchDTO.commit() != null) ? githubBranchDTO.commit().sha() : "";

        return new Branch(
            githubBranchDTO.name(),
            lastCommitSha
        );
    }
    
}
