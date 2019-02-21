package br.com.hbsis.tonaarea.business;

import android.content.Context;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.hbsis.tonaarea.entities.Audit;
import br.com.hbsis.tonaarea.entities.AuditDTO;
import br.com.hbsis.tonaarea.repositories.Repository;

public class DetailsBusiness {

    Repository mRepository = new Repository();

    public Audit getAudit(String endpoint, String id, Context context){
        return mRepository.getAudit(endpoint, id, context);
    }

    public List<String> getAuditsIds(String endpoint, String usuarioId, int statusWorkFlow, boolean last60days, Context context){
        List<AuditDTO> list = mRepository.getAudits(endpoint,usuarioId,statusWorkFlow,last60days,context);

        List<String> ids = new ArrayList<>();

        for (AuditDTO audit: list){
            ids.add(audit.getId());
        }
        return ids;
    }

}
