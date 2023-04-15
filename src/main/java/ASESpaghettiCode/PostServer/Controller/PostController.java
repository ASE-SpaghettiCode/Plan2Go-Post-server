package ASESpaghettiCode.PostServer.Controller;


import ASESpaghettiCode.PostServer.Model.Post;
import ASESpaghettiCode.PostServer.Model.User;
import ASESpaghettiCode.PostServer.Model.PostDTO;
import ASESpaghettiCode.PostServer.Model.PostLikes;
import ASESpaghettiCode.PostServer.Repository.PostRepository;
import ASESpaghettiCode.PostServer.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Value("${UserServerLocation}")
    private String UserServerLocation;

    @Autowired
    private RestTemplate restTemplate = new RestTemplateBuilder()
            .errorHandler(new RestTemplateErrorHandler())
            .build();


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

    @DeleteMapping("/users/{userId}/posts/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable String postId, @PathVariable String userId) {
        postService.deletePost(postId, userId);
    }

    @PutMapping("users/{userId}/editing/posts/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateNote(@PathVariable String postId, @PathVariable String userId, @RequestBody Post post) {
        postService.updatePost(postId, userId, post);
    }

    @PostMapping("users/{userId}/likes/posts/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostLikes userLikesNote(@PathVariable String userId, @PathVariable String postId) {
        return postService.userLikesPost(userId, postId);
    }

    @DeleteMapping("users/{userId}/likes/posts/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostLikes userUnlikesNote(@PathVariable String userId, @PathVariable String postId) {
        return postService.userUnlikesPost(userId, postId);
    }

    @GetMapping("users/{userId}/whetherLikes/posts/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostLikes whetherUserLikesPost(@PathVariable String userId, @PathVariable String postId) {
        return postService.whetherUserLikesPost(userId,postId);
    }

    @GetMapping("/posts/following/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<PostDTO> findFollowingNotes(@PathVariable String userId) {
        // get all the authorId that a user is following
        List<User> followingUser = restTemplate.getForObject(UserServerLocation + "/users/" + userId + "/followings", List.class);
        // retrive all authorId(followingUserId) from users
        List<String> followingUserId = new ArrayList<>();
        for (User user: followingUser){
            String authorId = user.getUserId();
            if (!followingUserId.contains(authorId)){
                followingUserId.add(authorId);
            }
        }
        // find all posts with the followingUserId
        List<Post> postList = postService.findPostOfFollowees(followingUserId);
        return postService.addUsernameImagePathtothePostlist(postList);
    }

}
