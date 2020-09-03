package com.gemfire.caching;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class QuoteService {
    protected static final String
            ID_BASED_QUOTE_SERVICE_URL="http://gturnquist-quoters.cfapps.io/api/{id}";

    protected static final String
            RANDOM_BASED_QUOTE_SERVICE_URL="http://gturnquist-quoters.cfapps.io/api/random";

    private final RestTemplate quoteServiceTemplate = new RestTemplate();

    private volatile boolean cacheMiss = false;

    public boolean isCacheMiss(){
        boolean cacheMiss = this.cacheMiss;
        this.cacheMiss = false; //resetting for next read
        return cacheMiss;
    }

    protected void setCacheMiss(){
        this.cacheMiss = true;
    }

    /*
    requests a quote with specific quote id - caching using @Cacheable
     */
    @Cacheable("Quotes")
    public Quote requestQuote(Long id) {
        setCacheMiss();
        return requestQuote(ID_BASED_QUOTE_SERVICE_URL, Collections.singletonMap("id", id));
    }

    /*
    request a random quote

    #request.id SPEL expression to access the result of the service method
    invocation
     */
    @CachePut(cacheNames = "Quotes", key = "#result.id")
    public Quote requestRandomQuote(){
        setCacheMiss();
        return requestQuote(RANDOM_BASED_QUOTE_SERVICE_URL);
    }

    protected Quote requestQuote(String URL){
        return requestQuote(URL, Collections.emptyMap());
    }

    protected Quote requestQuote(String URL, Map<String, Object> urlVariables) {
        return Optional.ofNullable(this.quoteServiceTemplate
                .getForObject(URL, QuoteResponse.class, urlVariables))
                .map(QuoteResponse::getQuote)
                .orElse(null);
    }
}
