package br.com.hbsis.tonaarea.business;

import android.content.Context;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import br.com.hbsis.tonaarea.repositories.CallbackReponse;
import br.com.hbsis.tonaarea.repositories.Repository;

public class MainBusiness implements CallbackReponse{
    private Repository mRepository;
    private Repository mRepository2;
    private CallbackReponse callbackReponse;
    private Context context;
    private int position = 0;
    private String[] dates = new String[2];

    public MainBusiness(CallbackReponse callbackReponse, Context context) {
        this.callbackReponse = callbackReponse;
        this.context = context;
        mRepository = new Repository(callbackReponse);
        mRepository2 = new Repository(this);
        updateDates();
    }

    public void getQuantity(){
        mRepository.getQuantity(context);
    }


    public void getClients(String startDate, String endDate){
     mRepository.getClients(context,startDate,endDate);
    }

    public void getProducts(String startDate, String endDate){
        mRepository.getProducts(endDate,startDate);
    }

    public String[] getDates() {
        return dates;
    }

    public void updateDates(){
        mRepository2.getLastUpdateProduct();
        mRepository2.getLastUpdateClient(context);
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        try {
            dates[position] = jsonObject.getString("dataUltimaAtualizacao");
            position++;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(JSONObject jsonObject) {
        Toast.makeText(context, "Erro ao pegar as datas", Toast.LENGTH_LONG).show();
    }
}
