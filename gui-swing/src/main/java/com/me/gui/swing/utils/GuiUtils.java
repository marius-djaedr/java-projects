package com.me.gui.swing.utils;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.lang3.tuple.Pair;

import com.me.gui.swing.views.crud.AbstractCrudPojo;

public final class GuiUtils {
	private GuiUtils() {}

	public static JPanel commonBuildPanel(final GridBagConstraints c) {
		final JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.;
		c.weighty = 1.;
		c.gridx = 0;
		c.gridy = 0;

		return panel;
	}

	public static <V extends AbstractCrudPojo> boolean entityForm(final V existing, final List<UpdatePopupField<V>> fieldSpecs) {
		return entityForm(Arrays.asList(Pair.of(null, fieldSpecs)), existing);
	}

	public static <V extends AbstractCrudPojo> boolean entityForm(final List<Pair<String, List<UpdatePopupField<V>>>> catSpecs, final V existing) {
		final Map<UpdatePopupField<V>, JComponent> fieldMap = catSpecs.stream().map(Pair::getRight).flatMap(List::stream)
				.collect(Collectors.toMap(Function.identity(), f -> f.instantiateField(existing)));

		final JPanel panel = new JPanel(new GridLayout(0, 1));

		for(final Pair<String, List<UpdatePopupField<V>>> catSpec : catSpecs) {
			final String category = catSpec.getLeft();
			if(category != null) {
				panel.add(new JLabel(category));
			}
			for(final UpdatePopupField<V> fieldSpec : catSpec.getRight()) {
				panel.add(new JLabel(fieldSpec.getLabel()));
				panel.add(fieldMap.get(fieldSpec));
			}
		}

		final int result = JOptionPane.showConfirmDialog(null, panel, "Add/Edit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(result == JOptionPane.OK_OPTION) {
			fieldMap.forEach((upf, jc) -> upf.pullFromField(jc, existing));

			return true;
		} else {
			return false;
		}

	}

	public static interface UpdatePopupField<T extends AbstractCrudPojo> {
		JComponent instantiateField(T existing);

		String getLabel();

		void pullFromField(final JComponent field, final T toFill);
	}
}
