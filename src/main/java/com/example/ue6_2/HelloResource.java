package com.example.ue6_2;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

@Path("/news-program")
public class HelloResource {
    @GET
    @Produces("text/plain")
    public String netNews(@QueryParam("topic") String topic) {
        disableSSLValidation();
        if(topic==null||topic.length()==0){
            return "Please enter a news topic as query parameter!";
        }
        String apiToken = "pub_221618bffa3dbf961df8f9f7f4194bb4cf1e2"; //just here for the purposes of the exercise. It's a bad habit, usually you would NEVER leave an API token in the code
        StringBuilder output = new StringBuilder();

        Client client = ClientBuilder.newClient();
        String url = "https://newsdata.io/api/1/news?apikey="+apiToken+"&q="+ topic+"&language=en";
        Response response = client.target(url).request(MediaType.APPLICATION_JSON).get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {

            AllNews result = response.readEntity(AllNews.class);
            for (SingleArticle article : result.getResults().subList(0,10)) {
                output.append("Title: " + article.getTitle()+"\n");
                output.append("Link: " + article.getLink()+"\n");
                output.append("Published at: " + article.getPubDate()+"\n");
                output.append("--------------------------------------------------------------------\n");
            }
        } else {
            output.append("Error occurred: HTTP " + response.getStatus());
        }

        response.close();
        client.close();
        return output.toString();
    }

    private static void disableSSLValidation() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}