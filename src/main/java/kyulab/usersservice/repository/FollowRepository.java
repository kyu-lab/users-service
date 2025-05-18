package kyulab.usersservice.repository;

import kyulab.usersservice.dto.res.FollowingCountDto;
import kyulab.usersservice.entity.Follow;
import kyulab.usersservice.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

	List<Follow> findFollowsByFollower(Users users);

	boolean existsByFollowerAndFollowing(Users follower, Users following);

	void deleteByFollowerAndFollowing(Users follower, Users following);

	@Query("""
	 select new kyulab.usersservice.dto.res.FollowingCountDto (
	 	(select count(f) from Follow f where f.follower.id = :userId),
		(select count(f) from Follow f where f.following.id = :userId)
	 )
	""")
	FollowingCountDto countFollowingByUserId(@Param("userId") long userId);

}
