package com.jts.movie.request;

import com.jts.movie.enums.gender;
import lombok.Data;

@Data
public class UserRequest {

	private String firstName;
	private String lastName;
	private Integer age;
	private String address;
	private String mobileNo;
	private String emailId;
	private gender gender;  // Gender enum
	private String roles;
	private String password;  // password
	private Boolean promotionalOptIn; // Optional for promotions
}

    /*public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public gender getGender() {
		return gender;
	}
	public void setGender(gender gender) {
		this.gender = gender;
	}
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	
	
}*/