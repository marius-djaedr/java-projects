package com.me.turnips.services;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.me.turnips.constants.CurveType;
import com.me.turnips.constants.DayTime;
import com.me.turnips.dtos.CostCurve;

public class CachePojo implements Serializable {
	private static final long serialVersionUID = 2L;

	CachePojoId id = new CachePojoId();
	List<CostCurve> costCurves;

	public static class CachePojoId implements Serializable {
		private static final long serialVersionUID = 2L;

		boolean firstTime;
		CurveType previousPattern;
		EnumMap<DayTime, Integer> input;

		@Override
		public boolean equals(final Object obj) {
			return EqualsBuilder.reflectionEquals(this, obj);
		}

		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this);
		}
	}
}
