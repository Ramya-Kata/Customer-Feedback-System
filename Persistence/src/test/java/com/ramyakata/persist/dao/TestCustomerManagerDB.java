package com.ramyakata.persist.dao;

import java.time.LocalDate;

import com.ramyakata.persist.entity.CustomerInput;
import com.ramyakata.persist.entity.CustomerOutput;
import com.ramyakata.persist.exception.CustomerException;
import com.ramyakata.persist.repo.CustomerUtil;
import com.ramyakata.persist.service.CustomerManager;

import junit.framework.TestCase;

public class TestCustomerManagerDB extends TestCase {

	private CustomerManager customerObj = null;
	private CustomerUtil utilObj = null;
	private String file = "src/main/resources/config.csv";

	protected void setUp() throws Exception {
		customerObj = new CustomerManager(file);
		utilObj = new CustomerUtil();
	}

	protected void tearDown() throws Exception {
		customerObj = null;
		utilObj.closeConnection();

	}

	public void testAddCustomer() {
		try {
			CustomerInput inObj1 = new CustomerInput();
			inObj1.setFirstName("Ramya");
			inObj1.setLastName("Kata");
			inObj1.setEmailAddress("ramyakata@gmail.com");
			inObj1.setDate(LocalDate.of(2024, 5, 30));
			inObj1.setRate(4);
			inObj1.setCustomerID(101);

			CustomerInput inObj2 = new CustomerInput();
			inObj2.setFirstName("Manasa");
			inObj2.setLastName("Kata");
			inObj2.setEmailAddress("manasakata@gmail.com");
			inObj2.setDate(LocalDate.of(2024, 2, 19));
			inObj2.setRate(4);
			inObj2.setCustomerID(102);

			CustomerInput inObj3 = new CustomerInput();
			inObj3.setFirstName("Jyothi");
			inObj3.setLastName("Paladi");
			inObj3.setEmailAddress("jyothipaladi@gmail.com");
			inObj3.setDate(LocalDate.of(2024, 2, 4));
			inObj3.setRate(4);
			inObj3.setCustomerID(103);

			CustomerInput inObj4 = new CustomerInput();
			inObj4.setFirstName("Mallikarjun");
			inObj4.setLastName("Palle");
			inObj4.setEmailAddress("mallipalle@gmail.com");
			inObj4.setDate(LocalDate.of(2024, 4, 23));
			inObj4.setRate(4);
			inObj4.setCustomerID(104);

			CustomerInput inObj5 = new CustomerInput();
			inObj5.setFirstName("Kasi");
			inObj5.setLastName("Varma");
			inObj5.setEmailAddress("varmahasthi@gmail.com");
			inObj5.setDate(LocalDate.of(2024, 8, 22));
			inObj5.setRate(4);
			inObj5.setCustomerID(105);

			// ExpectedResult
			CustomerOutput[] expResult = new CustomerOutput[5];

			// Add customer
			customerObj.addCustomer(inObj1);
			customerObj.addCustomer(inObj2);
			customerObj.addCustomer(inObj3);
			customerObj.addCustomer(inObj4);
			customerObj.addCustomer(inObj5);

			expResult[0] = new CustomerOutput();
			expResult[0].setFirstName("Ramya");
			expResult[0].setLastName("Kata");
			expResult[0].setgmail("ramyakata@gmail.com");
			expResult[0].setDate(LocalDate.of(2024, 5, 30));
			expResult[0].setCustomerID(101); // Ensure this matches the actual ID generated
			expResult[0].setRate(4);

			expResult[1] = new CustomerOutput();
			expResult[1].setFirstName("Manasa");
			expResult[1].setLastName("Kata");
			expResult[1].setgmail("manasakata@gmail.com");
			expResult[1].setDate(LocalDate.of(2024, 2, 19));
			expResult[1].setCustomerID(102); // Adjust as per actual ID generation
			expResult[1].setRate(4);

			expResult[2] = new CustomerOutput();
			expResult[2].setFirstName("Jyothi");
			expResult[2].setLastName("Paladi");
			expResult[2].setgmail("jyothipaladi@gmail.com");
			expResult[2].setDate(LocalDate.of(2024, 2, 4));
			expResult[2].setCustomerID(103); // Adjust as per actual ID generation
			expResult[2].setRate(4);

			expResult[3] = new CustomerOutput();
			expResult[3].setFirstName("Mallikarjun");
			expResult[3].setLastName("Palle");
			expResult[3].setgmail("mallipalle@gmail.com");
			expResult[3].setDate(LocalDate.of(2024, 4, 23));
			expResult[3].setCustomerID(104); // Adjust as per actual ID generation
			expResult[3].setRate(4);

			expResult[4] = new CustomerOutput();
			expResult[4].setFirstName("Kasi");
			expResult[4].setLastName("Varma");
			expResult[4].setgmail("varmahasthi@gmail.com");
			expResult[4].setDate(LocalDate.of(2024, 8, 22));
			expResult[4].setCustomerID(105); // Adjust as per actual ID generation
			expResult[4].setRate(4);

			// ActualResult

			CustomerOutput[] actResult = customerObj.getallCustomers();

			// Compare expected and actual results

			for (int i = 0; i < expResult.length; i++) {
				assertEquals(expResult[i].getCustomerID(), actResult[i].getCustomerID());
				assertEquals(expResult[i].getFirstName(), actResult[i].getFirstName());
				assertEquals(expResult[i].getLatName(), actResult[i].getLatName());
				assertEquals(expResult[i].getGmail(), actResult[i].getGmail());
				assertEquals(expResult[i].getDate(), actResult[i].getDate());
				assertEquals(expResult[i].getRate(), actResult[i].getRate());

			}
		} catch (CustomerException e) {
			System.out.println("CustomerException caught: " + e.getMessage());
		}
	}

