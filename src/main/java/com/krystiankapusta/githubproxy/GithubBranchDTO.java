package com.krystiankapusta.githubproxy;

public record GithubBranchDTO(
    String name,
    CommitDTO commit
) {
    
}
