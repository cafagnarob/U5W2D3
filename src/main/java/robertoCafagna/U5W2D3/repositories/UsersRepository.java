package robertoCafagna.U5W2D3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import robertoCafagna.U5W2D3.entities.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
