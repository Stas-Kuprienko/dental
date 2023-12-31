package edu.dental;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.dental.service.HttpRequestSender;

public enum WebAPI {

    INSTANCE;

    public final String sessionUser = "user";
    public final String sessionToken = "token";

    private static final String apiUrl = "http://localhost:8080/dental-api/";

    private final HttpRequestSender requestSender;
    private final Gson parser;


    WebAPI() {
        this.requestSender = new HttpRequestSender(apiUrl);
        this.parser = new GsonBuilder().create();
    }


    public <T> String parseToJson(T object) {
        return parser.toJson(object);
    }

    public <T> T parseFromJson(String json, Class<T> clas) {
        return parser.fromJson(json, clas);
    }

    public HttpRequestSender requestSender() {
        return requestSender;
    }
}
