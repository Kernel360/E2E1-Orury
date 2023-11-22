package com.kernel360.orury.domain.user.db;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kernel360.orury.global.common.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserEntity extends BaseEntity {

	@JsonIgnore
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "email_addr", unique = true)
	private String emailAddr;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "password")
	private String password;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@LastModifiedDate
	private LocalDateTime passwdUpdateDate;
	@Column(name = "activated")
	private boolean activated;
	private Boolean isWithdrawal;
	private String withdrawalId;
	private LocalDateTime withdrawalAt;
	private String remark;

	@ManyToMany
	@JoinTable(
		name = "user_authority",
		joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "authority_name", referencedColumnName = "name"),
		foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),
		inverseForeignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
	)
	private Set<AuthorityEntity> authorities;

}
