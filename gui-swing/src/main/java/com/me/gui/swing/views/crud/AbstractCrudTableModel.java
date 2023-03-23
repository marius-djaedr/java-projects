package com.me.gui.swing.views.crud;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public abstract class AbstractCrudTableModel<T extends AbstractCrudPojo>extends AbstractTableModel {

	private static final long serialVersionUID = 7331819996280466804L;

	private List<T> data;

	private final List<ColumnSpecifier<T>> columns;

	public AbstractCrudTableModel() {
		super();
		data = new ArrayList<>();
		columns = getColumns();
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return columns.size();
	}

	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		return columns.get(columnIndex).getValue(data.get(rowIndex));
	}

	@Override
	public String getColumnName(final int column) {
		return columns.get(column).getName();
	}

	@Override
	public Class<?> getColumnClass(final int columnIndex) {
		return columns.get(columnIndex).getValueType();
	}

	@Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex) {
		return columns.get(columnIndex).isEditable();
	}

	public List<Integer> columnsToRemove() {
		return Arrays.asList();
	}

	public void updateData(final List<T> data) {
		this.data = data;

		this.fireTableDataChanged();
	}

	public T getEntityForAction(final ActionEvent e) {
		final int modelRow = Integer.valueOf(e.getActionCommand());
		return data.get(modelRow);
	}

	public int selectIndex() {
		return -1;
	}

	public int editIndex() {
		return -1;
	}

	public int deleteIndex() {
		return -1;
	}

	public int defaultSortIndex() {
		return 0;
	}

	protected abstract List<ColumnSpecifier<T>> getColumns();

	public static interface ColumnSpecifier<POJO> {
		String getName();

		Class<?> getValueType();

		Object getValue(POJO p);

		boolean isEditable();
	}

}
