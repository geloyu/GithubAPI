package com.api.JgitLibrary;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;

@Service
public class JgitService {
    // GitHub 계정명
    String gitUserId = "geloyu";
    // GitHub 계정토큰
    String gitUserToken = "";
    // GitHub Repository Url
    String gitRepositoryUrl = "https://github.com/geloyu/GithubAPI.git";
    // GitHub 커밋자 ID
    String committerId = "geloyu";
    // GitHub 커밋자 Email
    String committerEmail = "bbirds94@gmail.com";
    // GitHub 커밋메시지
    String commitMessage = "FILE UPLOAD WITH JGIT";

    // 업로드 샘플 파일 (Base64)
    String contentBase64 = "iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAApgAAAKYB3X3/OAAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAANCSURBVEiJtZZPbBtFFMZ/M7ubXdtdb1xSFyeilBapySVU8h8OoFaooFSqiihIVIpQBKci6KEg9Q6H9kovIHoCIVQJJCKE1ENFjnAgcaSGC6rEnxBwA04Tx43t2FnvDAfjkNibxgHxnWb2e/u992bee7tCa00YFsffekFY+nUzFtjW0LrvjRXrCDIAaPLlW0nHL0SsZtVoaF98mLrx3pdhOqLtYPHChahZcYYO7KvPFxvRl5XPp1sN3adWiD1ZAqD6XYK1b/dvE5IWryTt2udLFedwc1+9kLp+vbbpoDh+6TklxBeAi9TL0taeWpdmZzQDry0AcO+jQ12RyohqqoYoo8RDwJrU+qXkjWtfi8Xxt58BdQuwQs9qC/afLwCw8tnQbqYAPsgxE1S6F3EAIXux2oQFKm0ihMsOF71dHYx+f3NND68ghCu1YIoePPQN1pGRABkJ6Bus96CutRZMydTl+TvuiRW1m3n0eDl0vRPcEysqdXn+jsQPsrHMquGeXEaY4Yk4wxWcY5V/9scqOMOVUFthatyTy8QyqwZ+kDURKoMWxNKr2EeqVKcTNOajqKoBgOE28U4tdQl5p5bwCw7BWquaZSzAPlwjlithJtp3pTImSqQRrb2Z8PHGigD4RZuNX6JYj6wj7O4TFLbCO/Mn/m8R+h6rYSUb3ekokRY6f/YukArN979jcW+V/S8g0eT/N3VN3kTqWbQ428m9/8k0P/1aIhF36PccEl6EhOcAUCrXKZXXWS3XKd2vc/TRBG9O5ELC17MmWubD2nKhUKZa26Ba2+D3P+4/MNCFwg59oWVeYhkzgN/JDR8deKBoD7Y+ljEjGZ0sosXVTvbc6RHirr2reNy1OXd6pJsQ+gqjk8VWFYmHrwBzW/n+uMPFiRwHB2I7ih8ciHFxIkd/3Omk5tCDV1t+2nNu5sxxpDFNx+huNhVT3/zMDz8usXC3ddaHBj1GHj/As08fwTS7Kt1HBTmyN29vdwAw+/wbwLVOJ3uAD1wi/dUH7Qei66PfyuRj4Ik9is+hglfbkbfR3cnZm7chlUWLdwmprtCohX4HUtlOcQjLYCu+fzGJH2QRKvP3UNz8bWk1qMxjGTOMThZ3kvgLI5AzFfo379UAAAAASUVORK5CYII=";

    // 계정 정보
    CredentialsProvider credentials = new UsernamePasswordCredentialsProvider(gitUserId, gitUserToken);

    public void gitUploadProcess() throws Exception {

        File gitDir = new File("./Repository");

        if(gitDir.exists()) {
            FileUtils.deleteDirectory(gitDir);
        }

        gitDir.mkdirs();

        // Git Clone
        Git git = this.gitClone(gitDir);

        String fileDir = "./Repository/files/";
        String fileName = "SampleFile.jpg";

        byte[] decodedFile = Base64.getDecoder().decode(contentBase64);
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileDir + fileName));
        outputStream.write(decodedFile);
        outputStream.close();

        // Git Add
        this.gitAdd(git);

        // Git Commit
        this.gitCommit(git);

        // Git Push
        this.gitPush(git);

        git.close();

        FileUtils.deleteDirectory(gitDir);
    }

    public Git gitClone(File gitDir) throws Exception {
        Git git = Git.cloneRepository()
                .setURI(gitRepositoryUrl)
                .setCredentialsProvider(credentials)
                .setDirectory(gitDir)
                .call();

        return git;
    }

    public void gitAdd(Git git) throws Exception {
        git.add().addFilepattern(".").call();
    }

    public void gitCommit(Git git) throws  Exception {
        git.commit()
                .setAuthor(committerId, committerEmail)
                .setMessage(commitMessage)
                .call();
    }

    public void gitPush(Git git) throws Exception {
        git.push()
                .setCredentialsProvider(credentials)
                .setForce(true)
                .call();
    }
}
