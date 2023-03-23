package com.me.gui.swing.views.crud;

public abstract class AbstractCrudPojo {

	private final String id;

	public AbstractCrudPojo(final String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
