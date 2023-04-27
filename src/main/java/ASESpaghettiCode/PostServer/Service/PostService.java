package ASESpaghettiCode.PostServer.Service;

import ASESpaghettiCode.PostServer.Controller.CommentController;
import ASESpaghettiCode.PostServer.Controller.RestTemplateErrorHandler;
import ASESpaghettiCode.PostServer.Model.*;
import ASESpaghettiCode.PostServer.Repository.PostRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Value("${UserServerLocation}")
    private String UserServerLocation;

    @Value("http://localhost:8082")
    private String TravelNoteServerLocation;

    @Autowired
    private RestTemplate restTemplate = new RestTemplateBuilder()
            .errorHandler(new RestTemplateErrorHandler())
            .build();

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAllPost() {
        return this.postRepository.findAll();
    }

    public Post createPost(Post newPost) {
        List<Comment> initialComments = new ArrayList<>();
        newPost.setComments(initialComments);
        if (newPost.getSharedNoteId()!=null){
            Note note = restTemplate.getForObject(TravelNoteServerLocation + "/notes/" + newPost.getSharedNoteId(), Note.class);
            newPost.setSharedNoteCoverImage(note.getCoverImage());
        }
        return postRepository.save(newPost);
    }

    public Post getPostById(String postId) {
        Optional<Post> targetPost = postRepository.findById(postId);
        if (targetPost.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post is not found!");
        }
        return targetPost.get();
    }

    public List<Post> findPostByUserId(String userId) {
        Optional<List<Post>> sortedListOfPost = Optional.ofNullable(postRepository.findByUserIdInOrderByCreatedDateAsc(userId, Sort.by(Sort.Direction.DESC, "createdTime")));
        if (sortedListOfPost.isEmpty()) {
            return new ArrayList<>();
        }
        return sortedListOfPost.get();
    }

    public void deletePost(String postId, String userId) {
        Optional<Post> targetPost = postRepository.findById(postId);
        if (targetPost.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post is not found!");
        } else if (!Objects.equals(userId, targetPost.get().getAuthorId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot delete this Post!");
        }
        else {
            postRepository.delete(postRepository.findById(postId).get());
        }
    }

    public void updatePost(String postId, String userId, Post post) {
        Optional<Post> targetPost = postRepository.findById(postId);
        if (targetPost.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post is not found!");
        } else if (!Objects.equals(userId, targetPost.get().getAuthorId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot edit this Post!");
        }
        else {
            targetPost.get().setContent(post.getContent());
            postRepository.save(targetPost.get());
        }
    }


    public PostLikes userLikesPost(String userId, String postId) {
        Optional<Post> targetPost = postRepository.findById(postId);
        if (targetPost.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The travel note is not found!");
        }
        if(!targetPost.get().getLikedUsers().contains(userId)) {
            targetPost.get().addLikedUsers(userId);
            postRepository.save(targetPost.get());
        }
        String ownerId = targetPost.get().getAuthorId();
        restTemplate.postForLocation(UserServerLocation+"/notifications",createLikesNotification(userId, postId, ownerId));
        return getPostLikes(userId, targetPost);
    }

    public Notification createLikesNotification(String userId, String noteId,String ownerId){
        Notification notification = new Notification();
        notification.setActorId(userId);
        notification.setMethod("like");
        notification.setOwnerId(ownerId);
        notification.setTargetType("note");
        notification.setTargetId(noteId);
        return notification;
    }

    public PostLikes userUnlikesPost(String userId, String noteId) {
        Optional<Post> targetNote = postRepository.findById(noteId);
        if (targetNote.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The travel note is not found!");
        }
        if (!targetNote.get().getLikedUsers().contains(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user didn't like this travel note");
        }
        targetNote.get().removeLikedUsers(userId);
        postRepository.save(targetNote.get());
        return getPostLikes(userId, targetNote);
    }

    private PostLikes getPostLikes(String userId, Optional<Post> targetNote) {
        List<String> likedUsers = targetNote.get().getLikedUsers();
        boolean whetherLikes = likedUsers.contains(userId);
        int likeNum = likedUsers.size();
        return new PostLikes(likeNum, whetherLikes);
    }

    public PostLikes whetherUserLikesPost(String userId, String noteId) {
        Optional<Post> targetNote = postRepository.findById(noteId);
        if (targetNote.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The travel note is not found!");
        }
        return getPostLikes(userId, targetNote);
    }


    public List<Post> findPostOfFollowees(List<String> followingUserId) {
        Optional<List<Post>> sortedList = Optional.ofNullable(postRepository.findByUserIdListInOrderByCreatedDateAsc(followingUserId, Sort.by(Sort.Direction.DESC, "createdTime")));
        if (sortedList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User haven't follow anyone!");
        }
        return sortedList.get();

    }

    public List<PostDTO> addUsernameImagePathtothePostlist(List<Post> postList){
        List<PostDTO> postDTOS = new ArrayList<>();
        for (Post post : postList){
            postDTOS.add(new PostDTO(post));
        }
        List<String> authorIdList = new ArrayList<>();
        List<String> authorNameList = new ArrayList<>();
        List<String> ImagePathList = new ArrayList<>();
        for (PostDTO postDTO : postDTOS){
            String authorId = postDTO.getPost().getAuthorId();
            if(!authorIdList.contains(authorId)){
                authorIdList.add(authorId);
                User user = restTemplate.getForObject(UserServerLocation + "/users/" + authorId, User.class);
                authorNameList.add(user.getUsername());
                ImagePathList.add(user.getImageLink());
            }
            postDTO.setAuthorName(authorNameList.get(authorIdList.indexOf(authorId)));
            postDTO.setImagePath(ImagePathList.get(authorIdList.indexOf(authorId)));
        }
        return postDTOS;
    }

}
