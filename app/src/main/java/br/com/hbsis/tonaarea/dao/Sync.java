package br.com.hbsis.tonaarea.dao;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.hbsis.tonaarea.db.DataOpenHelper;
import br.com.hbsis.tonaarea.entities.Client;
import br.com.hbsis.tonaarea.entities.Product;
import br.com.hbsis.tonaarea.repositories.CallbackReponse;
import br.com.hbsis.tonaarea.repositories.Repository;
import br.com.hbsis.tonaarea.util.Constants;
import br.com.hbsis.tonaarea.util.Mock;
import br.com.hbsis.tonaarea.util.SecurityPreferences;

public class Sync extends IntentService implements CallbackReponse {

    private Repository mRepository;
    private ClientRepository mClientRepository;
    private ProductRepository mProductRepository;
    private SQLiteDatabase connection;
    private SecurityPreferences mSecurityPreferences;
    String[] dates = new String[2];
    int position = 0;
    Context context;

    public Sync(String name) {
        super(name);
        this.context = context;
    }

        @Override
    public void onHandleIntent(Intent intent) {
        getRepositories(context);
        position = 0;
        mSecurityPreferences = new SecurityPreferences(context);
        mRepository = new Repository(this);
        mRepository.getLastUpdateClient(context);
        mRepository.getLastUpdateProduct();
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
            dates[position] = jsonObject.getString("dataUltimaAtualizacao");
            position++;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            Client client = new Client();
            client.setClientId(jsonObject.getString("id"));
            client.setClientName(jsonObject.getString("nomeCliente"));
            client.setClienteCode(jsonObject.getString("codigoCliente"));
            client.setDate(jsonObject.getString("dataInclusaoAlteracao"));
            client.setRevendaName(jsonObject.getString("nomeRevenda"));
            client.setActive(jsonObject.getBoolean("ativo"));
            mClientRepository.insert(client);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }

        try {
            Product product = new Product();
            product.setProductId(jsonObject.getString("id"));
            product.setProductName(jsonObject.getString("nomeProduto"));
            product.setActive(jsonObject.getBoolean("ativo"));
            product.setDate(jsonObject.getString("dataInclusaoAlteracao"));
            mProductRepository.insert(product);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (position == 2) {
            mRepository.getClients(context, mSecurityPreferences.getStoredString(Constants.SECURITY_PREFERENCES_CONSTANTS.CLIENT_DATE), dates[0]);
            mRepository.getProducts(mSecurityPreferences.getStoredString(Constants.SECURITY_PREFERENCES_CONSTANTS.PRODUCT_DATE), dates[1]);
        }

    }

    @Override
    public void onError(JSONObject jsonObject) {
        Log.e("", "");
    }
}

