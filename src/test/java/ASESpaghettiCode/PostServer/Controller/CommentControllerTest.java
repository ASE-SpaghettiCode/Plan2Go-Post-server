package ASESpaghettiCode.PostServer.Controller;

import ASESpaghettiCode.PostServer.Model.Comment;
import ASESpaghettiCode.PostServer.Model.CommentPostDTO;
import ASESpaghettiCode.PostServer.Repository.CommentRepository;
import ASESpaghettiCode.PostServer.Repository.PostRepository;
import ASESpaghettiCode.PostServer.Service.CommentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {
    @MockBean
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private PostRepository postRepository;

    @Autowired
    private MockMvc mockMvc;

    Comment comment = new Comment("commentAuthorId","commentAuthorName","commentAuthorName","targetPostId","commentText");

    @Test
    void getCommentsByNoteIdTest() throws Exception {
        List<Comment> comments = List.of(comment);
        given(commentService.findCommentsByPostId(any(String.class))).willReturn(comments);

        MockHttpServletRequestBuilder getRequest = get("/posts/1/comments");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].commentAuthorId", is(comment.getCommentAuthorId())))
                .andExpect(jsonPath("$[0].commentAuthorName", is(comment.getCommentAuthorName())))
                .andExpect(jsonPath("$[0].commentAuthorImage", is(comment.getCommentAuthorImage())))
                .andExpect(jsonPath("$[0].targetPostId", is(comment.getTargetPostId())))
                .andExpect(jsonPath("$[0].commentText", is(comment.getCommentText())));
    }

    @Test
    void createCommentForAGivenNoteTest() throws Exception {
        CommentPostDTO commentPostDTO = new CommentPostDTO();
        given(commentService.createComment(any(String.class),any(CommentPostDTO.class))).willReturn(comment);

        MockHttpServletRequestBuilder postRequest = post("/posts/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(commentPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.commentAuthorId", is(comment.getCommentAuthorId())))
                .andExpect(jsonPath("$.commentAuthorName", is(comment.getCommentAuthorName())))
                .andExpect(jsonPath("$.commentAuthorImage", is(comment.getCommentAuthorImage())))
                .andExpect(jsonPath("$.targetPostId", is(comment.getTargetPostId())))
                .andExpect(jsonPath("$.commentText", is(comment.getCommentText())));
    }

    @Test
    void deleteCommentTest() throws Exception {
        doNothing().when(commentService).deleteComment(any(String.class),any(String.class));

        MockHttpServletRequestBuilder deleteRequest = delete("/users/1/comments/1");

        mockMvc.perform(deleteRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    void updateCommentTest() throws Exception {
        doNothing().when(commentService).updateComment(any(String.class),any(String.class),any(CommentPostDTO.class));

        MockHttpServletRequestBuilder putRequest = put("/users/1/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new CommentPostDTO()));

        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
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
