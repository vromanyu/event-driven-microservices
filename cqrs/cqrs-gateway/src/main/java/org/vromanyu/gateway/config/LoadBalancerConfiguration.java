package org.vromanyu.gateway.config;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@LoadBalancerClients(value = {
        @LoadBalancerClient(name = "write-ms", configuration = WriteServiceLoadBalancerConfiguration.class),
        @LoadBalancerClient(name = "query-ms", configuration = QueryServiceLoadBalancerConfiguration.class)
})
public class LoadBalancerConfiguration {
}
