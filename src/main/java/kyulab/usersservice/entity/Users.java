package kyulab.usersservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(unique = true, length = 100)
	private String email;

	@Column(unique = true, nullable = false, length = 30)
	private String name;

	@JsonIgnore
	@Column(nullable = false, length = 100)
	private String password;

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt;

	public Users(String email, String name, String password) {
		this.email = email;
		this.name = name;
		this.password = password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
