package org.springframework.samples.petclinic.web.config;

import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.web.config.CacheConfiguration;

import javax.cache.configuration.Configuration;
import javax.cache.configuration.MutableConfiguration;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CacheConfigurationTest {

	@Test
	void testCacheConfiguration() throws Exception {
		// Create an instance of the class containing the private method
		CacheConfiguration cacheConfiguration = new CacheConfiguration();

		// Use reflection to access the private method
		Method method = CacheConfiguration.class.getDeclaredMethod("cacheConfiguration");
		method.setAccessible(true);

		// Invoke the private method
		Configuration<Object, Object> result = (Configuration<Object, Object>) method.invoke(cacheConfiguration);

		// Assert the result is not null and is of the expected type
		assertNotNull(result);
		assertTrue(result instanceof MutableConfiguration);

		// Assert that statistics are enabled
		MutableConfiguration<Object, Object> mutableConfig = (MutableConfiguration<Object, Object>) result;
		assertTrue(mutableConfig.isStatisticsEnabled());
	}
}
