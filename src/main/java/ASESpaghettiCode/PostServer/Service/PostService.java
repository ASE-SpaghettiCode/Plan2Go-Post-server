package ASESpaghettiCode.PostServer.Service;

import ASESpaghettiCode.PostServer.Model.Post;
import ASESpaghettiCode.PostServer.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAllPost() {
        return this.postRepository.findAll();
    }

    public Post createPost(Post newPost) {
        return postRepository.save(newPost);
    }

    public Post getPostById(String postId) {
        Optional<Post> targetPost = postRepository.findById(postId);
        if (targetPost.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post is not found!");
        }
        return targetPost.get();
    }

    public List<Post> findPostByUserId(String userId) {
        List<Post> listOfPost = new ArrayList<>();
        for (Post post : postRepository.findAll()) {
            if (Objects.equals(post.getAuthorId(), userId)) {
                listOfPost.add(post);
            }
        }
        return listOfPost;
    }
}
