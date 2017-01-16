package com.codizer.jdbc;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Component;

@Component
public class PersonaDaoImpl implements PersonaDao {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		/**
		 * No es común que se utilicen las 2 platillas, sin embargo si es posible
		 * la diferencia es el manejo de parámetros por indice o por nombre
		 */
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	// Query con parametros por nombre
	// Omitimos la PK ya que es autoincrementable
	private static final String SQL_INSERT_PERSONA = 
			"INSERT INTO PERSONA (nombre, ape_paterno, ape_materno, email) VALUES (:nombre, :apePaterno, :apeMaterno, :email)";
	
	// Query con parametros por indice
	// private static final String SQL_INSERT_PERSONA = 
	// "INSERT INTO PERSONA (username, password, fullname, email, update_by_email) VALUES (?, ?, ?, ?, ?)";
	
	// Parametros por nombre
	private static final String SQL_UPDATE_PERSONA = 
			"UPDATE PERSONA SET nombre = :nombre, ape_paterno = :apePaterno, ape_materno = :apeMaterno, email = :email WHERE id_persona = :idPersona";
	
	private static final String SQL_DELETE_PERSONA = 
			"DELETE FROM PERSONA WHERE id_persona = :idPersona";
	
	private static final String SQL_SELECT_PERSONA =
			"SELECT id_persona, nombre, ape_paterno, ape_materno, email FROM PERSONA";
	
	// Parametros pro indice
	private static final String SQL_SELECT_PERSONA_BY_ID =
			SQL_SELECT_PERSONA + " WHERE id_persona = ?";
	
	@Override
	public void insertPersona(Persona persona) {
		SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(persona);
		this.namedParameterJdbcTemplate.update(SQL_INSERT_PERSONA, parameterSource);
	}

	@Override
	public void updatePersona(Persona persona) {
		SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(persona);
		this.namedParameterJdbcTemplate.update(SQL_UPDATE_PERSONA, parameterSource);
	}

	@Override
	public void deletePersona(Persona persona) {
		
	}

	@Override
	public Persona findPersonaById(long idPersona) {
		Persona persona = null;
		
		try {
			// Utilizamos la clase PersonaRowMapper
			persona = jdbcTemplate.queryForObject(SQL_SELECT_PERSONA_BY_ID, 
					new PersonaRowMapper(), idPersona);
		} catch (EmptyResultDataAccessException e) {
			persona = null;
		}
		
		return persona;
		
		// Esta es otra forma sin utilizar la clase PersonaRowMapper
		// BeanPropertyRowMapper<Persona> personaRowMapper = BeanPropertyRowMapper.newInstance(Persona.class);
		// return jdbcTemplate.queryForObject(SQL_SELECT_PERSONA_BY_ID, personaRowMapper, idPersona);
	}

	@Override
	public List<Persona> findAllPersonas() {
		// Esta consulta es equivalente
		// String sql = "SELECT * FROM PERSONA";
		RowMapper<Persona> personaRowMapper = ParameterizedBeanPropertyRowMapper.newInstance(Persona.class);
		return this.jdbcTemplate.query(SQL_SELECT_PERSONA, personaRowMapper);
	}

	@Override
	public int contadorPersonaPorNombre(Persona persona) {
		String sql = "SELECT COUNT(*) FROM PERSONA WHERE nombre = :nombre";
		
		// Permite evitar crear un MAP de parametros y utilizar directamente el objeto persona
		// Los atributos que coincidan con el nombre de los parametros por nombre del query
		// seran utilizados y proporcionados como atributos al query
		
		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(persona);
		
		// Unicamente retorna un valor el método queryForInt
		return this.namedParameterJdbcTemplate.queryForInt(sql, namedParameters);
	}

	@Override
	public int contadorPersonas() {
		String sql = "SELECT COUNT(*) FROM PERSONA";
		return this.jdbcTemplate.queryForInt(sql);
		
		// Esta es otra opción si no tuvieramos jdbcTemplate
		// return this.namedParameterJdbcTemplate.getJdbcOperations().queryForInt(sql);
	}

	@Override
	public Persona getPersonaByEmail(Persona persona) {
		String sql = "SELECT * FROM PERSONA WHERE email = :email";
		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(persona);
		
		// Si no se tiene el objeto RowMapper, se puede utilizar la siguiente linea para crear este objeto
		// RowMapper<Persona> personaRowMapper = ParameterizedBeanPropertyRowMapper.newInstance(Persona.class);
		
		return this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters, new PersonaRowMapper());
	}

}
