package hu.webuni.airport.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.airport.dto.AirportDto;
import hu.webuni.airport.mapper.AirportMapper;
import hu.webuni.airport.service.AirportService;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

	@Autowired
	AirportService airportService;
	
	@Autowired
	AirportMapper airportMapper;

	@GetMapping
	public List<AirportDto> getAll() {
		return airportMapper.airportsToDtos(airportService.findAll());
	}

//	@GetMapping("/{id}")
//	public AirportDto getById(@PathVariable long id) {
//		AirportDto airportDto = airports.get(id);
//		if (airportDto != null)
//			return airportDto;
//		else
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//	}
//
//	@PostMapping
//	public AirportDto createAirport(@RequestBody @Valid AirportDto airportDto) {
//		checkUniqueIata(airportDto.getIata());
//		airports.put(airportDto.getId(), airportDto);
//		return airportDto;
//	}
//
//	@PutMapping("/{id}")
//	public ResponseEntity<AirportDto> modifyAirport(@PathVariable long id, @RequestBody @Valid AirportDto airportDto) {
//		if (!airports.containsKey(id))
//			return ResponseEntity.notFound().build();
//		checkUniqueIata(airportDto.getIata());
//		airportDto.setId(id);
//		airports.put(id, airportDto);
//		return ResponseEntity.ok(airportDto);
//	}
//
//	private void checkUniqueIata(String iata) {
//		Optional<AirportDto> airportWithSameIata = airports.values().stream()
//				.filter(a -> a.getIata().equals(iata))
//				.findAny();
//		if (airportWithSameIata.isPresent())
//			throw new NonUniqueIataException(iata);
//	}
//
//	@DeleteMapping("/{id}")
//	public void deleteAirport(@PathVariable long id) {
//		airports.remove(id);
//	}

}
