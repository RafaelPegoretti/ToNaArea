package br.com.hbsis.tonaarea.repositories;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.hbsis.tonaarea.AppApplication;
import br.com.hbsis.tonaarea.entities.APIObject;
import br.com.hbsis.tonaarea.entities.Audit;
import br.com.hbsis.tonaarea.entities.AuditDTO;
import br.com.hbsis.tonaarea.entities.Client;
import br.com.hbsis.tonaarea.entities.Login;
import br.com.hbsis.tonaarea.entities.Product;
import br.com.hbsis.tonaarea.repositories.api.QuantityApi;
import br.com.hbsis.tonaarea.util.Constants;
import br.com.hbsis.tonaarea.util.SecurityPreferences;
import br.com.hbsis.tonaarea.util.Util;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    private CallbackReponse callbackReponse;
    private SecurityPreferences mSecurityPreferences;

    public Repository() {
    }

    public Repository(CallbackReponse callbackReponse) {
        this.callbackReponse = callbackReponse;
    }

    JSONObject jsonObject;

    public void getAudit(Context context, String id){
        Call<ResponseBody> call = AppApplication.getInstance().getApiUnsafe().getAudit(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            if (callbackReponse != null) {
                                callbackReponse.onSuccess(jsonObject);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        if (callbackReponse != null) {
                            callbackReponse.onSuccess(jsonObject);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (callbackReponse != null) {
                    callbackReponse.onError(jsonObject);
                }
            }
        });
    }

    public void getAudits(Context context) {
        mSecurityPreferences = new SecurityPreferences(context);
        Call<ResponseBody> call = AppApplication.getInstance().getApiUnsafe().getAudits(mSecurityPreferences.getStoredString(Constants.SECURITY_PREFERENCES_CONSTANTS.USER_ID),9,true);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (callbackReponse != null) {
                                callbackReponse.onSuccess(jsonObject);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        if (callbackReponse != null) {
                            callbackReponse.onSuccess(jsonObject);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (callbackReponse != null) {
                    callbackReponse.onError(jsonObject);
                }
            }
        });
    }

    public void getQuantity(Context context) {
        mSecurityPreferences = new SecurityPreferences(context);
        Call<ResponseBody> call = AppApplication.getInstance().getApiUnsafe().getQuantity(mSecurityPreferences.getStoredString(Constants.SECURITY_PREFERENCES_CONSTANTS.USER_ID));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        if (callbackReponse != null) {
                            callbackReponse.onSuccess(jsonObject);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        if (callbackReponse != null) {
                            callbackReponse.onSuccess(jsonObject);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (callbackReponse != null) {
                    callbackReponse.onError(jsonObject);
                }
            }
        });
    }

    public void getClients(Context context, String startDate, String endDate){
        mSecurityPreferences = new SecurityPreferences(context);
        Call<ResponseBody> call = AppApplication.getInstance().getApiUnsafe().getClients(mSecurityPreferences.getStoredString(Constants.SECURITY_PREFERENCES_CONSTANTS.USER_ID),mSecurityPreferences.getStoredString(Constants.SECURITY_PREFERENCES_CONSTANTS.REV_ID), Util.formateDate(startDate), Util.formateDate(endDate), true);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (callbackReponse != null) {
                                callbackReponse.onSuccess(jsonObject);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        if (callbackReponse != null) {
                            callbackReponse.onSuccess(jsonObject);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (callbackReponse != null) {
                    callbackReponse.onError(jsonObject);
                }
            }
        });
    }

    public void getProducts(String startDate, String endDate){
        Call<ResponseBody> call = AppApplication.getInstance().getApiUnsafe().getProducts(Util.formateDate(startDate), Util.formateDate(endDate));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (callbackReponse != null) {
                                callbackReponse.onSuccess(jsonObject);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        if (callbackReponse != null) {
                            callbackReponse.onSuccess(jsonObject);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (callbackReponse != null) {
                    callbackReponse.onError(jsonObject);
                }
            }
        });
    }

    public void getLastUpdateProduct(){
        Call<ResponseBody> call = AppApplication.getInstance().getApiUnsafe().getLastUpdateProduct();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (callbackReponse != null) {
                                callbackReponse.onSuccess(jsonObject);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        if (callbackReponse != null) {
                            callbackReponse.onSuccess(jsonObject);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (callbackReponse != null) {
                    callbackReponse.onError(jsonObject);
                }
            }
        });
    }

    public void getLastUpdateClient(Context context){
        mSecurityPreferences = new SecurityPreferences(context);
        Call<ResponseBody> call = AppApplication.getInstance().getApiUnsafe().getLastUpdateClient(mSecurityPreferences.getStoredString(Constants.SECURITY_PREFERENCES_CONSTANTS.REV_ID));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (callbackReponse != null) {
                                callbackReponse.onSuccess(jsonObject);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        if (callbackReponse != null) {
                            callbackReponse.onSuccess(jsonObject);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (callbackReponse != null) {
                    callbackReponse.onError(jsonObject);
                }
            }
        });
    }


    public Date getLastUpdateProduct(String url, Context context) {

        APIObject apiObject = new APIObject(url, Constants.OPERATION_METHOD.GET);

        APIRepository apiRepository = new APIRepository(context);
        apiRepository.execute(apiObject);
        String s;


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            s = apiRepository.get();

            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    return sdf.parse(jsonObject.getString("dataUltimaAtualizacao"));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;

    }

    public Date getLastUpdateClient(String endpoint, Context context) {

        mSecurityPreferences = new SecurityPreferences(context);
        StringBuilder url = new StringBuilder();
        url.append(endpoint);
        url.append("?revendaId=");
        url.append(mSecurityPreferences.getStoredString(Constants.SECURITY_PREFERENCES_CONSTANTS.REV_ID));


        APIObject apiObject = new APIObject(url.toString(), Constants.OPERATION_METHOD.GET);

        APIRepository apiRepository = new APIRepository(context);
        apiRepository.execute(apiObject);
        String s;


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            s = apiRepository.get();

            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    return sdf.parse(jsonObject.getString("dataUltimaAtualizacao"));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }



    public void postNewAudit(Audit audit, final Context context) {

        try {
            AppApplication.getInstance().getApiUnsafe().sendImage(audit).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            jsonObject = new JSONObject(response.body().string());
                            if (callbackReponse != null) {
                                callbackReponse.onSuccess(jsonObject);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            jsonObject = new JSONObject(response.errorBody().string());
                            if (callbackReponse != null) {
                                callbackReponse.onSuccess(jsonObject);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("OK", "ok");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postLogin(Login login, Context context) {

        Call<ResponseBody> call = AppApplication.getInstance().getApiUnsafe().login(login);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        if (callbackReponse != null) {
                            callbackReponse.onSuccess(jsonObject);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        if (callbackReponse != null) {
                            callbackReponse.onSuccess(jsonObject);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (callbackReponse != null) {
                    callbackReponse.onError(jsonObject);
                }
            }
        });
    }

    public void postNewPDV(Client client, final Context context) {

        try {
            AppApplication.getInstance().getApiUnsafe().sendClient(client).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            jsonObject = new JSONObject(response.body().string());
                            if (callbackReponse != null) {
                                callbackReponse.onSuccess(jsonObject);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            jsonObject = new JSONObject(response.errorBody().string());
                            if (callbackReponse != null) {
                                callbackReponse.onSuccess(jsonObject);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("OK", "ok");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
