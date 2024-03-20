package edu.java.repos.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubData(
    @JsonProperty int numberOfBranches,
    @JsonProperty int numberOfPullRequests,
    @JsonProperty int branchesHash,
    @JsonProperty int pullRequestsHash
) {
}
