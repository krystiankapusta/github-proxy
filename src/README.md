# Github Proxy 
Simple Spring Boot proxy for GitHub API that returns only non-fork repositories with their branches and last commit SHA.

## Tech Stack
* Java 25
* Spring Boot 4.0.6
* Build Gradle Kotlin DSL
* WireMock Standalone 3.12.0

## Features
* Returns list of non-fork repositories for a given GitHub user
* For each repository returns all branches with last commit SHA
* Returns proper 404 response when user does not exist

## Getting Started

### Prerequisites
JDK 25 installed and configured in your environment.

## Build and Run the Application
To build the project and launch the server locally, run the following command in your terminal
```bash
./gradlew bootRun
```
The server will start by default on http://localhost:8080.

## Running Tests
Project contains only integration tests using WireMoc. Run the tests:
```bash
./gradlew test
```

## API Documentation

1. Fetch User Repositories 
```HTTP
GET /users/{username}/repos
```
**Example curl:**
```bash
curl -X GET http://localhost:8080/users/octocat/repos
```

Example of response: 
```json
[
    {
        "repositoryName": "git-consortium",
        "ownerLogin": "octocat",
        "branches": [
            {
                "name": "master",
                "lastCommitSha": "b33a9c7c02ad93f621fa38f0e9fc9e867e12fa0e"
            }
        ]
    }
]
```

2. User Not Found
If the requested GitHub user does not exist, the API returns a structured error payload.
```HTTP
GET /users/non-existing-user/repos
```
**Example curl:**
```bash
curl -X GET http://localhost:8080/users/non-existing-user-xyz123/repos
```

Example of response:  
```json
{
    "status": 404,
    "message": "User not found"
}
```


