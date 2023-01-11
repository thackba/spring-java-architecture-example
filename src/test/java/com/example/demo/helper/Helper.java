package com.example.demo.helper;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

public class Helper {

	public static MeterRegistry getMeterRegistry() {
		return new SimpleMeterRegistry();
	}

	public static long getCounterValue(MeterRegistry registry, String counterName) {
		return Math.round(registry.get(counterName).counter().count());
	}
}