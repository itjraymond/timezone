package ca.jent.tzWebJpa.converter;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import ca.jent.tzWebJpa.utils.DateUtils;


@Converter(autoApply=true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalDate localDate) {
		return localDate == null ? null : DateUtils.asJavaUtil_Date(localDate);
	}

	@Override
	public LocalDate convertToEntityAttribute(Date date) {
		return date == null ? null : DateUtils.asJavaTime_LocalDate(date);
	}

}
