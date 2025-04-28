package com.ramyakata.persist.dao;

import java.sql.Connection;

import com.ramyakata.persist.entity.CustomerInput;
import com.ramyakata.persist.exception.JdbcException;

public interface ICustomerJdbc {

	public Connection connectDB() throws JdbcException;

	public void viewTables() throws JdbcException;

	public void createTable() throws JdbcException;

	public void insert(CustomerInput values) throws JdbcException;

	public void readTable(String column) throws JdbcException;

	public void readTable(String column, int id) throws JdbcException;

	public void updateTable(String update, String value, int id) throws JdbcException;

	public void remove(int id) throws JdbcException;

	public void deleteTable() throws JdbcException;

	public void closeConnection() throws JdbcException;
}
