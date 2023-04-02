package ASESpaghettiCode.PostServer.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Post {
    @Id
    private String postId;

    private String authorId;

    private String content;

    private List<String> likedUsers;

    private LocalDateTime createdTime;

    public Post(String authorId, String content) {
        this.authorId = authorId;
        this.content = content;
        this.likedUsers = new ArrayList<>();
        this.createdTime = LocalDateTime.now();
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(List<String> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public void addLikedUsers(String userId) {
        this.likedUsers.add(userId);
    }

    public void removeLikedUsers(String userId) {
        this.likedUsers.remove(userId);
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
