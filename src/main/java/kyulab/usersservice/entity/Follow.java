package kyulab.usersservice.entity;

import jakarta.persistence.*;
import kyulab.usersservice.util.UserContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Users follower;

	@ManyToOne(fetch = FetchType.LAZY)
	private Users following;

	public Follow(Users follower, Users following) {
		this.follower = follower;
		this.following = following;
	}

}
