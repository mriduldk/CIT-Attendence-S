package com.ajstudios.easyattendance.model;

import com.google.gson.annotations.SerializedName;

public class User {

	@SerializedName("name")
	private String name;

	@SerializedName("password")
	private String password;

	@SerializedName("phone_no")
	private String phone_no;

	@SerializedName("department")
	private String department;

	@SerializedName("semester")
	private String semester;

	@SerializedName("roll_no")
	private String roll_no;

	@SerializedName("id")
	private String id;

	@SerializedName("userType")
	private String userType;

	@SerializedName("attendanceStatus")
	private String attendanceStatus;


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone_no() {
		return phone_no;
	}

	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getRoll_no() {
		return roll_no;
	}

	public void setRoll_no(String roll_no) {
		this.roll_no = roll_no;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getAttendanceStatus() {
		return attendanceStatus;
	}

	public void setAttendanceStatus(String attendanceStatus) {
		this.attendanceStatus = attendanceStatus;
	}
}