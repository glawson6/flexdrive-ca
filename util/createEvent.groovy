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
    String fileName = args[0];
    System.out.println("Using file => ${fileName}");
    String jsonFileText = new File(fileName).getText('UTF-8')
    Document jsonFileDoc = Document.parse(jsonFileText);

    String authFilename = (null != System.getProperty("auth"))?System.getProperty("auth"):null;
    String authorization = resolveBasicAuth(authFilename);
    String accessToken = getAccessToken(authorization);

    System.out.println("Sending json...");
    System.out.println(jsonFileDoc.toJson(new JsonWriterSettings(true)))

    String operationURL = "https://${getAPIHost()}:443/events";
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
    //System.out.println("Must pass a fileName!");

    throw new IllegalArgumentException("Must pass a fileName!");
}
