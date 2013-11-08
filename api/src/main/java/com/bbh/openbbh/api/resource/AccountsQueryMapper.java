package com.bbh.openbbh.api.resource;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class AccountsQueryMapper {

    static final String DEFAULT_LIMIT = "10";
    static final String DEFAULT_SKIP = "0";

    private final ImmutableMap<String, Object> _filter_criteria;
    private final ImmutableList<String> checked;
    private final int limit;
    private final int skip;

    public static AccountsQueryMapper of(Query query) {
        return of(query, null);
    }

    public static AccountsQueryMapper of(Query query, String checked) {
        Builder builder = new Builder();
        Map<String, Object> filter = Maps.newTreeMap();

        AccountsQueryMapper.Criteria criteria = parseAccountCriteria(query.criteria);
        addFieldToFilter(filter, "name", criteria.name);
        addFieldToFilter(filter, "number", criteria.number);

        String checkedString = Optional.fromNullable(checked).or(criteria.checked);
        addDisplayOptionToFilter(filter, criteria.display, checkedString);

        ImmutableList<String> checkedList = ImmutableList.copyOf(Splitter.on(",").trimResults()
                .omitEmptyStrings().split(Strings.nullToEmpty(checkedString)));

        builder.setChecked(checkedList);
        builder.setFilter(ImmutableMap.copyOf(filter));
        builder.setLimit(Integer.valueOf(Optional.fromNullable(query.limit).or(DEFAULT_LIMIT)));
        builder.setSkip(Integer.valueOf(Optional.fromNullable(query.skip).or(DEFAULT_SKIP)));

        return builder.build();
    }

    static AccountsQueryMapper.Criteria parseAccountCriteria(String criteriaJson) {
        ObjectMapper mapper = new ObjectMapper(); // TODO - can this be
                                                  // injected?
        AccountsQueryMapper.Criteria criteria = AccountsQueryMapper.Criteria.empty();
        try {
            criteria = mapper.readValue(Optional.fromNullable(criteriaJson).or("{}"),
                    AccountsQueryMapper.Criteria.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return criteria;
    }

    private static void addFieldToFilter(Map<String, Object> criteria, String field, String value) {
        if (!Strings.isNullOrEmpty(value)) {
            criteria.put("{" + field + ":#}",
                    Pattern.compile("^" + value + ".*", Pattern.CASE_INSENSITIVE));
        }
    }

    private static void addDisplayOptionToFilter(Map<String, Object> criteria, String display,
            String checked) {
        if (!Strings.isNullOrEmpty(display)) {
            String filterBySelection = "";
            if (display.equalsIgnoreCase("checked")) {
                filterBySelection = "{number:{$in:#}}";
            } else if (display.equalsIgnoreCase("unchecked")) {
                filterBySelection = "{number:{$not:{$in:#}}}";
            }

            if (!Strings.isNullOrEmpty(filterBySelection)) {
                List<String> numbers = Lists.newArrayList(Splitter.on(",").trimResults()
                        .omitEmptyStrings().split(Strings.nullToEmpty(checked)));

                criteria.put(filterBySelection, numbers);
            }
        }
    }

    private AccountsQueryMapper(ImmutableMap<String, Object> criteria,
            ImmutableList<String> checked, int limit, int skip) {
        this._filter_criteria = criteria;
        this.checked = checked;
        this.limit = limit;
        this.skip = skip;
    }

    public int limit() {
        return limit;
    }

    public int skip() {
        return skip;
    }

    public Optional<String> queryString() {

        StringBuilder queryStrBuilder = new StringBuilder();
        Iterator<String> i = _filter_criteria.keySet().iterator();
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
            queryStr = new StringBuilder("{$and:[").append(queryStrBuilder.toString()).append("]}")
                    .toString();
        }

        return Optional.fromNullable(queryStr);
    }

    public Object[] queryParams() {
        return _filter_criteria.values().toArray();
    }

    public Optional<String> getChecked() {

        Optional<String> val = Optional.absent();
        if (!checked.isEmpty()) {
            val = Optional.of(Joiner.on(",").join(checked));
        }

        return val;
    }

    public Set<String> checkedAccountNumbers() {
        return Sets.newHashSet(checked);
    }

    public static Query createQueryWith(Query query, Set<String> selectedAccountNumbers) {

        AccountsQueryMapper.Criteria criteria = parseAccountCriteria(query.criteria);
        criteria.checked = Joiner.on(",").join(selectedAccountNumbers);

        Query copyOf = new Query(query);
        // copyOf.criteria = criteria; // TODO

        // TODO Auto-generated method stub
        return copyOf;
    }

    private static class Builder {

        private ImmutableMap<String, Object> filter;
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

        Builder setFilter(ImmutableMap<String, Object> filter) {
            this.filter = filter;
            return this;
        }

        AccountsQueryMapper build() {
            return new AccountsQueryMapper(filter, checked, limit, skip);
        }
    }

    static class Criteria {
        public String number;
        public String name;
        public String display;
        public String checked;

        public static Criteria empty() {
            return new Criteria();
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("number", number)
                    .add("name", name)
                    .add("display", display)
                    .add("checked", checked)
                    .toString();
        }
    }
}
