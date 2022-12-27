package com.example.apigatewayservice.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

//@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {
	private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    public LoggingFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        //Logging Pre Filter
        return (exchange, chain)->{
            ServerHttpRequest request=exchange.getRequest(); //pre 필터
            ServerHttpResponse response=exchange.getResponse(); //post 필터

            logger.info("Logging PRE filter: request id -> {}",config.getBaseMessage());
            if(config.isPreLogger()) {
            	logger.info("Logging PRE filter: request uri -> {}", request.getURI());
            }

            //Logging Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
            	if(config.postLogger)
                 logger.info("Logging POST filter: response code" +  "id -> {}",response.getStatusCode());
            }));
        };
    }

    public static class Config{
    	private String baseMessage;
    	private boolean preLogger;
    	private boolean postLogger;
    	public String getBaseMessage() {
			return baseMessage;
		}
		public void setBaseMessage(String baseMessage) {
			this.baseMessage = baseMessage;
		}
		public boolean isPreLogger() {
			return preLogger;
		}
		public void setPreLogger(boolean preLogger) {
			this.preLogger = preLogger;
		}
		public boolean isPostLogger() {
			return postLogger;
		}
		public void setPostLogger(boolean postLogger) {
			this.postLogger = postLogger;
		}
		
    }
}
