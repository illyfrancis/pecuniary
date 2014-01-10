package illyfrancis.pecuniary.dao;

import static com.google.common.collect.Lists.*;

import java.util.List;

import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

public class Accounts {

    private static MongoCollection accounts;

    static {
        Jongo jongo = new Jongo(MongoDB.mongo.getDB("pecuniary"));
        accounts = jongo.getCollection("accounts");
    }

    public static List<Account> get() {
        return newArrayList(accounts.find()
                .limit(2000) // there are 5408 but it's too big
                .as(Account.class));
    }

    public static Account get(String id) {
        if (ObjectId.isValid(id)) {
            return accounts.findOne(new ObjectId(id)).as(Account.class);
        }
        else {
            return null;
        }
    }

    public static Account put(Account account) {
        accounts.save(account);
        return account;
    }

    public static void delete(String id) {
        accounts.remove(new ObjectId(id));
    }

    public static class Account {
        ObjectId _id;
        String number;
        String name;
    }
}