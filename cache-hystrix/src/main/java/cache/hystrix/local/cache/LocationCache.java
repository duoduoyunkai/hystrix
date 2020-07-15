package cache.hystrix.local.cache;

import java.util.HashMap;
import java.util.Map;

public class LocationCache {
private static Map<String, String> nameMap = new HashMap<String, String>();
	
	static {
		nameMap.put("7011428", "TESTER-001"); 
	}
	
	public static String getName(String soldToCustomerNumber) {
		return nameMap.get(soldToCustomerNumber);
	}
}
