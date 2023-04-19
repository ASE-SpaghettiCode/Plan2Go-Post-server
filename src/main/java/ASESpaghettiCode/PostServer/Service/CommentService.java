package ASESpaghettiCode.PostServer.Service;

import ASESpaghettiCode.PostServer.Controller.RestTemplateErrorHandler;
import ASESpaghettiCode.PostServer.Model.Comment;
import ASESpaghettiCode.PostServer.Model.CommentPostDTO;
import ASESpaghettiCode.PostServer.Model.Post;
import ASESpaghettiCode.PostServer.Model.User;
import ASESpaghettiCode.PostServer.Repository.CommentRepository;
import ASESpaghettiCode.PostServer.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@Transactional
public class CommentService {
    @Value("${UserServerLocation}")
    private String UserServerLocation;

    @Autowired
    private RestTemplate restTemplate = new RestTemplateBuilder()
            .errorHandler(new RestTemplateErrorHandler())
            .build();
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentService(@Qualifier("commentRepository")CommentRepository commentRepository, @Qualifier("postRepository")PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public List<Comment> findCommentsByPostId(String postId) {
        Optional<Post> targetPost = postRepository.findById(postId);
        if (targetPost.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post is not found!");
        }
        return targetPost.get().getComments();
    }

    public Comment createComment(String targetPostId, CommentPostDTO commentPostDTO) {
        String authorId = commentPostDTO.getCommentAuthorId();
        User user = restTemplate.getForObject(UserServerLocation + "/users/" + authorId, User.class);
        Comment newComment = new Comment(authorId,user.getUsername(), user.getImageLink(),targetPostId, commentPostDTO.getCommentText());
        Optional<Post> targetNote =  postRepository.findById(targetPostId);
        if (targetNote.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The post is not found!");
        }
        commentRepository.save(newComment);
        targetNote.get().addComments(newComment);
        postRepository.save(targetNote.get());
        return commentRepository.save(newComment);
    }

    public void deleteComment(String commentId, String userId) {
        Optional<Comment> targetComment = commentRepository.findById(commentId);
        if (targetComment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment is not found!");
        } else if (!Objects.equals(userId, targetComment.get().getCommentAuthorId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "unauthorized delete!");
        } else {
            Optional<Post> targetPost = postRepository.findById(targetComment.get().getTargetPostId());
            if (targetPost.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note is not found!");
            } else {
                targetPost.get().deleteComments(targetComment.get());
                postRepository.save(targetPost.get());
            }
            commentRepository.delete(targetComment.get());
        }
    }

    public void updateComment(String userId, String commentId, CommentPostDTO commentPostDTO) {
        Optional<Comment> targetComment = commentRepository.findById(commentId);
        if (targetComment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment is not found!");
        } else if (!Objects.equals(userId, targetComment.get().getCommentAuthorId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized edit!");
        }
        else {
            targetComment.get().setCommentText(commentPostDTO.getCommentText());
            commentRepository.save(targetComment.get());
        }
    }
}
