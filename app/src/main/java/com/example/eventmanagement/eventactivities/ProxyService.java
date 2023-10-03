//package com.example.eventmanagement.eventactivities;
//
//
//
//
//        import android.os.Build;
//        import android.util.Log;
//
//        import org.apache.http.HttpResponse;
//        import org.apache.http.NameValuePair;
//        import org.apache.http.client.config.RequestConfig;
//        import org.apache.http.client.entity.EntityBuilder;
//        import org.apache.http.client.entity.UrlEncodedFormEntity;
//        import org.apache.http.client.methods.CloseableHttpResponse;
//        import org.apache.http.client.methods.HttpGet;
//        import org.apache.http.client.methods.HttpPost;
//        import org.apache.http.entity.ContentType;
//        import org.apache.http.impl.client.CloseableHttpClient;
//        import org.apache.http.impl.client.HttpClientBuilder;
//        import org.apache.http.message.BasicNameValuePair;
//
//
//        import java.io.ByteArrayInputStream;
//        import java.io.IOException;
//        import java.util.ArrayList;
//        import java.util.List;
//
//
//public class ProxyService {
//    private static String cookie = null;
//    private static String username = null, password = null;
//
//    private String url;
//
//    private String prometheusPath;
//
//    private String loginProcessingPath;
//
//    public static String getCookie() {
//        return cookie;
//    }
//
////    public static void main(String[] args) {
//    public  void doPost() {
//        String url = "https://eventmanagementplatform.onrender.com";
//        url += "/user/events/post";
//
//        ProxyService o = new ProxyService();
//        o.fetchUsernamePassword();
//        o.refreshCookie();
//
//        System.out.println(url);
//        System.out.println(username);
//        System.out.println(password);
//        System.out.println(cookie);
//
//        try (CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.DEFAULT).build()) {
//
//            Log.d("Nav","Cookie = "+ getCookie());
//            HttpPost get = new HttpPost(url);
//
//            get.addHeader("Cookie", cookie);
//            String respBody = "{\"ID\":\"99999\", \"title\":\"a\", \"content\":\"a\", \"location\":\"a\", \"startDay\":\"2023-09-24\", \"endDay\":\"2023-09-28\", \"startTime\":\"10:12:04.748194\", \"endTime\":\"12:12:04.748265\"}";
//            get.setEntity(EntityBuilder.create().setStream(new ByteArrayInputStream(respBody.getBytes())).setContentType(ContentType.APPLICATION_JSON).build());
//            CloseableHttpResponse response = httpClient.execute(get);
//            Log.d("Nav","Response = "+EventPostData.Companion.convertStreamToString(response.getEntity().getContent(),null));
//            System.out.println(response.getStatusLine().getStatusCode());
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
////                System.out.println(new String(response.getEntity().getContent().readAllBytes()));
//            }
//        } catch (IOException e) {
//
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    public String getPrometheusFromEnvVarWithUsernameAndPassword() {
//
//        if (username == null || password == null) fetchUsernamePassword();
//        if (cookie == null) refreshCookie();
//
//
//        try (CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.DEFAULT).build()) {
//            HttpGet get = new HttpGet(url + prometheusPath);
//            get.addHeader("Cookie", cookie);
//            org.apache.http.client.methods.CloseableHttpResponse response = httpClient.execute(get);
//
//            String responseString = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
////                responseString = new String(response.getEntity().getContent().readAllBytes());
//            }
//
//            if (response.getStatusLine().getStatusCode() != 200 || responseString.contains("<!DOCTYPE html>")) {
//                refreshCookie();
//                return getPrometheusFromEnvVarWithUsernameAndPassword();
//            }
//
//            return responseString;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private void fetchUsernamePassword() {
//        username = System.getenv("username");
//        password = System.getenv("password");
//
//        username = "lovepreet";
//        password = "abc";
//    }
//
//    private void refreshCookie() {
//        String url = "https://eventmanagementplatform.onrender.com";
//        url += "/login";
//        try (CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.DEFAULT).build()) {
//            HttpPost post = new HttpPost(url);
//
//            List<NameValuePair> formParameters = new ArrayList<>();
//            formParameters.add(new BasicNameValuePair("username", username));
//            formParameters.add(new BasicNameValuePair("password", password));
//
//            post.setEntity(new UrlEncodedFormEntity(formParameters));
//
//            HttpResponse response = httpClient.execute(post);
//
//            org.apache.http.HeaderElement temp = response.getFirstHeader("Set-cookie").getElements()[0];
//            cookie = temp.getName() + "=" + temp.getValue();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}