package br.com.hbsis.tonaarea.dao;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.hbsis.tonaarea.db.DataOpenHelper;
import br.com.hbsis.tonaarea.entities.Client;
import br.com.hbsis.tonaarea.entities.Product;
import br.com.hbsis.tonaarea.repositories.CallbackReponse;
import br.com.hbsis.tonaarea.repositories.Repository;
import br.com.hbsis.tonaarea.util.Constants;
import br.com.hbsis.tonaarea.util.SecurityPreferences;

public class SyncUpdate extends IntentService implements CallbackReponse {

    private Repository mRepository;
    private ClientRepository mClientRepository;
    private ProductRepository mProductRepository;
    private SQLiteDatabase connection;
    private SecurityPreferences mSecurityPreferences;
    String[] dates = new String[2];
    int position = 0;

    public SyncUpdate() {
        super("SyncUpdate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        getRepositories(this);
        mSecurityPreferences = new SecurityPreferences(this);
        mRepository = new Repository(this);
        mRepository.getClients(this, "0", "0");
        mRepository.getProducts("0", "0");
    }

    private void getRepositories(Context context) {
        try {
            DataOpenHelper dataOpenHelper = new DataOpenHelper(context);
            connection = dataOpenHelper.getWritableDatabase();
            mProductRepository = new ProductRepository(connection);
            mClientRepository = new ClientRepository(connection);
        } catch (SQLException e) {
        }
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


            for (Client clientDB: mClientRepository.getAll()){
                if (client.getClientId().equals(clientDB.getClientId())){
                    if (!clientDB.getClienteCode().equals(client.getClienteCode())
                            || !clientDB.getClientName().equals(client.getClientName())
                            || !clientDB.isActive() == client.isActive()
                            || !clientDB.getDate().equals(client.getDate())
                            || !clientDB.getRevendaName().equals(client.getRevendaName())) {
                        mClientRepository.update(client);
                    }
                    break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            Product product = new Product();
            product.setProductId(jsonObject.getString("id"));
            product.setProductName(jsonObject.getString("nomeProduto"));
            product.setActive(jsonObject.getBoolean("ativo"));
            product.setDate(jsonObject.getString("dataInclusaoAlteracao"));

            for (Product productDB: mProductRepository.getAll()){
                if (product.getProductId().equals(productDB.getProductId())){
                    if (!productDB.getProductName().equals(product.getProductName())
                            || !productDB.isActive() == product.isActive()
                            || !productDB.getDate().equals(product.getDate())){
                        mProductRepository.update(product);
                    }
                    break;
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onError(JSONObject jsonObject) {

    }
}

