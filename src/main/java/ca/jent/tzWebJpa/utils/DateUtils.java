package ca.jent.tzWebJpa.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateUtils {

	public static java.util.Date asJavaUtil_Date(LocalDate localDate) {
		if (localDate == null) throw new IllegalArgumentException(java.time.LocalDate.class.getName() +" argument cannot be null.");
		return java.sql.Date.valueOf(localDate);
	}
	
	public static java.util.Date asJavaUtil_Date(LocalDateTime localDateTime) {
		if (localDateTime == null) throw new IllegalArgumentException("java.time.LocalDateTime argument cannot be null.");
		return java.util.Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static java.util.Date asJavaUtil_Date(ZonedDateTime zonedDateTime) {
		if (zonedDateTime == null) throw new IllegalArgumentException("java.time.ZonedDateTime argument cannot be null.");
		return java.util.Date.from(zonedDateTime.toInstant());
	}
	
	public static java.sql.Time asJavaSql_Time(LocalTime localTime) {
		if (localTime == null) throw new IllegalArgumentException("java.time.LocalTime argument cannot be null.");
		return java.sql.Time.valueOf(localTime);
	}
	
	public static java.sql.Timestamp asJavaSql_Timestamp(LocalDateTime localDateTime) {
		if (localDateTime == null) throw new IllegalArgumentException("java.time.LocalDateTime argument cannot be null.");
		return java.sql.Timestamp.valueOf(localDateTime);
	}
	
	public static java.sql.Timestamp asJavaSql_Timestamp(Instant instant) {
		if (instant == null) throw new IllegalArgumentException("java.time.Instant argument cannot be null.");
		return java.sql.Timestamp.from(instant);
	}
	
	public static LocalDate asJavaTime_LocalDate(java.util.Date date) {
		if (date == null) throw new IllegalArgumentException("java.util.Date argument cannot be null.");
		//return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
		return asJavaTime_ZonedDateTime(date).toLocalDate();
	}
	
	public static LocalDateTime asJavaTime_LocalDateTime(java.util.Date date) {
		if (date == null) throw new IllegalArgumentException("java.util.Date argument cannot be null.");
		//return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
		return asJavaTime_ZonedDateTime(date).toLocalDateTime();
	}
	
	public static ZonedDateTime asJavaTime_ZonedDateTime(java.util.Date date) {
		if (date == null) throw new IllegalArgumentException("java.util.Date argument cannot be null.");
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault());
	}
	
	public static LocalTime asJavaTime_LocalTime(java.sql.Time time) {
		if (time == null) throw new IllegalArgumentException("java.sql.Time argument cannot be null.");
		return time.toLocalTime();
	}
	
	public static Instant asJavaTime_Instant(java.sql.Timestamp timestamp) {
		if (timestamp == null) throw new IllegalArgumentException(java.sql.Timestamp.class.getName() + " argument cannot be null.");
		return timestamp.toInstant();
	}



}
