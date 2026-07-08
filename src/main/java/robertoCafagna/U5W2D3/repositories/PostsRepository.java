package robertoCafagna.U5W2D3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import robertoCafagna.U5W2D3.entities.Post;

@Repository
public interface PostsRepository extends JpaRepository<Post, Long> {
}
