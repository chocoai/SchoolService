package com.wts.entity.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseStudentcoursesemester<M extends BaseStudentcoursesemester<M>> extends Model<M> implements IBean {

	public void setStudentId(java.lang.Integer studentId) {
		set("student_id", studentId);
	}

	public java.lang.Integer getStudentId() {
		return get("student_id");
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

}
