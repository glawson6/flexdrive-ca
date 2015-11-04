package com.ca.flex.component;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.bson.BasicBSONObject;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by tap on 10/28/15.
 */
public class HttpRequest implements Callable {
    private final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private final static DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);

    @Override
    public Object onCall(MuleEventContext eventContext) throws Exception {
        HttpResponse<String> response = Unirest.post("https://sandbox.api.manheim.com:443/oauth2/token")
                .header("authorization", "Basic ZHV1Ym11YWhteTdoYjRmdGNobm1uamNtOjQ0ZmNUdzJIZFk=")
                .header("cache-control", "no-cache")
                .header("postman-token", "c571d55d-5247-ff35-3d68-18e6a7b9ae57")
                .body("{\n    \"grant_type\":\"client_credentials\"\n}")
                .asString();
        Document jsonDoc = Document.parse(response.getBody());
        BasicBSONObject bsonObject = new BasicBSONObject("href","");
        return null;
    }

}
