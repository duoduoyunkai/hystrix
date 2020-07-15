package cache.hystrix;

import com.alibaba.fastjson.JSON;

import cache.hystrix.model.OrderVo;
import cache.hystrix.util.HttpClientUtils;

public class RejectTest {
	public static void main(String[] args) throws Exception {
		for(int i = 0; i < 25; i++) {
			new TestThread(i).start();
		}
	}
	
	private static class TestThread extends Thread {
		
		private int index;
		
		public TestThread(int index) {
			this.index = index;
		}
		
		@Override
		public void run() {
			String response = HttpClientUtils.sendGetRequest("http://localhost:8080/getOrder?orderNumber=003");  
			OrderVo orderVo = JSON.parseObject(response, OrderVo.class);
			String salesOrderNumber = orderVo.getSalesOrderNumber();
			System.out.println("订单号==="+salesOrderNumber);
			System.out.println("第" + (index + 1) + "次请求，结果为：" + response);  
		}
		
	}
}
