package com.ramyakata.persist.repo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ramyakata.persist.dao.ICustomerDB;
import com.ramyakata.persist.entity.CustomerOutput;
import com.ramyakata.persist.exception.JdbcException;

public class CustomerDB implements ICustomerDB {

	private Connection con = null;
	private CustomerUtil utilObj = null;

	public CustomerDB(CustomerUtil utilObj) throws JdbcException {

		this.utilObj = utilObj;
		try {
			this.con = utilObj.connectDB();
		} catch (JdbcException e) {
			throw new JdbcException("Connection Failure: " + e.getMessage());
		}
	}

	public String getTableName() {
		return utilObj.getTableName();
	}

	public String getSchemaName() {
		return utilObj.getSchemaName();
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
		String query = " CREATE TABLE IF NOT EXISTS " + getTableName()
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

	private boolean idExists(int id) throws JdbcException {
		String query = "SELECT COUNT(*) FROM " + getTableName() + " WHERE ID = " + id;
		Statement stmt = null;
		ResultSet res = null;
		try {
			stmt = con.createStatement();
			res = stmt.executeQuery(query);
			if (res.next()) {
				return res.getInt(1) > 0;
			}
			return false;
		} catch (SQLException e) {
			throw new JdbcException("Error while checking if ID exists: " + e.getMessage());
		} finally {
			try {
				if (res != null)
					res.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				throw new JdbcException("Error while closing resources: " + e.getMessage());
			}
		}
	}

	@Override
	public void insert(CustomerOutput values) throws JdbcException {
		if (getTableName() == null || getTableName().trim().isEmpty()) {
			throw new JdbcException("Table name is not set.");
		}
		if (idExists(values.getCustomerID())) {
			throw new JdbcException("ID already exists");
		}
		readTable("*");
		String query = "INSERT INTO " + getTableName() + " (ID, FIRST_NAME, LAST_NAME, EMAIL, TODAY, RATING) VALUES ("
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
	public List<CustomerOutput> readTable(String column) throws JdbcException {
		List<CustomerOutput> outObj = new ArrayList<>();
		String query = "Select " + column + " from " + getTableName();
		Statement stmt = null;
		ResultSet res = null;
		try {
			stmt = con.createStatement();
			res = stmt.executeQuery(query);
			while (res.next()) {
				CustomerOutput customer = new CustomerOutput();
				customer.setCustomerID(res.getInt("ID"));
				customer.setFirstName(res.getString("FIRST_NAME"));
				customer.setLastName(res.getString("LAST_NAME"));
				customer.setgmail(res.getString("EMAIL"));
				Date today = res.getDate("TODAY");
				LocalDate formattedDate = today.toLocalDate();
				customer.setDate(formattedDate);
				customer.setRate(res.getInt("RATING"));
				outObj.add(customer);
			}
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
		return outObj;
	}

	@Override
	public void readTable(String column, int id) throws JdbcException {
		String query = "Select" + column + " from " + getTableName() + " WHERE ID = " + id;
		Statement stmt = null;
		ResultSet res = null;
		try {
			stmt = con.createStatement();
			res = stmt.executeQuery(query);
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
		if (!idExists(id)) {
			throw new JdbcException("ID doesnot exist");
		}
		String query = "UPDATE " + getTableName() + " SET " + update + " = '" + value + "' WHERE ID = " + id;
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
	public void updateTable(String update, Date value, int id) throws JdbcException {
		if (!idExists(id)) {
			throw new JdbcException("ID doesnot exist");
		}
		String query = "UPDATE " + getTableName() + " SET " + update + " = '" + value + "' WHERE ID = " + id;
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
	public void updateTable(String update, int value, int id) throws JdbcException {
		if (!idExists(id)) {
			throw new JdbcException("ID doesnot exist");
		}
		String query = "UPDATE " + getTableName() + " SET " + update + " = '" + value + "' WHERE ID = " + id;
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
		if (!idExists(id)) {
			throw new JdbcException("ID doesnot exist");
		}
		String query = "DELETE FROM " + getTableName() + " WHERE ID = " + id;
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
	public void sortTable(String column) throws JdbcException {
		String query = "Select * from " + getTableName() + " ORDER BY " + column + " DESC";
		System.out.println(query);
		Statement stmt = null;
		ResultSet res = null;
		try {
			stmt = con.createStatement();
			res = stmt.executeQuery(query);
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
	public void deleteTable() throws JdbcException {
		String query = "DROP TABLE " + getTableName();
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

}
