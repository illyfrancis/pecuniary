package com.bbh.openbbh.api.dao;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import com.bbh.openbbh.api.resource.TransactionResource.Model;
import com.bbh.openbbh.api.resource.TransactionResource.Query;

public class Transactions {

	private static MongoCollection transactions;

	static {
		Jongo jongo = new Jongo(MongoDB.getDB());
		transactions = jongo.getCollection("transactions");
	}

	public static List<Model> find() {
		return newArrayList(transactions.find().as(Model.class));
	}

	public static long count(Query query) {
<<<<<<< HEAD
		return transactions.count(query.getCriteria());
=======
		return transactions.count(query.getCriteria(),
				query.getTemplateParams());
>>>>>>> 2677b0d75b0959762136978f5b15485e5a5d33cf
	}
	
	public static List<Model> findBy(Query query) {
		// assume query is structured like
		// { 
		//	criteria: { ... },
		//	fields: { ... },
		//	sort: { ... }
		//	limit: 10
		//	offset: 1
		// }
<<<<<<< HEAD
		return newArrayList(transactions.find(query.getCriteria())
=======
		return newArrayList(transactions.find(
				query.getCriteria(), query.getTemplateParams())		
>>>>>>> 2677b0d75b0959762136978f5b15485e5a5d33cf
				.projection(query.getFields())
				.sort(query.getSort())
				.skip(query.getSkipOffset())	// TODO - apparently skip() is very inefficient...
				.limit(query.getLimit())
				.as(Model.class));
	}

	public static Model get(String id) {
		if (ObjectId.isValid(id)) {
			return transactions.findOne(new ObjectId(id)).as(Model.class);
		}
		else {
			return null;
		}
	}

	public static Model put(Model transaction) {
		transactions.save(transaction);
		return transaction;
	}

	public static void delete(String id) {
		transactions.remove(new ObjectId(id));
	}
}
