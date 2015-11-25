@Grab('com.mashape.unirest:unirest-java:1.4.7')
@Grab('org.mongodb:bson:3.0.4')
@GrabConfig(systemClassLoader=true)

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest
import groovy.transform.BaseScript;
import org.bson.Document
import org.bson.json.JsonWriterSettings;

@BaseScript Tools tools

if (args.length > 0) {
    String authFilename = (null != System.getProperty("auth"))?System.getProperty("auth"):null;
    String authorization = resolveBasicAuth(authFilename);
    String accessToken = getAccessToken(authorization);

    String operationURL = "https://${getAPIHost()}:443/events/subscriber/${args[0]}?startTime=2015-10-26T22:06:42.188Z&endTime=2015-12-30T22:06:42.188Z";
    //String operationURL = "https://${getAPIHost()}:443/events/subscriber/${args[0]}";
    System.out.println("Sending operation to url => ${operationURL}")
    HttpResponse<String> subscriberResponse = Unirest.get(operationURL)
            .header("authorization", "Bearer " + accessToken)
            .asString();


    System.out.println("Subscriber Response Headers => " + subscriberResponse.headers);
    System.out.println("Subscriber Response Body => "+subscriberResponse.getBody());
    Document subscribersDoc = (null != subscriberResponse.getBody())?Document.parse(subscriberResponse.getBody()):"";

    System.out.println("Subscriber Response JSON => "+subscribersDoc.toJson(new JsonWriterSettings(true)));
    System.out.println("Location => " + subscriberResponse.headers.get("location"))
} else {
    throw new IllegalArgumentException("Must pass a subscriber id");
}
