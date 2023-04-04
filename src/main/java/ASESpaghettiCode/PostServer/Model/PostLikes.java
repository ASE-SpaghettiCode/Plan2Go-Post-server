package ASESpaghettiCode.PostServer.Model;

import lombok.Data;

@Data
public class PostLikes {
    private int postLikeNum;
    private boolean whetherLikePost;

    public PostLikes(int postLikeNum, boolean whetherLikePost) {
        this.postLikeNum = postLikeNum;
        this.whetherLikePost = whetherLikePost;
    }
}
