package ASESpaghettiCode.PostServer.Repository;

import ASESpaghettiCode.PostServer.Model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

}
