package com.pulsefit.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;

public class FeignClientConfig {

	@Bean
	public RequestInterceptor bearerTokenRequestInterceptor() {

	    return requestTemplate -> {

	        ServletRequestAttributes attributes =
	                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

	        if (attributes != null) {

	            String authorization =
	                    attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);

	            if (authorization != null && !authorization.isBlank()) {

	                System.out.println("Feign Authorization header found");

	                requestTemplate.header(
	                        HttpHeaders.AUTHORIZATION,
	                        authorization
	                );

	            } else {

	                System.out.println("Feign Authorization header NOT found");
	            }
	        }
	    };
	}
}