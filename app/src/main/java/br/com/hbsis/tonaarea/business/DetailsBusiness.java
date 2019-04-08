package br.com.hbsis.tonaarea.business;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.hbsis.tonaarea.entities.Audit;
import br.com.hbsis.tonaarea.entities.AuditDTO;
import br.com.hbsis.tonaarea.repositories.CallbackReponse;
import br.com.hbsis.tonaarea.repositories.Repository;

public class DetailsBusiness implements CallbackReponse{

    Repository mRepository;
    Repository mRepository2;
    CallbackReponse callbackReponse;
    Context context;
    List<String> ids = new ArrayList<>();

    public DetailsBusiness(CallbackReponse callbackReponse, Context context) {
        this.callbackReponse = callbackReponse;
        this.context = context;
        mRepository = new Repository(callbackReponse);
        mRepository2 = new Repository(this);
    }

    public void getAudit(String id, Context context){
        mRepository2.getAudits(context);
        mRepository.getAudit(context,id);
    }

    public List<String> getIds() {
        return ids;
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        try {
            ids.add(jsonObject.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(JSONObject jsonObject) {
        Toast.makeText(context, "Erro ao pegar os ids", Toast.LENGTH_LONG).show();
    }
}
