package kyulab.usersservice.repository;

import kyulab.usersservice.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

	boolean existsByName(String name);

	Optional<Users> findByName(String name);

}
