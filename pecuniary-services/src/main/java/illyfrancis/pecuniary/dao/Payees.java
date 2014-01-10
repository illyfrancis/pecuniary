package illyfrancis.pecuniary.dao;

import static com.google.common.collect.Lists.*;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

public class Payees {

    private static MongoCollection payees;

    static {
        Jongo jongo = new Jongo(MongoDB.mongo.getDB("pecuniary"));
        payees = jongo.getCollection("payees");
    }

    public static List<Payee> get() {
        return newArrayList(payees.find()
                .limit(2000) // there are 5408 but it's too big
                .as(Payee.class));
    }

    public static Payee get(String id) {
        if (ObjectId.isValid(id)) {
            return payees.findOne(new ObjectId(id)).as(Payee.class);
        }
        else {
            return null;
        }
    }

    public static Payee put(Payee payee) {
        payees.save(payee);
        return payee;
    }

    public static void delete(String id) {
        payees.remove(new ObjectId(id));
    }

    public static class Payee {
        ObjectId _id;
        String name;
        String phone;
        String accountNumber;
        Date lastPaid;
    }
}
