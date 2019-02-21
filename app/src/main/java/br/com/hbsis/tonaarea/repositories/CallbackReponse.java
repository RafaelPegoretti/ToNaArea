package br.com.hbsis.tonaarea.repositories;

import org.json.JSONObject;

public interface CallbackReponse {
    void onSuccess(JSONObject jsonObject);
    void onError(JSONObject jsonObject);
}
