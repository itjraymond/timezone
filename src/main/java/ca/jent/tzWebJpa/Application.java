package ca.jent.tzWebJpa;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ca.jent.tzWebJpa.entities.Temporal;
import ca.jent.tzWebJpa.services.TemporalService;

/**
 * A quick and "dirty" project setup to do some testing for java.time package (Java 8).
 * see src/test/java/  for actual testing of saving date/time/zone and DST.
 * Changing the default Timezone of the OS or JVM has no effect whatsoever on the date/time/zone 
 * stored into the database as databases will always internally store a timestamp in UTC (otherwise
 * it would not make sense :-).  For instance, the JDBC Driver will always and need to assume
 * the default timezone of your system/JVM to translate into UTC (unless you change this by providing
 * a Calendar with different timezone).  Once stored in UTC, changing
 * your default timezone won't change the date/time value as it will translate it into your
 * new timezone with the correct date/time for that timezone.  It will appear different but really 
 * represent the same moment in time (i.e. same point on the timeline). 
 * We can view UTC as the universal clock which represent a specific instant on the timeline, independant
 * of timezone and DST.
 * Handling of Timezone and DST are done by the Java API (java.time).  Only ZoneId has rules for DST (not ZoneOffset)
 * Best Practice:  Always be explicit about your timezone (i.e. don't use the default of your system
 * but rather use something like ZoneId.of("America/Edmonton");  or ZoneId.of("Asia/Tokyo");
 * Best Practice:  Always try to work with Instant and not ZonedDateTime or LocalDateTime. Those are
 * used when you need to "present to the human eye" the date/time.  The machine does not care about
 * seeing the date/time in a specific timezone;  but cares about specific point on the timeline (Instant).
 * Best Practice: Try to always work with ZoneId and not ZoneOffset (when dealing with DST)
 * @author jraymond
 *
 */
@SpringBootApplication // mouse over to see all annotation it automatically include
@RestController
public class Application {
	
	@Autowired
	private TemporalService temporalService;
	
	
	@GetMapping("/temporal/{id}")
	public Temporal getTemporal(@PathVariable("id") Long id) {
		return temporalService.getTemporal(id);
	}
	
	@GetMapping("/temporal/all")
	public List<Temporal> getAll() {
		return temporalService.getTemporals();
	}
	
	@GetMapping("/temporal/create")
	public Temporal create() {
		Temporal temporal = new Temporal();
		temporal.setLocalDate(LocalDate.now());
		temporal.setLocalTime(LocalTime.now());
		temporal.setInstant(Instant.now());
		temporal.setZoneId(ZoneId.systemDefault());
		temporal.setZoneOffset(ZonedDateTime.now().getOffset());
		
		Temporal entity = temporalService.save(temporal);
		return entity;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
