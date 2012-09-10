package be.steria.datapoc.IntegrationTests.tools;

import junit.framework.Assert;

import be.steria.datapoc.client.Address;
import be.steria.datapoc.client.Person;
import be.steria.datapoc.client.QueryPersons;


public class PersonTestTools {
	
	public static void verifyPersonInServer(TestScenario testScenario,
			int serverId, Person person) throws Exception {

		QueryPersons queryPersons = TestTools.getQueryPersonsServiceClient(testScenario.getServerPort(serverId));

		Person personInServer = queryPersons.getPersonById(person.getIdPerson());

		Assert.assertNotNull(personInServer);

		Assert.assertTrue(isSamePersonInfo(personInServer, person));



	}


	public static boolean isSamePersonInfo(Person personA, Person personB) {

		
		/*
		if (personA.getBirthDate() == null) {
			if (personB.getBirthDate() != null)
				return false;
		} else if (personA.getBirthDate().getTime() != personB.getBirthDate().getTime())
			return false;
			*/
		if (personA.getFirstName() == null) {
			if (personB.getFirstName() != null)
				return false;
		} else if (!personA.getFirstName().equals(personB.getFirstName()))
			return false;
		if (personA.getIdPerson() == null) {
			if (personB.getIdPerson() != null)
				return false;
		} else if (!personA.getIdPerson().equals(personA.getIdPerson()))
			return false;
		if (personA.getLastName() == null) {
			if (personB.getLastName() != null)
				return false;
		} else if (!personA.getLastName().equals(personB.getLastName()))
			return false;
		
		if (personA.getAddress().size() != personB.getAddress().size())
			return false;
		
		int verifiedAddresses = 0;
		
		for (Address addressA : personA.getAddress())
			if (!isAddressRegistered(personB, addressA))
				return false;
			else
				verifiedAddresses++;
		
		if (verifiedAddresses!=personA.getAddress().size())
			return false;
		
		return true;
	}
	
	
	private static boolean isAddressRegistered(Person person, Address address) {
		for (Address personAddress : person.getAddress())
			if (isSameAddressInfo(personAddress, address))
				return true;
		return false;
	}
	
	
	private static boolean isSameAddressInfo(Address addressA, Address addressB) {
		if (addressA.getCity() == null) {
			if (addressB.getCity() != null)
				return false;
		} else if (!addressA.getCity().equals(addressB.getCity()))
			return false;
		if (addressA.getCountry() == null) {
			if (addressB.getCountry() != null)
				return false;
		} else if (!addressA.getCountry().equals(addressB.getCountry()))
			return false;
		if (addressA.getNumber() != addressB.getNumber())
			return false;
		if (addressA.getPostalCode() == null) {
			if (addressB.getPostalCode() != null)
				return false;
		} else if (!addressA.getPostalCode().equals(addressB.getPostalCode()))
			return false;
		if (addressA.getStreet() == null) {
			if (addressB.getStreet() != null)
				return false;
		} else if (!addressA.getStreet().equals(addressB.getStreet()))
			return false;
		return true;
	}


}
