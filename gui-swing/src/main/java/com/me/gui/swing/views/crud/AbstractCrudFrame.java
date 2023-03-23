package com.me.gui.swing.views.crud;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.me.gui.swing.utils.GuiUtils;
import com.me.gui.swing.utils.GuiUtils.UpdatePopupField;
import com.me.gui.swing.views.ButtonColumn;
import com.me.gui.swing.views.GuiFrame;
import com.me.gui.swing.views.GuiLaunchView;

public abstract class AbstractCrudFrame<V extends AbstractCrudPojo> implements GuiFrame {
	private static final Logger logger = LoggerFactory.getLogger(AbstractCrudFrame.class);

	@Autowired
	private GuiLaunchView actionManager;

	private AbstractCrudTableModel<V> tableModel;
	private TableRowSorter<AbstractCrudTableModel<V>> sorter;
	private JPanel topPanel;

	@Override
	public JPanel buildPanel() {
		final GridBagConstraints c = new GridBagConstraints();
		topPanel = GuiUtils.commonBuildPanel(c);

		final JPanel additionalTop = buildAdditionalTop();

		final JButton addButton = new JButton("Create New");
		addButton.addActionListener(this::addEntityActionListener);

		c.weighty = 0.;
		if(additionalTop != null) {
			topPanel.add(additionalTop, c);
			c.gridy++;

		}
		topPanel.add(addButton, c);
		c.gridy++;

		c.weighty = 1.;
		topPanel.add(buildTable(), c);

		setData();
		return topPanel;
	}

	private JPanel buildTable() {

		final GridBagConstraints c = new GridBagConstraints();
		final JPanel panel = GuiUtils.commonBuildPanel(c);

		tableModel = newTableModel();
		final JTable table = new JTable(tableModel);
		table.setDefaultRenderer(String.class, new DefaultTableCellRenderer());

		if(tableModel.selectIndex() > -1) {
			new ButtonColumn(table, this::selectEntityActionListener, tableModel.selectIndex());
		}
		if(tableModel.editIndex() > -1) {
			new ButtonColumn(table, this::editEntityActionListener, tableModel.editIndex());
		}
		if(tableModel.deleteIndex() > -1) {
			new ButtonColumn(table, this::deleteEntityActionListener, tableModel.deleteIndex());
		}

		sorter = new TableRowSorter<>(tableModel);
		sorter.toggleSortOrder(tableModel.defaultSortIndex());
		sorter.toggleSortOrder(tableModel.defaultSortIndex());
		table.setRowSorter(sorter);

		int removed = 0;
		for(final Integer toRemove : tableModel.columnsToRemove()) {
			table.removeColumn(table.getColumnModel().getColumn(toRemove - removed));
			removed += 1;
		}

		final JScrollPane scroll = new JScrollPane(table);
		panel.add(scroll, c);
		return panel;
	}

	private void addEntityActionListener(final ActionEvent e) {
		try {
			final V entity = newEntity(UUID.randomUUID().toString());
			if(GuiUtils.entityForm(entity, getUpdateFields())) {
				logger.info("Adding [" + entity + "]");
				getService().create(entity);
				actionManager.actionDone();
			} else {
				logger.warn("add cancelled");
			}
		} catch(final RuntimeException exception) {
			logger.error("Error with add", exception);
			JOptionPane.showMessageDialog(topPanel, exception.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void editEntityActionListener(final ActionEvent e) {
		try {
			final V entity = tableModel.getEntityForAction(e);
			logger.info("Editing [" + entity + "]");
			if(GuiUtils.entityForm(entity, getUpdateFields())) {
				getService().update(entity);
				actionManager.actionDone();
			} else {
				logger.warn("edit cancelled");
			}
		} catch(final RuntimeException exception) {
			logger.error("Error with edit", exception);
			JOptionPane.showMessageDialog(topPanel, exception.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void deleteEntityActionListener(final ActionEvent e) {
		try {
			final V entity = tableModel.getEntityForAction(e);
			logger.info("Deleting [" + entity + "]");

			final int chosen = JOptionPane.showOptionDialog(topPanel, "Are you sure you want to delete " + entity, "Delete Confirmation",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Yes, Delete", "Cancel"}, "Cancel");

			if(chosen != 0) {
				logger.warn("delete canceled");
				return;
			}

			getService().delete(entity);
			actionManager.actionDone();
		} catch(final RuntimeException exception) {
			logger.error("Error with delete", exception);
			JOptionPane.showMessageDialog(topPanel, exception.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void selectEntityActionListener(final ActionEvent e) {
		try {
			final V entity = tableModel.getEntityForAction(e);
			logger.info("Selecting [" + entity + "]");
			selectEntity(entity);
		} catch(final RuntimeException exception) {
			logger.error("Error with select", exception);
			JOptionPane.showMessageDialog(topPanel, exception.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void updateFilter(final RowFilter<AbstractCrudTableModel<V>, Integer> filter) {
		sorter.setRowFilter(filter);
	}

	public void setData() {
		tableModel.updateData(getService().getAll());
	}

	protected void selectEntity(final V entity) {
		throw new UnsupportedOperationException("Select not implemented");
	}

	protected JPanel buildAdditionalTop() {
		return null;
	}

	protected abstract AbstractCrudTableModel<V> newTableModel();

	protected abstract V newEntity(String id);

	protected abstract CrudService<V> getService();

	protected abstract List<UpdatePopupField<V>> getUpdateFields();
}