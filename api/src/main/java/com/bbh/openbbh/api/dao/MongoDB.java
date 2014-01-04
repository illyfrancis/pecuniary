package com.bbh.openbbh.api.dao;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

class MongoDB {
	
	static MongoClient mongo;

    static {
        try {
<<<<<<< HEAD
            mongo = new MongoClient("127.0.0.1", 27017);
=======
            String port = System.getProperty("db-port", "27017");
            mongo = new MongoClient("127.0.0.1", (new Integer(port)).intValue());
>>>>>>> 2677b0d75b0959762136978f5b15485e5a5d33cf
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (MongoException e) {
            throw new RuntimeException(e);
        }
    }

    static DB getDB() {
    	return mongo.getDB("openbbh");
    }
}
