package com.ramyakata.persist.dao;

import com.ramyakata.persist.exception.JdbcException;
import com.ramyakata.persist.repo.CustomerUtil;

import junit.framework.TestCase;

public class TestCustomer extends TestCase {

	private static String filename = "src/main/resources/config.csv";
	private CustomerUtil cUObj  = null;
	protected void setUp() throws Exception {
		cUObj = new CustomerUtil();
	}

	protected void tearDown() throws Exception {
		cUObj = null;
	}

	public void testReadFromCSV() {
		try {
			cUObj.readFromCSV(filename);
			cUObj.connectDB();
			cUObj.closeConnection();
		} catch (JdbcException e) {
			// TODO Auto-generated catch block
			System.out.println("error with file");
		}
		
	}

}
