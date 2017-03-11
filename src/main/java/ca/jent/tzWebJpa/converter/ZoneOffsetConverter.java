package ca.jent.tzWebJpa.converter;

import java.time.ZoneOffset;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class ZoneOffsetConverter implements AttributeConverter<ZoneOffset, String> {

	@Override
	public String convertToDatabaseColumn(ZoneOffset zoneOffset) {
		return zoneOffset == null ? null : zoneOffset.toString();
	}

	@Override
	public ZoneOffset convertToEntityAttribute(String offsetId) {
		return offsetId == null ? null : ZoneOffset.of(offsetId);
	}

}
