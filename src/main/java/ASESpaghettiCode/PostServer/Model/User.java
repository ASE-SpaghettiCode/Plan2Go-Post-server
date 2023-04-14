package ASESpaghettiCode.PostServer.Model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@Data
public class User{

    public String userId;
    public String username;
    public String password;
    public String token;
    public String intro;
    public String imageLink;
    public List<String> followers;
    public List<String> followings;

    public User(String username, String password, String token){
        this.password=password;
        this.username=username;
        this.token=token;
    }

    @JsonCreator
    public static User create(@JsonProperty("userId") String userId,
                              @JsonProperty("username") String username,
                              @JsonProperty("password") String password,
                              @JsonProperty("token") String token,
                              @JsonProperty("intro") String intro,
                              @JsonProperty("imageLink") String imageLink){
        User user = new User(username,password,token);
        user.setUserId(userId);
        user.setIntro(intro);
        user.setImageLink(imageLink);
        return user;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername(){
        return username;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIntro(){
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getImageLink() {
        return imageLink;
    }

    public List<String> getFollowers() {
        return this.followers;
    }

    public void setFollowers(List<String> initialList) {
        this.followers = initialList;
    }

    public void addFollowers(String userId) {
        this.followers.add(userId);
    }

    public void removeFollowers(String userId) {
        this.followers.remove(userId);
    }

    public List<String> getFollowings() {
        return this.followings;
    }

    public void setFollowings(List<String> initialList) {
        this.followings = initialList;
    }

    public void addFollowings(String userId) {
        this.followings.add(userId);
    }

    public void removeFollowings(String userId) {
        this.followings.remove(userId);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", password=" + password+'\''+
                ", intro=" + intro +'}';
    }
}
