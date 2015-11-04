@Grab('com.mashape.unirest:unirest-java:1.4.7')
@Grab('org.mongodb:bson:3.0.4')
@Grab('org.mongodb:mongodb-driver:3.0.4')
@GrabConfig(systemClassLoader=true)

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest
import groovy.transform.BaseScript
import org.bson.BasicBSONObject;
import org.bson.Document
import org.bson.json.JsonWriterSettings;

@BaseScript Tools tools

if (args.length > 0) {
    Document jsonFileDoc = null;
    if (null != System.getProperty("subscription")){
        String fileName = System.getProperty("subscription");
        String jsonFileText = new File(fileName).getText('UTF-8')
        jsonFileDoc = Document.parse(jsonFileText);
        BasicBSONObject bsonObject = new BasicBSONObject("href",args[0]);
        jsonFileDoc.put("subscriber", bsonObject);
    } else {
        throw new IllegalArgumentException("You must specify a subscription System property pointing to json file. " +
                "Ex groovy -Dsubscription=createSubscription.json createSubscription.groovy " +
                "https://sandbox.api.manheim.com/subscribers/id/098feb80-7d0a-11e5-abc9-6745720a6e75");
    }

    String authFilename = (null != System.getProperty("auth"))?System.getProperty("auth"):null;
    String authorization = resolveBasicAuth(authFilename);
    String accessToken = getAccessToken(authorization);

    System.out.println("Sending json...");
    System.out.println(jsonFileDoc.toJson(new JsonWriterSettings(true)))

    String operationURL = "https://${getAPIHost()}:443/subscriptions";
    System.out.println("Sending operation to url => ${operationURL}")
    HttpResponse<String> subscriberResponse = Unirest.post(operationURL)
            .header("authorization", "Bearer " + accessToken)
            .header("content-type","application/json")
            .body(jsonFileDoc.toJson())
            .asString();

    System.out.println("Subscriber Response Body => " + subscriberResponse.getBody());
    System.out.println("Subscriber Response Headers => " + subscriberResponse.headers);
    System.out.println("Location => " + subscriberResponse.headers.get("location"))
} else {
    throw new IllegalArgumentException("You must specify a subscriber url. " +
            "Ex groovy -Dsubscription=createSubscription.json createSubscription.groovy " +
            "https://sandbox.api.manheim.com/subscribers/id/098feb80-7d0a-11e5-abc9-6745720a6e75");
}
