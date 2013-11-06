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

    public static List<Model> findBy(Optional<String> queryString, Object[] queryParams, int limit, int skip) {
        return Lists.newArrayList(accounts.find(queryString.or("{}"), queryParams)
                .skip(skip)
                .limit(limit)
                .sort("{number: 1}")
                .as(Model.class));
    }

    public static List<Model> findBy(Optional<String> queryString, Object[] queryParams) {
        return Lists.newArrayList(accounts.find(queryString.or("{}"), queryParams).as(Model.class));
    }

    public static long count(Optional<String> queryString, Object[] queryParams) {
        return accounts.count(queryString.or("{}"), queryParams);
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
