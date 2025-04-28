package com.ramyakata.persist.dao;

import java.time.LocalDate;

import com.ramyakata.persist.entity.CustomerOutput;
import com.ramyakata.persist.exception.JdbcException;
import com.ramyakata.persist.repo.CustomerDB;
import com.ramyakata.persist.repo.CustomerUtil;

import junit.framework.TestCase;

public class TestCustomerDB extends TestCase {

	private CustomerDB jdbcObj = null;
	private CustomerUtil utilObj = null;
	private String filePath = "src/main/resources/config.csv";

	protected void setUp() throws JdbcException {
		try {
			utilObj = new CustomerUtil();
			utilObj.readFromCSV(filePath);
			// utilObj.connectDB();

			jdbcObj = new CustomerDB(utilObj);

		} catch (Exception e) {
			System.out.println("Error creating instances" + e.getMessage());
		}

	}

	protected void tearDown() throws JdbcException {
		jdbcObj = null;
		utilObj.closeConnection();
	}

	public void testViewTables() {
		try {
			System.out.println(jdbcObj.getSchemaName());
			System.out.println("testing viewTables");
			jdbcObj.viewTables();
		} catch (JdbcException e) {
			// TODO Auto-generated catch block
			System.out.println("testing viewTables" + e.getMessage());
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}

	public void testCreateTable() {
		System.out.println(jdbcObj.getSchemaName());
		try {
			System.out.println("testing Creating Table");
			jdbcObj.createTable();
		} catch (JdbcException e) {
			// TODO Auto-generated catch block
			System.out.println("testing Insert Table" + e.getMessage());
		}
	}

	public void testInsert() {
		System.out.println(jdbcObj.getSchemaName());
		try {
			System.out.println("testing Inserting Table");
			jdbcObj.viewTables();
			CustomerOutput obj = new CustomerOutput();
			obj.setCustomerID(100);
			obj.setFirstName("Ramya");
			obj.setLastName("Kata");
			obj.setgmail("ramyakata@gmail.com");
			obj.setDate(LocalDate.of(2024, 5, 30));
			obj.setRate(5);
			jdbcObj.insert(obj);
		} catch (JdbcException e) {
			// TODO Auto-generated catch block
			System.out.println("testing Insert Table" + e.getMessage());
		}
	}

	public void testReadTableString() {
		System.out.println(jdbcObj.getSchemaName());
		try {
			jdbcObj.readTable("*");
		} catch (JdbcException e) {
			// TODO Auto-generated catch block
			System.out.println("testing Insert Table" + e.getMessage());
		}
	}

	public void testReadTableStringInt() {
		try {
			CustomerOutput obj = new CustomerOutput();
			obj.setCustomerID(103);
			obj.setFirstName("Manasa");
			obj.setLastName("Kata");
			obj.setgmail("manasakata@gmail.com");
			obj.setDate(LocalDate.of(2024, 2, 19));
			obj.setRate(4);
			jdbcObj.insert(obj);
			System.out.println("testing Inserting Table");
			jdbcObj.readTable("*", 103);
		} catch (JdbcException e) {
			// TODO Auto-generated catch block
			System.out.println("testing Insert Table" + e.getMessage());
		}
	}

	public void testUpdateTable() {
		System.out.println(jdbcObj.getSchemaName());
		try {
			jdbcObj.updateTable("LAST_NAME", "kasi", 101);
		} catch (JdbcException e) {
			// TODO Auto-generated catch block
			System.out.println("testing Insert Table" + e.getMessage());
		}
	}

	public void testRemove() {
		System.out.println(jdbcObj.getSchemaName());
		try {
			jdbcObj.remove(101);
		} catch (JdbcException e) {
			// TODO Auto-generated catch block
			System.out.println("testing Insert Table" + e.getMessage());
		}
	}

	public void testDeleteTable() {
		System.out.println(jdbcObj.getSchemaName());
		try {
			jdbcObj.deleteTable();
		} catch (JdbcException e) {
			// TODO Auto-generated catch block
			System.out.println("Deletion Of table: " + e.getMessage());
		}
	}
}
