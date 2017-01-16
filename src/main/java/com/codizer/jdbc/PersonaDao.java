package com.codizer.jdbc;

import java.util.List;

public interface PersonaDao {

	void insertPersona(Persona persona);
	
	void updatePersona(Persona persona);
	
	void deletePersona(Persona persona);
	
	Persona findPersonaById(long idPersona);
	
	List<Persona> findAllPersonas();
	
	int contadorPersonaPorNombre(Persona persona);
	
	int contadorPersonas();
	
	Persona getPersonaByEmail(Persona persona);
}
