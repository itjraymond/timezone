package ca.jent.tzWebJpa.converter;

import java.sql.Time;
import java.time.LocalTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import ca.jent.tzWebJpa.utils.DateUtils;

@Converter(autoApply=true)
public class LocalTimeConverter implements AttributeConverter<LocalTime, java.sql.Time> {

	@Override
	public Time convertToDatabaseColumn(LocalTime localTime) {
		return localTime == null ? null : DateUtils.asJavaSql_Time(localTime);
	}

	@Override
	public LocalTime convertToEntityAttribute(Time time) {
		return time == null ? null : DateUtils.asJavaTime_LocalTime(time);
	}

}