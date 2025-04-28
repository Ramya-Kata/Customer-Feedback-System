package com.ramyakata.persist.repo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ramyakata.persist.dao.ICustomerJdbc;
import com.ramyakata.persist.entity.CustomerInput;
import com.ramyakata.persist.exception.JdbcException;

public class CustomerJdbc implements ICustomerJdbc {

	private String userName = null;
	private String password = null;
	private String dbms = null;
	private String serverName = null;
	private int portNumber = 0;
	private String schemaName = null;
	private String tableName = null;
	private Connection con = null;

	public CustomerJdbc(String dbms, String serverName, int portNumber, String userName, String password) {
		this.dbms = dbms;
		this.serverName = serverName;
		this.portNumber = portNumber;
		this.userName = userName;
		this.password = password;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return schemaName;
	}

	@Override
	public Connection connectDB() throws JdbcException {
		try {
			if (dbms.equalsIgnoreCase("mysql")) {
				if (schemaName != null) {
					con = DriverManager.getConnection(
							"jdbc:" + dbms + "://" + serverName + ":" + portNumber + "/" + schemaName, userName,
							password);
				} else {
					con = DriverManager.getConnection("jdbc:" + dbms + "://" + serverName + ":" + portNumber + "/",
							userName, password);
				}
			}
		} catch (SQLException e) {
			throw new JdbcException("Error while Connecting" + e.getMessage());
		}
		System.out.println("Connected to database");
		return con;
	}

	@Override
	public void viewTables() throws JdbcException {
		String query = "Show Tables";
		Statement stmt = null;
		ResultSet res = null;
		try {
			stmt = con.createStatement();
			res = stmt.executeQuery(query);
			while (res.next()) {

				String tableName = res.getString(1);
				System.out.println("Table: " + tableName);
			}
		} catch (SQLException e) {
			throw new JdbcException("Error while printing all the available tables" + e.getMessage());
		}
		try {
			if (res != null)
				res.close();
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			throw new JdbcException("Error while closing resources: " + e.getMessage());
		}

	}

