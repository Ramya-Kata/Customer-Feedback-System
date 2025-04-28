package com.ramyakata.persist.dao;

import com.ramyakata.persist.entity.CustomerInput;
import com.ramyakata.persist.entity.CustomerOutput;
import com.ramyakata.persist.exception.CustomerException;

public interface ICustomerManager {

	public void addCustomer(CustomerInput inObj) throws CustomerException;

	public CustomerOutput[] getallCustomers() throws CustomerException;

	public CustomerOutput getCustomer(int id) throws CustomerException;

	public void updateCustomer(CustomerInput inObj) throws CustomerException;

	public CustomerOutput[] sortCustomers() throws CustomerException;

	public void removeCustomer(int id) throws CustomerException;

}
