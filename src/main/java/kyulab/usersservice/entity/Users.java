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

	@Column(unique = true)
	private String name;

	@JsonIgnore
	private String passWord;

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt;

	public Users(String name, String passWord) {
		this.name = name;
		this.passWord = passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

}
