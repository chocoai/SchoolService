package com.wts.entity.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseAssessment<M extends BaseAssessment<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setTitle(java.lang.String title) {
		set("title", title);
	}

	public java.lang.String getTitle() {
		return get("title");
	}

	public void setContent(java.lang.String content) {
		set("content", content);
	}

	public java.lang.String getContent() {
		return get("content");
	}

	public void setTime(java.util.Date time) {
		set("time", time);
	}

	public java.util.Date getTime() {
		return get("time");
	}

	public void setType(java.lang.Integer type) {
		set("type", type);
	}

	public java.lang.Integer getType() {
		return get("type");
	}

	public void setTeacherId(java.lang.Integer teacherId) {
		set("teacher_id", teacherId);
	}

	public java.lang.Integer getTeacherId() {
		return get("teacher_id");
	}

	public void setCourseId(java.lang.Integer courseId) {
		set("course_id", courseId);
	}

	public java.lang.Integer getCourseId() {
		return get("course_id");
	}

	public void setSemesterId(java.lang.Integer semesterId) {
		set("semester_id", semesterId);
	}

	public java.lang.Integer getSemesterId() {
		return get("semester_id");
	}

	public void setState(java.lang.Integer state) {
		set("state", state);
	}

	public java.lang.Integer getState() {
		return get("state");
	}

	public void setScope(java.lang.Integer scope) {
		set("scope", scope);
	}

	public java.lang.Integer getScope() {
		return get("scope");
	}

}
