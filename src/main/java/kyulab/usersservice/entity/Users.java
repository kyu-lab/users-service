package kyulab.usersservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kyulab.usersservice.domain.UserStatus;
import kyulab.usersservice.dto.req.UsersSignUpDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(unique = true, length = 100)
	private String email;

	@Column(unique = true, nullable = false, length = 30)
	private String name;

	@JsonIgnore
	@Column(nullable = false, length = 100)
	private String password;
	
	@Column
	private String iconUrl;

	@Column
	private String bannerUrl;

	@Column(length = 100)
	private String description;

	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt;

	public Users(String email, String name, String password) {
		this.email = email;
		this.name = name;
		this.password = password;
		this.status = UserStatus.ACTIVE;
	}

	public void updatePassword(String password) {
		this.password = password;
	}

	public void updateIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public void updateBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public void updateUserStatus(UserStatus status) {
		this.status = status;
	}

}
