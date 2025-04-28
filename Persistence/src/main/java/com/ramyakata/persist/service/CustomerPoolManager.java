package com.ramyakata.persist.service;

import java.util.List;

import com.ramyakata.persist.dao.ICustomerManager;
import com.ramyakata.persist.entity.CustomerInput;
import com.ramyakata.persist.entity.CustomerOutput;
import com.ramyakata.persist.exception.CustomerException;
import com.ramyakata.persist.exception.JdbcException;
import com.ramyakata.persist.repo.CustomerPoolJdbc;
import com.ramyakata.persist.repo.CustomerPoolUtil;
import com.ramyakata.persist.util.AppConfigReader; // NEW

public class CustomerPoolManager implements ICustomerManager {

    private int currentIndex;
    private CustomerOutput[] customer;
    private CustomerPoolJdbc dbObj;
    private CustomerPoolUtil utilObj;

    public CustomerPoolManager() throws CustomerException {
        try {
            customer = new CustomerOutput[5];
            currentIndex = 0;
            utilObj = new CustomerPoolUtil();

            // 🔥 Instead of taking path from UI, read from properties internally
            String dbConfigPath = AppConfigReader.getProperty("db.config.path");
            utilObj.readFromCSV(dbConfigPath);

            dbObj = new CustomerPoolJdbc(utilObj);
            dbObj.createTable();
            dbObj.readTable("*");
            reloadCustomers();
        } catch (JdbcException e) {
            throw new CustomerException("Error initializing Customer Manager: " + e.getMessage());
        }
    }

    private void reloadCustomers() throws CustomerException {
        try {
            List<CustomerOutput> dbCustomers = dbObj.readTable("*");
            customer = new CustomerOutput[5];
            currentIndex = 0;
            for (CustomerOutput dbCustomer : dbCustomers) {
                if (currentIndex >= customer.length) {
                    increaseCapacity(2 * customer.length);
                }
                customer[currentIndex++] = dbCustomer;
            }
        } catch (JdbcException e) {
            throw new CustomerException("Error loading customers from database: " + e.getMessage());
        }
    }

    private void increaseCapacity(int capacity) {
        CustomerOutput[] newCustomerArray = new CustomerOutput[capacity];
        System.arraycopy(customer, 0, newCustomerArray, 0, currentIndex);
        customer = newCustomerArray;
    }

    @Override
    public void addCustomer(CustomerInput inObj) throws CustomerException {
        if (customer.length == currentIndex) {
            increaseCapacity(2 * currentIndex + 1);
        }
        if (inObj == null) {
            throw new CustomerException("Input cannot be null");
        }

        for (int i = 0; i < currentIndex; i++) {
            if (customer[i] != null &&
                (inObj.getCustomerID() == customer[i].getCustomerID() ||
                inObj.getFirstName().equalsIgnoreCase(customer[i].getFirstName()))) {
                throw new CustomerException("Customer already exists. Please update instead of adding.");
            }
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
            throw new CustomerException("Error inserting into database: " + e.getMessage());
        }
        currentIndex++;
    }

    @Override
    public CustomerOutput[] getallCustomers() throws CustomerException {
        reloadCustomers();
        if (currentIndex == 0) {
            throw new CustomerException("No customers found.");
        }
        CustomerOutput[] customers = new CustomerOutput[currentIndex];
        System.arraycopy(customer, 0, customers, 0, currentIndex);
        return customers;
    }

    @Override
    public CustomerOutput getCustomer(int id) throws CustomerException {
        if (currentIndex == 0) {
            throw new CustomerException("No customers found.");
        }
        for (int i = 0; i < currentIndex; i++) {
            if (id == customer[i].getCustomerID()) {
                return customer[i];
            }
        }
        throw new CustomerException("Customer with ID " + id + " not found.");
    }

    @Override
    public void updateCustomer(CustomerInput inObj) throws CustomerException {
        if (currentIndex == 0) {
            throw new CustomerException("No customers found to update.");
        }
        if (inObj == null) {
            throw new CustomerException("Input cannot be null.");
        }

        for (int i = 0; i < currentIndex; i++) {
            if (inObj.getCustomerID() == customer[i].getCustomerID()
                    && inObj.getFirstName().equalsIgnoreCase(customer[i].getFirstName())
                    && inObj.getLatName().equalsIgnoreCase(customer[i].getLatName())) {
                customer[i].setgmail(inObj.getGmail());
                customer[i].setRate(inObj.getRate());
                customer[i].setDate(inObj.getDate());
                String formattedDate = inObj.getDate().toString();
                try {
                    dbObj.updateTable("EMAIL", customer[i].getGmail(), customer[i].getCustomerID());
                    dbObj.updateTable("RATING", customer[i].getRate(), customer[i].getCustomerID());
                    dbObj.updateTable("TODAY", formattedDate, customer[i].getCustomerID());
                } catch (JdbcException e) {
                    throw new CustomerException("Error updating database: " + e.getMessage());
                }
                return;
            }
        }
        throw new CustomerException("No customer found with the given ID and name to update.");
    }

    @Override
    public CustomerOutput[] sortCustomers() throws CustomerException {
        if (currentIndex == 0) {
            throw new CustomerException("No customers to sort.");
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
            throw new CustomerException("No customers to remove.");
        }
        int customerIndex = -1;
        for (int i = 0; i < currentIndex; i++) {
            if (id == customer[i].getCustomerID()) {
                customerIndex = i;
                break;
            }
        }
        if (customerIndex == -1) {
            throw new CustomerException("Customer ID " + id + " not found.");
        }
        try {
            dbObj.remove(id);
        } catch (JdbcException e) {
            throw new CustomerException("Error removing customer from DB: " + e.getMessage());
        }
        for (int i = customerIndex; i < currentIndex - 1; i++) {
            customer[i] = customer[i + 1];
        }
        customer[currentIndex - 1] = null;
        currentIndex--;
    }
}
