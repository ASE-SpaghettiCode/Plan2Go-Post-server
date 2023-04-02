package ASESpaghettiCode.PostServer;

import ASESpaghettiCode.PostServer.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class PostServerApplication {

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    @Autowired
    private PostRepository postRepository;

    public static void main(String[] args) {

        SpringApplication.run(PostServerApplication.class, args);
    }

}
