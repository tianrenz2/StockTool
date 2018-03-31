package main;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;



public class Data_Fetcher {
    private static final String BASE_URL = "https://www.alphavantage.co/query?";
    int timeOut = 5;
    public String Data_Fetcher(String func, String sym, int interval, String apikey) throws IOException {
        get_trust();
        String params = "function=" + func + "&symbol=" + sym + "&interval=" + interval + "min&" + "apikey=" + apikey;

        URL request = new URL(BASE_URL + params);
        URLConnection connection = request.openConnection();
        connection.setConnectTimeout(timeOut);
        InputStreamReader inputStream = new InputStreamReader(connection.getInputStream(), "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStream);
        StringBuilder responseBuilder = new StringBuilder();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            responseBuilder.append(line);
        }
        bufferedReader.close();
        return responseBuilder.toString();
    }



    public void get_trust(){
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {

                    public java.security.cert.X509Certificate[] getAcceptedIssuers()
                    {
                        return null;
                    }
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                    {
                        //No need to implement.
                    }
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                    {
                        //No need to implement.
                    }
                }
        };

// Install the all-trusting trust manager
        try
        {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }

}
