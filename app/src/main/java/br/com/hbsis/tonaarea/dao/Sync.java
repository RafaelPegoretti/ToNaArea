package br.com.hbsis.tonaarea.dao;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import br.com.hbsis.tonaarea.db.DataOpenHelper;
import br.com.hbsis.tonaarea.entities.Client;
import br.com.hbsis.tonaarea.entities.Product;
import br.com.hbsis.tonaarea.repositories.Repository;
import br.com.hbsis.tonaarea.util.Constants;
import br.com.hbsis.tonaarea.util.SecurityPreferences;

public class Sync extends IntentService {

    private Repository mRepository = new Repository();
    private ClientRepository mClientRepository;
    private ProductRepository mProductRepository;
    private SQLiteDatabase connection;
    private SecurityPreferences mSecurityPreferences;

    public Sync(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        getRepositories(this);
        mSecurityPreferences = new SecurityPreferences(this);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        try {
            Date[] beforeDates = new Date[]{
                    sdf.parse(mSecurityPreferences.getStoredString(Constants.SECURITY_PREFERENCES_CONSTANTS.CLIENT_DATE)),
                    sdf.parse(mSecurityPreferences.getStoredString(Constants.SECURITY_PREFERENCES_CONSTANTS.PRODUCT_DATE))};
            Date[] afterDates = new Date[]{
                    mRepository.getLastUpdateClient(Constants.URL.URL_CLIENTES_ULTIMA_ATUALIZACAO, this),
                    mRepository.getLastUpdateProduct(Constants.URL.URL_PRODUTOS_ULTIMA_ATUALIZACAO, this)};


            if (afterDates[0].after(beforeDates[0])) {
                List<Client> clients = mRepository.getClientsDates(Constants.URL.URL_CLIENTES, this, sdf.format(afterDates[0]), sdf.format(beforeDates[0]));
                for (Client client : clients) {
                    mClientRepository.insert(client);
                }

                List<Client> clientsApi = mRepository.getClients(Constants.URL.URL_CLIENTES, this);
                List<Client> clientsDB = mClientRepository.getAll();
                for (Client clientDB : clientsDB) {
                    boolean exist = false;
                    for (Client clientApi : clientsApi) {
                        if (clientDB.getClientId().equals(clientApi.getClientId())) {
                            if (!clientDB.getClienteCode().equals(clientApi.getClienteCode())
                                    || !clientDB.getClientName().equals(clientApi.getClientName())
                                    || !clientDB.isActive() == clientApi.isActive()
                                    || !clientDB.getDate().equals(clientApi.getDate())
                                    || !clientDB.getRevendaName().equals(clientApi.getRevendaName())) {
                                mClientRepository.update(clientApi);
                            }
                            exist = true;
                            break;
                        }else{
                            exist = false;
                        }
                    }
                    if (!exist){
                        mClientRepository.delete(clientDB.getClientId());
                    }
                }
                mSecurityPreferences.storeString(Constants.SECURITY_PREFERENCES_CONSTANTS.CLIENT_DATE, sdf.format(afterDates[0]));
            }

            if (afterDates[1].after(beforeDates[1])) {
                List<Product> products = mRepository.getProductsDates(Constants.URL.URL_CLIENTES, this, sdf.format(afterDates[1]), sdf.format(beforeDates[1]));
                for (Product product : products) {
                    mProductRepository.insert(product);
                }

                List<Product> productsApi = mRepository.getProducts(Constants.URL.URL_PRODUTOS, this);
                List<Product> productsDB = mProductRepository.getAll();
                for (Product productDB : productsDB) {
                    boolean exist = false;
                    for (Product productApi : productsApi) {
                        if (productDB.getProductId().equals(productApi.getProductId())) {
                            if (!productDB.getProductName().equals(productApi.getProductName())
                                    || !productDB.isActive() == productApi.isActive()
                                    || !productDB.getDate().equals(productApi.getDate())) {
                                mProductRepository.update(productApi);
                            }
                            exist = true;
                            break;
                        } else {
                            exist = false;
                        }
                    }
                    if (!exist) {
                        mProductRepository.delete(productDB.getProductId());
                    }
                }
                mSecurityPreferences.storeString(Constants.SECURITY_PREFERENCES_CONSTANTS.PRODUCT_DATE, sdf.format(afterDates[1]));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void getRepositories(Context context) {
        try {
            DataOpenHelper dataOpenHelper = new DataOpenHelper(context);
            connection = dataOpenHelper.getWritableDatabase();
            mProductRepository = new ProductRepository(connection);
            mClientRepository = new ClientRepository(connection);
        } catch (SQLException e) {
            Log.d("OK", "ok");
        }
    }

}

