package com.gemfire.caching;

import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.cache.config.EnableGemfireCaching;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions;

import java.util.Optional;

/*
Spring Caching Abstration:

@EnableGemfireCaching annotation
enables Pivotal GemFire/ Apache Geode as a Caching Provider in
Spring Cache's Abstraction.

@ClientCacheApplication annotation
enables a Spring Data GemFire/Geode based application to become
a GemFire/Geode cache client(client cache).

@EnableCachingDefinedRegions annotation
marks a Spring @Configuration application annotated class
to enable the creation of Pivotal GemFire/Apache Geode Regions
based on Spring's Cache Abstraction Annotations applied to
application service methods and types.

Additionally, this annotation enables Spring's cache abstraction with SDG's
EnableGemfireCaching annotation, which declares Spring's EnableCaching
annotation as well as declares the SDG GemfireCacheManager bean definition.

ClientRegionShortcut - predefined RegionAttributes in a client cache
ENUM

ClientRegionShortcut.LOCAL
A LOCAL region only has local state & never sends operations to a server.

 */
@SpringBootApplication
@EnableGemfireCaching
@ClientCacheApplication(name = "CachingGemfireApplication", logLevel = "debug")
@EnableCachingDefinedRegions(clientRegionShortcut = ClientRegionShortcut.LOCAL)
public class CachingGemfireApplication /*implements ApplicationRunner*/ {

	public static void main(String[] args) {
		SpringApplication.run(CachingGemfireApplication.class, args);
	}

	@Bean
	ApplicationRunner runner(QuoteService quoteService) {
		return args  -> {
			Quote quote = requestQuote(quoteService, 12L);
			requestQuote(quoteService, quote.getId());
			requestQuote(quoteService, 10L);
			requestQuote(quoteService, null);
			requestQuote(quoteService, null);
			requestQuote(quoteService, null);
			requestQuote(quoteService, null);
			requestQuote(quoteService, null);requestQuote(quoteService, null);requestQuote(quoteService, null);
			requestQuote(quoteService, null);
			requestQuote(quoteService, 4L);
			requestQuote(quoteService, 10L);
			requestQuote(quoteService, 10L);




		};
	}

	private Quote requestQuote(QuoteService quoteService, Long id) {
		long startTime = System.currentTimeMillis();
		Quote quote = Optional.ofNullable(id)
						.map(quoteService::requestQuote)
						.orElseGet(quoteService::requestRandomQuote);
		long endTime = System.currentTimeMillis();
		System.out.printf("\"%1$s\"%nCache Miss [%2$s] - Elapsed Time [%3$s ms]%n", quote,
				quoteService.isCacheMiss(), (endTime - startTime));

		return quote;
	}

}
