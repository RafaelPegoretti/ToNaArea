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
import br.com.hbsis.tonaarea.repositories.Repository;

public class AuditsDetailsBusiness {

    Repository mRepository = new Repository();

    public List<AuditDTO> filter(String status, List<AuditDTO> audits){
        List<AuditDTO> list = new ArrayList<>();
        for (AuditDTO x: audits){
            if (x.getStatus().equals(status)){
                list.add(x);
            }
        }
        return list;
    }


    public List<AuditDTO> getAudits(String endpoint,String usuarioId, int statusWorkFlow, boolean last60days, Context context){
        List<AuditDTO> list = mRepository.getAudits(endpoint,usuarioId,statusWorkFlow,last60days,context);
        Collections.sort(list);
        return list;
    }



}
