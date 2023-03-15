package logic;

/**
 * Project Name: finalProjectEkt_Client Logic class that contains the details
 * needed to save up for each report.
 * 
 * @author Maxim Lebedinsky
 * @version 16/12/2022
 */
public class Report {
	/**
	 * Report logic part. private fields that will contain: report's id, stock
	 * status of each machine and customer histogram
	 */
	private Integer reportID;
	private Integer stockStatusOfMachines;
	private String customerHistogram;

	/**
	 * Report constructor.
	 * 
	 * @param reportID
	 * @param stockStatusOfMachines
	 * @param customerHistogram
	 */
	public Report(Integer reportID, Integer stockStatusOfMachines, String customerHistogram) {
		this.reportID = reportID;
		this.stockStatusOfMachines = stockStatusOfMachines;
		this.customerHistogram = customerHistogram;
	}

	/**
	 * getting report id
	 * 
	 * @return report id
	 */
	public Integer getReportID() {
		return reportID;
	}

	/**
	 * setting report id
	 * 
	 * @param reportID
	 */
	public void setReportID(Integer reportID) {
		this.reportID = reportID;
	}

	/**
	 * getting machines stock status
	 * 
	 * @return stockStatusOfMachines
	 */
	public Integer getStockStatusOfMachines() {
		return stockStatusOfMachines;
	}

	/**
	 * setting machines stock status
	 * 
	 * @param stockStatusOfMachines
	 */
	public void setStockStatusOfMachines(Integer stockStatusOfMachines) {
		this.stockStatusOfMachines = stockStatusOfMachines;
	}

	/**
	 * getting customer histogram
	 * 
	 * @return customerHistogram
	 */
	public String getCustomerHistogram() {
		return customerHistogram;
	}

	/**
	 * setting customer histogram
	 * 
	 * @param customerHistogram
	 */
	public void setCustomerHistogram(String customerHistogram) {
		this.customerHistogram = customerHistogram;
	}

	/**
	 * toString method, returns report details
	 */
	@Override
	public String toString() {
		return "Report [reportID=" + reportID + ", stockStatusOfMachines=" + stockStatusOfMachines
				+ ", customerHistogram=" + customerHistogram + "]";
	}

}
