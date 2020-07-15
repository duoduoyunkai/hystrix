package cache.hystrix.command;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;

import cache.hystrix.model.OrderVo;
import cache.hystrix.util.HttpClientUtils;

/**
 * @author Administrator
 *
 */
public class GetOrderInfosCommand extends HystrixObservableCommand<OrderVo> {

	private String[] orderNumbers;
	
	public GetOrderInfosCommand(String[] orderNumbers) {
		super(HystrixCommandGroupKey.Factory.asKey("GetOrderInfoGroup"));
		this.orderNumbers = orderNumbers;
	}
	
	@Override
	protected Observable<OrderVo> construct() {
		return Observable.create(new Observable.OnSubscribe<OrderVo>() {

			public void call(Subscriber<? super OrderVo> observer) {
				try {
					for(String orderNumber : orderNumbers) {
						String url = "http://127.0.0.1:8082/orders?orderNumber==" + orderNumber;
						String response = HttpClientUtils.sendGetRequest(url);
						OrderVo orderVo = JSONObject.parseObject(response, OrderVo.class); 
						observer.onNext(orderVo); 
					}
					observer.onCompleted();
				} catch (Exception e) {
					observer.onError(e);  
				}
			}
			
		}).subscribeOn(Schedulers.io());
	}

}
