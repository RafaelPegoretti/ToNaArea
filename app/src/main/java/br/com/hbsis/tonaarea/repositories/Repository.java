package br.com.hbsis.tonaarea.repositories;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
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
import br.com.hbsis.tonaarea.util.Constants;
import br.com.hbsis.tonaarea.util.SecurityPreferences;
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


    public Audit getAudit(String endpoint, String id, Context context) {

        String url = endpoint + "/" + id;
        APIObject apiObject = new APIObject(url, Constants.OPERATION_METHOD.GET);

        APIRepository apiRepository = new APIRepository(context);
        apiRepository.execute(apiObject);


        String s;
        Audit audit = new Audit();

        try {
            s = apiRepository.get();

            try {
                JSONArray jsonArray = new JSONArray(s);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    audit.setId(jsonObject.getString("id"));
                    audit.setNameRevenda(jsonObject.getString("nomeRevenda"));
                    audit.setName(jsonObject.getString("nomeCliente"));
                    audit.setProduct(jsonObject.getString("nomeProduto"));
                    audit.setTTCRevenda(jsonObject.getString("valorTtcRevenda"));
                    audit.setTTVRevenda(jsonObject.getString("valorTtvRevenda"));
                    audit.setTTCConcorrente(jsonObject.getString("valorTtcAuditado"));
                    audit.setTTVConcorrente(jsonObject.getString("valorTtvAuditado"));
                    audit.setDescription(jsonObject.getString("descricao"));
                    audit.setLatitude(jsonObject.getDouble("latitude"));
                    audit.setLongitude(jsonObject.getDouble("longitude"));
                    audit.setNameAuditor(jsonObject.getString("nomeAuditor"));
                    audit.setNameUserWorkFlow(jsonObject.getString("nomeUsuarioWorkflow"));
                    audit.setInstant(jsonObject.getString("dataAuditoria"));
                    audit.setStatus(jsonObject.getString("statusWorkFlow"));

                    JSONObject jsonImage = jsonObject.getJSONObject("imagem");
                    audit.getImagem().setImageId(jsonImage.getString("id"));
                    audit.getImagem().setImageIrregularPrice(jsonImage.getString("precoIrregularBase64"));
                    audit.getImagem().setImageLotNumber(jsonImage.getString("loteBase64"));

                    try {
                        audit.setDateWorkFlow(jsonObject.getString("dataWorkflow"));
                    } catch (JSONException e) {
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return audit;
    }

    public List<AuditDTO> getAudits(String endpoint, String usuarioId, int statusWorkflow, boolean last30days, Context context) {

        StringBuilder url = new StringBuilder();
        url.append(endpoint);
        url.append("?usuarioId=");
        url.append(usuarioId);
        url.append("&statusWorkflow=");
        url.append(statusWorkflow);
        url.append("&ultimos30dias=");
        url.append(last30days);

        List<AuditDTO> list = new ArrayList<>();

        APIObject apiObject = new APIObject(url.toString(), Constants.OPERATION_METHOD.GET);

        APIRepository apiRepository = new APIRepository(context);
        apiRepository.execute(apiObject);

        String s;
        try {
            s = apiRepository.get();

            try {
                JSONArray jsonArray = new JSONArray(s);


                for (int i = 0; i < jsonArray.length(); i++) {
                    AuditDTO audit = new AuditDTO();

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    audit.setId(jsonObject.getString("id"));
                    audit.setNameRevenda(jsonObject.getString("nomeRevenda"));
                    audit.setName(jsonObject.getString("nomeCliente"));
                    audit.setProduct(jsonObject.getString("nomeProduto"));
                    audit.setNameAuditor(jsonObject.getString("nomeAuditor"));
                    audit.setStatus(jsonObject.getString("statusWorkflow"));
                    audit.setInstant(jsonObject.getString("dataAuditoria"));

                    list.add(audit);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return list;
    }

    public int[] getQuantity(String endpoint, Context context) {

        mSecurityPreferences = new SecurityPreferences(context);
        StringBuilder sb = new StringBuilder();
        sb.append(endpoint);
        sb.append("?usuarioId=");
        sb.append(mSecurityPreferences.getStoredString(Constants.SECURITY_PREFERENCES_CONSTANTS.USER_ID));

        String url = sb.toString();


        APIObject apiObject = new APIObject(url, Constants.OPERATION_METHOD.GET);

        APIRepository apiRepository = new APIRepository(context);
        apiRepository.execute(apiObject);
        String s;
        int[] quantity = new int[3];

        try {
            s = apiRepository.get();

            try {
                JSONArray jsonArray = new JSONArray(s);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    quantity[0] = jsonObject.getInt("pendente");
                    quantity[1] = jsonObject.getInt("aprovada");
                    quantity[2] = jsonObject.getInt("reprovada");

                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return quantity;
    }

    public List<Client> getClients(String endpoint, Context context) {

        mSecurityPreferences = new SecurityPreferences(context);
        StringBuilder url = new StringBuilder();
        url.append(endpoint);
        url.append("?usuarioId=");
        url.append(mSecurityPreferences.getStoredString(Constants.SECURITY_PREFERENCES_CONSTANTS.USER_ID));
        url.append("&revendaId=");
        url.append(mSecurityPreferences.getStoredString(Constants.SECURITY_PREFERENCES_CONSTANTS.REV_ID));

        APIObject apiObject = new APIObject(url.toString(), Constants.OPERATION_METHOD.GET);

        APIRepository apiRepository = new APIRepository(context);
        apiRepository.execute(apiObject);
        String s;
        List<Client> clients = new ArrayList<>();

        try {
            s = apiRepository.get();

            try {
                JSONArray jsonArray = new JSONArray(s);

                for (int i = 0; i < jsonArray.length(); i++) {
                    Client client = new Client();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    client.setClientId(jsonObject.getString("id"));
                    client.setClientName(jsonObject.getString("nomeCliente"));
                    client.setClienteCode(jsonObject.getString("codigoCliente"));
                    client.setDate(jsonObject.getString("dataInclusaoAlteracao"));
                    client.setRevendaName(jsonObject.getString("nomeRevenda"));
                    client.setActive(jsonObject.getBoolean("ativo"));
                    clients.add(client);
                }


                } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public List<Product> getProducts(String url, Context context) {

        mSecurityPreferences = new SecurityPreferences(context);

        APIObject apiObject = new APIObject(url, Constants.OPERATION_METHOD.GET);

        APIRepository apiRepository = new APIRepository(context);
        apiRepository.execute(apiObject);
        String s;
        List<Product> products = new ArrayList<>();

        try {
            s = apiRepository.get();

            try {
                JSONArray jsonArray = new JSONArray(s);

                for (int i = 0; i < jsonArray.length(); i++) {
                    Product product = new Product();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    product.setProductId(jsonObject.getString("id"));
                    product.setProductName(jsonObject.getString("nomeProduto"));
                    product.setActive(jsonObject.getBoolean("ativo"));
                    product.setDate(jsonObject.getString("dataInclusaoAlteracao"));
                    products.add(product);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return products;
    }


    public List<Client> getClientsDates(String endpoint, Context context, String startDate, String endDate) {

        mSecurityPreferences = new SecurityPreferences(context);
        StringBuilder url = new StringBuilder();
        url.append(endpoint);
        url.append("?usuarioId=");
        url.append(mSecurityPreferences.getStoredString(Constants.SECURITY_PREFERENCES_CONSTANTS.USER_ID));
        url.append("&revendaId=");
        url.append(mSecurityPreferences.getStoredString(Constants.SECURITY_PREFERENCES_CONSTANTS.REV_ID));
        url.append("&dataInicial=");
        url.append(startDate);
        url.append("&dataFinal=");
        url.append(endDate);

        APIObject apiObject = new APIObject(url.toString(), Constants.OPERATION_METHOD.GET);

        APIRepository apiRepository = new APIRepository(context);
        apiRepository.execute(apiObject);
        String s;
        List<Client> clients = new ArrayList<>();

        try {
            s = apiRepository.get();

            try {
                JSONArray jsonArray = new JSONArray(s);

                for (int i = 0; i < jsonArray.length(); i++) {
                    Client client = new Client();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    client.setClientId(jsonObject.getString("id"));
                    client.setClientName(jsonObject.getString("nomeCliente"));
                    client.setClienteCode(jsonObject.getString("codigoCliente"));
                    client.setDate(jsonObject.getString("dataInclusaoAlteracao"));
                    client.setRevendaName(jsonObject.getString("nomeRevenda"));
                    client.setActive(jsonObject.getBoolean("ativo"));
                    clients.add(client);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public List<Product> getProductsDates(String endpoint, Context context, String startDate, String endDate) {

        mSecurityPreferences = new SecurityPreferences(context);

        StringBuilder url = new StringBuilder();

        url.append("?dataInicial=");
        url.append(startDate);
        url.append("&dataFinal=");
        url.append(endDate);

        APIObject apiObject = new APIObject(url.toString(), Constants.OPERATION_METHOD.GET);

        APIRepository apiRepository = new APIRepository(context);
        apiRepository.execute(apiObject);
        String s;
        List<Product> products = new ArrayList<>();

        try {
            s = apiRepository.get();

            try {
                JSONArray jsonArray = new JSONArray(s);

                for (int i = 0; i < jsonArray.length(); i++) {
                    Product product = new Product();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    product.setProductId(jsonObject.getString("id"));
                    product.setProductName(jsonObject.getString("nomeProduto"));
                    product.setActive(jsonObject.getBoolean("ativo"));
                    product.setDate(jsonObject.getString("dataInclusaoAlteracao"));
                    products.add(product);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return products;
    }


    public Date getLastUpdateProduct(String url, Context context){

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
            AppApplication.getInstance().getApi().sendImage(audit).enqueue(new Callback<ResponseBody>() {
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
                        }                    }
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
            AppApplication.getInstance().getApi().sendClient(client).enqueue(new Callback<ResponseBody>() {
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
                        }                    }
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
