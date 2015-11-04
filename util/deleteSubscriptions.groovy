@Grab('com.mashape.unirest:unirest-java:1.4.7')
@Grab('org.mongodb:bson:3.0.4')
@GrabConfig(systemClassLoader=true)

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.bson.Document;


String apiHost = (null != System.getProperty("api.host"))?System.getProperty("api.host"):"sandbox.api.manheim.com";
String tokenURL = "https://${apiHost}:443/oauth2/token";
System.out.println("Sending api requests to host => ${apiHost}");
System.out.println("Sending token to request url => ${tokenURL}");
HttpResponse<String> tokenResponse = Unirest.post(tokenURL)
        .header("authorization", "Basic ZHV1Ym11YWhteTdoYjRmdGNobm1uamNtOjQ0ZmNUdzJIZFk=")
        .header("cache-control", "no-cache")
        .header("postman-token", "c571d55d-5247-ff35-3d68-18e6a7b9ae57")
        .body("{\n    \"grant_type\":\"client_credentials\"\n}")
        .asString();
Document tokenDoc = Document.parse(tokenResponse.getBody());
System.out.println("Token Response Body => "+tokenResponse.getBody());
System.out.println("Token Response Headers => "+tokenResponse.headers);
System.out.println("Access token => "+tokenDoc.get("accessToken"));
String accessToken = tokenDoc.get("accessToken");

//String id = (args.length > 0)?args[0]:null;
if (args.length > 0) {
    args.each {
        String deleteURL = "https://sandbox.api.manheim.com/subscriptions/id/" + it;
        HttpResponse<String> subscriberResponse = Unirest.delete(deleteURL)
                .header("authorization", "Bearer " + accessToken)
                .asString();

        System.out.println("Delete Response Body => " + subscriberResponse.getBody());
        System.out.println("Delete Response Headers => " + subscriberResponse.headers);
        System.out.println("Location => " + subscriberResponse.headers.get("Location"))
    }

} else {
    System.out.println("Must provide a non null id");
}
