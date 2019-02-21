package br.com.hbsis.tonaarea.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.hbsis.tonaarea.R;
import br.com.hbsis.tonaarea.business.MainBusiness;
import br.com.hbsis.tonaarea.dao.ClientRepository;
import br.com.hbsis.tonaarea.dao.ProductRepository;
import br.com.hbsis.tonaarea.dao.Sync;
import br.com.hbsis.tonaarea.db.DataOpenHelper;
import br.com.hbsis.tonaarea.entities.Client;
import br.com.hbsis.tonaarea.entities.Product;
import br.com.hbsis.tonaarea.util.Constants;
import br.com.hbsis.tonaarea.util.Mock;
import br.com.hbsis.tonaarea.util.SecurityPreferences;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();
    private MainBusiness mMainBusiness = new MainBusiness();
    private ProductRepository mProductRepository;
    private ClientRepository mClientRepository;
    private SQLiteDatabase connection;
    private SecurityPreferences mSecurityPreferences;
    int[] quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSecurityPreferences = new SecurityPreferences(this);
        getRepositories();
        mClientRepository = new ClientRepository(connection);
        mProductRepository = new ProductRepository(connection);
        firstLauch();

        this.mViewHolder.buttonNewAudit = findViewById(R.id.buttonNewAudit);
        this.mViewHolder.stubStatus = findViewById(R.id.stubStatus);
//        this.mViewHolder.layoutStatus = findViewById(R.id.layoutStatus);
        this.mViewHolder.textStatus = findViewById(R.id.textStatus);
//        this.mViewHolder.scrollView = findViewById(R.id.scrollView);
        mViewHolder.buttonNewAudit.setOnClickListener(this);

        quantity = mMainBusiness.getQuantity(Constants.URL.URL_AUDITORIAS_QUANTIDADE, this);

        if (quantity[0] > 0 || quantity[1] > 0 || quantity[2] > 0) {
            status();
        } else {
            mViewHolder.layoutStatus.setBackgroundResource(R.color.colorBackground);
            mViewHolder.textStatus.setText(getString(R.string.sem_auditorias));
            mViewHolder.scrollView.getLayoutParams().height = FrameLayout.LayoutParams.WRAP_CONTENT;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, Sync.class));
        quantity = mMainBusiness.getQuantity(Constants.URL.URL_AUDITORIAS_QUANTIDADE, this);
        if (quantity[0] > 0 || quantity[1] > 0 || quantity[2] > 0) {
            setQuantities();
        }
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
        }
    }

    private void status() {
        mViewHolder.stubStatus.setLayoutResource(R.layout.main_status_layout);
        mViewHolder.stubStatus.inflate();
        this.mViewHolder.buttonDetails = findViewById(R.id.buttonDetails);
        this.mViewHolder.textNumberOnApproval = findViewById(R.id.textNumberOnApproval);
        this.mViewHolder.textNumberApproved = findViewById(R.id.textNumberApproved);
        this.mViewHolder.textNumberDisapproved = findViewById(R.id.textNumberDisapproved);

        mViewHolder.textStatus.setTextSize(16);

        setQuantities();


        this.mViewHolder.buttonDetails.setOnClickListener(this);
    }

    public void firstLauch() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPreferences.getBoolean(Constants.SECURITY_PREFERENCES_CONSTANTS.KEY_PREFS_FIRST_LAUNCH, true)) {
            List<Client> clients = mMainBusiness.getClients(Constants.URL.URL_CLIENTES, this);
            for (Client client: clients){
                mClientRepository.insert(client);
            }
            for (Product product: mMainBusiness.getProducts(Constants.URL.URL_PRODUTOS, this)){
                mProductRepository.insert(product);
            }
            SharedPreferences.Editor prefEditor = sharedPreferences.edit();
            prefEditor.putBoolean(Constants.SECURITY_PREFERENCES_CONSTANTS.KEY_PREFS_FIRST_LAUNCH, false);
            prefEditor.commit();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date[] dates = mMainBusiness.getDates(this);
            mSecurityPreferences.storeString(Constants.SECURITY_PREFERENCES_CONSTANTS.CLIENT_DATE, sdf.format(dates[0]));
            mSecurityPreferences.storeString(Constants.SECURITY_PREFERENCES_CONSTANTS.PRODUCT_DATE, sdf.format(dates[1]));

        }
    }

    private void setQuantities(){
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
            Log.d("OK", "ok");
        }
    }



    private static class ViewHolder {
        private Button buttonNewAudit;
        private TextView textStatus;
        private Button buttonDetails;
        private TextView textNumberOnApproval;
        private TextView textNumberApproved;
        private TextView textNumberDisapproved;
        private ViewStub stubStatus;
        private ConstraintLayout layoutStatus;
        private ScrollView scrollView;
    }
}
