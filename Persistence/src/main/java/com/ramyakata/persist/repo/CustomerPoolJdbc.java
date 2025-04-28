package com.ramyakata.persist.repo;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ramyakata.persist.dao.ICustomerPoolJdbc;
import com.ramyakata.persist.entity.CustomerOutput;
import com.ramyakata.persist.exception.JdbcException;

public class CustomerPoolJdbc implements ICustomerPoolJdbc {

	private CustomerPoolUtil utilObj = null;

	public CustomerPoolJdbc(CustomerPoolUtil utilObj) {
		this.utilObj = utilObj;
	}

	public String getTableName() {
		return utilObj.getTableName();
	}

	public String getSchemaName() {
		return utilObj.getSchemaName();
	}

	@Override
	public void createTable() throws JdbcException {
		String query = " CREATE TABLE IF NOT EXISTS " + getTableName()
				+ "( ID INT PRIMARY KEY, FIRST_NAME VARCHAR(255) NOT NULL, LAST_NAME VARCHAR(255) NOT NULL, EMAIL VARCHAR(255) NOT NULL, TODAY DATE, RATING INT)";
		utilObj.connectDB();
		utilObj.executeU(query);
		System.out.println("Table Created Succesfully");

	}

	@Override
	public void insert(CustomerOutput values) throws JdbcException {
		String query = "INSERT INTO " + getTableName() + " (ID, FIRST_NAME, LAST_NAME, EMAIL, TODAY, RATING) VALUES ("
				+ values.getCustomerID() + ", '" + values.getFirstName() + "', '" + values.getLatName() + "', '"
				+ values.getGmail() + "', '" + values.getDate() + "', " + values.getRate() + ")";

		if (getTableName() == null || getTableName().trim().isEmpty()) {
			throw new JdbcException("Table name is not set.");
		}
		if (utilObj.idExists(getTableName(), values.getCustomerID())) {
			throw new JdbcException("ID already exists");
		}
		readTable("*");
		utilObj.connectDB();
		int rowsInserted = utilObj.executeU(query);
		System.out.println(rowsInserted + " Row(s) Inserted Successfully");

	}

	@Override
	public List<CustomerOutput> readTable(String column) throws JdbcException {
		List<CustomerOutput> outObj = new ArrayList<>();
		if (getTableName() == null || getTableName().trim().isEmpty()) {
			throw new JdbcException("Table name is not set.");
		}
		String query = "SELECT " + column + " FROM " + getTableName();
		ResultSet res = utilObj.executeQ(query);
		try {

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
		} catch (SQLException e) {
			throw new JdbcException("Error while closing resources: " + e.getMessage());
		}
		return outObj;
	}

	@Override
	public void updateTable(String update, String value, int id) throws JdbcException {
		if (!utilObj.idExists(getTableName(), id)) {
			throw new JdbcException("ID doesnot exist");
		}
		String query = "UPDATE " + getTableName() + " SET " + update + " = '" + value + "' WHERE ID = " + id;
		utilObj.connectDB();
		int res = utilObj.executeU(query);
		System.out.println("Rows Updated: " + res);

	}

	@Override
	public void updateTable(String update, Date value, int id) throws JdbcException {
		if (!utilObj.idExists(getTableName(), id)) {
			throw new JdbcException("ID doesnot exist");
		}
		String query = "UPDATE " + getTableName() + " SET " + update + " = '" + value + "' WHERE ID = " + id;
		utilObj.connectDB();
		int res = utilObj.executeU(query);
		System.out.println("Rows Updated: " + res);

	}

	@Override
	public void updateTable(String update, int value, int id) throws JdbcException {
		if (!utilObj.idExists(getTableName(), id)) {
			throw new JdbcException("ID doesnot exist");
		}
		String query = "UPDATE " + getTableName() + " SET " + update + " = '" + value + "' WHERE ID = " + id;
		utilObj.connectDB();
		int res = utilObj.executeU(query);
		System.out.println("Rows Updated: " + res);

	}

	@Override
	public void remove(int id) throws JdbcException {
		if (!utilObj.idExists(getTableName(), id)) {
			throw new JdbcException("ID doesnot exist");
		}
		String query = "DELETE FROM " + getTableName() + " WHERE ID = " + id;
		utilObj.connectDB();
		int res = utilObj.executeU(query);
		System.out.println("Rows Deleted: " + res);
	}

	@Override
	public void deleteTable() throws JdbcException {
		if (getTableName() == null || getTableName().trim().isEmpty()) {
			throw new JdbcException("Table name is not set.");
		}
		String query = "DROP TABLE " + getTableName();
		utilObj.connectDB();
		utilObj.executeU(query);

	}

}
