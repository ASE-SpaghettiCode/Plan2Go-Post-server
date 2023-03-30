package ASESpaghettiCode.PostServer;

import ASESpaghettiCode.PostServer.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PostServerApplication {

    @Autowired
    private PostRepository postRepository;

    public static void main(String[] args) {

        SpringApplication.run(PostServerApplication.class, args);
    }

}
