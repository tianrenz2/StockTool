package main;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoDBJDBC{
    MongoDatabase mongoDatabase;
    public MongoDBJDBC(){
        try{
            // Connect to MongoDB Service
            MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
            mongoDatabase = mongoClient.getDatabase("stock_data");
            System.out.println("Connect to database successfully");
        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void create_col(String col_name){
        mongoDatabase.createCollection(col_name);
        System.out.println("Created a collection named "+col_name);
    }

    public void append_data(String col_name, Double data[][], List<String> cutline, String keywords[]){
        if (mongoDatabase.getCollection(col_name)==null)
            create_col(col_name);
        try{
            MongoCollection<Document> collection = mongoDatabase.getCollection(col_name);
            System.out.println("Collection " + col_name + "chosen");
            List<Document> documents = new ArrayList<Document>();
            for (int i = 0; i < cutline.size(); i++) {
                Document document = new Document("Date", cutline.get(i));
                for (int j = 0; j < keywords.length; j++) {
                    document.append(keywords[j],data[j][i]);
                }
                documents.add(document);
            }
            collection.insertMany(documents);
            System.out.println("Successfully appended data!");
        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void query_data(String col_name){
        MongoCollection<Document> collection = mongoDatabase.getCollection(col_name);
        FindIterable<Document> findIterable = collection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        System.out.println(" Date "+"     open "+" high "+" low "+" close ");
        while(mongoCursor.hasNext()){
            Document current = mongoCursor.next();
//            System.out.println(current);
            System.out.println(current.get("Date")+" "+current.get("open")+" "+current.get("high")+" "+current.get("low")+" "+current.get("close"));
        }

    }
}