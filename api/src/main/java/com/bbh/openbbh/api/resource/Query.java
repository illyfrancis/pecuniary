package com.bbh.openbbh.api.resource;

import com.google.common.base.Objects;

public class Query {
    public String criteria;
    public String limit;
    public String skip;
    public String sort;
    public String fields;
    
    public Query() {
    }
    
    public Query(Query orig) {
        this.criteria = orig.criteria;
        this.limit = orig.limit;
        this.skip = orig.skip;
        this.sort = orig.sort;
        this.fields = orig.fields;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("criteria", criteria).add("limit", limit).add("skip", skip).add("sort", sort)
                .add("fields", fields).omitNullValues().toString();
    }
}
