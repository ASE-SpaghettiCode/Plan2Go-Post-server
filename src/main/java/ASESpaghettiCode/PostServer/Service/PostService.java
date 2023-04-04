package ASESpaghettiCode.PostServer.Service;

import ASESpaghettiCode.PostServer.Model.Post;
import ASESpaghettiCode.PostServer.Model.PostLikes;
import ASESpaghettiCode.PostServer.Repository.PostRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAllPost() {
        return this.postRepository.findAll();
    }

    public Post createPost(Post newPost) {
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
        List<Post> listOfPost = new ArrayList<>();
        for (Post post : postRepository.findAll()) {
            if (Objects.equals(post.getAuthorId(), userId)) {
                listOfPost.add(post);
            }
        }
        return listOfPost;
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


    public PostLikes userLikesPost(String userId, String noteId) {
        Optional<Post> targetNote = postRepository.findById(noteId);
        if (targetNote.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The travel note is not found!");
        }
        if(!targetNote.get().getLikedUsers().contains(userId)) {
            targetNote.get().addLikedUsers(userId);
            postRepository.save(targetNote.get());
        }
        return getPostLikes(userId, targetNote);
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


}
