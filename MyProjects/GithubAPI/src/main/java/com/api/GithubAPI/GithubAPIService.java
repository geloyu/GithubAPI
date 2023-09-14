package com.api.GithubAPI;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
public class GithubAPIService {
    // Github 소유자 토큰 (개인 토큰 입력 필요)
    String gitToken = "";

    // Github 레포지터리 소유자명
    String repositoryOwner = "geloyu";

    // Github 레포지터리 명
    String repositoryName = "GithubAPI";

    // Github 커밋자 ID
    String committerId = "geloyu";

    // Github 커밋자 Email
    String committerEmail = "bbirds94@gmail.com";

    // Github 커밋메시지
    String commitMessage = "Github Upload Test Commit";

    // 업로드 샘플 파일 (Base64)
    String contentBase64 = "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";


    /**
     * 파일 업로드 API
     */
    public void githubFileUpload() {

        RestTemplate restTemplate = new RestTemplate();
        // Github 레포지터리 업로드 경로 (경로/파일명)
        String uploadPath = "testFolder/NewFile.jpg";
        // Github API url
        String apiUrl = "https://api.github.com/repos/" + repositoryOwner + "/" + repositoryName + "/contents/" + uploadPath;

        // 요청 Header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + gitToken);
        headers.set("Accept", "application/vnd.github.v3+json");
        headers.set("X-GitHub-Api-Version", "2022-11-28");

        // 요청 Body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("message", commitMessage);
        Map<String, Object> committerInfo = new HashMap<>();
        committerInfo.put("name", committerId);
        committerInfo.put("email", committerEmail);
        requestBody.put("committer", committerInfo);
        requestBody.put("content", contentBase64);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.PUT, requestEntity, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK || responseEntity.getStatusCode() == HttpStatus.CREATED) {
            System.out.println("FILE UPLOAD SUCCESS");
        } else {
            System.err.println("FILE UPLOAD FAIL. Status code: " + responseEntity.getStatusCode());
            System.err.println("FILE UPLOAD FAIL. Status code VALUE: " + responseEntity.getStatusCodeValue());
        }
    }

    /**
     * 파일 Sha 조회 API
     *
     * @return Sha
     */
    public String githubFileShaInquiry(String filePath) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();

        // Github API url
        String apiUrl = "https://api.github.com/repos/" + repositoryOwner + "/" + repositoryName + "/contents/" + filePath;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + gitToken);
        headers.set("Accept", "application/vnd.github.v3+json");
        headers.set("X-GitHub-Api-Version", "2022-11-28");

        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, new URI(apiUrl));
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {});

        if (!(responseEntity.getStatusCode() == HttpStatus.OK)) {
            return responseEntity.getStatusCode().toString();
        }

        return responseEntity.getBody().get("sha").toString();
    }

    /**
     * 파일 업데이트 API
     */
    public void githubFileUpDate() throws URISyntaxException {

        RestTemplate restTemplate = new RestTemplate();

        // Github 레포지터리 파일 업로드 경로 (경로/파일명)
        String uploadPath = "testFolder/NewFile";

        // Github API url
        String apiUrl = "https://api.github.com/repos/" + repositoryOwner + "/" + repositoryName + "/contents/" + uploadPath;

        // Github 기존 파일 sha 조회
        String sha = this.githubFileShaInquiry(uploadPath);

        // 요청 Header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + gitToken);
        headers.set("Accept", "application/vnd.github.v3+json");
        headers.set("X-GitHub-Api-Version", "2022-11-28");

        // 요청 Body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("message", commitMessage);
        Map<String, Object> committerInfo = new HashMap<>();
        committerInfo.put("name", committerId);
        committerInfo.put("email", committerEmail);
        requestBody.put("committer", committerInfo);
        requestBody.put("content", contentBase64);
        requestBody.put("sha", sha);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.PUT, requestEntity, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            System.out.println("FILE UPDATE SUCCESS");
        } else {
            System.err.println("FILE UPDATE FAIL. Status code: " + responseEntity.getStatusCodeValue());
            System.err.println("FILE UPDATE FAIL. Status code VALUE: " + responseEntity.getStatusCodeValue());
        }
    }

    /**
     * 파일 삭제 API
     */
    public void githubFileDelete() throws URISyntaxException {

        RestTemplate restTemplate = new RestTemplate();

        // Github 레포지터리 파일 업로드 경로 (경로/파일명)
        String uploadPath = "testFolder/NewFile5";

        // Github API url
        String apiUrl = "https://api.github.com/repos/" + repositoryOwner + "/" + repositoryName + "/contents/" + uploadPath;

        // Github 기존 파일 sha 조회
        String sha = this.githubFileShaInquiry(uploadPath);

        // 요청 Header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + gitToken);
        headers.set("Accept", "application/vnd.github.v3+json");
        headers.set("X-GitHub-Api-Version", "2022-11-28");

        // 요청 Body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("message", commitMessage);
        Map<String, Object> committerInfo = new HashMap<>();
        committerInfo.put("name", committerId);
        committerInfo.put("email", committerEmail);
        requestBody.put("committer", committerInfo);
        requestBody.put("content", contentBase64);
        requestBody.put("sha", sha);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.DELETE, requestEntity, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            System.out.println("FILE DELETE SUCCESS");
        } else {
            System.err.println("FILE DELETE FAIL. Status code: " + responseEntity.getStatusCodeValue());
            System.err.println("FILE DELETE FAIL. Status code VALUE: " + responseEntity.getStatusCodeValue());
        }
    }
}
