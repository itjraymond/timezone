-- Note: JDBC Driver (which will execute the insert statements below) assumes 
--       my local ZoneId of 'America/Edmonton' and thus all date/time
--       below are according to that ZoneId and not UTC.


-- Pacific Daylight Time (PDT)
-- Note: INSTANT_TS inserted below has date [May 2000] falling within Pacific Daylight-Saving Time (PDT) and thus we hardcode -06:00 for the corresponding ZONE_OFFSET
insert into TEMPORALS (ID, LOCAL_DATE, LOCAL_TIME, INSTANT_TS, ZONE_ID, ZONE_OFFSET)
values (1000, 
        TO_DATE('1945-02-16','YYYY-MM-DD'), 
        TO_DATE('03:30:30', 'HH24:MI:ss'),
        TO_DATE('2000-05-22 12:30:40', 'YYYY-MM-DD HH24:MI:ss'),
        'America/Edmonton', '-06:00');


-- Pacific Standard Time (PST)        
-- Note: INSTANT_TS inserted below [2010-03-14] is one second just before Daylight Saving Time and thus has offset of -07:00        
insert into TEMPORALS (ID, LOCAL_DATE, LOCAL_TIME, INSTANT_TS, ZONE_ID, ZONE_OFFSET)
values (1001, 
        TO_DATE('1945-02-16','YYYY-MM-DD'), 
        TO_DATE('03:30:30', 'HH24:MI:ss'),
        TO_DATE('2010-03-14 01:59:59', 'YYYY-MM-DD HH24:MI:ss'),
        'America/Edmonton', '-07:00');
        