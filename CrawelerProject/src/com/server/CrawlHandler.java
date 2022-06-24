package com.server;

import com.exceptions.InvalidParams;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.utilities.CrawlerHelper;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class CrawlHandler implements HttpHandler {
    private final static String PARAM_URL = "url";
    private final static String PARAM_NUMBER = "number";
    private final CrawlerHelper mCrawlerHelper = new CrawlerHelper();


    @Override
    public void handle(HttpExchange httpExchange) {
        Map<String, String> params = getParamsHashMap(httpExchange);
        String url = params.get(PARAM_URL);
        String number = params.get(PARAM_NUMBER);
        try {
            verifyParams(url, number);
            Map<String, String> response = mCrawlerHelper.crawlUrl(url);
            handleSuccessResponse(httpExchange, response, Integer.parseInt(number));
        } catch (Exception exception) {
            handleFailedResponse(httpExchange, exception.getLocalizedMessage());
        }
    }

    private void handleSuccessResponse(HttpExchange httpExchange, Map<String, String> map, int number) {
        try {
            OutputStream outputStream = httpExchange.getResponseBody();
            String response = convertHashMapToJsonArrayString(map, number);
            httpExchange.sendResponseHeaders(200, response.length());
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {

        }
    }

    private String convertHashMapToJsonArrayString(Map<String, String> map, int number) {
        JsonArray jsonArray = new JsonArray();

        if (number != 0) {
            int counter = 0;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("url: "+entry.getKey(),"title: "+ entry.getValue());
                jsonArray.add(jsonObject);
                counter++;
                if (counter == number) {
                    break;
                }
            }
        }
        return jsonArray.toString();

    }


    private void handleFailedResponse(HttpExchange httpExchange, String reason) {
        try {
            OutputStream outputStream = httpExchange.getResponseBody();
            httpExchange.sendResponseHeaders(400, reason.length());
            outputStream.write(reason.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {

        }
    }

    private void verifyParams(String url, String number) throws Exception {
        if (url != null && number != null) {
            if (!url.isEmpty() && !number.isEmpty()) {
                try {
                    int res = Integer.parseInt(number);
                    if (res < 0) {
                        throw new InvalidParams(url, number);
                    }
                } catch (Exception e) {
                    throw new InvalidParams(url, number);
                }
            } else {
                throw new InvalidParams(url, number);
            }
        } else {
            throw new InvalidParams(url, number);
        }
    }


    private Map<String, String> getParamsHashMap(HttpExchange httpExchange) {
        return getQueryMap(httpExchange.getRequestURI().toString().split("\\?")[1]);
    }

    private Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }
}
