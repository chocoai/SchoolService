package com.wts.entity.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseStudenttag<M extends BaseStudenttag<M>> extends Model<M> implements IBean {

	public void setStudentId(java.lang.Integer studentId) {
		set("student_id", studentId);
	}

	public java.lang.Integer getStudentId() {
		return get("student_id");
	}

	public void setTagId(java.lang.Integer tagId) {
		set("tag_id", tagId);
	}

	public java.lang.Integer getTagId() {
		return get("tag_id");
	}

}