package ASESpaghettiCode.PostServer.Service;

import ASESpaghettiCode.PostServer.Model.Comment;
import ASESpaghettiCode.PostServer.Model.CommentPostDTO;
import ASESpaghettiCode.PostServer.Model.Post;
import ASESpaghettiCode.PostServer.Repository.CommentRepository;
import ASESpaghettiCode.PostServer.Repository.PostRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentServiceTest {
    private final CommentRepository commentRepository = mock(CommentRepository.class);
    private final PostRepository postRepository = mock(PostRepository.class);

    private final CommentService commentService = new CommentService(commentRepository, postRepository);
    Post post = new Post("authorId", "content");
    Comment comment = new Comment("commentAuthorId", "commentAuthorName", "commentAuthorName", "targetPostId", "commentText");

    @Test
    void findCommentsByPostIdTest_Success() {
        post.setComments(List.of(comment.getCommentText()));
        when(postRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(post));
        when(commentRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(comment));

        assertEquals(List.of(comment), commentService.findCommentsByPostId("1"));
    }

    @Test
    void findCommentsByPostIdTest_Fail() {
        when(postRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> commentService.findCommentsByPostId("1"));
    }

    @Test
    void deleteCommentTest_Success() {
        List<String> initialComments = new ArrayList<>();
        post.setComments(initialComments);
        post.addComments(comment.getCommentId());
        when(commentRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(comment));
        when(postRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(post));

        commentService.deleteComment("1", "commentAuthorId");

        verify(postRepository, times(1)).save(any(Post.class));
        verify(commentRepository, times(1)).delete(any(Comment.class));
    }

    @Test
    void deleteCommentTest_NoComment() {
        when(commentRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> commentService.deleteComment("1", "1"));
    }

    @Test
    void deleteCommentTest_Unauthorized() {
        when(commentRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(comment));

        assertThrows(RuntimeException.class, () -> commentService.deleteComment("1", "1"));
    }

    @Test
    void deleteCommentTest_NoPost() {
        when(commentRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(comment));
        when(postRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> commentService.deleteComment("1", "commentAuthorId"));
    }

    @Test
    void updateCommentTest_Success() {
        CommentPostDTO commentPostDTO = new CommentPostDTO();
        when(commentRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(comment));

        commentService.updateComment("commentAuthorId", "1", commentPostDTO);

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void updateCommentTest_NoComment() {
        when(commentRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> commentService.updateComment("1", "1", new CommentPostDTO()));
    }

    @Test
    void updateCommentTest_Unauthorized() {
        when(commentRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(comment));

        assertThrows(RuntimeException.class, () -> commentService.updateComment("1", "1", new CommentPostDTO()));
    }
}
