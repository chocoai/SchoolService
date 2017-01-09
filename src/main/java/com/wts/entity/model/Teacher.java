package com.wts.entity.model;

import com.jfinal.plugin.activerecord.Page;
import com.wts.entity.base.BaseTeacher;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Teacher extends BaseTeacher<Teacher> {
	public static final Teacher dao = new Teacher();
	public Page<Teacher> paginate2(int pageNumber, int pageSize, String name) {
		return paginate(pageNumber, pageSize, "SELECT *",
						"FROM teacher WHERE name LIKE '%"+name+"%' ORDER BY id DESC");
	}
}
