package com.ramyakata.persist.entity;

import java.time.LocalDate;

import com.ramyakata.persist.exception.CustomerException;

/**
 * Description: This class represents the Customer details who signed in.
 */
public class CustomerInput {

	private String firstName;
	private String lastName;
	private String gmail;
	private int customerID;
	private LocalDate date;
	private int rate;

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setFirstName(String firstName) {

		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * 
	 * @param gmail
	 * @throws CustomerException
	 */
	public void setEmailAddress(String gmail) throws CustomerException {
		if (!gmail.endsWith("@gmail.com")) {
			throw new CustomerException("Email address must end with @gmail.com");
		}
		this.gmail = gmail;
	}

	/**
	 * 
	 * @param rate
	 * @throws CustomerException
	 */
	public void setRate(int rate) throws CustomerException {
		if (rate < 0 || rate > 5) {
			throw new CustomerException("Rating should be in between 0 and 5");
		}
		this.rate = rate;

	}

	/**
	 * 
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * 
	 * @return
	 */
	public String getLatName() {
		return lastName;
	}

	/**
	 * 
	 * @return
	 */
	public String getGmail() {
		return gmail;
	}

	/**
	 * 
	 * @return
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * 
	 * @return
	 */
	public int getRate() {
		return rate;
	}
}
