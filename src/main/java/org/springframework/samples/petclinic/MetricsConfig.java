package org.springframework.samples.petclinic;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.jvm.ClassLoadingGaugeSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.netflix.hystrix.contrib.codahalemetricspublisher.HystrixCodaHaleMetricsPublisher;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;

@Configuration
@EnableMetrics(proxyTargetClass=true)
public class MetricsConfig extends MetricsConfigurerAdapter {

	@Value("${metrics.graphite.host}")
	private String graphiteHost;

	@Value("${metrics.graphite.port}")
	private int graphitePort;

	@Value("${metrics.prefix}")
	private String prefix;

	@Override
    public void configureReporters(MetricRegistry metricRegistry) {
		metricRegistry.registerAll(new MemoryUsageGaugeSet());
		metricRegistry.registerAll(new GarbageCollectorMetricSet());
		metricRegistry.registerAll(new ThreadStatesGaugeSet());
		metricRegistry.registerAll(new ClassLoadingGaugeSet());
		HystrixPlugins.getInstance().registerMetricsPublisher(new HystrixCodaHaleMetricsPublisher(metricRegistry));
		final Graphite graphite = new Graphite(new InetSocketAddress(graphiteHost, graphitePort));
		final GraphiteReporter reporter = GraphiteReporter.forRegistry(metricRegistry).prefixedWith(prefix).build(graphite);
        // registerReporter allows the MetricsConfigurerAdapter to
        // shut down the reporter when the Spring context is closed
		registerReporter(reporter).start(2000, TimeUnit.MILLISECONDS);
    }
}
