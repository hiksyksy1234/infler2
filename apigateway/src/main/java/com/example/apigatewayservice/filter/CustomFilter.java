package com.example.apigatewayservice.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {
	private static final Logger logger = LoggerFactory.getLogger(CustomFilter.class);
    public CustomFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        //Custom Pre Filter

        return (exchange, chain)->{
            ServerHttpRequest request=exchange.getRequest(); //pre 필터
            ServerHttpResponse response=exchange.getResponse(); //post 필터
            logger.info("remote Address = " + request.getRemoteAddress());
            logger.info("요청 주소 = " + request.getURI());
            logger.info("Custom PRE filter: request id -> {}",request.getId());

            //Custom Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                logger.info("Custom POST filter: response code" +  "id -> {}",response.getStatusCode());
            }));
        };
    }

    public static class Config{

    }
}
