package ca.jent.tzWebJpa.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.jent.tzWebJpa.entities.Temporal;
import ca.jent.tzWebJpa.repositories.TemporalRepository;

@Service
@Transactional
public class TemporalService {

	@Autowired
	private TemporalRepository repository;
	
	public Temporal getTemporal(Long id) {
		return repository.findOne(id);
	}
	
	public List<Temporal> getTemporals() {
		return repository.findAll();
	}
	
	public Temporal save(Temporal temporal) {
		return repository.save(temporal);
	}
	
	public void delete(Long id) {
		repository.delete(id);
	}
	
	public List<Temporal> findTemporalByLocalDate(LocalDate localDate) {
		return repository.findTemporalByLocalDate(localDate);
	}
}
