@Grab('com.mashape.unirest:unirest-java:1.4.7')
@Grab('org.mongodb:bson:3.0.4')
@GrabConfig(systemClassLoader=true)

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.bson.Document
import org.bson.json.JsonWriterSettings;


if (args.length > 0) {
    String fileName = args[0];
    System.out.println("Using file => ${fileName}");
    String jsonFileText = new File(fileName).getText('UTF-8')
    Document jsonFileDoc = Document.parse(jsonFileText);
    String apiHost = (null != System.getProperty("api.host"))?System.getProperty("api.host"):"sandbox.api.manheim.com";
    String tokenURL = "https://${apiHost}:443/oauth2/token";
    System.out.println("Sending api requests to host => ${apiHost}");
    System.out.println("Sending token to request url => ${tokenURL}");
    HttpResponse<String> tokenResponse = Unirest.post(tokenURL)
            .header("authorization", "Basic ZHV1Ym11YWhteTdoYjRmdGNobm1uamNtOjQ0ZmNUdzJIZFk=")
            .body("{\n    \"grant_type\":\"client_credentials\"\n}")
            .asString();
    Document tokenDoc = Document.parse(tokenResponse.getBody());
    System.out.println("Token Response Body => " + tokenResponse.getBody());
    System.out.println("Token Response Headers => " + tokenResponse.headers);
    System.out.println("Access token => " + tokenDoc.get("accessToken"));
    String accessToken = tokenDoc.get("accessToken");
    String operationURL = "https://${apiHost}:443/units";
    System.out.println("Sending operation to url => ${operationURL}")
    HttpResponse<String> subscriberResponse = Unirest.post(operationURL)
            .header("authorization", "Bearer " + accessToken)
            .header("content-type","application/json")
            .body(jsonFileDoc.toJson())
            .asString();

    System.out.println("Subscriber Response Headers => " + subscriberResponse.headers);
    System.out.println("Subscriber Response Status => " + subscriberResponse.status);
    System.out.println("Subscriber Response Status Text => " + subscriberResponse.statusText);
    System.out.println("Subscriber Response Body => "+subscriberResponse.getBody());
    Document subscribersDoc = (null != subscriberResponse.getBody())?Document.parse(subscriberResponse.getBody()):"";

    System.out.println("Subscriber Response JSON => "+subscribersDoc.toJson(new JsonWriterSettings(true)));
    System.out.println("Location => " + subscriberResponse.headers.get("location"))
} else {
    System.out.println("Must pass a fileName!");
}
