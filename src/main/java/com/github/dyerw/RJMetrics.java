package com.github.dyerw;

import com.github.dyerw.UpsertCompletionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.asynchttpclient.*;

import java.util.ArrayList;
import java.util.List;

public class RJMetrics<T> {

    private static final String RJMETRICS_BASEURL = "https://pipeline-gateway.rjmetrics.com";
    private static final String RJMETRICS_UPSERT_PATH = "/push";
    private static final String UPSERT_ACTION = "upsert";

    private String apiKey;
    private int clientId;

    public RJMetrics(String apiKey, int clientId) {
        this.apiKey = apiKey;
        this.clientId = clientId;
    }



    public void upsert(List<T> data, String tableName, List<String> keys, final UpsertCompletionHandler completionHandler) {
        List<RJMetricsRequest<T>> requests = new ArrayList<RJMetricsRequest<T>>();

        for (T singleData: data) {
            requests.add(new RJMetricsRequest<T>(this.clientId, tableName, 10000, UPSERT_ACTION, keys, singleData));
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(requests);
        } catch(JsonProcessingException e) {
            return;
        }

        RequestBuilder builder = new RequestBuilder("POST");
        Request request = builder.setUrl(RJMETRICS_BASEURL + RJMETRICS_UPSERT_PATH)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + this.apiKey)
                .setBody(jsonBody)
                .build();

        AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
        asyncHttpClient.executeRequest(request, new AsyncCompletionHandler<Response>() {

            @Override
            public Response onCompleted(Response response) throws Exception {
                completionHandler.onCompleted(response.toString());
                return response;
            }

            @Override
            public void onThrowable(Throwable t) {
                System.err.println(t.toString());
            }

        });
    }

    private class RJMetricsRequest<U> {
        int clientId;
        String tableName;
        int sequence;
        String action;
        List<String> keyNames;
        U data;

        private RJMetricsRequest(int clientId, String tableName, int sequence, String action, List<String> keyNames, U data) {
            this.clientId = clientId;
            this.tableName = tableName;
            this.sequence = sequence;
            this.action = action;
            this.keyNames = keyNames;
            this.data = data;
        }

        public int getClientId() {
            return clientId;
        }

        public void setClientId(int clientId) {
            this.clientId = clientId;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public int getSequence() {
            return sequence;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public List<String> getKeyNames() {
            return keyNames;
        }

        public void setKeyNames(List<String> keyNames) {
            this.keyNames = keyNames;
        }

        public U getData() {
            return data;
        }

        public void setData(U data) {
            this.data = data;
        }
    }
}
