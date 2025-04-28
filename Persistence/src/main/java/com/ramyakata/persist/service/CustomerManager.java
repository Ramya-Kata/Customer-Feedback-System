package com.ramyakata.persist.service;

import java.util.List;

import com.ramyakata.persist.dao.ICustomerManager;
import com.ramyakata.persist.entity.CustomerInput;
import com.ramyakata.persist.entity.CustomerOutput;
import com.ramyakata.persist.exception.CustomerException;
import com.ramyakata.persist.exception.JdbcException;
import com.ramyakata.persist.repo.CustomerDB;
import com.ramyakata.persist.repo.CustomerUtil;

public class CustomerManager implements ICustomerManager {

	private int currentIndex;
	private CustomerOutput[] customer;
	private CustomerDB dbObj;
	private CustomerUtil utilObj = null;
	private String file;

	public CustomerManager(String file) throws CustomerException {
		try {
			customer = new CustomerOutput[5];
			currentIndex = 0;
			utilObj = new CustomerUtil();
			this.file = file;
			utilObj.readFromCSV(this.file);
			dbObj = new CustomerDB(utilObj);
			dbObj.createTable();
			dbObj.readTable("*");
			reloadCustomers();
		} catch (JdbcException e) {
			throw new CustomerException("Error reading csv file" + e.getMessage());
		}
	}

	public CustomerManager(int initialCapacity) throws CustomerException {
		if (initialCapacity < 0) {
			throw new CustomerException("Entered negative Initial Capacity");
		}
		customer = new CustomerOutput[initialCapacity];
		currentIndex = 0;
	}

	private void reloadCustomers() throws CustomerException {
		try {
			List<CustomerOutput> dbCustomers = dbObj.readTable("*");
			// Reset current data
			customer = new CustomerOutput[5];
			currentIndex = 0;

			// Load data from the database into memory
			for (CustomerOutput dbCustomer : dbCustomers) {
				if (currentIndex >= customer.length) {
					increaseCapacity(2 * customer.length); // Increase capacity if needed
				}
				customer[currentIndex++] = dbCustomer;
			}
		} catch (JdbcException e) {
			throw new CustomerException("Error while loading customers from the database: " + e.getMessage());
		}
	}

	@Override
	public void addCustomer(CustomerInput inObj) throws CustomerException {
		if (customer.length == currentIndex) {
			increaseCapacity(2 * currentIndex + 1);
		}

		for (int i = 0; i < customer.length; i++) {
			if (customer[i] != null) {
				if (inObj.getCustomerID() == (customer[i].getCustomerID())) {
					throw new CustomerException("Customer already exists. Please update instead of adding.");
				}
			}
		}
		if (inObj == null) {
			throw new CustomerException("Input cannot be null");
		}

		CustomerOutput newCustomer = new CustomerOutput();
		newCustomer.setFirstName(inObj.getFirstName());
		newCustomer.setLastName(inObj.getLatName());
		newCustomer.setRate(inObj.getRate());
		newCustomer.setgmail(inObj.getGmail());
		newCustomer.setDate(inObj.getDate());
		newCustomer.setCustomerID(inObj.getCustomerID());
		customer[currentIndex] = newCustomer;

		try {
			dbObj.insert(customer[currentIndex]);
		} catch (JdbcException e) {
			throw new CustomerException("Error while inserting into the database" + e.getMessage());
		}
		currentIndex++;
	}

	private void increaseCapacity(int capacity) {
		CustomerOutput[] bigCustomers = new CustomerOutput[capacity];
		System.arraycopy(customer, 0, bigCustomers, 0, currentIndex);
		customer = bigCustomers;
	}

	@Override
	public CustomerOutput[] getallCustomers() throws CustomerException {
		reloadCustomers();
		if (currentIndex == 0) {
			throw new CustomerException("No Customers to fetch");
		}
		CustomerOutput[] customers = new CustomerOutput[currentIndex];
		System.arraycopy(customer, 0, customers, 0, currentIndex);
		return customers;
	}

	@Override
	public CustomerOutput getCustomer(int id) throws CustomerException {
		if (currentIndex == 0) {
			throw new CustomerException("No Customers to fetch");
		}
		for (int i = 0; i < currentIndex; i++) {
			if (id == customer[i].getCustomerID()) {
				return customer[i];
			}
		}
		throw new CustomerException("Customer with the name " + id + " not found");
	}

	@Override
	public void updateCustomer(CustomerInput inObj) throws CustomerException {
		if (currentIndex == 0) {
			throw new CustomerException("No Customers to update");
		}
		if (inObj == null) {
			throw new CustomerException("Input cannot be null");
		}

		for (int i = 0; i < currentIndex; i++) {
			if (inObj.getCustomerID() == customer[i].getCustomerID()
					&& inObj.getFirstName().equalsIgnoreCase(customer[i].getFirstName())
					&& inObj.getLatName().equalsIgnoreCase(customer[i].getLatName())) {
				customer[i].setgmail(inObj.getGmail());
				customer[i].setRate(inObj.getRate());
				customer[i].setDate(inObj.getDate());
				// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				// String formattedDate = sdf.format(customer[i].getDate());
				String formattedDate = inObj.getDate().toString();
				try {
					dbObj.updateTable("EMAIL", customer[i].getGmail(), customer[i].getCustomerID());
					dbObj.updateTable("RATING", customer[i].getRate(), customer[i].getCustomerID());
					dbObj.updateTable("TODAY", formattedDate, customer[i].getCustomerID());
				} catch (JdbcException e) {
					throw new CustomerException(
							"Error while reading the given id Customer details from the database" + e.getMessage());
				}
				return;
			}
		}
		throw new CustomerException("No customer found with the given name to update");
	}

	@Override
	public CustomerOutput[] sortCustomers() throws CustomerException {
		if (currentIndex == 0) {
			throw new CustomerException("No Customers to sort");
		}
		CustomerOutput[] sortedCustomers = new CustomerOutput[currentIndex];
		System.arraycopy(customer, 0, sortedCustomers, 0, currentIndex);
		for (int i = 0; i < currentIndex - 1; i++) {
			for (int j = 0; j < currentIndex - i - 1; j++) {
				if (sortedCustomers[j].getDate().isAfter(sortedCustomers[j + 1].getDate())) {
					CustomerOutput temp = sortedCustomers[j];
					sortedCustomers[j] = sortedCustomers[j + 1];
					sortedCustomers[j + 1] = temp;
				}
			}
		}
		return sortedCustomers;
	}

	@Override
	public void removeCustomer(int id) throws CustomerException {
		if (currentIndex == 0) {
			throw new CustomerException("No Customers to remove");
		}
		int customerIndex = -1;
		for (int i = 0; i < currentIndex; i++) {
			if (id == customer[i].getCustomerID()) {
				customerIndex = i;
				break;
			}
		}
		if (customerIndex == -1) {
			throw new CustomerException("No customer with the name " + id + " found to remove");
		}
		try {
			dbObj.remove(id);
		} catch (JdbcException e) {
			throw new CustomerException("Error while reading all the table values from the database" + e.getMessage());
		}
		for (int i = customerIndex; i < currentIndex - 1; i++) {
			customer[i] = customer[i + 1];
		}
		customer[currentIndex - 1] = null;
		currentIndex--;
	}
}
