package com.me.gui.swing.views.crud;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import javax.swing.RowFilter;
import javax.swing.table.TableModel;

public class GenericRowFilter<M extends TableModel>extends RowFilter<M, Integer> {

	private final Map<Integer, Predicate<Object>> andFilterMap, orFilterMap;

	public GenericRowFilter(final Integer column, final Predicate<Object> filter) {
		andFilterMap = new HashMap<>();
		orFilterMap = new HashMap<>();

		andFilterMap.put(column, filter);
	}

	/**
	 * 
	 * For a row to not be included, one of the "and" must be false OR all of the "or" must be false
	 * 
	 * @param andFilterMap - all of these must be true. If a single is false, will not include the row
	 * @param orFilterMap - any of these can be true. Assuming all "and" filters are true, if a single is "or" true, will include the row
	 */
	public GenericRowFilter(final Map<Integer, Predicate<Object>> andFilterMap, final Map<Integer, Predicate<Object>> orFilterMap) {
		this.andFilterMap = andFilterMap;
		this.orFilterMap = orFilterMap;
	}

	@Override
	public boolean include(final Entry<? extends M, ? extends Integer> entry) {

		for(final Map.Entry<Integer, Predicate<Object>> andFilter : andFilterMap.entrySet()) {
			final boolean filterResult = andFilter.getValue().test(entry.getValue(andFilter.getKey()));
			if(!filterResult) {
				//shortcircuit
				return false;
			}
		}
		if(orFilterMap.isEmpty()) {
			return true;
		}

		for(final Map.Entry<Integer, Predicate<Object>> orFilter : orFilterMap.entrySet()) {
			final boolean filterResult = orFilter.getValue().test(entry.getValue(orFilter.getKey()));
			if(filterResult) {
				//shortcircuit
				return true;
			}
		}

		return false;
	}

}
