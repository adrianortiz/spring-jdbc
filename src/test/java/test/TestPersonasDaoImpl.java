package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.codizer.jdbc.Persona;
import com.codizer.jdbc.PersonaDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:datasource-test.xml", "classpath:applicationContext.xml"})
public class TestPersonasDaoImpl {
	
	private static Log logger = LogFactory.getLog("TestPersonasDaoImpl");
	
	@Autowired
	private PersonaDao personaDao;
	
	@Test
	@Ignore
	public void deberiaMostrarPersonas() {
		try {
			System.out.println();
			logger.info("Inicio del test deberiaMostrarPersonas");
			
			List<Persona> personas = personaDao.findAllPersonas();
			
			int contadorPersonas = 0;
			
			for (Persona persona : personas) {
				logger.info("Persona: " + persona);
				contadorPersonas++;
			}
			
			// Segun el numero de personas recuperadas, deberia ser el mismo de la tabla
			assertEquals(contadorPersonas, personaDao.contadorPersonas());
			
		} catch (Exception e) {
			logger.error("Error JDBC: " + e);
		}
	}
	
	@Test
	@Ignore
	public void testContarPersonasPorNombre() {
		try {
			System.out.println();
			logger.info("START TEST: testContarPersonasPorNombre()");
			
			String nombre ="Juan";
			Persona personaEjemplo = new Persona();
			personaEjemplo.setNombre(nombre);
			
			int noPersonasEncontradas = personaDao.contadorPersonaPorNombre(personaEjemplo);
			
			logger.info("Personas encontradas por nombre '" + nombre + "': " + noPersonasEncontradas);
			assertEquals(2, noPersonasEncontradas);
			
			logger.info("ENDS TEST: testContarPersonasPorNombre()");
			
		} catch (Exception e) {
			logger.error("Error JDBC: ", e);
		}
	}
	
	@Test
	@Ignore
	public void deberiaEncontrarPersonaPorId() {
		try {
			System.out.println();
			logger.info("START TEST: deberiaEncontrarPersonaPorId()");
			
			int idPersona = 1;
			Persona persona = personaDao.findPersonaById(idPersona);
			
			// Segun la persona recuperada, deberia ser la misma que el registro 1
			assertEquals("Admin", persona.getNombre());
			
			// Imprimimos todo el objeto
			logger.info("Persona recuperada (id=" + idPersona + "): " + persona);

			logger.info("ENDS TEST: deberiaEncontrarPersonaPorId()");
		} catch (Exception e) {
			logger.error("Error JDBC: ", e);
		}
	}
	
	@Test
	@Ignore
	public void deberiaInsertarPersona() {
		try {
			System.out.println();
			logger.info("START TEST: deberiaInsertarPersona()");
			
			// El script de datos tiene 3 registros
			assertEquals(3, personaDao.contadorPersonas());
			
			Persona persona = new Persona();
			persona.setNombre("Remedios");
			persona.setApePaterno("Ortiz");
			persona.setApeMaterno("Martinez");
			persona.setEmail("reme@gmail.com");
			
			personaDao.insertPersona(persona);
			
			// Recuperamos a la persona recien insertada por su Email
			persona = personaDao.getPersonaByEmail(persona);
			logger.info("PERSON INSERTED: " + persona);
			
			// Deberia haber ya 4 personas
			assertEquals(4, personaDao.contadorPersonas());
			
			logger.info("ENDS TEST: deberiaInsertarPersona()");
			
		} catch (Exception e) {
			logger.error("Error JDBC ", e);
		}
	}
	
	@Test
	@Ignore
	public void deberiaActualizarPersona() {
		try {
			System.out.println();
			logger.info("START TEST deberiaActualizarPersona()");
			
			int idPersona = 1;
			
			Persona persona = personaDao.findPersonaById(idPersona);
			
			logger.info("Persona a modificar (id=" + idPersona + "): " + persona);
			
			// Atualizamos nombre y apellido paterno
			persona.setNombre("Administrador");
			persona.setApePaterno("Sistemas");
			
			personaDao.updatePersona(persona);
			
			// voldemos a leer el usuario
			persona = personaDao.findPersonaById(idPersona);
			
			// segun la persona recuperada, deberia ser la misma que el registro 1
			assertEquals("Administrador", persona.getNombre());
			
			// Imprimimos todo el objeto
			logger.info("Persona modificada (id=" + idPersona + "): " + persona);
			
			logger.info("ENDS TEST: deberiaActualizarPersona()");
			
		} catch (Exception e) {
			logger.error("Error JDBC ", e);
		}
	}
	
	@Test
	public void deberiaEliminarPersona() {
		try {
			System.out.println();
			logger.info("START TEST: deberiaEliminarPersona()");
			
			// Buscamos eliminar la persona con id 2
			int idPersona = 2;
			
			Persona persona = personaDao.findPersonaById(idPersona);
			logger.info("Persona a eliminar (id=" + idPersona + "): " + persona);
			
			// Eliminamos la persona recuperada
			personaDao.deletePersona(persona);
			
			persona = personaDao.findPersonaById(idPersona);
			
			// Deberia de regresar nulo al buscar la persona 2
			assertNull(persona);
			
			// Imprimos todo el objeto
			logger.info("Nuevo listado de personas:");
			
			List<Persona> personas = personaDao.findAllPersonas();
			
			int contadorPersonas = 0;
			for (Persona persona2 : personas) {
				logger.info("Persona: " + persona2);
				contadorPersonas++;
			}
			
			// Segun el numero de personas recuperadas, deberia ser el mismo de la tabla
			assertEquals(contadorPersonas, personaDao.contadorPersonas());
			
			logger.info("ENDS TEST: deberiaEliminarPersona()");
			System.out.println();
		} catch (Exception e) {
			logger.error("Error JDBC: ", e);
		}
	}

}
