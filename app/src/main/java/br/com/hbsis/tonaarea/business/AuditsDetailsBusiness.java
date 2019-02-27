package br.com.hbsis.tonaarea.business;

import android.content.Context;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.hbsis.tonaarea.entities.Audit;
import br.com.hbsis.tonaarea.entities.AuditDTO;
import br.com.hbsis.tonaarea.repositories.CallbackReponse;
import br.com.hbsis.tonaarea.repositories.Repository;

public class AuditsDetailsBusiness{

    Repository mRepository;
    Context context;
    CallbackReponse callbackReponse;

    public AuditsDetailsBusiness(CallbackReponse callbackReponse,Context context) {
        this.callbackReponse = callbackReponse;
        this.context = context;
        mRepository = new Repository(callbackReponse);

    }

    public List<AuditDTO> filter(String status, List<AuditDTO> audits){
        List<AuditDTO> list = new ArrayList<>();
        for (AuditDTO x: audits){
            if (x.getStatus().equals(status)){
                list.add(x);
            }
        }
        return list;
    }


    public void getAudits(){
        mRepository.getAudits(context);
    }



}
