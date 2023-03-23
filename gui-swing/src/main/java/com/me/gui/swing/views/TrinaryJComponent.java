package com.me.gui.swing.views;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;

public class TrinaryJComponent extends JComboBox<String> {

	private static final long serialVersionUID = 8141773692218563661L;

	private static final String[] TRINARY_OPTIONS = new String[]{"null", "true", "false"};
	private static final Map<Integer, Boolean> GET_MAP = buildGetMap();
	private static final Map<Boolean, Integer> SET_MAP = buildSetMap();

	public TrinaryJComponent() {
		super(TRINARY_OPTIONS);
		setValue(null);
	}

	public void setValue(final Boolean value) {
		super.setSelectedIndex(SET_MAP.get(value));
	}

	public Boolean getValue() {
		return GET_MAP.get(super.getSelectedIndex());
	}

	private static Map<Boolean, Integer> buildSetMap() {
		final Map<Boolean, Integer> map = new HashMap<>();
		map.put(null, 0);
		map.put(true, 1);
		map.put(false, 2);
		return map;
	}

	private static Map<Integer, Boolean> buildGetMap() {
		final Map<Integer, Boolean> map = new HashMap<>();
		map.put(-1, null);
		map.put(0, null);
		map.put(1, true);
		map.put(2, false);
		return map;
	}

}
