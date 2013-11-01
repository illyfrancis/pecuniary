package com.bbh.openbbh.api.resource;

import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;

public class QueryBuilder {
    
    private final String selections;

    public QueryBuilder(String selections) {
        this.selections = selections;
    }

    public QueryBuilder addCriteria(String field, String value) {
        return this;
    }

    public QueryBuilder addPartial(String field, String value) {

        return this;
    }

    public String build() {
        Optional<String> accountNumbers = Optional.fromNullable(selections);
        if (accountNumbers.isPresent()) {
            List<String> numbersList = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(accountNumbers.get());
        }
        
        return "";
    }
}
