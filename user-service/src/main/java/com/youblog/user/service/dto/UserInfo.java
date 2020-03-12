package com.youblog.user.service.dto;
import com.youblog.user.persistence.model.Role;

public class UserInfo {
	private Role role;
	private String email;
	private String nickName;
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	
}
