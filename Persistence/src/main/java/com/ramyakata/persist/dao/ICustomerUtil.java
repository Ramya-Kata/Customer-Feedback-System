package com.ramyakata.persist.dao;

import java.sql.Connection;

import com.ramyakata.persist.exception.JdbcException;

public interface ICustomerUtil {

	public void readFromCSV(String filePath) throws JdbcException;

	public Connection connectDB() throws JdbcException;

	public void closeConnection() throws JdbcException;
}
