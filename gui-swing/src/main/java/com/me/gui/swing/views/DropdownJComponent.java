package com.me.gui.swing.views;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;

public class DropdownJComponent extends JComboBox<String> {
	private static final long serialVersionUID = 274282363151259460L;
	private final Map<Integer, String> GET_MAP;
	private final Map<String, Integer> SET_MAP;

	public DropdownJComponent(final String[] values) {
		super(values);
		GET_MAP = new HashMap<>();
		SET_MAP = new HashMap<>();
		for(int i = 0 ; i < values.length ; i++) {
			GET_MAP.put(i, values[i]);
			SET_MAP.put(values[i], i);
		}
		GET_MAP.put(-1, null);
		SET_MAP.put(null, -1);
	}

	public void setValue(final String value) {
		super.setSelectedIndex(SET_MAP.get(value));
	}

	public String getValue() {
		return GET_MAP.get(super.getSelectedIndex());
	}
}
