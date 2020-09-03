package com.gemfire.caching;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {
    private Long id;
    private String quote;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quote that = (Quote) o;
        return ObjectUtils.nullSafeEquals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        int hashValue = 17;
        hashValue = 37 * hashValue + ObjectUtils.nullSafeHashCode(getId());
        return hashValue;
    }

    @Override
    public String toString() {
        return getQuote();
    }
}
