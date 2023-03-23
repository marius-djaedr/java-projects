package com.me.gui.swing.views.crud;

import java.util.List;

public interface CrudService<V extends AbstractCrudPojo> {
	List<V> getAll();

	void create(V entity);

	void update(V entity);

	void delete(V entity);

}
