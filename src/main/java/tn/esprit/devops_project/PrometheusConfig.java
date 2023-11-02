package tn.esprit.devops_project;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class PrometheusConfig {
    private static final CollectorRegistry registry = CollectorRegistry.defaultRegistry;


    // Define your custom metrics
    public static final Counter requestsTotal = Counter.build()
            .name("myapp_requests_total")
            .help("Total HTTP requests made")
            .register(registry);

    public static final Histogram requestLatency = Histogram.build()
            .name("myapp_request_latency_seconds")
            .help("Request latency in seconds")
            .register(registry);
}

