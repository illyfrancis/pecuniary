package com.bbh.openbbh.api.dao;

import com.google.common.collect.Lists;

import java.util.List;

import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import com.google.common.base.Optional;

public class Accounts {

    private static MongoCollection accounts;

    static {
        Jongo jongo = new Jongo(MongoDB.getDB());
        accounts = jongo.getCollection("accounts");
    }

    public static List<Model> get() {
        return Lists.newArrayList(accounts.find()
                .limit(2000) // there are 5408 but it's too big
                .as(Model.class));
    }

    public static List<Model> get(Integer limit, Integer skip) {
        return Lists.newArrayList(accounts.find()
                .skip(skip)
                .limit(limit)
                .as(Model.class));
    }

    public static List<Model> findBy(Optional<String> number, Optional<String> name,
            Optional<String> include, Integer limit, Integer skip) {

        List<String> numbers = Lists.newArrayList("0150110", "0153452", "12345");

        return Lists.newArrayList(accounts.find(
                // "{name: {$regex: #}, number: {$regex: #}}",
                // "^ING.*", "^015.*")
                // "{name: {$regex: #}, number: {$in: #}}",
                // "^ING.*", numbers
                "{$and: [{name: {$regex: #}}, {number: {$regex: #}}, {number: {$in: #}}] }",
                "ING.*",
                "^015.*",
                numbers
                )
                .skip(skip)
                .limit(limit)
                .as(Model.class));
    }

    public static List<Model> findBy(Optional<String> criteria, Object[] values, int limit, int skip) {
        return Lists.newArrayList(accounts.find(criteria.or("{}"), values)
                .skip(skip)
                .limit(limit)
                .sort("{number: 1}")
                .as(Model.class));
    }

    public static List<Model> findBy(Optional<String> criteria, Object[] values) {
        return Lists.newArrayList(accounts.find(criteria.or("{}"), values).as(Model.class));
    }

    public static long count(Optional<String> criteria, Object[] values) {
        return accounts.count(criteria.or("{}"), values);
    }

    public static long count() {
        return accounts.count();
    }

    public static Model get(String id) {
        if (ObjectId.isValid(id)) {
            return accounts.findOne(new ObjectId(id)).as(Model.class);
        }
        else {
            return null;
        }
    }

    public static Model put(Model account) {
        accounts.save(account);
        return account;
    }

    public static void delete(String id) {
        accounts.remove(new ObjectId(id));
    }

    public static class Model {
        // ObjectId _id;
        String number;
        String name;
        Boolean selected;

        public String getNumber() {
            return number;
        }

        public void select(boolean state) {
            selected = Boolean.valueOf(state);
        }
    }
}
