package hu.webuni.airport.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.airport.model.Airport;
import hu.webuni.airport.model.Flight;
import hu.webuni.airport.repository.AirportRepository;
import hu.webuni.airport.repository.FlightRepository;

@Service
public class AirportService {

//	private Map<Long, Airport> airports = new HashMap<>();
//
//	{
//		airports.put(1L, new Airport(1, "abc", "XYZ"));
//		airports.put(2L, new Airport(2, "def", "UVW"));
//		airports.put(3L, new Airport(3, "ghi", "RST"));
//
//	}

//	@PersistenceContext
//	EntityManager em;

	@Autowired
	AirportRepository airportRepository;

	@Autowired
	FlightRepository flightRepository;

	@Transactional
	public Airport save(Airport airport) {
		checkUniqueIata(airport.getIata(), null);
//		em.persist(airport);
		return airportRepository.save(airport);
	}

	@Transactional
	public Airport update(Airport airport) {
		checkUniqueIata(airport.getIata(), airport.getId());
		if (airportRepository.existsById(airport.getId()))
			return airportRepository.save(airport);
		else
			throw new NoSuchElementException();
//		return em.merge(airport);

	}

	public List<Airport> findAll() {
//		return em.createQuery("SELECT a FROM Airport a", Airport.class).getResultList();
		return airportRepository.findAll();
	}

	public Optional<Airport> findById(long id) {
//		return em.find(Airport.class, id);
		return airportRepository.findById(id);
	}

	@Transactional
	public void delete(long id) {
//		em.remove(findById(id));
		airportRepository.deleteById(id);
	}

	private void checkUniqueIata(String iata, Long id) {
		boolean forUpdate = id != null;
//		TypedQuery<Long> query = em
//				.createNamedQuery(forUpdate ? "Airport.countByIataAndIdNotIn" : "Airport.countByIata", Long.class)
//				.setParameter("iata", iata);
//		if (forUpdate)
//				query.setParameter("id", id);
//		Long count = query.getSingleResult();
		Long count = forUpdate ? airportRepository.countByIataAndIdNot(iata, id) : airportRepository.countByIata(iata);

		if (count > 0)
			throw new NonUniqueIataException(iata);
	}

	@Transactional
	public void createFlight() {
		Flight flight = new Flight();
		flight.setFlightNumber("WZ6054");
		flight.setTakeoff(airportRepository.findById(1L).get());
		flight.setLanding(airportRepository.findById(4L).get());
		flight.setTakeoffTime(LocalDateTime.of(2021, 4, 10, 10, 0, 0));
		flightRepository.save(flight);

	}

}
