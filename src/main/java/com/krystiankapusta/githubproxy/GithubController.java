package com.krystiankapusta.githubproxy;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class GithubController {
    
    private final GithubService githubService;

    GithubController(GithubService githubService) {
        this.githubService = githubService;
    }


    @GetMapping("/users/{username}/repos")
    public List<Repository> getUserRepositories(@PathVariable String username) {
        
        return githubService.getUserRepositories(username);
    }
    

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(HttpClientErrorException.NotFound exception) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "User not found"
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    

}
