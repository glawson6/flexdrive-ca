@Grab('com.mashape.unirest:unirest-java:1.4.7')
@Grab('org.mongodb:bson:3.0.4')
@GrabConfig(systemClassLoader=true)

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.bson.Document;



//String id = (args.length > 0)?args[0]:null;
if (args.length > 0) {
    String authFilename = (null != System.getProperty("auth"))?System.getProperty("auth"):null;
    String authorization = resolveBasicAuth(authFilename);
    String accessToken = getAccessToken(authorization);
    args.each {
        String deleteURL = "https://${getAPIHost()}:443/subscribers/id/${it}";
        System.out.println("Sending operation to url => ${operationURL}")
        HttpResponse<String> subscriberResponse = Unirest.delete(deleteURL)
                .header("authorization", "Bearer " + accessToken)
                .asString();

        System.out.println("Delete Response Body => " + subscriberResponse.getBody());
        System.out.println("Delete Response Headers => " + subscriberResponse.headers);
        System.out.println("Location => " + subscriberResponse.headers.get("Location"))
    }

} else {
    throw new IllegalArgumentException("Must pass a space separate list of subscriber ids!");
}
