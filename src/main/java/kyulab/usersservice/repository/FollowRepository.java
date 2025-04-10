package kyulab.usersservice.repository;

import kyulab.usersservice.entity.Follow;
import kyulab.usersservice.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

	List<Follow> findFollowsByFollower(Users users);

}
