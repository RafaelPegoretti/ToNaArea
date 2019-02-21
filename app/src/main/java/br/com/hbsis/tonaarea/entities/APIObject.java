package br.com.hbsis.tonaarea.entities;

import org.json.JSONObject;

public class APIObject {

    private String URL;
    private String method;
    private JSONObject json;

    public APIObject(String URL, String method) {
        this.URL = URL;
        this.method = method;
    }

    public APIObject(String URL, String method, JSONObject json) {
        this.URL = URL;
        this.method = method;
        this.json = json;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }
}
