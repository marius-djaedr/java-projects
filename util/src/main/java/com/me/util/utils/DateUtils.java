package com.me.util.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
	public static Date convert(final LocalDate date) {
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate convert(final Date date) {
		if(date instanceof java.sql.Date) {
			return ((java.sql.Date) date).toLocalDate();
		} else {
			return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		}
	}
}
