package ca.jent.tzWebJpa.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.jent.tzWebJpa.entities.Temporal;

@Repository
public interface TemporalRepository extends JpaRepository<Temporal, Long> {

	List<Temporal> findTemporalByLocalDate(LocalDate localDate);
}
