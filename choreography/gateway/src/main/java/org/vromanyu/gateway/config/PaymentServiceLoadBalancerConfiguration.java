package org.vromanyu.gateway.config;

import org.jspecify.annotations.NonNull;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Flux;

import java.util.List;

public class PaymentServiceLoadBalancerConfiguration {


    @Bean
    @Profile("!prod")
    public ServiceInstanceListSupplier serviceInstanceListSupplier() {
        return new InstanceSupplier("payment-ms", "localhost", 8086);
    }


    @Bean
    @Profile("prod")
    public ServiceInstanceListSupplier paymentServiceInstanceProdSupplier() {
        return new InstanceSupplier("payment-ms", "payment-ms", 8086);
    }

    static class InstanceSupplier implements ServiceInstanceListSupplier {

        private final String serviceId;
        private final String host;
        private final int port;

        public InstanceSupplier(String serviceId, String host, int port) {
            this.serviceId = serviceId;
            this.host = host;
            this.port = port;
        }

        @Override
        public @NonNull String getServiceId() {
            return serviceId;
        }

        @Override
        public Flux<List<ServiceInstance>> get() {
            return Flux.just(List.of(
                    new DefaultServiceInstance(serviceId + "1", serviceId, host, port, false)
            ));
        }
    }

}
