package ASESpaghettiCode.PostServer.Repository;

import ASESpaghettiCode.PostServer.Model.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    @Query("{'authorId': {$in: ?0}}")
    List<Post> findByUserIdListInOrderByCreatedDateAsc(List<String> followingUserId, Sort createdTime);
}
