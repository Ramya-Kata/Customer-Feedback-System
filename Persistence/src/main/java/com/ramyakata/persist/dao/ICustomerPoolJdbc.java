package com.ramyakata.persist.dao;

import java.sql.Date;
import java.util.List;

import com.ramyakata.persist.entity.CustomerOutput;
import com.ramyakata.persist.exception.JdbcException;

public interface ICustomerPoolJdbc {

	public void createTable() throws JdbcException;

	public void insert(CustomerOutput values) throws JdbcException;

	public List<CustomerOutput> readTable(String column) throws JdbcException;

	public void updateTable(String update, String value, int id) throws JdbcException;

	public void updateTable(String update, Date value, int id) throws JdbcException;

	public void updateTable(String update, int value, int id) throws JdbcException;

	public void remove(int id) throws JdbcException;

	public void deleteTable() throws JdbcException;
}
