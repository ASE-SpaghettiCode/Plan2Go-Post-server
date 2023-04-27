package ASESpaghettiCode.PostServer.Controller;

import ASESpaghettiCode.PostServer.Model.Post;
import ASESpaghettiCode.PostServer.Model.PostDTO;
import ASESpaghettiCode.PostServer.Model.PostLikes;
import ASESpaghettiCode.PostServer.Model.User;
import ASESpaghettiCode.PostServer.Repository.PostRepository;
import ASESpaghettiCode.PostServer.Service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;

import static org.mockito.BDDMockito.given;

@WebMvcTest(PostController.class)
public class PostControllerTest {
    @MockBean
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @Autowired
    private MockMvc mockMvc;

    Post post = new Post("authorId","content");

    @Test
    void findAllPostTest() throws Exception {
        List<Post> posts = List.of(post);

        given(postService.findAllPost()).willReturn(posts);

        MockHttpServletRequestBuilder getRequest = get("/posts")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].authorId", is(post.getAuthorId())))
                .andExpect(jsonPath("$[0].content", is(post.getContent())));
    }

    @Test
    void createNoteTest() throws Exception {
        post.setCreatedTime(null);
        given(postService.createPost(any(Post.class))).willReturn(post);

        MockHttpServletRequestBuilder postRequest = post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(post));

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.authorId", is(post.getAuthorId())))
                .andExpect(jsonPath("$.content", is(post.getContent())));
    }

    @Test
    void getPostByIdTest() throws Exception {
        given(postService.getPostById(any(String.class))).willReturn(post);

        MockHttpServletRequestBuilder getRequest = get("/posts/1");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorId", is(post.getAuthorId())))
                .andExpect(jsonPath("$.content", is(post.getContent())));
    }

    @Test
    void getPostByUserIdTest() throws Exception {
        given(postService.findPostByUserId(any(String.class))).willReturn(List.of(post));

        MockHttpServletRequestBuilder getRequest = get("/users/1/posts");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].authorId", is(post.getAuthorId())))
                .andExpect(jsonPath("$[0].content", is(post.getContent())));
    }

    @Test
    void deleteNoteTest() throws Exception {
        doNothing().when(postService).deletePost(any(String.class),any(String.class));

        MockHttpServletRequestBuilder deleteRequest = delete("/users/1/posts/1");

        mockMvc.perform(deleteRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    void updateNote() throws Exception {
        post.setCreatedTime(null);
        doNothing().when(postService).updatePost(any(String.class),any(String.class),any(Post.class));

        MockHttpServletRequestBuilder putRequest = put("/users/1/editing/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(post));

        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }

    @Test
    void userLikesNoteTest() throws Exception {
        PostLikes postLikes = new PostLikes(1,true);

        given(postService.userLikesPost(any(String.class),any(String.class))).willReturn(postLikes);

        MockHttpServletRequestBuilder postRequest = post("/users/1/likes/posts/1");

        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postLikeNum", is(postLikes.getPostLikeNum())))
                .andExpect(jsonPath("$.whetherLikePost", is(true)));
    }

    @Test
    void userUnlikesNoteTest() throws Exception {
        PostLikes postLikes = new PostLikes(0,false);

        given(postService.userUnlikesPost(any(String.class),any(String.class))).willReturn(postLikes);

        MockHttpServletRequestBuilder deleteRequest = delete("/users/1/likes/posts/1");

        mockMvc.perform(deleteRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postLikeNum", is(postLikes.getPostLikeNum())))
                .andExpect(jsonPath("$.whetherLikePost", is(false)));
    }

    @Test
    void whetherUserLikesPostTest() throws Exception {
        PostLikes postLikes = new PostLikes(1,true);

        given(postService.whetherUserLikesPost(any(String.class),any(String.class))).willReturn(postLikes);

        MockHttpServletRequestBuilder getRequest = get("/users/1/whetherLikes/posts/1");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postLikeNum", is(postLikes.getPostLikeNum())))
                .andExpect(jsonPath("$.whetherLikePost", is(true)));
    }

    @MockBean
    private RestTemplate restTemplate;
    @Test
    void findFollowingNotesTest() throws Exception {
        User user = new User("1","1","1");
        List<User> followingUsers = List.of(user);
        ResponseEntity<List<User>> response = ResponseEntity.ok(followingUsers);
        PostDTO postDTO = new PostDTO(post);

        given(restTemplate.exchange(any(String.class), any(), any(), eq(new ParameterizedTypeReference<List<User>>() {}))).willReturn(response);
        given(postService.findPostOfFollowees(any())).willReturn(List.of(post));
        given(postService.addUsernameImagePathtothePostlist(List.of(post))).willReturn(List.of(postDTO));

        MockHttpServletRequestBuilder getRequest = get("/posts/following/1");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    private String asJsonString(final Object o) {
        try {
            return new ObjectMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e));
        }
    }
}
