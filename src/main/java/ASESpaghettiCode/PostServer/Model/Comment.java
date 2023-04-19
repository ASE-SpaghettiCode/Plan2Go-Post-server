package ASESpaghettiCode.PostServer.Model;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class Comment {
    @Id
    private String commentId;

    private String commentAuthorId;

    private String commentAuthorName;

    private String commentAuthorImage;

    private LocalDateTime createdTime;

    private String targetPostId;

    private String commentText;

    public Comment(String commentAuthorId, String commentAuthorName, String commentAuthorImage, String targetPostId, String commentText) {
        this.commentAuthorId = commentAuthorId;
        this.commentAuthorName = commentAuthorName;
        this.commentAuthorImage = commentAuthorImage;
        this.createdTime = LocalDateTime.now();
        this.targetPostId = targetPostId;
        this.commentText = commentText;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentAuthorId() {
        return commentAuthorId;
    }

    public void setCommentAuthorId(String commentAuthorId) {
        this.commentAuthorId = commentAuthorId;
    }

    public String getCommentAuthorName() {
        return commentAuthorName;
    }

    public void setCommentAuthorName(String commentAuthorName) {
        this.commentAuthorName = commentAuthorName;
    }

    public String getCommentAuthorImage() {
        return commentAuthorImage;
    }

    public void setCommentAuthorImage(String commentAuthorImage) {
        this.commentAuthorImage = commentAuthorImage;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getTargetPostId() {
        return targetPostId;
    }

    public void setTargetPostId(String targetNoteId) {
        this.targetPostId = targetNoteId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
