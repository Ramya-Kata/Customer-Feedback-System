package com.ramyakata.persist.dao;

import java.time.LocalDate;

import com.ramyakata.persist.entity.CustomerInput;
import com.ramyakata.persist.exception.CustomerException;
import com.ramyakata.persist.exception.JdbcException;
import com.ramyakata.persist.repo.CustomerJdbc;

import junit.framework.TestCase;

public class TestCustomerJdbc extends TestCase {

	private CustomerJdbc jdbcObj = null;

	protected void setUp() throws Exception {
		jdbcObj = new CustomerJdbc("mysql", "127.0.0.1", 3306, "root", "Taehyung@3031");
	}

	protected void tearDown() throws Exception {
		jdbcObj = null;
	}

	public void testViewTables() {
		jdbcObj.setSchemaName("student_example");
		System.out.println(jdbcObj.getSchemaName());
		try {
			System.out.println("testing viewTables");
			jdbcObj.connectDB();
			jdbcObj.viewTables();
			jdbcObj.closeConnection();
		} catch (JdbcException e) {
			// TODO Auto-generated catch block
			System.out.println("testing viewTables" + e.getMessage());
		}
	}

	public void testCreateTable() {
		jdbcObj.setSchemaName("student_example");
		jdbcObj.setTableName("CustomerDetails");
		System.out.println(jdbcObj.getSchemaName());
		try {
			System.out.println("testing Create Table");
			jdbcObj.connectDB();
			jdbcObj.createTable();
			jdbcObj.closeConnection();
		} catch (JdbcException e) {
			// TODO Auto-generated catch block
			System.out.println("testing Create Table" + e.getMessage());
		}
	}

	public void testInsert() {
		jdbcObj.setSchemaName("student_example");
		jdbcObj.setTableName("CustomerDetails");
		System.out.println(jdbcObj.getSchemaName());
		try {
			System.out.println("testing Inserting Table");
			jdbcObj.connectDB();
			jdbcObj.viewTables();
			CustomerInput obj = new CustomerInput();
			obj.setCustomerID(100);
			obj.setFirstName("Ramya");
			obj.setLastName("Kata");
			obj.setEmailAddress("ramyakata@gmail.com");
			obj.setDate(LocalDate.of(2024, 5, 30));
			obj.setRate(5);
			jdbcObj.getTablename();
			jdbcObj.insert(obj);
			jdbcObj.closeConnection();
		} catch (JdbcException e) {
			// TODO Auto-generated catch block
			System.out.println("testing Insert Table" + e.getMessage());
		} catch (CustomerException e) {
			// TODO Auto-generated catch block
			System.out.println("testing Insert Table" + e.getMessage());
		}
	}

	public void testReadTableString() {
		jdbcObj.setSchemaName("student_example");
		jdbcObj.setTableName("CustomerDetails");
		System.out.println(jdbcObj.getSchemaName());
		try {
			System.out.println("testing Inserting Table");
			jdbcObj.connectDB();
			jdbcObj.viewTables();
			jdbcObj.getTablename();
			System.out.println("CurrentTable:" + jdbcObj.getTablename());
			jdbcObj.readTable("*");
			jdbcObj.closeConnection();
		} catch (JdbcException e) {
			// TODO Auto-generated catch block
			System.out.println("testing Insert Table" + e.getMessage());
		}
	}

	public void testReadTableStringInt() {
		jdbcObj.setSchemaName("student_example");
		jdbcObj.setTableName("CustomerDetails");
		System.out.println(jdbcObj.getSchemaName());
		try {
			System.out.println("testing Inserting Table");
			jdbcObj.connectDB();
			jdbcObj.viewTables();
			jdbcObj.getTablename();
			System.out.println("CurrentTable:" + jdbcObj.getTablename());
			jdbcObj.readTable("*", 103);
			jdbcObj.closeConnection();
		} catch (JdbcException e) {
			// TODO Auto-generated catch block
			System.out.println("testing Insert Table" + e.getMessage());
		}
	}

	public void testUpdateTable() {
		jdbcObj.setSchemaName("student_example");
		jdbcObj.setTableName("CustomerDetails");
		System.out.println(jdbcObj.getSchemaName());
		try {
			jdbcObj.connectDB();
			jdbcObj.viewTables();
			jdbcObj.getTablename();
			System.out.println("CurrentTable:" + jdbcObj.getTablename());
			jdbcObj.updateTable("LAST_NAME", "kata", 101);
			jdbcObj.closeConnection();
		} catch (JdbcException e) {
			// TODO Auto-generated catch block
			System.out.println("testing Insert Table" + e.getMessage());
		}
	}

	public void testRemove() {
		jdbcObj.setSchemaName("student_example");
		jdbcObj.setTableName("CustomerDetails");
		System.out.println(jdbcObj.getSchemaName());
		try {
			jdbcObj.connectDB();
			jdbcObj.viewTables();
			jdbcObj.getTablename();
			System.out.println("CurrentTable:" + jdbcObj.getTablename());
			jdbcObj.remove(101);
			jdbcObj.closeConnection();
		} catch (JdbcException e) {
			// TODO Auto-generated catch block
			System.out.println("testing Insert Table" + e.getMessage());
		}
	}

	public void testDeleteTable() {
		jdbcObj.setSchemaName("student_example");
		jdbcObj.setTableName("CustomerDetails");
		System.out.println(jdbcObj.getSchemaName());
		try {
			jdbcObj.connectDB();
			jdbcObj.viewTables();
			jdbcObj.getTablename();
			jdbcObj.deleteTable();
			jdbcObj.closeConnection();
		} catch (JdbcException e) {
			// TODO Auto-generated catch block
			System.out.println("Deletion Of table: " + e.getMessage());
		}
	}

}
