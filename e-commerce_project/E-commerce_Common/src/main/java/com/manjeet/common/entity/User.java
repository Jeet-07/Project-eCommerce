package com.manjeet.common.entity;
import java.nio.file.Paths;
import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name="users")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length=128,nullable=false,unique=true)
	private String email;
	
	@Column(length=64,nullable=false)
	private String password;
	
	@Column(name="first_name",length=50,nullable=false)
	private String firstName;
	
	@Column(name="last_name",length=50,nullable=false)
	private String lastName;
	
	@Column(length=64)
	private String photos;
	
	private boolean enabled;
	
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name="users_roles",
			joinColumns= @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="role_id")
			)
	private Set<Role> roles=new HashSet<>();
	
	public User() {
		super();
	}
	
	public User(String email, String password, String firstName, String lastName, String photos, boolean enabled) {
		super();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.photos = photos;
		this.enabled = enabled;
	}
	
	public User(String email, String password, String firstName, String lastName, String photos, boolean enabled,
			Set<Role> roles) {
		super();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.photos = photos;
		this.enabled = enabled;
		this.roles = roles;
	}

	public User(Integer id, String email, String password, String firstName, String lastName, String photos,
			boolean enabled, Set<Role> roles) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.photos = photos;
		this.enabled = enabled;
		this.roles = roles;
	}
	
	public void addRole(Role role) {
		this.roles.add(role);
	}

	@Transient
	public String getPhotoPath(){
		if(this.id==null || this.photos == null)
			return "/images/default-user.jpg";

		return "/user-photos/"+this.id+"/"+this.photos;
	}

}
