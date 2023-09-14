package com.api;

import com.api.GithubAPI.GithubAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GithubApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(GithubApiApplication.class, args);
	}
}
