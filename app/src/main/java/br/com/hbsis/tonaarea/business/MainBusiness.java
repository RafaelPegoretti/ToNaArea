package br.com.hbsis.tonaarea.business;

import android.content.Context;

import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.hbsis.tonaarea.entities.Audit;
import br.com.hbsis.tonaarea.entities.Client;
import br.com.hbsis.tonaarea.entities.Product;
import br.com.hbsis.tonaarea.repositories.Repository;
import br.com.hbsis.tonaarea.util.Constants;
import br.com.hbsis.tonaarea.util.Mock;
import br.com.hbsis.tonaarea.util.SecurityPreferences;

public class MainBusiness {

    Repository mRepository = new Repository();

    public int[] getQuantity(String url, Context context){
        return mRepository.getQuantity(url, context);
    }

    public List<Client> getClients(String url, Context context){
        return mRepository.getClients(url, context);
    }

    public List<Product> getProducts(String url, Context context){
        return mRepository.getProducts(url, context);
    }

    public Date[] getDates(Context context){
        return new Date[]{
                mRepository.getLastUpdateClient(Constants.URL.URL_CLIENTES_ULTIMA_ATUALIZACAO, context),
                mRepository.getLastUpdateProduct(Constants.URL.URL_PRODUTOS_ULTIMA_ATUALIZACAO, context)};
    }

}
