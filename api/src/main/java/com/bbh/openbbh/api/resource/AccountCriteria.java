package com.bbh.openbbh.api.resource;

import com.google.common.base.Objects;

public class AccountCriteria {
    public String number;
    public String name;
    public String display;
    public String checked;

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
