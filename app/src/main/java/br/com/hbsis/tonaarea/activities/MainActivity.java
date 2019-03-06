package br.com.hbsis.tonaarea.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.hbsis.tonaarea.R;
import br.com.hbsis.tonaarea.business.MainBusiness;
import br.com.hbsis.tonaarea.dao.ClientRepository;
import br.com.hbsis.tonaarea.dao.ProductRepository;
import br.com.hbsis.tonaarea.dao.Sync;
import br.com.hbsis.tonaarea.dao.SyncUpdate;
import br.com.hbsis.tonaarea.db.DataOpenHelper;
import br.com.hbsis.tonaarea.entities.Client;
import br.com.hbsis.tonaarea.entities.Login;
import br.com.hbsis.tonaarea.entities.Product;
import br.com.hbsis.tonaarea.repositories.CallbackReponse;
import br.com.hbsis.tonaarea.util.Constants;
import br.com.hbsis.tonaarea.util.SecurityPreferences;
import br.com.hbsis.tonaarea.util.Util;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CallbackReponse {

    private ViewHolder mViewHolder = new ViewHolder();
    private MainBusiness mMainBusiness;
    private ProductRepository mProductRepository;
    private ClientRepository mClientRepository;
    private SQLiteDatabase connection;
    private SecurityPreferences mSecurityPreferences;
    int[] quantity = new int[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainBusiness = new MainBusiness(this, this);
        mSecurityPreferences = new SecurityPreferences(this);
        getRepositories();
        mClientRepository = new ClientRepository(connection);
        mProductRepository = new ProductRepository(connection);

        this.mViewHolder.buttonNewAudit = findViewById(R.id.buttonNewAudit);
        this.mViewHolder.includeStatus = findViewById(R.id.includeStatus);
        this.mViewHolder.layoutStatus = findViewById(R.id.layoutStatus);
        this.mViewHolder.textStatus = findViewById(R.id.textStatus);
        this.mViewHolder.textLogout = findViewById(R.id.textLogout);
        mViewHolder.buttonNewAudit.setOnClickListener(this);
        mViewHolder.textLogout.setOnClickListener(this);
        firstLauch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMainBusiness.getQuantity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonDetails:
                startActivity(new Intent(this, AuditsDetailsActivity.class));
                break;
            case R.id.buttonNewAudit:
                startActivity(new Intent(this, NewAuditActivity.class));
                break;
            case R.id.textLogout:
                Util.logout(this);
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    private void status() {
        if (quantity[0] > 0 || quantity[1] > 0 || quantity[2] > 0) {
            mViewHolder.includeStatus.setVisibility(View.VISIBLE);
            this.mViewHolder.buttonDetails = findViewById(R.id.buttonDetails);
            this.mViewHolder.textNumberOnApproval = findViewById(R.id.textNumberOnApproval);
            this.mViewHolder.textNumberApproved = findViewById(R.id.textNumberApproved);
            this.mViewHolder.textNumberDisapproved = findViewById(R.id.textNumberDisapproved);

            this.mViewHolder.buttonDetails.setOnClickListener(this);
            mViewHolder.textStatus.setTextSize(16);
            setQuantities();
        } else {
            mViewHolder.includeStatus.setVisibility(View.GONE);
            mViewHolder.layoutStatus.setBackgroundResource(R.color.colorBackground);
            mViewHolder.textStatus.setText(getString(R.string.sem_auditorias));
        }
    }

    public void firstLauch() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPreferences.getBoolean(Constants.SECURITY_PREFERENCES_CONSTANTS.KEY_PREFS_FIRST_LAUNCH, true)) {
            mMainBusiness.getClients("0", "0");
            mMainBusiness.getProducts("0", "0");

            SharedPreferences.Editor prefEditor = sharedPreferences.edit();
            prefEditor.putBoolean(Constants.SECURITY_PREFERENCES_CONSTANTS.KEY_PREFS_FIRST_LAUNCH, false);
            prefEditor.commit();
        } else {
            startService(new Intent(this, Sync.class));
            //startService(new Intent(this, SyncUpdate.class));
        }
    }

    private void setQuantities() {
        mViewHolder.textNumberOnApproval.setText("" + quantity[0]);
        mViewHolder.textNumberApproved.setText("" + quantity[1]);
        mViewHolder.textNumberDisapproved.setText("" + quantity[2]);
    }

    private void getRepositories() {
        try {
            DataOpenHelper dataOpenHelper = new DataOpenHelper(this);
            connection = dataOpenHelper.getWritableDatabase();
            mProductRepository = new ProductRepository(connection);
            mClientRepository = new ClientRepository(connection);
        } catch (SQLException e) {
        }
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {

        String[] dates = mMainBusiness.getDates();

        try {
            quantity[0] = jsonObject.getInt("pendente");
            quantity[1] = jsonObject.getInt("aprovada");
            quantity[2] = jsonObject.getInt("reprovada");
            status();
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
            mSecurityPreferences.storeString(Constants.SECURITY_PREFERENCES_CONSTANTS.CLIENT_DATE, dates[0]);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            Product product = new Product();
            product.setProductId(jsonObject.getString("id"));
            product.setProductName(jsonObject.getString("nomeProduto"));
            product.setActive(jsonObject.getBoolean("ativo"));
            product.setDate(jsonObject.getString("dataInclusaoAlteracao"));
            mProductRepository.insert(product);
            mSecurityPreferences.storeString(Constants.SECURITY_PREFERENCES_CONSTANTS.PRODUCT_DATE, dates[1]);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(JSONObject jsonObject) {
        mViewHolder.layoutStatus.setBackgroundResource(R.color.colorBackground);
        mViewHolder.textStatus.setText("Erro ao conectar com o servidor");
    }


    private static class ViewHolder {
        private Button buttonNewAudit;
        private TextView textStatus;
        private Button buttonDetails;
        private TextView textNumberOnApproval;
        private TextView textNumberApproved;
        private TextView textNumberDisapproved;
        private View includeStatus;
        private ConstraintLayout layoutStatus;
        private TextView textLogout;
    }
}
