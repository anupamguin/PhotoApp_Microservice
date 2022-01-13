package com.api.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import reactor.core.publisher.Mono;

@Configuration
public class MyGlobalFilter {

	final Logger logger=LoggerFactory.getLogger(MyGlobalFilter.class);
	
	@Order(1)
	@Bean
	public GlobalFilter secondPreFilter() {
		return(exchange,chain)->{
		
			logger.warn("Second Global Pre-filter work....");
			
			return chain.filter(exchange).then(Mono.fromRunnable(()->{
				logger.error("My Second Global Post-filter Working Well.....");
			}));
		};
	}
	
	@Order(2)
	@Bean
	public GlobalFilter secondthirdFilter() {
		return(exchange,chain)->{
		
			logger.warn("Third Global Pre-filter work....");
			
			return chain.filter(exchange).then(Mono.fromRunnable(()->{
				logger.error("My First Global Post-filter Working Well.....");
			}));
		};
	}
}

/*
 This MyGlobalFilter is created for Write Prefilter & postFilter in one Class.
 By the above logger message we can understood that which area for which Filter.
 Now without @Order if we use the which filter run first that we don't know so we need to set
 it's order.
 
 Now in this Order concept if a prefilter order is 0 means it is execute first.
 if a prefilter order is 1 means it is execute next .Means order small means execute first
 
 PreFilter  0		|
 			1		|
 			2		v
 		   In this Order
 		   
 But if a postfilter order is 0 that means it is execute last. 
 if a postfilter order is 1 means it is excute before , means order big means execute first.
 
 PostFilter 0		M
 			1		|
 			2		|
 		  In this Order
 
 ------------------------------------
 
 But For the MyPreFilter class , to do the same thing we have to implements Orderded
 and override it's method and set it's order 
 */