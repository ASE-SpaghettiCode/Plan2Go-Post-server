package ASESpaghettiCode.PostServer.Service;

import ASESpaghettiCode.PostServer.Model.Post;
import ASESpaghettiCode.PostServer.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
