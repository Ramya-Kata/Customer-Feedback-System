package com.ramyakata.persist.dao;

import java.sql.Connection;
import java.sql.ResultSet;

import com.ramyakata.persist.exception.JdbcException;

public interface ICustomerPoolUtil {

	public void readFromCSV(String filePath) throws JdbcException;

	public Connection connectDB() throws JdbcException;

	public void closeConnection() throws JdbcException;

	public ResultSet executeQ(String query) throws JdbcException;

	public int executeU(String query) throws JdbcException;

	public boolean idExists(String tableName, int id) throws JdbcException;
}
