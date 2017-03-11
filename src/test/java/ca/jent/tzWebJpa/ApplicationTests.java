package ca.jent.tzWebJpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import ca.jent.tzWebJpa.configuration.DataStoreJpaConfiguration;
import ca.jent.tzWebJpa.entities.Temporal;
import ca.jent.tzWebJpa.services.TemporalService;

/**
 * Using @Transactional so that all test data will be rollback at end 
 * of each test methods.
 * Using same configuration as the application i.e. H2 with JPA/Hibernate
 * @author jraymond
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes=DataStoreJpaConfiguration.class)
@Transactional
public class ApplicationTests {

	@Autowired
	private TemporalService temporalService;
	
	/**
	 * Simply check if the one insert from h2.data.sql file
	 * has been executed (@see DataStoreJpaConfiguration.class)
	 */
	@Test
	public void basicQueryCount() {
		List<Temporal> list = temporalService.getTemporals();
		assertNotNull(list);
		assertEquals(2, list.size());
	}
	
	/**
	 * Query the Temporal entry inserted by the h2.data.sql file
	 */
	@Test
	public void verifyTemporalEntry() {
		Temporal temporal = temporalService.getTemporal(1000L);
		assertNotNull(temporal);
		
		assertEquals(LocalDate.of(1945, 2, 16), temporal.getLocalDate());
		assertEquals(LocalTime.of(3, 30, 30), temporal.getLocalTime());
		ZonedDateTime edmontonDateTime = ZonedDateTime.of(2000, 5, 22, 12, 30, 40, 0, ZoneId.of("America/Edmonton"));
		Instant instant = edmontonDateTime.toInstant();
		assertEquals(instant, temporal.getInstant());
		
		assertEquals(ZoneId.of("America/Edmonton"), temporal.getZoneId());
		
		// Note: ZoneOffset may actually "vary" within a ZoneId (for region/city having DST) so
		//       the ZoneOffset really depend on 1. the "point on the timeline" (i.e. Instant)
		//       and 2. if the ZoneId has DST rules which is the case for "America/Edmonton"
		ZoneOffset zoneOffset = ZoneId.of("America/Edmonton").getRules().getOffset(instant);
		assertEquals(zoneOffset, temporal.getZoneOffset());

		// BEST PRACTICE:  always use ZoneId over ZoneOffset when possible. 
		//                 never use ZoneOffset if you need to support Daylight Saving Time (as the offset varies)
		
	}
	
	/**
	 * Verify our service layer can Save a Temporal entity.
	 */
	@Test
	public void verifySavingTemporal() {
		Temporal temporal = new Temporal();
		temporal.setLocalDate(LocalDate.now());
		temporal.setLocalTime(LocalTime.now());
		Instant instant = Instant.now();
		temporal.setInstant(instant);
		temporal.setZoneId(ZoneId.of("America/Edmonton"));
		temporal.setZoneOffset(ZoneId.of("America/Edmonton").getRules().getOffset(instant));
		Temporal t = temporalService.save(temporal);
		
		// fetch it from db
		Temporal entity = temporalService.getTemporal(t.getId());
		
		assertEquals(temporal.getLocalDate(), entity.getLocalDate());
		assertEquals(temporal.getLocalTime(), entity.getLocalTime());
		assertEquals(temporal.getInstant(), entity.getInstant());
		assertEquals(temporal.getZoneId(), entity.getZoneId());
		assertEquals(temporal.getZoneOffset(), entity.getZoneOffset());
	}
	
	@Test
	public void verifyUpdatingTemporal() {
		Temporal temporal = new Temporal();
		temporal.setLocalDate(LocalDate.now());
		temporal.setLocalTime(LocalTime.now());
		Instant instant = Instant.now();
		temporal.setInstant(instant);
		temporal.setZoneId(ZoneId.of("America/Edmonton"));
		temporal.setZoneOffset(ZoneId.of("America/Edmonton").getRules().getOffset(instant));
		Temporal t = temporalService.save(temporal);
		// change values on it
		t.setLocalDate(LocalDate.of(1945, 9, 2)); // end of WWII
		temporalService.save(t);
		
		// fetch it from db
		Temporal entity = temporalService.getTemporal(t.getId());
		
		assertEquals(LocalDate.of(1945, 9, 2), entity.getLocalDate());
		
	}
	
	/**
	 * In order to test Daylight Saving Time (DST), we first need 
	 * to choose a ZoneId that implement DST.
	 * The ZoneId "America/Edmonton" does implement DST.
	 * For year 2017 in "America/Edmonton" the DST boundary are as follow:
	 * 
	 * - The GAP: ----------------------------------------------------------------
	 * 2017-03-12 02:00:00 becomes 2017-03-12 03:00:00  (we consider this boundary 
	 * as the "gap" as there is one hour "missing" or "not existing"; For instance, 
	 * 2017-03-12 02:15:30 does not exist for that timezone) 
	 * 
	 * - The REPEAT:  ------------------------------------------------------------
	 * 2017-11-05 02:00:00 becomes 2017-11-05 01:00:00  (we consider this boundary
	 * as the "repeat" as there is one hour "repeated twice"  i.e. from 1AM to 2AM
	 * it occurs twice.  Note the first 1AM to 2AM is considered as Pacific 
	 * Daylight-Saving Time (PDT) while the second (the repeat) from 1AM to 2AM is 
	 * considered as Pacific Standard Time (PST)
	 * 
	 * see https://www.timeanddate.com/time/change/canada/edmonton?year=2017
	 * 
	 * The test will create Temporals containing an Instant (point on the timeline) that
	 * falls just before the GAP, within the GAP, and after the GAP (we will see
	 * that within the GAP and after the GAP both fall within DST).  Other Temporals will
	 * test the REPEAT i.e. during the first 1AM to 2AM, during the second (repeat) 1AM
	 * to 2AM.
	 * 
	 * Creating those Instant should be done using a ZoneId that do not have DST.  Although,
	 * UTC is not a timezone, java.time.Instant is a representation of UTC anyway so we will 
	 * use that.  Now we need to figure out what would be the Instant corresponding to
	 * 2017-03-12 01:59:59 [America/Edmonton] (in other word, what is the UTC value for
	 * such date/time/zone?  It should be: 2017-03-12 07:59:59 [UTC]
	 *   2017-03-12 08:59:59 [UTC]  -->  2017-03-12 01:59:59 [America/Edmonton]
	 *   2017-03-12 09:00:00 [UTC]  -->  2017-03-12 03:00:00 [America/Edmonton]
	 *   2017-03-12 09:30:00 [UTC]  -->  2017-03-12 03:30:00 [America/Edmonton]
	 *   -- so from 02:00:00 to 02:59:59 it does not exist; one hour forwarded 
	 *  
	 *   2017-11-05 08:00:00 [UTC]  -->  2017-11-05 01:00:00 [America/Edmonton]
	 *   2017-11-05 09:00:00 [UTC]  -->  2017-11-05 01:00:00 [America/Edmonton]
	 *   2017-11-05 10:00:00 [UTC]  -->  2017-11-05 02:00:00 [America/Edmonton]
	 *   -- so 01:00:00 to 01:59:59 is repeated twice. 
	 *   
	 * Since we essentially have 6 date/time to test, we will have 6 Temporal entity
	 * that we will also store into the database and retrieve it and check that the
	 * retrieve date/time in America/Edmonton zone has the expected values.
	 * (off course we don't have to do that to test daylight saving time but it is
	 * to show the storage in the database and retrieving it does not change the "time".)  
	 */
	@Test
	public void verifyDaylightSavingTimeTemporal() {
		
		// create ZonedDateTime from UTC (although UTC is not a TimeZone) but it shows that we are 
		// creating a date/time not specific to America/Edmonton
		ZonedDateTime gap08_59_59_UTC = ZonedDateTime.of(2017, 3, 12, 8, 59, 59, 0, ZoneId.of("UTC"));
		ZonedDateTime gap09_00_00_UTC = ZonedDateTime.of(2017, 3, 12, 9, 0, 0, 0, ZoneId.of("UTC"));
		ZonedDateTime gap09_30_00_UTC = ZonedDateTime.of(2017, 3, 12, 9, 30, 0, 0, ZoneId.of("UTC"));

		// Create the Temporal using UTC-based date/time
		Temporal t08_59_59_UTC = new Temporal();
		t08_59_59_UTC.setLocalDate(gap08_59_59_UTC.toLocalDate());
		t08_59_59_UTC.setLocalTime(gap08_59_59_UTC.toLocalTime());
		t08_59_59_UTC.setInstant(gap08_59_59_UTC.toInstant());
		t08_59_59_UTC.setZoneId(ZoneId.of("UTC"));
		t08_59_59_UTC.setZoneOffset(ZoneId.of("UTC").getRules().getOffset(gap08_59_59_UTC.toInstant()));
		
		Temporal t09_00_00_UTC = new Temporal();
		t09_00_00_UTC.setLocalDate(gap09_00_00_UTC.toLocalDate());
		t09_00_00_UTC.setLocalTime(gap09_00_00_UTC.toLocalTime());
		t09_00_00_UTC.setInstant(gap09_00_00_UTC.toInstant());
		t09_00_00_UTC.setZoneId(ZoneId.of("UTC"));
		t09_00_00_UTC.setZoneOffset(ZoneId.of("UTC").getRules().getOffset(gap09_00_00_UTC.toInstant()));
		
		Temporal t09_30_00_UTC = new Temporal();
		t09_30_00_UTC.setLocalDate(gap09_30_00_UTC.toLocalDate());
		t09_30_00_UTC.setLocalTime(gap09_30_00_UTC.toLocalTime());
		t09_30_00_UTC.setInstant(gap09_30_00_UTC.toInstant());
		t09_30_00_UTC.setZoneId(ZoneId.of("UTC"));
		t09_30_00_UTC.setZoneOffset(ZoneId.of("UTC").getRules().getOffset(gap09_30_00_UTC.toInstant()));
		
		// Save to database and fetch the inserted entity so that we can get the identifier (ID)
		Temporal t1 = temporalService.save(t08_59_59_UTC);
		Temporal t2 = temporalService.save(t09_00_00_UTC);
		Temporal t3 = temporalService.save(t09_30_00_UTC);
		
		// Fetch from Database
		Temporal entity08_59_59_UTC = temporalService.getTemporal(t1.getId());
		Temporal entity09_00_00_UTC = temporalService.getTemporal(t2.getId());
		Temporal entity09_30_00_UTC = temporalService.getTemporal(t3.getId());
		
		// Lets print few date/time for our human eyes.  The last date/time printed on each line came from database.
		System.out.println("1. " + gap08_59_59_UTC.withZoneSameInstant(ZoneId.of("UTC")) + "\t\t" 
		                         + gap08_59_59_UTC.withZoneSameInstant(ZoneId.of("America/Edmonton")) + "\t\t"
		                         + ZonedDateTime.ofInstant(entity08_59_59_UTC.getInstant(), ZoneId.of("America/Edmonton")));

		System.out.println("2. " + gap09_00_00_UTC.withZoneSameInstant(ZoneId.of("UTC")) + "\t\t" 
                + gap09_00_00_UTC.withZoneSameInstant(ZoneId.of("America/Edmonton")) + "\t\t"
                + ZonedDateTime.ofInstant(entity09_00_00_UTC.getInstant(), ZoneId.of("America/Edmonton")));

		System.out.println("3. " + gap09_30_00_UTC.withZoneSameInstant(ZoneId.of("UTC")) + "\t\t" 
                + gap09_30_00_UTC.withZoneSameInstant(ZoneId.of("America/Edmonton")) + "\t\t"
                + ZonedDateTime.ofInstant(entity09_30_00_UTC.getInstant(), ZoneId.of("America/Edmonton")));
		
		// Pacific Standard Time (PST) period (just before the GAP --> 2017-03-12 01:59:59-07:00[America/Edmonton])
		assertEquals(ZonedDateTime.of(2017, 3, 12, 1, 59, 59, 0, ZoneId.of("America/Edmonton")).toInstant(), entity08_59_59_UTC.getInstant());
		// Pacific Daylight-Saving Time (PDT) period (One second later, the clock jump one hour in America/Edmonton) but not for UTC.
		assertEquals(ZonedDateTime.of(2017, 3, 12, 3, 0, 0, 0, ZoneId.of("America/Edmonton")).toInstant(), entity09_00_00_UTC.getInstant());
		assertEquals(ZonedDateTime.of(2017, 3, 12, 3, 30, 0, 0, ZoneId.of("America/Edmonton")).toInstant(), entity09_30_00_UTC.getInstant());

	}

}










