package be.steria.datapoc.IntegrationTests;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import be.steria.datapoc.IntegrationTests.tools.PersonTestTools;
import be.steria.datapoc.IntegrationTests.tools.TestScenario;
import be.steria.datapoc.IntegrationTests.tools.TestTools;
import be.steria.datapoc.client.Address;
import be.steria.datapoc.client.CreatePersonRequest;
import be.steria.datapoc.client.Person;
import be.steria.datapoc.client.PersonService;



public class SimplePersonCreationTest {

	private TestScenario testScenario;
	
	
	
	
	@Before
	public void configureTests() throws Exception {
		
		testScenario =  new TestScenario(3, 0, 8080);
		testScenario.mountScenario();
		
		System.out.println("Preconfiguration finished");
		
	}
	
	@Test
	public void test() throws Exception {
		
		List<Person>testPersons = new ArrayList<Person>();
		
		Person person = new Person();
        person.setIdPerson("ABC");
        person.setFirstName("Pedro");
        person.setLastName("Perez");

        
        Address address = new Address();
        address.setCity("La Paz");
        address.setCountry("Bolivia");
        address.setNumber(92);
        address.setStreet("Avaroa");
    	person.getAddress().add(address);
    	
    	testPersons.add(person);
    	
    	person = new Person();
        person.setIdPerson("XYZ");
        person.setFirstName("John");
        person.setLastName("Wayne");

        
        address = new Address();
        address.setCity("New York");
        address.setCountry("USA");
        address.setNumber(5644);
        address.setStreet("Main");
    	person.getAddress().add(address);
    	
    	address = new Address();
        address.setCity("Chicago");
        address.setCountry("USA");
        address.setNumber(112222);
        address.setStreet("First");
    	person.getAddress().add(address);
    	
    	testPersons.add(person);
		
    	//while(true);
    	
		
    	CreatePersonRequest createPersonRequest = new CreatePersonRequest();
    	createPersonRequest.setPerson(testPersons.get(0));
		
    	PersonService personService = TestTools.getPersonServiceClient(testScenario.getServerPort(1));
		personService.createPerson(createPersonRequest);
		
		createPersonRequest = new CreatePersonRequest();
    	createPersonRequest.setPerson(testPersons.get(1));
		
    	personService = TestTools.getPersonServiceClient(testScenario.getServerPort(2));
		personService.createPerson(createPersonRequest);
		
		
		Thread.sleep(5000);
		
		
		
		
		PersonTestTools.verifyPersonInServer(testScenario, 0, testPersons.get(0));
		PersonTestTools.verifyPersonInServer(testScenario, 1, testPersons.get(0));
		PersonTestTools.verifyPersonInServer(testScenario, 2, testPersons.get(0));
		
		PersonTestTools.verifyPersonInServer(testScenario, 0, testPersons.get(1));
		PersonTestTools.verifyPersonInServer(testScenario, 1, testPersons.get(1));
		PersonTestTools.verifyPersonInServer(testScenario, 2, testPersons.get(1));
		
		
    	
		
	}
	
	
	
	
	@After
	public void closeServices() throws Exception{
		testScenario.unMountScenario();
		
	}

}
