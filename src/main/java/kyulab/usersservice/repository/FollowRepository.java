package kyulab.usersservice.repository;

import kyulab.usersservice.dto.res.FollowInfoDto;
import kyulab.usersservice.dto.res.FollowingCountDto;
import kyulab.usersservice.entity.Follow;
import kyulab.usersservice.entity.Users;
import org.springframework.data.domain.Pageable;
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

	@Query("""
			select new kyulab.usersservice.dto.res.FollowInfoDto (
				u.id, u.name, u.iconUrl, u.bannerUrl, u.description
			)
			from Follow f
			inner join Users u on u.id = f.following.id
			where f.follower.id = :userId
			and (:cursor IS NULL OR f.following.id < :cursor)
			order by f.following.id desc
	""")
	List<FollowInfoDto> findFollowerByCuror(@Param("userId") long userId, @Param("cursor") Long cursor, Pageable pageable);

	@Query("""
			select new kyulab.usersservice.dto.res.FollowInfoDto (
				u.id, u.name, u.iconUrl, u.bannerUrl, u.description
			)
			from Follow f
			inner join Users u on u.id = f.follower.id
			where f.following.id = :userId
			and (:cursor IS NULL OR f.following.id < :cursor)
			order by f.following.id desc
	""")
	List<FollowInfoDto> findFollowingByCuror(@Param("userId") long userId, @Param("cursor") Long cursor, Pageable pageable);

}
