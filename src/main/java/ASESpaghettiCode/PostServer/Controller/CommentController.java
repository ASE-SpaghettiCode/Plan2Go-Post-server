package ASESpaghettiCode.PostServer.Controller;

import ASESpaghettiCode.PostServer.Model.Comment;
import ASESpaghettiCode.PostServer.Model.CommentPostDTO;
import ASESpaghettiCode.PostServer.Service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class CommentController {
    private final CommentService commentService;

    CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @GetMapping("/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<Comment> getCommentsByNoteId(@PathVariable String postId){
        return commentService.findCommentsByPostId(postId);
    }

    @PostMapping("/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createCommentForAGivenNote(@PathVariable String postId, @RequestBody CommentPostDTO commentPostDTO){
        return commentService.createComment(postId, commentPostDTO);
    }

    @DeleteMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable String userId, @PathVariable String commentId) {
        commentService.deleteComment(commentId, userId);
    }

    @PutMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateComment(@PathVariable String userId, @PathVariable String commentId, @RequestBody CommentPostDTO commentPostDTO) {
        commentService.updateComment(userId, commentId, commentPostDTO);
    }

}