	/**public void testGetallCustomers() {
		try {
			CustomerInput inObj6 = new CustomerInput();
			inObj6.setFirstName("Vaishnavi");
			inObj6.setLastName("Manga");
			inObj6.setEmailAddress("vaishmanga@gmail.com");
			inObj6.setDate(LocalDate.of(2024, 12, 12));
			inObj6.setRate(3);
			inObj6.setCustomerID(106);
			CustomerInput inObj7 = new CustomerInput();
			inObj7.setFirstName("Kavya");
			inObj7.setLastName("Inampudi");
			inObj7.setEmailAddress("kavyaInampud@gmail.com");
			inObj7.setDate(LocalDate.of(2024, 12, 4));
			inObj7.setRate(3);
			inObj7.setCustomerID(107);
			try {
				// Add customer
				customerObj.addCustomer(inObj6);
				customerObj.addCustomer(inObj7);

				// ExpectedResult
				// Initialize the expected result with the correct size (currentIndex)
				CustomerOutput[] expResult = new CustomerOutput[2];

				expResult[0] = new CustomerOutput();
				expResult[0].setFirstName("Vaishnavi");
				expResult[0].setLastName("Manga");
				expResult[0].setgmail("vaishmanga@gmail.com");
				expResult[0].setDate(LocalDate.of(2024, 12, 12));
				expResult[0].setCustomerID(106); // Adjust as per actual ID generation
				expResult[0].setRate(3);

				expResult[1] = new CustomerOutput();
				expResult[1].setFirstName("Kavya");
				expResult[1].setLastName("Inampudi");
				expResult[1].setgmail("kavyaInampud@gmail.com");
				expResult[1].setDate(LocalDate.of(2024, 12, 4));
				expResult[1].setCustomerID(107); // Adjust as per actual ID generation
				expResult[1].setRate(3);

				// ActualResult

				CustomerOutput[] actResult = customerObj.getallCustomers();

				// Compare expected and actual results

				for (int i = 0; i < expResult.length; i++) {
					assertEquals(expResult[i].getCustomerID(), actResult[i].getCustomerID());
					assertEquals(expResult[i].getFirstName(), actResult[i].getFirstName());
					assertEquals(expResult[i].getLatName(), actResult[i].getLatName());
					assertEquals(expResult[i].getGmail(), actResult[i].getGmail());
					assertEquals(expResult[i].getDate(), actResult[i].getDate());
					assertEquals(expResult[i].getRate(), actResult[i].getRate());
				}

			} catch (CustomerException e) {
				System.out.println("CustomerException caught: " + e.getMessage());
			}
		} catch (CustomerException e) {
			System.out.println("CustomerException caught: " + e.getMessage());
		}
	}
**/
	public void testGetCustomer() {
		try {
			CustomerInput inObj1 = new CustomerInput();
			inObj1.setFirstName("Harika");
			inObj1.setLastName("Inam");
			inObj1.setEmailAddress("harikai@gmail.com");
			inObj1.setDate(LocalDate.of(2024, 4, 23));
			inObj1.setRate(4);
			inObj1.setCustomerID(108);
			try {
				// Add customer
				customerObj.addCustomer(inObj1);
				// ExpectedResult
				CustomerOutput expResult = new CustomerOutput();
				expResult.setFirstName("Harika");
				expResult.setLastName("Inam");
				expResult.setgmail("harikai@gmail.com");
				expResult.setDate(LocalDate.of(2024, 4, 23));
				expResult.setCustomerID(108); // Ensure this matches the actual ID generated
				expResult.setRate(4);
				// ActualResult
				CustomerOutput actResult = customerObj.getCustomer(108);

				// Compare expected and actual results

				assertEquals(expResult.getCustomerID(), actResult.getCustomerID());
				assertEquals(expResult.getFirstName(), actResult.getFirstName());
				assertEquals(expResult.getLatName(), actResult.getLatName());
				assertEquals(expResult.getGmail(), actResult.getGmail());
				assertEquals(expResult.getDate(), actResult.getDate());
				assertEquals(expResult.getRate(), actResult.getRate());

			} catch (CustomerException e) {
				System.out.println("CustomerException caught: " + e.getMessage());
			}
		} catch (CustomerException e) {
			System.out.println("CustomerException caught: " + e.getMessage());
		}
	}

/**	public void testSortCustomers() {
		try {
			CustomerInput inObj1 = new CustomerInput();
			inObj1.setFirstName("prathhyu");
			inObj1.setLastName("potru");
			inObj1.setEmailAddress("pratyuPotru@gmail.com");
			inObj1.setDate(LocalDate.of(2024, 7, 04));
			inObj1.setCustomerID(109);
			inObj1.setRate(4);

			CustomerInput inObj2 = new CustomerInput();
			inObj2.setFirstName("lachi");
			inObj2.setLastName("sri");
			inObj2.setEmailAddress("srilachi@gmail.com");
			inObj2.setDate(LocalDate.of(2024, 2, 14));
			inObj2.setRate(4);
			inObj2.setCustomerID(110);

			CustomerInput inObj3 = new CustomerInput();
			inObj3.setFirstName("jishnu");
			inObj3.setLastName("mainedi");
			inObj3.setEmailAddress("jishnuminedi@gmail.com");
			inObj3.setDate(LocalDate.of(2024, 4, 19));
			inObj3.setRate(4);
			inObj3.setCustomerID(111);

			// Add customers
			customerObj.addCustomer(inObj1);
			customerObj.addCustomer(inObj2);
			customerObj.addCustomer(inObj3);

			// Expected result after sorting by date
			CustomerOutput[] expResult = new CustomerOutput[3];

			expResult[0] = new CustomerOutput();
			expResult[0].setFirstName("lachi");
			expResult[0].setLastName("sri");
			expResult[0].setgmail("srilachi@gmail.com");
			expResult[0].setDate(LocalDate.of(2024, 2, 14));
			expResult[0].setCustomerID(110);
			expResult[0].setRate(4);

			expResult[1] = new CustomerOutput();
			expResult[1].setFirstName("jishnu");
			expResult[1].setLastName("mainedi");
			expResult[1].setgmail("jishnuminedi@gmail.com");
			expResult[1].setDate(LocalDate.of(2024, 4, 19));
			expResult[1].setCustomerID(111);
			expResult[1].setRate(4);

			expResult[2] = new CustomerOutput();
			expResult[2].setFirstName("prathhyu");
			expResult[2].setLastName("potru");
			expResult[2].setgmail("pratyuPotru@gmail.com");
			expResult[2].setDate(LocalDate.of(2024, 7, 04));
			expResult[2].setCustomerID(109);
			expResult[2].setRate(4);

			// Get actual result
			CustomerOutput[] actResult = customerObj.sortCustomers();

			// Assert results
			for (int i = 0; i < expResult.length; i++) {
				assertEquals(expResult[i].getCustomerID(), actResult[i].getCustomerID());
				assertEquals(expResult[i].getFirstName(), actResult[i].getFirstName());
				assertEquals(expResult[i].getLatName(), actResult[i].getLatName());
				assertEquals(expResult[i].getGmail(), actResult[i].getGmail());
				assertEquals(expResult[i].getDate(), actResult[i].getDate());
				assertEquals(expResult[i].getRate(), actResult[i].getRate());
			}
		} catch (CustomerException e) {
			System.out.println("CustomerException caught: " + e.getMessage());
		}
	}

	public void testRemoveCustomer() {
		try {
			CustomerInput inObj1 = new CustomerInput();
			inObj1.setFirstName("shankar");
			inObj1.setLastName("jammala");
			inObj1.setEmailAddress("shankarjammala@gmail.com");
			inObj1.setDate(LocalDate.of(2024, 4, 7));
			inObj1.setRate(4);
			inObj1.setCustomerID(112);
			CustomerInput inObj2 = new CustomerInput();
			inObj2.setFirstName("taehyung");
			inObj2.setLastName("v");
			inObj2.setEmailAddress("taehyung@gmail.com");
			inObj2.setDate(LocalDate.of(2024, 12, 30));
			inObj2.setRate(4);
			inObj2.setCustomerID(113);

			// Add customers
			customerObj.addCustomer(inObj1);
			customerObj.addCustomer(inObj2);

			// Expected result
			CustomerOutput[] expResult = new CustomerOutput[1];

			expResult[0] = new CustomerOutput();
			expResult[0].setFirstName("taehyung");
			expResult[0].setLastName("v");
			expResult[0].setgmail("taehyung@gmail.com");
			expResult[0].setDate(LocalDate.of(2024, 12, 30));
			expResult[0].setCustomerID(113);
			expResult[0].setRate(4);

			// Remove customer
			customerObj.removeCustomer(112);

			// Get actual result
			CustomerOutput[] actResult = customerObj.getallCustomers();

			// Assert results
			for (int i = 0; i < expResult.length; i++) {
				assertEquals(expResult[i].getCustomerID(), actResult[i].getCustomerID());
				assertEquals(expResult[i].getFirstName(), actResult[i].getFirstName());
				assertEquals(expResult[i].getLatName(), actResult[i].getLatName());
				assertEquals(expResult[i].getGmail(), actResult[i].getGmail());
				assertEquals(expResult[i].getDate(), actResult[i].getDate());
				assertEquals(expResult[i].getRate(), actResult[i].getRate());
			}
		} catch (CustomerException e) {
			System.out.println("CustomerException caught: " + e.getMessage());
		}
	}**/
}
