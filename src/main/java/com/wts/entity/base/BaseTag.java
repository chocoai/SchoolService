package com.wts.entity.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseTag<M extends BaseTag<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setType(java.lang.Integer type) {
		set("type", type);
	}

	public java.lang.Integer getType() {
		return get("type");
	}

	public void setState(java.lang.Integer state) {
		set("state", state);
	}

	public java.lang.Integer getState() {
		return get("state");
	}

	public void setCategory(java.lang.Integer category) {
		set("category", category);
	}

	public java.lang.Integer getCategory() {
		return get("category");
	}

	public void setQyId(java.lang.Integer qyId) {
		set("qy_id", qyId);
	}

	public java.lang.Integer getQyId() {
		return get("qy_id");
	}

	public void setFwId(java.lang.Integer fwId) {
		set("fw_id", fwId);
	}

	public java.lang.Integer getFwId() {
		return get("fw_id");
	}

	public void setDyId(java.lang.Integer dyId) {
		set("dy_id", dyId);
	}

	public java.lang.Integer getDyId() {
		return get("dy_id");
	}

}