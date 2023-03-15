package Server;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Server.ServerUI.threadForReports;
import database.DatabaseController;
import database.DatabaseSimpleOperation;

//Test class of the ServerUI method "createInventoryReport" that inserts reports to the db each month
class ServerUITest {
	private threadForReports threadReports;
	
	@BeforeEach
	void setUp() throws Exception {	
		threadReports = new ServerUI().new threadForReports();
		DatabaseController.setDatabasePassword("Aa123456");
	}

	// Test 1
	//check functionality: Testing the functionality of creating an inventory report
	//input data: current date, last month
	//expected result: New report inserted into table "reports", 
	// 					counter of rows after insert > counter of rows before insert
    @Test
    public void createInventoryReport_WithValidDateRange() {
        int currentMonth = 2;
        int currentYear = 2023;
        int monthBefore = 1;
        int yearBefore = 2023;
        int counterBeforeOperation = 0, counterAfterOperation = 0;
        
        // verify that the report table has the correct data
        String sql = "SELECT * FROM ektdb.report";
        ResultSet results = DatabaseSimpleOperation.executeQueryWithResults(sql, null);
        try {
			while(results.next()){ 
				counterBeforeOperation++;
			}
		} catch (SQLException e) {
			fail();
			e.printStackTrace();
		}
        
        // insert into report
        assertTrue(threadReports.createInventoryReport(currentMonth, currentYear, monthBefore, yearBefore));
        // verify that the report table has the correct data
        results = DatabaseSimpleOperation.executeQueryWithResults(sql, null);
        try {
			while(results.next()){ 
				counterAfterOperation++;
			}
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
        
        assertEquals(counterAfterOperation, counterBeforeOperation + 1);
    }

    // Test 2
 	//check functionality: Testing the functionality of creating an inventory report with no existing information
    //to add from "restock_in_machine" table
 	//input data: current date, last month
 	//expected result: table report not updated, no data to insert in provided dates
    // 					counter of rows after insert == counter of rows before insert
    @Test
    public void createInventoryReport_PreviousYearNoRestocks() {
    	int currentMonth = 1;
        int currentYear = 2023;
        int monthBefore = 12;
        int yearBefore = 2022;
        int counterBeforeOperation = 0, counterAfterOperation = 0;
        // verify that the report table has the correct data
        String sql = "SELECT * FROM ektdb.report";
        ResultSet results = DatabaseSimpleOperation.executeQueryWithResults(sql, null);
        try {
			while(results.next()){ 
				counterBeforeOperation++;
			}
		} catch (SQLException e) {
			fail();
			e.printStackTrace();
		}
        
        // assert that no insert happened
        assertFalse(threadReports.createInventoryReport(currentMonth, currentYear, monthBefore, yearBefore));
        // verify that the report table has the correct data
        sql = "SELECT * FROM ektdb.report";
        results = DatabaseSimpleOperation.executeQueryWithResults(sql, null);
        try {
			while(results.next()){ 
				counterAfterOperation++;
			}
		} catch (SQLException e) {
			fail();
			e.printStackTrace();
		}
        assertEquals(counterAfterOperation, counterBeforeOperation);
        
    }

    // Test 3
 	//check functionality: Testing the functionality of creating an inventory report with invalid dates
 	//input data: invalid current month, invalid last month
 	//expected result: Report insert to DB not performed
    @Test
    public void createInventoryReport_WithInvalidDateRange() {
    	int currentMonth = -1;
        int currentYear = 2023;
        int monthBefore = -1;
        int yearBefore = 2022;
        //Assert query execution failed -> report not created
        assertFalse(threadReports.createInventoryReport(currentMonth, currentYear, monthBefore, yearBefore));
    }

}
