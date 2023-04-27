package ASESpaghettiCode.PostServer.Model;



// There are 3 types of notifications:
// 1. NotificationTypeText: Pure message
// 2. NotificationTypeFollows:somebody follows you
// 3. NotificationTypeLikesOrComments: somebody likes/comments your note/post


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@Document(collection = "notification")
public class Notification {

    @Id
    private String notificationId;

    private boolean read;

    private String ownerId;

    private String method; // "like" or "comment" or "follow" in string

    private String actorId; //id of who likes/comment your post/notes

    private String actorName; //id of who likes/comment your post/notes

    private String targetType; // "note" or "post" in string or null

    private String targetId; // the postId or noteId or null

    private String context; //can be null

    private LocalDateTime createdTime;

    public Notification() {
        this.createdTime = LocalDateTime.now();
        this.read = false;
    }


    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
