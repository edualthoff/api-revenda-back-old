package br.ml.api.buscas.mercadolivre;

import java.time.Duration;
import java.time.Instant;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchedulingTime {

	@Field(type = FieldType.Long)
	private long hours;
	@Setter(value = AccessLevel.NONE)
	@Field(type = FieldType.Long)
	private long minutes;
	@Field(type=FieldType.Date, format=DateFormat.date_optional_time)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
	private Instant nextDate;

	public SchedulingTime(int hours, int minutes) {
		super();
		this.hours = hours;
		this.minutes = minutes;
	}

	public SchedulingTime() {
		super();
	}	
	
	public Instant nextDateUpdateOrCreate() {
		Instant next;
		if(this.nextDate == null) {
			return next = this.nextDate = Instant.now();
		}
		if(this.hours != 0L) {
			next = this.nextDate.plus(Duration.ofHours(this.hours));
		}
		next = this.nextDate.plus(Duration.ofMinutes(this.minutes));
		return next;
	}

	public void setMinutes(long minutes) {
		this.minutes = minutes == 0L? 20L : minutes;
	}
}
