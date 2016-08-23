package me.j360.dubbo.model;

import com.pajk.sims.entity.enums.DoctorType;

import java.io.Serializable;

public class UserQuery extends BaseQuery<UserInfo> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String headName;	//组长姓名
	private String memberName;	//组员姓名
	private DoctorType doctorType;	//组长类型
	public String getHeadName() {
		return headName;
	}
	public void setHeadName(String headName) {
		this.headName = headName;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public DoctorType getDoctorType() {
		return doctorType;
	}
	public void setDoctorType(DoctorType doctorType) {
		this.doctorType = doctorType;
	}
}
