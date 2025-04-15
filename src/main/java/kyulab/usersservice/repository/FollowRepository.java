package kyulab.usersservice.repository;

import kyulab.usersservice.entity.Follow;
import kyulab.usersservice.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

	List<Follow> findFollowsByFollower(Users users);

	boolean existsByFollowerAndFollowing(Users follower, Users following);

	void deleteByFollowerAndFollowing(Users follower, Users following);

}
