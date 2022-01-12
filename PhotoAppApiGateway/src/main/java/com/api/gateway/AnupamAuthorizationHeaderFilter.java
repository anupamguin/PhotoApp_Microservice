package com.api.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.google.common.net.HttpHeaders;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import reactor.core.publisher.Mono;

@Component /*
			 * so that spring framework can create an instance of it and place it in the
			 * application context at the time when our application starts up
			 */
public class AnupamAuthorizationHeaderFilter
		extends AbstractGatewayFilterFactory<AnupamAuthorizationHeaderFilter.Config> {

	@Value("${token.secret}")
	private String secretKey;

	public AnupamAuthorizationHeaderFilter() {
		super(Config.class); // this constructor tells the super class which config class to use
	}

	public static class Config {
		// put cofiguration properties here
	}

	@Override
	public GatewayFilter apply(Config config) {

		return (exchange, chain) -> {

			ServerHttpRequest request = exchange.getRequest();

			if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				return onError(exchange, "No Authorization Header", HttpStatus.UNAUTHORIZED);
			}

			String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			String jwt = authorizationHeader.substring(7).trim();

			System.err.println("path=" + request.getURI().getPath());

			if (!isJwtValid(jwt)) {
				return onError(exchange, "JWT token invalid", HttpStatus.UNAUTHORIZED);
			}
			return chain.filter(exchange);
		};
	}

	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		return response.setComplete();
	}

	private boolean isJwtValid(String jwt) {
		boolean returnValue = true;
		Claims claims;
		String subject = null;

		try {
			claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
			subject = claims.getSubject();
		} catch (Exception ex) {
			System.out.println(ex);
			returnValue = false;
		}
		if (subject == null || subject.isEmpty()) {
			returnValue = false;
		}
		return returnValue;
	}

}
