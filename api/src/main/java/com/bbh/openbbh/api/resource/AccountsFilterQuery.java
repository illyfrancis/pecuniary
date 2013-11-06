package com.bbh.openbbh.api.resource;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

class AccountsFilterQuery {

    private Filter filter;
    private Modifier modifier;

    Filter getFilter() {
        return this.filter;
    }

    Modifier getModifier() {
        return this.modifier;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("filter", filter).add("modifier", modifier).toString();
    }

    static class Filter {
        private String number;
        private String name;
        private String display;
        private String checked;

        String getNumber() {
            return number;
        }

        String getName() {
            return name;
        }

        String getDisplay() {
            return display;
        }

        String getChecked() {
            return checked;
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("number", number).add("name", name).add("display", display)
                    .add("checked", checked).toString();
        }

    }

    static class Modifier {
        private String limit;
        private String skip;

        String getLimit() {
            return limit;
        }

        String getSkip() {
            return skip;
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("limit", limit).add("skip", skip).toString();
        }
    }

    static class Builder {
        private String limit;
        private String skip;
        private String number;
        private String name;
        private String display;
        private String checked;

        public Builder() {}

        public Builder setLimit(String limit) {
            this.limit = limit;
            return this;
        }

        public Builder setSkip(String skip) {
            this.skip = skip;
            return this;
        }

        public Builder setNumber(String number) {
            this.number = number;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDisplay(String display) {
            this.display = display;
            return this;
        }

        public Builder setChecked(String checked) {
            this.checked = checked;
            return this;
        }

        public AccountsFilterQuery build() {
            AccountsFilterQuery query = new AccountsFilterQuery();

            if (limit != null || skip != null) {
                query.modifier = new Modifier();
                query.modifier.limit = limit != null ? limit.toString() : null;
                query.modifier.skip = skip != null ? skip.toString() : null;
            }

            if (!Strings.isNullOrEmpty(number) || !Strings.isNullOrEmpty(name)
                    || !Strings.isNullOrEmpty(display) || !Strings.isNullOrEmpty(checked)) {
                query.filter = new Filter();
                query.filter.number = number;
                query.filter.name = name;
                query.filter.display = display;
                query.filter.checked = checked;
            }

            return query;
        }
    }
}