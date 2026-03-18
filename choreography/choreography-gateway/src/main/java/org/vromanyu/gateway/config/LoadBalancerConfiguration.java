package org.vromanyu.gateway.config;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@LoadBalancerClients(value = {
        @LoadBalancerClient(name = "order-ms", configuration = OrderServiceLoadBalancerConfiguration.class),
        @LoadBalancerClient(name = "payment-ms", configuration = PaymentServiceLoadBalancerConfiguration.class)
})
public class LoadBalancerConfiguration {
}
