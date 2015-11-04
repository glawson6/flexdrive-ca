@Grab('com.mashape.unirest:unirest-java:1.4.7')
@Grab('org.mongodb:bson:3.0.4')
@GrabConfig(systemClassLoader=true)

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest
import groovy.transform.BaseScript;
import org.bson.Document
import org.bson.json.JsonWriterSettings;

@BaseScript Tools tools

String authFilename = (null != System.getProperty("auth"))?System.getProperty("auth"):null;
String authorization = resolveBasicAuth(authFilename);
String accessToken = getAccessToken(authorization);

String operationURL = "https://${getAPIHost()}:443/subscribers/mine";
System.out.println("Sending operation to url => ${operationURL}")
HttpResponse<String> subscriberResponse = Unirest.get(operationURL)
        .header("authorization", "Bearer "+accessToken)
        .header("cache-control", "no-cache")
        .header("postman-token", "15729ed9-9d24-892b-ddb5-e78242da955d")
        .asString();

Document subscribersDoc = (null != subscriberResponse.getBody())?Document.parse(subscriberResponse.getBody()):"";

System.out.println("Subscriber Response Body => "+subscribersDoc.toJson(new JsonWriterSettings(true)));
System.out.println("Subscriber Response Headers => "+subscriberResponse.headers);
