package com.api.GithubAPI;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
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
    String commitMessage = "[파일 업로드]";

    // 업로드 샘플 파일 (Base64)
    String contentBase64 = "iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAApgAAAKYB3X3/OAAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAANCSURBVEiJtZZPbBtFFMZ/M7ubXdtdb1xSFyeilBapySVU8h8OoFaooFSqiihIVIpQBKci6KEg9Q6H9kovIHoCIVQJJCKE1ENFjnAgcaSGC6rEnxBwA04Tx43t2FnvDAfjkNibxgHxnWb2e/u992bee7tCa00YFsffekFY+nUzFtjW0LrvjRXrCDIAaPLlW0nHL0SsZtVoaF98mLrx3pdhOqLtYPHChahZcYYO7KvPFxvRl5XPp1sN3adWiD1ZAqD6XYK1b/dvE5IWryTt2udLFedwc1+9kLp+vbbpoDh+6TklxBeAi9TL0taeWpdmZzQDry0AcO+jQ12RyohqqoYoo8RDwJrU+qXkjWtfi8Xxt58BdQuwQs9qC/afLwCw8tnQbqYAPsgxE1S6F3EAIXux2oQFKm0ihMsOF71dHYx+f3NND68ghCu1YIoePPQN1pGRABkJ6Bus96CutRZMydTl+TvuiRW1m3n0eDl0vRPcEysqdXn+jsQPsrHMquGeXEaY4Yk4wxWcY5V/9scqOMOVUFthatyTy8QyqwZ+kDURKoMWxNKr2EeqVKcTNOajqKoBgOE28U4tdQl5p5bwCw7BWquaZSzAPlwjlithJtp3pTImSqQRrb2Z8PHGigD4RZuNX6JYj6wj7O4TFLbCO/Mn/m8R+h6rYSUb3ekokRY6f/YukArN979jcW+V/S8g0eT/N3VN3kTqWbQ428m9/8k0P/1aIhF36PccEl6EhOcAUCrXKZXXWS3XKd2vc/TRBG9O5ELC17MmWubD2nKhUKZa26Ba2+D3P+4/MNCFwg59oWVeYhkzgN/JDR8deKBoD7Y+ljEjGZ0sosXVTvbc6RHirr2reNy1OXd6pJsQ+gqjk8VWFYmHrwBzW/n+uMPFiRwHB2I7ih8ciHFxIkd/3Omk5tCDV1t+2nNu5sxxpDFNx+huNhVT3/zMDz8usXC3ddaHBj1GHj/As08fwTS7Kt1HBTmyN29vdwAw+/wbwLVOJ3uAD1wi/dUH7Qei66PfyuRj4Ik9is+hglfbkbfR3cnZm7chlUWLdwmprtCohX4HUtlOcQjLYCu+fzGJH2QRKvP3UNz8bWk1qMxjGTOMThZ3kvgLI5AzFfo379UAAAAASUVORK5CYII=";


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

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
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

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("FILE DELETE SUCCESS");
        } else {
            System.err.println("FILE DELETE FAIL. Status code: " + responseEntity.getStatusCodeValue());
            System.err.println("FILE DELETE FAIL. Status code VALUE: " + responseEntity.getStatusCodeValue());
        }
    }

    /**
     * 파일 목록 조회 API
     *
     * @return Sha
     */
    public void githubFilesInquiry() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();

        // Github API url
        String apiUrl = "https://api.github.com/repos/" + repositoryOwner + "/" + repositoryName + "/contents/files";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + gitToken);
        headers.set("Accept", "application/vnd.github.v3+json");
        headers.set("X-GitHub-Api-Version", "2022-11-28");

        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, new URI(apiUrl));
        ResponseEntity<List<Map<String, Object>>> responseEntity = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<List<Map<String, Object>>>() {});

        if (!(responseEntity.getStatusCode() == HttpStatus.OK)) {
            System.out.println("FILES INQUIRY FAILED");
        }

        System.out.println(responseEntity.getBody());
    }
}
