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
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {
	private static final Logger logger = LoggerFactory.getLogger(GlobalFilter.class);
    public GlobalFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        //Global Pre Filter
        return ((exchange, chain)->{
            ServerHttpRequest request=exchange.getRequest();
            ServerHttpResponse response=exchange.getResponse();
            logger.info("remote Address = " + request.getRemoteAddress());
            logger.info("요청 주소 = " + request.getURI());
            logger.info("Global Filter baseMessage:{}",config.getBaseMessage());
            if(config.isPreLogger()){
                logger.info("Global Filter Start: request id -> {}", request.getId());
            }

            //Global Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if(config.isPostLogger()){ //
                    logger.info("Global Filter End: response id -> {}", response.getStatusCode());
                }
            }));
        });
    }

    //getter와 setter가 만들어 집니다.
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
