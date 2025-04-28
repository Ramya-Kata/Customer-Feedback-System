package com.ramyakata.persist.repo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ramyakata.persist.dao.ICustomerPoolUtil;
import com.ramyakata.persist.exception.JdbcException;

public class CustomerPoolUtil implements ICustomerPoolUtil {

	private String userName = null;
	private String password = null;
	private String dbms = null;
	private String serverName = null;
	private int portNumber = 0;
	private String schemaName = null;
	private String tableName = null;
	private Connection con;

	private Connection[] pool;
	private int currentSize = 0;

	public CustomerPoolUtil() {
		pool = new Connection[10];
	}

	@Override
	public void readFromCSV(String filePath) throws JdbcException {
		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
		         BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
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

	private Connection createConnection() throws JdbcException {
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
			throw new JdbcException("Error while creating a connection " + e.getMessage());
		}
		System.out.println("Connected to database" + schemaName);
		return con;
	}

	@Override
	public Connection connectDB() throws JdbcException {
		try {
			for (int i = 0; i < currentSize; i++) {
				if (pool[i] != null && !pool[i].isClosed()) {
					System.out.println("Reusing connection from pool");
					return pool[i];
				}
			}
			if (currentSize >= (pool.length - 3)) {
				increasePoolSize(2 * currentSize + 1);
			}
			con = createConnection();
			pool[currentSize++] = con;
			System.out.println("New connection created and added to pool");
			return con;
		} catch (SQLException e) {
			throw new JdbcException("Error while Connecting " + e.getMessage());
		}
	}

	private void increasePoolSize(int size) {
		Connection[] newPool = new Connection[size];
		System.arraycopy(pool, 0, newPool, 0, currentSize);

		pool = newPool;
	}

	@Override
	public void closeConnection() throws JdbcException {
		try {
			for (int i = 0; i < currentSize; i++) {
				if (pool[i] != null && !pool[i].isClosed()) {
					pool[i].close();
					pool[i] = null;
				}
			}
			currentSize = 0;
			System.out.println("All connections in pool closed.");
		} catch (SQLException e) {
			throw new JdbcException("Error while closing resources: " + e.getMessage());
		}

	}

	@Override
	public ResultSet executeQ(String query) throws JdbcException {
		Statement stmt = null;
		ResultSet res = null;
		try {
			stmt = con.createStatement();
			res = stmt.executeQuery(query);
		} catch (SQLException e) {
			throw new JdbcException("Error while executing query: " + e.getMessage());
		}
		return res;
	}

	@Override
	public int executeU(String query) throws JdbcException {
		Statement stmt = null;
		int res = 0;
		try {
			stmt = con.createStatement();
			res = stmt.executeUpdate(query);
		} catch (SQLException e) {
			throw new JdbcException("Error while executing query: " + e.getMessage());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				throw new JdbcException("Error while closing Statement: " + e.getMessage());
			}
		}
		return res;
	}

	@Override
	public boolean idExists(String tableName, int id) throws JdbcException {
		String query = "SELECT COUNT(*) FROM " + tableName + " WHERE ID = " + id;
		try (ResultSet res = executeQ(query)) {
			if (res.next()) {
				return res.getInt(1) > 0;
			}
			return false;
		} catch (SQLException e) {
			throw new JdbcException("Error while executing query: " + e.getMessage());
		}
	}
}
