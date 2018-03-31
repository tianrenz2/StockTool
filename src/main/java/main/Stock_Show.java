package main;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import java.io.IOException;
import java.util.*;

public class Stock_Show {
    static final private String datatype= "TIME_SERIES_DAILY";
    static final private String apikey= "KIB0SJ12V207ABJO";

    public static void main(String args[]) throws IOException {
//      Read from Config file

        Config cf = new Config();
        String startDate = cf.getProperty("StartDate");
        String endDate = cf.getProperty("EndDate");
        String stock = cf.getProperty("Stock");
        String exchange = cf.getProperty("Exchange");
        String plot = cf.getProperty("Plotter");
        LinkedHashMap<String, JSONObject> dailystocks;
        String [] keywords = {"open","high","low","close"};


//      Fetch data using Alpha Vantage API
        Data_Fetcher df = new Data_Fetcher();
        String fetched_data = df.Data_Fetcher(datatype, stock, 1, apikey);
        System.out.println(fetched_data);
        JSONObject object = JSON.parseObject(fetched_data);
        Object obj = object.get("Time Series (Daily)").toString();
        Object dailydata = object.get("Time Series (Daily)");
        String parsedata = '[' + dailydata.toString().substring(1, dailydata.toString().length() - 1) + ']';
        dailystocks = JSON.parseObject(dailydata.toString(), new TypeReference<LinkedHashMap<String, JSONObject>>() {
        });
        if (dailydata!=null)
            System.out.println("Successfully fetched data");
        else
            System.out.println("Could not fetch data");
//      Get data from a specified range
        List<String> cutline;
        Double data[][];
        Plotter plotter = new Plotter();
        cutline = plotter.ParseDate( dailystocks, keywords, startDate, endDate);
        data = plotter.ParseNumber(dailystocks,cutline,keywords);

//        Drawing Chart
        if (Boolean.parseBoolean(plot)){
            System.out.println("Generating the graph...");
            plotter.PlotData(cutline,keywords,data);
        }

//      Store obtained and parsed data into local database
        MongoDBJDBC mdb = new MongoDBJDBC();
        mdb.append_data(stock,data,cutline,keywords);

//      Use this line to check if the data is in the database
//        mdb.query_data(stock);

    }



}
