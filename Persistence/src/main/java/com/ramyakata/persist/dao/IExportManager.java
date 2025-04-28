package com.ramyakata.persist.dao;

import com.ramyakata.persist.exception.CustomerException;

public interface IExportManager {
    
    void exportToCsv(int customerId) throws CustomerException;
    void exportToJson(int customerId) throws CustomerException;
	void exportToTxt(int customerId) throws CustomerException;
}
