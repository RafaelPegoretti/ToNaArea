package br.com.hbsis.tonaarea.business;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import br.com.hbsis.tonaarea.entities.Audit;
import br.com.hbsis.tonaarea.entities.Client;
import br.com.hbsis.tonaarea.entities.Product;
import br.com.hbsis.tonaarea.repositories.CallbackReponse;
import br.com.hbsis.tonaarea.repositories.Repository;
import br.com.hbsis.tonaarea.util.Constants;

public class NewAuditBusiness implements CallbackReponse{

    Repository mRepository;
    Repository mRepository2;
    CallbackReponse callbackReponse;
    List<Client> clients = new ArrayList<>();
    List<Product> products = new ArrayList<>();
    Context context;

    public NewAuditBusiness(CallbackReponse callbackReponse, Context context) {
        this.callbackReponse = callbackReponse;
        this.context = context;
        mRepository = new Repository(callbackReponse);
        mRepository2 = new Repository(this);
        mRepository2.getClients(context, "0", "0");
        mRepository2.getProducts( "0", "0");
    }

    public boolean priceValidation(String TTVCompetitor, String TTVResale, String TTCCompetitor, String TTCResale) {

        if (priceNotEmpty(TTVCompetitor, TTVResale, TTCCompetitor, TTCResale)) {

            double priceTTVCompetitor = Double.parseDouble(TTVCompetitor);
            double priceTTVResale = Double.parseDouble(TTVResale);
            double priceTTCCompetitor = Double.parseDouble(TTCCompetitor);
            double priceTCCResale = Double.parseDouble(TTCResale);

            return priceTTVCompetitor < 9999.99 && priceTTVCompetitor > 0
                    && priceTTVResale < 9999.99 && priceTTVResale > 0
                    && priceTTCCompetitor < 9999.99 && priceTTCCompetitor > 0
                    && priceTCCResale < 9999.99 && priceTCCResale > 0;
        }else {
            return false;
        }
    }

    private boolean priceNotEmpty(String TTVCompetitor, String TTVResale, String TTCCompetitor, String TTCResale) {
        if (TTVCompetitor.equals("") || TTCResale.equals("") || TTCCompetitor.equals("") || TTVResale.equals("")) {
            return false;
        }else if (TTVCompetitor.equals(".") || TTCResale.equals(".") || TTCCompetitor.equals(".") || TTVResale.equals(".")){
            return false;
        }else {
            return true;
        }
    }

    public void postNewAudit(Audit audit){
        mRepository.postNewAudit(audit,context);
    }

    public void postNewPDV(Client client){
        if (client.getClientName().equals("")) {
            Toast.makeText(context, "O Nome deve ser Informado", Toast.LENGTH_LONG).show();
        }else if (client.getClienteCode().equals("")){
            Toast.makeText(context, "O Código deve ser Informado", Toast.LENGTH_LONG).show();
        }else{
            mRepository.postNewPDV(client, context);
        }

    }

    public boolean clientValidator(String clientName, List<Client> clients){

        for (Client c: clients){
            if (c.getClientName().equals(clientName)){
                return true;
            }
        }
        return false;
    }

    public boolean productValidator(String productName, List<Product> products){

        for (Product p: products){
            if (p.getProductName().equals(productName)){
                return true;
            }
        }
        return false;
    }

    public List<Client> getClients() {
        return clients;
    }

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        try {
            Client client = new Client();
            client.setClientId(jsonObject.getString("id"));
            client.setClientName(jsonObject.getString("nomeCliente"));
            client.setClienteCode(jsonObject.getString("codigoCliente"));
            client.setDate(jsonObject.getString("dataInclusaoAlteracao"));
            client.setRevendaName(jsonObject.getString("nomeRevenda"));
            client.setActive(jsonObject.getBoolean("ativo"));
            clients.add(client);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            Product product = new Product();
            product.setProductId(jsonObject.getString("id"));
            product.setProductName(jsonObject.getString("nomeProduto"));
            product.setActive(jsonObject.getBoolean("ativo"));
            product.setDate(jsonObject.getString("dataInclusaoAlteracao"));
            products.add(product);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(JSONObject jsonObject) {
    }
}
