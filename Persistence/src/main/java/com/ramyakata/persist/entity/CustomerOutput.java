package com.ramyakata.persist.entity;

import java.time.LocalDate;

public class CustomerOutput {

	private String firstName;
	private String lastName;
	private String gmail;
	private int customerID;
	private LocalDate date;
	private int rate;

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setgmail(String gmail) {
		this.gmail = gmail;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public int getCustomerID() {
		return customerID;
	}

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
