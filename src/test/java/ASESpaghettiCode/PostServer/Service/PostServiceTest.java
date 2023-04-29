package ASESpaghettiCode.PostServer.Service;

import ASESpaghettiCode.PostServer.Controller.RestTemplateErrorHandler;
import ASESpaghettiCode.PostServer.Model.Post;
import ASESpaghettiCode.PostServer.Model.PostLikes;
import ASESpaghettiCode.PostServer.Repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostServiceTest {
    private final PostRepository postRepository = mock(PostRepository.class);
    private final RestTemplate restTemplate = mock(RestTemplate.class);
    private final RestTemplateBuilder restTemplateBuilder = mock(RestTemplateBuilder.class);
    private final RestTemplateErrorHandler restTemplateErrorHandler = mock(RestTemplateErrorHandler.class);

    PostService postService = new PostService(postRepository);

    Post post = new Post("authorId","content");

    @Test
    void findAllPostTest() {
        when(postRepository.findAll()).thenReturn(List.of(post));

        assertEquals(List.of(post), postService.findAllPost());
    }

    @Test
    void createPostTest() {
        when(postRepository.save(any(Post.class))).thenReturn(post);

        assertEquals(post, postService.createPost(post));
    }

    @Test
    void getPostByIdTest_Success() {
        when(postRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(post));

        assertEquals(post, postService.getPostById("1"));
    }

    @Test
    void getPostByIdTest_Fail() {
        when(postRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> postService.getPostById("1"));
    }

    @Test
    void findPostByUserIdTest_HasPost() {
        when(postRepository.findByUserIdInOrderByCreatedDateAsc(any(String.class),any())).thenReturn(List.of(post));

        assertEquals(List.of(post), postService.findPostByUserId("1"));
    }

    @Test
    void findPostByUserIdTest_NoPost() {
        when(postRepository.findByUserIdInOrderByCreatedDateAsc(any(String.class),any())).thenReturn(null);

        assertEquals(new ArrayList<>(), postService.findPostByUserId("1"));
    }

    @Test
    void deletePostTest_Success() {
        when(postRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(post));

        postService.deletePost("1","authorId");

        verify(postRepository,times(1)).delete(any(Post.class));
    }

    @Test
    void deletePostTest_Unauthorized() {
        when(postRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(post));

        assertThrows(ResponseStatusException.class, () -> postService.deletePost("1","1"));
    }

    @Test
    void deletePostTest_NoPost() {
        when(postRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> postService.deletePost("1","1"));
    }

    @Test
    void updatePostTest_Success() {
        when(postRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(post));

        postService.updatePost("1","authorId",post);

        verify(postRepository,times(1)).save(any(Post.class));
    }

    @Test
    void updatePostTest_NoPost() {
        when(postRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> postService.updatePost("1","1",post));
    }

    @Test
    void updatePostTest_Unauthorized() {
        when(postRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(post));

        assertThrows(ResponseStatusException.class, () -> postService.updatePost("1","1",post));
    }

//    @Test
//    void userLikesPostTest_Success() {
//        when(postRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(post));
//        when(restTemplate.postForLocation(any(),any(Notification.class))).thenReturn(null);
//
//        assertEquals(new PostLikes(1,true),postService.userLikesPost("authorId","1"));
//        verify(postRepository,times(1)).save(any(Post.class));
//    }

    @Test
    void userLikesPostTest_Fail() {
        when(postRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> postService.userLikesPost("1","1"));
    }

    @Test
    void userUnlikesPostTest_Success() {
        List<String> likedUsers = new ArrayList<>();
        likedUsers.add("authorId");
        post.setLikedUsers(likedUsers);
        when(postRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(post));

        assertEquals(new PostLikes(0,false),postService.userUnlikesPost("authorId","1"));
        verify(postRepository,times(1)).save(any(Post.class));
    }

    @Test
    void userUnlikesPostTest_NoPost() {
        when(postRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> postService.userUnlikesPost("1","1"));
    }

    @Test
    void userUnlikesPostTest_Unauthorized() {
        when(postRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(post));

        assertThrows(ResponseStatusException.class, () -> postService.userUnlikesPost("1","1"));
    }

    @Test
    void whetherUserLikesPostTest() {
        when(postRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(post));

        assertEquals(new PostLikes(0,false), postService.whetherUserLikesPost("1","1"));
    }

    @Test
    void findPostOfFollowees_Success() {
        when(postRepository.findByUserIdListInOrderByCreatedDateAsc(any(),any())).thenReturn(List.of(post));

        assertEquals(List.of(post), postService.findPostOfFollowees(List.of("1")));
    }

    @Test
    void findPostOfFollowees_Fail() {
        when(postRepository.findByUserIdListInOrderByCreatedDateAsc(any(),any())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> postService.findPostOfFollowees(List.of("1")));
    }
}
