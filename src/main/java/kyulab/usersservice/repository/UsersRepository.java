package kyulab.usersservice.repository;

import kyulab.usersservice.dto.gateway.res.UsersPreviewGatewayDto;
import kyulab.usersservice.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

	boolean existsByName(String name);

	boolean existsByEmail(String email);

	Optional<Users> findByEmail(String email);

	@Query("""
		select
		new kyulab.usersservice.dto.gateway.res.UsersPreviewGatewayDto(
			u.id, u.email, u.name, u.iconUrl, u.bannerUrl, FALSE
		)
		from Users u
		left join Follow f
		on f.follower.id = u.id
		where u.id in :ids
	""")
	List<UsersPreviewGatewayDto> findUserWithFollow(@Param("ids") Set<Long> userIds);

	@Query("""
		select
		new kyulab.usersservice.dto.gateway.res.UsersPreviewGatewayDto(
			u.id, u.email, u.name, u.iconUrl, u.bannerUrl,
			CASE WHEN f.follower IS NOT NULL THEN TRUE ELSE FALSE END
		)
		from Users u
		left join Follow f
		on f.follower.id = u.id
		and f.following.id = :requestId
		where u.id in :ids
	""")
	List<UsersPreviewGatewayDto> findUserWithFollowForLogin(@Param("requestId") long requestId, @Param("ids") Set<Long> userIds);

}
