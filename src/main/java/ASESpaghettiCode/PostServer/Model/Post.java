package ASESpaghettiCode.PostServer.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Post {
    @Id
    private String postId;

    private String authorId;

    private String content;

    public Post(String authorId, String content) {
        this.authorId = authorId;
        this.content = content;
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
}
