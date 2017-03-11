package ca.jent.tzWebJpa.entities;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ca.jent.tzWebJpa.converter.InstantConverter;
import ca.jent.tzWebJpa.converter.LocalDateConverter;
import ca.jent.tzWebJpa.converter.LocalTimeConverter;
import ca.jent.tzWebJpa.converter.ZoneIdConverter;
import ca.jent.tzWebJpa.converter.ZoneOffsetConverter;

@Entity
@Table(name="TEMPORALS")
public class Temporal {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="LOCAL_DATE")
	@Convert(converter=LocalDateConverter.class)
	private LocalDate localDate;
	
	@Column(name="LOCAL_TIME")
	@Convert(converter=LocalTimeConverter.class)
	private LocalTime localTime;
	
	@Column(name="INSTANT_TS")
	@Convert(converter=InstantConverter.class)
	private Instant instant;
	
	@Column(name="ZONE_ID")
	@Convert(converter=ZoneIdConverter.class)
	private ZoneId zoneId;
	
	@Column(name="ZONE_OFFSET")
	@Convert(converter=ZoneOffsetConverter.class)
	private ZoneOffset zoneOffset;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDate getLocalDate() {
		return localDate;
	}
	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}
	public LocalTime getLocalTime() {
		return localTime;
	}
	public void setLocalTime(LocalTime localTime) {
		this.localTime = localTime;
	}
	public Instant getInstant() {
		return instant;
	}
	public void setInstant(Instant instant) {
		this.instant = instant;
	}
	public ZoneId getZoneId() {
		return zoneId;
	}
	public void setZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
	}
	public ZoneOffset getZoneOffset() {
		return zoneOffset;
	}
	public void setZoneOffset(ZoneOffset zoneOffset) {
		this.zoneOffset = zoneOffset;
	}
	@Override
	public String toString() {
		return "Temporal [id=" + id + ", localDate=" + localDate + ", localTime=" + localTime + ", instant=" + instant
				+ ", zoneId=" + zoneId + ", zoneOffset=" + zoneOffset + "]";
	}
	
	
	
}
