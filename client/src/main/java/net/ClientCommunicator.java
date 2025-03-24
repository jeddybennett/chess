package net;
import com.google.gson.Gson;
import exception.ResponseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ClientCommunicator {
    private final String serverURL;
    private final Gson gson;

    public ClientCommunicator(String serverURL){
        this.serverURL = serverURL;
        this.gson = new Gson();
    }

    public <T> T makeRequest(String method, String path, Object request, Class<T>responseClass,
                             String authToken) throws ResponseException{
        try{
            URL url = new URL(serverURL + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            writeBody(request, connection);


        } catch(Exception e){
            throw new ResponseException(500, e.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection url) throws IOException {
        if(request!=null){
            url.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try(OutputStream reqBody = url.getOutputStream()){
                reqBody.write(reqData.getBytes());
        }
    }


}
