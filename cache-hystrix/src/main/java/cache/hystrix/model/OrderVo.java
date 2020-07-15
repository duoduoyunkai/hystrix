package cache.hystrix.model;

/**
 * Order VO.
 * 
 * @author houyunj
 *
 */
public class OrderVo {

	private String salesOrderNumber;

	private String agreementNumber;

	private String lineOfBusinessCode;

	private String soldToCustomerNumber;

	private String resellerCustomerNumber;
	
	private String customerName;

	public String getSalesOrderNumber() {
		return salesOrderNumber;
	}

	public void setSalesOrderNumber(String salesOrderNumber) {
		this.salesOrderNumber = salesOrderNumber;
	}

	public String getAgreementNumber() {
		return agreementNumber;
	}

	public void setAgreementNumber(String agreementNumber) {
		this.agreementNumber = agreementNumber;
	}

	public String getLineOfBusinessCode() {
		return lineOfBusinessCode;
	}

	public void setLineOfBusinessCode(String lineOfBusinessCode) {
		this.lineOfBusinessCode = lineOfBusinessCode;
	}

	public String getSoldToCustomerNumber() {
		return soldToCustomerNumber;
	}

	public void setSoldToCustomerNumber(String soldToCustomerNumber) {
		this.soldToCustomerNumber = soldToCustomerNumber;
	}

	public String getResellerCustomerNumber() {
		return resellerCustomerNumber;
	}

	public void setResellerCustomerNumber(String resellerCustomerNumber) {
		this.resellerCustomerNumber = resellerCustomerNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Override
	public String toString() {
		return "OrderVo [salesOrderNumber=" + salesOrderNumber + ", agreementNumber=" + agreementNumber
				+ ", lineOfBusinessCode=" + lineOfBusinessCode + ", soldToCustomerNumber=" + soldToCustomerNumber
				+ ", resellerCustomerNumber=" + resellerCustomerNumber + ", customerName=" + customerName + "]";
	}


	
	
}
