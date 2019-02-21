package br.com.hbsis.tonaarea.business;

import android.content.Context;
import android.widget.Toast;

import java.util.List;
import br.com.hbsis.tonaarea.entities.Audit;
import br.com.hbsis.tonaarea.entities.Client;
import br.com.hbsis.tonaarea.entities.Product;
import br.com.hbsis.tonaarea.repositories.CallbackReponse;
import br.com.hbsis.tonaarea.repositories.Repository;

public class NewAuditBusiness {

    Repository mRepository;
    CallbackReponse callbackReponse;

    public NewAuditBusiness(CallbackReponse callbackReponse) {
        this.callbackReponse = callbackReponse;
        mRepository = new Repository(callbackReponse);
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

    public void postNewAudit(Audit audit, Context context){
        mRepository.postNewAudit(audit,context);
    }

    public void postNewPDV(Client client, Context context){
        if (client.getClientName().equals("")) {
            Toast.makeText(context, "O Nome deve ser Informado", Toast.LENGTH_LONG).show();
        }else if (client.getClienteCode().equals("")){
            Toast.makeText(context, "O Código deve ser Informado", Toast.LENGTH_LONG).show();
        }else{
            mRepository.postNewPDV(client, context);
        }

    }

    public List<Client> getClients(String url, Context context) {
        return mRepository.getClients(url, context);
    }

    public List<Product> getProducts(String url, Context context) {
        return mRepository.getProducts(url, context);
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
}
