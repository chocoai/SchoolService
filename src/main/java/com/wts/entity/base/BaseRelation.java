package com.wts.entity.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseRelation<M extends BaseRelation<M>> extends Model<M> implements IBean {

	public void setParentId(java.lang.Integer parentId) {
		set("parent_id", parentId);
	}

	public java.lang.Integer getParentId() {
		return get("parent_id");
	}

	public void setStudentId(java.lang.Integer studentId) {
		set("student_id", studentId);
	}

	public java.lang.Integer getStudentId() {
		return get("student_id");
	}

	public void setIdentityId(java.lang.Integer identityId) {
		set("identity_id", identityId);
	}

	public java.lang.Integer getIdentityId() {
		return get("identity_id");
	}

	public void setState(java.lang.Integer state) {
		set("state", state);
	}

	public java.lang.Integer getState() {
		return get("state");
	}

}