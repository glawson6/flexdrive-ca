//this.class.classLoader.rootLoader.addURL(
//        new URL("file:../src/main/resources"))
@Grab('com.mashape.unirest:unirest-java:1.4.7')
@Grab('org.mongodb:bson:3.0.4')
@Grab('org.mongodb:mongodb-driver:3.0.4')
@Grab('org.mongodb:mongo-java-driver:3.0.4')
@Grab('commons-collections:commons-collections:3.2.1')
@GrabConfig(systemClassLoader = true)
@Grab(group = 'org.springframework.boot', module = 'spring-boot-starter', version = '1.2.7.RELEASE')

import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.Unirest
import com.mongodb.Block
import com.mongodb.MongoClient
import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.MongoIterable
import com.mongodb.client.result.DeleteResult
import org.apache.commons.collections.IteratorUtils
import org.bson.Document
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.context.support.FileSystemXmlApplicationContext

def cli = new CliBuilder(usage: 'admin.groovy -[hclcu] [collection] | [file]')
// Create the list of options.
cli.with {
    h longOpt: 'help', 'Show usage information'
    c longOpt: 'create-collection', args: 1, argName: 'collection', 'Create Collection with name "format"'
    l longOpt: 'list-collections', 'List Collections'
    cu longOpt: 'create-user', args: 1, argName: 'user file', 'Create User with json file'
    pme longOpt: 'post-maintenance-event', args: 1, argName: 'maintenance event file', 'Post MaintenanceEvent to Mule ESB with json file'
    s longOpt: 'send-document',  args: 2, argName: 'collection', 'Send json file to collection'
    du longOpt: 'delete-from',  args: 2, argName: 'collection', 'Delete from collection with json or all'
}
cli.D(args:2, valueSeparator:'=', argName:'property=value', 'use value for given property')

System.setProperty("FlexEnv", "prod")
System.setProperty("database", "flexdrive")

def getClientName(){return "mongoClient"}
def getContext() { new ClassPathXmlApplicationContext("mongoConfig.xml") }

def getDatabaseName() {
    return (null != System.getenv("database") ? System.getenv("database") : System.getProperty("database"))
}

def list() {
    // "OFFERINGS.PURCHASED"
    MongoClient mongoClient = (MongoClient) getContext().getBean(getClientName())
    MongoDatabase database = mongoClient.getDatabase(getDatabaseName());
    MongoIterable<String> collection = database.listCollectionNames()
    collection.forEach(new Block<String>() {
        @Override
        void apply(String s) {
            System.out.println("Collection Name => ${s}")
        }
    });
    //List<Document> documents = IteratorUtils.toList(iterableDocs.iterator());
//    def documents = IteratorUtils.toList(iterableDocs.iterator());
//    documents.each {
//        System.out.println("docs ${it}");
//    }
}

def sendFile(options){
    MongoClient mongoClient = (MongoClient) getContext().getBean(getClientName())
    MongoDatabase database = mongoClient.getDatabase(getDatabaseName());
    System.out.println("options arguments ${options.arguments()}")
    System.out.println("options s ${options.s}")
    MongoCollection<Document> collection = database.getCollection(options.s);
    String jsonFileText = new File(options.arguments()[0]).getText('UTF-8')
    Document jsonDocument = Document.parse(jsonFileText)
    Date now = new Date();
    jsonDocument.append("inserted",now);
    jsonDocument.append("insertedLong",now.getTime());
    collection.insertOne(jsonDocument);
}

def deleteFromCollection(options){
    MongoClient mongoClient = (MongoClient) getContext().getBean(getClientName())
    MongoDatabase database = mongoClient.getDatabase(getDatabaseName());
    def collection = database.getCollection(options.du);
    Document jsonDocument = null
    if (null != options.arguments()[0]){
        String jsonFileText = new File(options.arguments()[0]).getText('UTF-8')
        jsonDocument = Document.parse(jsonFileText)
    } else {
        jsonDocument = new Document();
    }
    DeleteResult result = collection.deleteMany(jsonDocument);
    System.out.println(" Deleted ${result.deletedCount} from Collection ${options.s}");

}

def postMaintenanceEvent(options){
    System.out.println("options arguments ${options.arguments()}")
    System.out.println("options s ${options.s}")
    String jsonFileText = new File(options.arguments()[0]).getText('UTF-8')
    HttpResponse<String> subscriberResponse = Unirest.post("http://0.0.0.0:8080/maintenanceEvent")
            .header("content-type", "application/json")
            .body(jsonFileText)
            .asString();
}

def options = cli.parse(args)
if (!options) {
    return
} else {
    if (options.h) {  // Using short option.
        System.out.println("Executing ${options.h}")
        cli.usage()
    } else if (options.c) {
        System.out.println("Executing ${options.c}")
    } else if (options.l) {
        System.out.println("Executing ${options.l}")
        list()
    } else if (options.cu) {
        System.out.println("Executing ${options.cu}")
    } else if (options.du) {
        System.out.println("Executing ${options.du}")
        deleteFromCollection(options)
    } else if (options.pme) {
        System.out.println("Executing ${options.pme}")
        postMaintenanceEvent(options)
    } else if (options.s) {
        System.out.println("Executing ${options.s}")
        sendFile(options)
    }
}
