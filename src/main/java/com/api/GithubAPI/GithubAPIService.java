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
    String contentBase64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII=";


    /**
     * 파일 업로드 API
     */
    public void githubFileUpload() {

        RestTemplate restTemplate = new RestTemplate();
        // Github 레포지터리 업로드 경로 (경로/파일명)
        String uploadPath = "files/NewFile.jpg";
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
        String uploadPath = "files/NewFile.jpg";

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
        String uploadPath = "files/NewFile.jpg";

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
