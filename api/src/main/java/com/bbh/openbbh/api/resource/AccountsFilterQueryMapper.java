package com.bbh.openbbh.api.resource;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class AccountsFilterQueryMapper {

    static final String DEFAULT_LIMIT = "10";
    static final String DEFAULT_SKIP = "0";

    private final ImmutableMap<String, Object> criteria;
    private final ImmutableList<String> checked;
    private final int limit;
    private final int skip;

    public static AccountsFilterQueryMapper of(AccountsFilterQuery query) {

        Builder builder = new Builder();

        Map<String, Object> criteria = Maps.newTreeMap();
        AccountsFilterQuery.Filter filter = query.getFilter();
        if (filter != null) {
            addFieldToCriteria(criteria, "name", filter.getName());
            addFieldToCriteria(criteria, "number", filter.getNumber());
            addDisplayOptionToCriteria(criteria, filter.getDisplay(), filter.getChecked());
        }
        
        builder.setCriteria(ImmutableMap.copyOf(criteria));
        builderSetCheckedList(builder, query.getFilter());
        builderSetModifier(builder, query.getModifier());

        return builder.build();
    }
    
    private static void addFieldToCriteria(Map<String, Object> criteria, String field, String value) {
        if (!Strings.isNullOrEmpty(value)) {
            
            // String.format("{%s:{$regex:#}}", field);
            // String.format("^%s.*");
            //
            // new
            // StringBuilder("{").append(field).append(":{$regex:#}}").toString();
            // new StringBuilder("^").append(value).append(".*");
            
            // criteria.put("{" + field + ":{$regex:#}}", "^" + value + ".*");
            criteria.put("{" + field + ":#}", Pattern.compile("^" + value + ".*", Pattern.CASE_INSENSITIVE));
//            criteria.put("{" + field + ":#}", Pattern.compile(value + ".*"));
        }
    }

    private static void addDisplayOptionToCriteria(Map<String, Object> criteria, String display,
            String checked) {
        if (!Strings.isNullOrEmpty(display)) {
            String filterBySelection = "";
            if (display.equalsIgnoreCase("checked")) {
                filterBySelection = "{number:{$in:#}}"; // $in
            } else if (display.equalsIgnoreCase("unchecked")) {
                filterBySelection = "{number:{$nin:#}}"; // $not in
            }
            
            if (!Strings.isNullOrEmpty(filterBySelection)) {
                List<String> numbers = Lists.newArrayList(Splitter.on(",").trimResults()
                        .omitEmptyStrings().split(Strings.nullToEmpty(checked)));

                criteria.put(filterBySelection, numbers);
            }
        }
    }

    private static void builderSetCheckedList(Builder builder, AccountsFilterQuery.Filter filter) {
        ImmutableList<String> checkedList = ImmutableList.of();
        if (filter != null) {
            // checked account numbers
            checkedList = ImmutableList.copyOf(Splitter.on(",").trimResults().omitEmptyStrings()
                    .split(Strings.nullToEmpty(filter.getChecked())));
        }
        
        builder.setChecked(checkedList);
    }

    private static void builderSetModifier(Builder builder, AccountsFilterQuery.Modifier modifier) {
        String limit = DEFAULT_LIMIT;
        String skip = DEFAULT_SKIP;
        if (modifier != null) {
            limit = Optional.fromNullable(modifier.getLimit()).or(DEFAULT_LIMIT);
            skip = Optional.fromNullable(modifier.getSkip()).or(DEFAULT_SKIP);
        }
        builder.setLimit(Integer.valueOf(limit));
        builder.setSkip(Integer.valueOf(skip));
    }

    private AccountsFilterQueryMapper(ImmutableMap<String, Object> criteria,
            ImmutableList<String> checked, int limit, int skip) {
        this.criteria = criteria;
        this.checked = checked;
        this.limit = limit;
        this.skip = skip;
    }

    public int getLimit() {
        return limit;
    }

    public int getSkip() {
        return skip;
    }

    public Optional<String> getCriteria() {
        
        StringBuilder queryStrBuilder = new StringBuilder();
        Iterator<String> i = criteria.keySet().iterator();
        while (i.hasNext()) {
            String field = i.next();
            queryStrBuilder.append(field);
            if (i.hasNext()) {
                queryStrBuilder.append(",");
            }
        }

        String queryStr = null;
        if (queryStrBuilder.length() > 0) {
            // wrap everything in $and
            queryStr = new StringBuilder("{$and:[").append(queryStrBuilder.toString()).append("]}").toString();
        }
        
        return Optional.fromNullable(queryStr);
    }

    public Object[] getValues() {
        return criteria.values().toArray();
    }

    public Optional<String> getChecked() {
        
        Optional<String> val = Optional.absent();
        if (!checked.isEmpty()) {
            val = Optional.of(Joiner.on(",").join(checked));
        }
        
        return val;
    }

    private static class Builder {

        private ImmutableMap<String, Object> criteria;
        private ImmutableList<String> checked;
        private int limit;
        private int skip;

        public Builder() {}

        Builder setLimit(int limit) {
            this.limit = limit;
            return this;
        }

        Builder setSkip(int skip) {
            this.skip = skip;
            return this;
        }

        Builder setChecked(ImmutableList<String> checked) {
            this.checked = checked;
            return this;
        }
        
        Builder setCriteria(ImmutableMap<String, Object> criteria) {
            this.criteria = criteria;
            return this;
        }

        AccountsFilterQueryMapper build() {
            return new AccountsFilterQueryMapper(criteria, checked, limit, skip);
        }
    }

}
