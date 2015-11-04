package com.ca.flex.component;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.collections.IteratorUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lt;

/**
 * Created by tap on 10/19/15.
 */
public class GetPurchaseEventsComponent implements Callable {
    private final Logger logger = LoggerFactory.getLogger(GetPurchaseEventsComponent.class);
    private final static DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
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
        //FindIterable<Document> documents = collection.find(and(gt("inserted", "2015-10-19T23:00:00.000Z"), lt("inserted", "2015-10-19T23:20:45.952Z")));
        //FindIterable<Document> documents = collection.find(gt("inserted", "2015-10-19T10:00:00.000Z"));lt("inserted", "2015-10-19T23:20:45.952Z")));
        Date greaterThanDate = format.parse("2015-10-19T10:00:00Z");
        Bson greaterDate = gt("inserted", format.parse("2015-10-19T10:00:00Z"));
        //Bson greaterDateLong = gt("insertedLong", greaterThanDate.getTime());
        Long longDate = new Long("1445352477163");
        Bson greaterDateLong = gt("insertedLong", longDate);
        Bson lessDate = lt("inserted", format.parse("2015-10-20T00:00:00Z"));
        FindIterable<Document> iterableDocs = collection.find(greaterDateLong);
        logger.info("docs {} ",iterableDocs.toString());
        List<Document> documents = IteratorUtils.toList(iterableDocs.iterator());
        /*
        final AtomicInteger count = new AtomicInteger();
        iterableDocs.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                logger.info("doc {}",document.toJson());
                count.incrementAndGet();
            }
        });
        */
        logger.info("count {}",documents.size());
        return documents;
    }

}