	@Override
	public void createTable() throws JdbcException {

		String query = " create table " + getTableName()
				+ "( ID INT PRIMARY KEY, FIRST_NAME VARCHAR(255) NOT NULL, LAST_NAME VARCHAR(255) NOT NULL, EMAIL VARCHAR(255) NOT NULL, TODAY DATE, RATING INT)";
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			stmt.execute(query);
			System.out.println("Table Created Succesfully");

		} catch (SQLException e) {
			throw new JdbcException("Error while creating the table" + e.getMessage());
		}
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			throw new JdbcException("Error while closing resources: " + e.getMessage());
		}
	}

	public String getTablename() {
		return tableName;
	}

	@Override
	public void insert(CustomerInput values) throws JdbcException {
		if (tableName == null || tableName.trim().isEmpty()) {
			throw new JdbcException("Table name is not set.");
		}
		String query = "INSERT INTO " + tableName + " (ID, FIRST_NAME, LAST_NAME, EMAIL, TODAY, RATING) VALUES ("
				+ values.getCustomerID() + ", '" + values.getFirstName() + "', '" + values.getLatName() + "', '"
				+ values.getGmail() + "', '" + values.getDate() + "', " + values.getRate() + ")";
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			int rs = stmt.executeUpdate(query);
			System.out.println(rs + "Row Inserted Successfully");
		} catch (SQLException e) {
			throw new JdbcException("Error while inserting data: " + e.getMessage());
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				throw new JdbcException("Error while closing resources: " + e.getMessage());
			}
		}

	}

	@Override
	public void readTable(String column) throws JdbcException {
		String query = "Select " + column + " from " + getTablename();
		System.out.println(query);
		Statement stmt = null;
		ResultSet res = null;
		try {
			stmt = con.createStatement();
			res = stmt.executeQuery(query);
			System.out.println(
					"+-------------+---------------+---------------+-----------------------------------+---------------+----------+");
			System.out.printf("| %-11s | %-13s | %-13s | %-33s | %-13s | %-8S |%n", "CustomerID", "First Name",
					"LastName", "EMail Address", "Today", "Rating");
			System.out.println(
					"+-------------+---------------+---------------+-----------------------------------+---------------+----------+");

			while (res.next()) {

				int id = res.getInt(1);
				String firstName = res.getString(2);
				String lastName = res.getString(3);
				String email = res.getString(4);
				String today = res.getDate(5).toString();
				int rate = res.getInt(6);
				System.out.printf("| %-11s | %-13s | %-13s | %-33s | %-13s | %-8S |%n", id, firstName, lastName, email,
						today, rate);
			}
			System.out.println(
					"+-------------+---------------+---------------+-----------------------------------+---------------+----------+");

		} catch (SQLException e) {
			throw new JdbcException("Error while printing all the available values from the table" + e.getMessage());
		}
		try {
			if (res != null)
				res.close();
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			throw new JdbcException("Error while closing resources: " + e.getMessage());
		}

	}

	@Override
	public void readTable(String column, int id) throws JdbcException {
		String query = "Select" + column + " from " + getTablename() + " WHERE ID = " + id;
		Statement stmt = null;
		ResultSet res = null;
		try {
			stmt = con.createStatement();
			res = stmt.executeQuery(query);
			System.out.println(
					"+-------------+---------------+---------------+-----------------------------------+---------------+----------+");
			System.out.printf("| %-11s | %-13s | %-13s | %-33s | %-13s | %-8S |%n", "CustomerID", "First Name",
					"LastName", "EMail Address", "Today", "Rating");
			System.out.println(
					"+-------------+---------------+---------------+-----------------------------------+---------------+----------+");

			while (res.next()) {

				int customerId = res.getInt(1);
				String firstName = res.getString(2);
				String lastName = res.getString(3);
				String email = res.getString(4);
				String today = res.getDate(5).toString();
				int rate = res.getInt(6);
				System.out.printf("| %-11s | %-13s | %-13s | %-33s | %-13s | %-8S |%n", customerId, firstName, lastName,
						email, today, rate);
			}
			System.out.println(
					"+-------------+---------------+---------------+-----------------------------------+---------------+----------+");

		} catch (SQLException e) {
			throw new JdbcException("Error while printing the selected from the table" + e.getMessage());
		}
		try {
			if (res != null)
				res.close();
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			throw new JdbcException("Error while closing resources: " + e.getMessage());
		}

	}

	@Override
	public void updateTable(String update, String value, int id) throws JdbcException {
		String query = "UPDATE " + getTablename() + " SET " + update + " = '" + value + "' WHERE ID = " + id;
		System.out.println(query);
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			int res = stmt.executeUpdate(query);
			System.out.println("Rows Updated: " + res);
		} catch (SQLException e) {
			throw new JdbcException("Error while updating the table" + e.getMessage());
		}
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			throw new JdbcException("Error while closing resources: " + e.getMessage());
		}

	}

	@Override
	public void remove(int id) throws JdbcException {
		String query = "DELETE FROM " + getTablename() + " WHERE ID = " + id;
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			stmt.execute(query);
			System.out.println("Deletion Successfull");
		} catch (SQLException e) {
			throw new JdbcException("Error while removing values from the table" + e.getMessage());
		}
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			throw new JdbcException("Error while closing resources: " + e.getMessage());
		}

	}

	@Override
	public void deleteTable() throws JdbcException {
		String query = "DROP TABLE " + getTablename();
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			stmt.execute(query);
			System.out.println("Table Deleted");
		} catch (SQLException e) {
			throw new JdbcException("Error while deleting table" + e.getMessage());
		}
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			throw new JdbcException("Error while closing resources: " + e.getMessage());
		}

	}

	@Override
	public void closeConnection() throws JdbcException {
		try {
			if (con != null && !con.isClosed()) {
				con.close();
				System.out.println("Connection closed.");
			}
		} catch (SQLException e) {
			throw new JdbcException("Error while closing resources: " + e.getMessage());
		}
	}

}
