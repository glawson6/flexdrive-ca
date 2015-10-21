package com.ca.flex.component;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tap on 10/19/15.
 */
public class PersistPurchaseEventComponent implements Callable {
    private final Logger logger = LoggerFactory.getLogger(PersistPurchaseEventComponent.class);

    @Autowired
    private  MongoClient mongoClient;

    @Override
    public Object onCall(MuleEventContext eventContext) throws Exception {
        /*
        ServerAddress serverAddress = new ServerAddress("localhost" , 27017 );
        MongoCredential mongoCredential = MongoCredential.createCredential("flexAdmin", "flexdrive", "flexAdmin".toCharArray());
        List<MongoCredential> mongoCredentialList = new ArrayList<>();
        mongoCredentialList.add(mongoCredential);
        MongoClient mongoClient = new MongoClient(serverAddress, mongoCredentialList);
        */
        MongoDatabase database = mongoClient.getDatabase("flexdrive");
        MongoCollection<Document> collection = database.getCollection("OFFERINGS.PURCHASED");
        Document jsonDocument = (Document)eventContext.getMessage().getPayload();
        Date now = new Date();
        jsonDocument.append("inserted",now);
        jsonDocument.append("insertedLong",now.getTime());
        collection.insertOne(jsonDocument);
        return jsonDocument;
    }
}
