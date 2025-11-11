package org.springframework.samples.petclinic.api.config;

import org.junit.jupiter.api.Test;

import javax.cache.configuration.Configuration;
import javax.cache.configuration.MutableConfiguration;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CacheConfigurationTest {

	@Test
	void testCacheConfiguration() throws Exception {
		CacheConfiguration cacheConfiguration = new CacheConfiguration();

		Method method = CacheConfiguration.class.getDeclaredMethod("cacheConfiguration");
		method.setAccessible(true);

		Configuration<Object, Object> result = (Configuration<Object, Object>) method.invoke(cacheConfiguration);

		assertNotNull(result);
		assertTrue(result instanceof MutableConfiguration);

		MutableConfiguration<Object, Object> mutableConfig = (MutableConfiguration<Object, Object>) result;
		assertTrue(mutableConfig.isStatisticsEnabled());
	}
}
