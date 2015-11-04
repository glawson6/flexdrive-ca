package com.ca.flex.component;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by tap on 10/19/15.
 */
public class PersistEventComponent implements Callable {
    private final Logger logger = LoggerFactory.getLogger(PersistEventComponent.class);

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
        String collectionName = eventContext.getMessage().getOutboundProperty("collection","DLQ");
        logger.info("Using collection {}",collectionName);
        MongoDatabase database = mongoClient.getDatabase("flexdrive");
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document jsonDocument = (Document)eventContext.getMessage().getPayload();
        Date now = new Date();
        jsonDocument.append("inserted",now);
        jsonDocument.append("insertedLong",now.getTime());
        collection.insertOne(jsonDocument);
        return jsonDocument;
    }
}
