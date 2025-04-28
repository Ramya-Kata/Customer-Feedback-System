package com.ramyakata.persist.repo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.ramyakata.persist.dao.ICustomerUtil;
import com.ramyakata.persist.exception.JdbcException;

public class CustomerUtil implements ICustomerUtil {

	private String userName = null;
	private String password = null;
	private String dbms = null;
	private String serverName = null;
	private int portNumber = 0;
	private String schemaName = null;
	private String tableName = null;
	private Connection con = null;

	@Override
	public void readFromCSV(String filePath) throws JdbcException {
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String headerLine = br.readLine(); // Read header line
			if (headerLine != null) {
				String line = br.readLine(); // Read the first data line
				if (line != null) {
					String[] values = line.split(",");
					if (values.length == 7) { // Correct length check based on CSV format
						this.dbms = values[0].trim();
						this.serverName = values[1].trim();
						this.portNumber = Integer.parseInt(values[2].trim());
						this.userName = values[3].trim();
						this.password = values[4].trim();
						this.schemaName = values[5].trim();
						this.tableName = values[6].trim();
					} else {
						throw new JdbcException("CSV file format is incorrect.");
					}
				} else {
					throw new JdbcException("No data found in CSV file.");
				}
			} else {
				throw new JdbcException("CSV file is empty.");
			}
		} catch (IOException e) {
			throw new JdbcException("Error reading CSV file: " + e.getMessage());
		}
	}

	public String getSchemaName() {
		return schemaName;
	}

	public String getTableName() {
		return tableName;
	}

	@Override
	public Connection connectDB() throws JdbcException {
		try {
			if (dbms == null || dbms.isEmpty()) {
				throw new JdbcException("DBMS is not specified.");
			}
			if (dbms.equalsIgnoreCase("mysql")) {
				if (schemaName != null && !schemaName.isEmpty()) {
					con = DriverManager.getConnection(
							"jdbc:" + dbms + "://" + serverName + ":" + portNumber + "/" + schemaName, userName,
							password);
				} else {
					con = DriverManager.getConnection("jdbc:" + dbms + "://" + serverName + ":" + portNumber + "/",
							userName, password);
				}
			}
		} catch (SQLException e) {
			throw new JdbcException("Error while Connecting " + e.getMessage());
		}
		System.out.println("Connected to database" + schemaName);
		return con;

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
