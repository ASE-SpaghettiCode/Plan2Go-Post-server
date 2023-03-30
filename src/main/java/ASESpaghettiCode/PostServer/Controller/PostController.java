package ASESpaghettiCode.PostServer.Controller;


import ASESpaghettiCode.PostServer.Model.Post;
import ASESpaghettiCode.PostServer.Repository.PostRepository;
import ASESpaghettiCode.PostServer.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    @ResponseStatus(HttpStatus.OK)
    public List<Post> findAllPost(){
            return postService.findAllPost();
    }

    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Post createNote(@RequestBody Post newPost) {
        return postService.createPost(newPost);
    }

    @GetMapping("/posts/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public Post getPostById(@PathVariable String postId) {
        return postService.getPostById(postId);
    }

    @GetMapping("/users/{userId}/posts")
    @ResponseStatus(HttpStatus.OK)
    public  List<Post> getPostByUserId(@PathVariable String userId) {
        return postService.findPostByUserId(userId);
    }


}
