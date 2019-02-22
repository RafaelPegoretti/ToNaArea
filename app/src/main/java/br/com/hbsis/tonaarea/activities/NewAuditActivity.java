package br.com.hbsis.tonaarea.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.hbsis.tonaarea.R;
import br.com.hbsis.tonaarea.business.NewAuditBusiness;
import br.com.hbsis.tonaarea.dao.ClientRepository;
import br.com.hbsis.tonaarea.dao.ProductRepository;
import br.com.hbsis.tonaarea.dao.Sync;
import br.com.hbsis.tonaarea.db.DataOpenHelper;
import br.com.hbsis.tonaarea.entities.Audit;
import br.com.hbsis.tonaarea.entities.AuditDTO;
import br.com.hbsis.tonaarea.entities.Client;
import br.com.hbsis.tonaarea.entities.Imagem;
import br.com.hbsis.tonaarea.entities.Product;
import br.com.hbsis.tonaarea.repositories.CallbackReponse;
import br.com.hbsis.tonaarea.util.Constants;
import br.com.hbsis.tonaarea.util.Mock;
import br.com.hbsis.tonaarea.util.SecurityPreferences;

public class NewAuditActivity extends AppCompatActivity implements View.OnClickListener, LocationListener, CallbackReponse {

    private static int REQUEST_LOCATION_CODE = 1;

    private ViewHolder mViewHolder = new ViewHolder();
    private ViewHolderPhoto mViewHolderPhoto = new ViewHolderPhoto();
    private ViewHolderForm mViewHolderForm = new ViewHolderForm();
    private ViewHolderPDV mViewHolderPDV = new ViewHolderPDV();
    private ViewHolderProduct mViewHolderProduct = new ViewHolderProduct();
    private NewAuditBusiness mNewAuditBusiness;
    private SecurityPreferences mSecurityPreferences;
    private int position = 1;
    private Audit audit = new Audit();
    private Imagem imagem = new Imagem();
    private Boolean[] first = new Boolean[4];
    private Boolean[] photos = new Boolean[]{false, false};
    private FusedLocationProviderClient mFusedLocationClient;
    private List<Client> clients;
    private List<Product> products;
    private String clientId;
    private String productId;
    private ProductRepository mProductRepository;
    private ClientRepository mClientRepository;
    private SQLiteDatabase connection;
    private int characters = 0;

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                setCordinates(location);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_audit);

        getRepositories();
        mNewAuditBusiness = new NewAuditBusiness(this);
        mSecurityPreferences = new SecurityPreferences(this);
        mProductRepository = new ProductRepository(connection);
        mClientRepository = new ClientRepository(connection);


        this.mViewHolder.textStep = findViewById(R.id.TextStep);
        this.mViewHolder.viewStubImage = findViewById(R.id.viewStubImage);
        this.mViewHolder.viewStubPrices = findViewById(R.id.viewStubPrices);
        this.mViewHolder.viewStubPDV = findViewById(R.id.viewStubPDV);
        this.mViewHolder.buttonPrevius = findViewById(R.id.buttonPrevious);
        this.mViewHolder.buttonNext = findViewById(R.id.buttonNext);
        this.mViewHolder.comfirmation = findViewById(R.id.comfirmation);
        this.mViewHolder.buttonComfirmation = findViewById(R.id.buttonComfirmation);
        this.mViewHolder.loadingView = findViewById(R.id.loadingView);
        this.mViewHolder.newPDV = findViewById(R.id.newPDV);
        this.mViewHolder.viewStubProduct = findViewById(R.id.viewStubProduct);

        mViewHolder.viewStubImage.setLayoutResource(R.layout.new_audit_layout);
        mViewHolder.viewStubImage.inflate();

        mViewHolder.viewStubPrices.setLayoutResource(R.layout.audit_form_layout);
        mViewHolder.viewStubPrices.inflate();
        mViewHolder.viewStubPrices.setVisibility(View.GONE);

        mViewHolder.viewStubPDV.setLayoutResource(R.layout.pdv_layout);
        mViewHolder.viewStubPDV.inflate();
        mViewHolder.viewStubPDV.setVisibility(View.GONE);

        mViewHolder.viewStubProduct.setLayoutResource(R.layout.product_name_layout);
        mViewHolder.viewStubProduct.inflate();
        mViewHolder.viewStubProduct.setVisibility(View.GONE);

        this.mViewHolderPhoto.buttonPhotograph = findViewById(R.id.buttonPhotograph);
        this.mViewHolderPhoto.imagePhoto = findViewById(R.id.imagePhoto);
        this.mViewHolderPhoto.imagePhotoInclude = findViewById(R.id.imagePhotoInclude);
        this.mViewHolderPhoto.textImageDescription = findViewById(R.id.textImageDescription);

        this.mViewHolderForm.editTTVCompetitor = findViewById(R.id.editTTVCompetitor);
        this.mViewHolderForm.editTTVResale = findViewById(R.id.editTTVResale);
        this.mViewHolderForm.editTTCCompetitor = findViewById(R.id.editTTCCompetitor);
        this.mViewHolderForm.editTTCResale = findViewById(R.id.editTTCResale);

        this.mViewHolderPDV.editPDV = findViewById(R.id.editPDV);
        this.mViewHolderPDV.editNewPDV = findViewById(R.id.editNewPDV);
        this.mViewHolderPDV.editCodePDV = findViewById(R.id.editCodePDV);

        this.mViewHolderPDV.buttonNewPDV = findViewById(R.id.buttonNewPDV);
        this.mViewHolderPDV.buttonAddPDV = findViewById(R.id.buttonAddPDV);
        this.mViewHolderPDV.buttonCancel = findViewById(R.id.buttonCancel);
        this.mViewHolderPDV.editNewPDV = findViewById(R.id.editNewPDV);
        this.mViewHolderPDV.editCodePDV = findViewById(R.id.editCodePDV);

        this.mViewHolderProduct.editProduct = findViewById(R.id.editProduct);
        this.mViewHolderProduct.editDescription = findViewById(R.id.editDescription);
        this.mViewHolderProduct.textCharacter = findViewById(R.id.textCharacter);

        mViewHolder.textStep.setText(getString(R.string.fotografe_preco_irregular));
        buttonNextEnabledFalse();

        listeners();

        getPermission();

        first[0] = false;
        first[1] = false;
        first[2] = false;

        clients = mClientRepository.getAll();
        products = mProductRepository.getAll();

        autoCompletePDV(clients);
        autoCompleteProduct(products);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, Sync.class));
        startLocationUpdates();
    }

    private void autoCompletePDV(List<Client> clients) {
        List<String> as = new ArrayList<>();
        for (Client x : clients) {
            as.add(x.getClientName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, as);
        mViewHolderPDV.editPDV.setAdapter(adapter);
        mViewHolderPDV.editPDV.setThreshold(1);
    }


    private void autoCompleteProduct(List<Product> products) {
        List<String> as = new ArrayList<>();
        for (Product x : products) {
            as.add(x.getProductName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, as);
        mViewHolderProduct.editProduct.setAdapter(adapter);
        mViewHolderProduct.editProduct.setThreshold(1);
    }

    private void buttonNextEnabledTrue() {
        mViewHolder.buttonNext.setEnabled(true);
        mViewHolder.buttonNext.setBackgroundResource(R.drawable.button_shape_blue);
    }

    private void buttonNextEnabledFalse() {
        mViewHolder.buttonNext.setEnabled(false);
           mViewHolder.buttonNext.setBackgroundResource(R.drawable.button_shape_blue_enabled);
    }

    public void listeners() {
        mViewHolder.buttonPrevius.setOnClickListener(this);
        mViewHolder.buttonNext.setOnClickListener(this);
        mViewHolderPhoto.buttonPhotograph.setOnClickListener(this);
        mViewHolder.buttonComfirmation.setOnClickListener(this);
        mViewHolderPDV.buttonNewPDV.setOnClickListener(this);
        mViewHolderPDV.buttonCancel.setOnClickListener(this);
        mViewHolderPDV.buttonAddPDV.setOnClickListener(this);
        mViewHolderProduct.editDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                characters = mViewHolderProduct.editDescription.length();
                mViewHolderProduct.textCharacter.setText(40 - characters + "");
            }
        });
    }

    private void setValues() {
        audit.setTTVConcorrente(mViewHolderForm.editTTVCompetitor.getText().toString());
        audit.setTTVRevenda(mViewHolderForm.editTTVResale.getText().toString());
        audit.setTTCConcorrente(mViewHolderForm.editTTCCompetitor.getText().toString());
        audit.setTTCRevenda(mViewHolderForm.editTTCResale.getText().toString());

        Calendar c = Calendar.getInstance();
        audit.setInstant(c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + 1);

        audit.setStatus(Constants.STATUS_WORK_FLOW.ON_APPROVAL);
    }

    private void coordinates() {

        mViewHolder.loadingView.setVisibility(View.VISIBLE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            setCordinates(location);
                            mViewHolder.loadingView.setVisibility(View.GONE);
                        }
                    }
                });

    }

    private void setCordinates(Location location) {
        if (location != null) {
            audit.setLongitude(location.getLongitude());
            audit.setLatitude(location.getLatitude());
        }
    }

    private void getPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_LOCATION_CODE
        );

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            coordinates();
        } else {
            Toast.makeText(this, "Você não terá as coordenadas para registrar", Toast.LENGTH_LONG).show();
        }

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if ((netInfo != null) && (!netInfo.isConnectedOrConnecting()) && (!netInfo.isAvailable())) {
            Toast.makeText(this, "não conectado a internet", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(REQUEST_LOCATION_CODE == requestCode){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                coordinates();
            } else {
                Toast.makeText(this, "Você não terá as coordenadas para registrar", Toast.LENGTH_LONG).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mFusedLocationClient.requestLocationUpdates(locationRequest,
                mLocationCallback,
                null);
    }

    private void setAudit(Audit audit) {

        audit.setRevendaId(mSecurityPreferences.getStoredString(Constants.SECURITY_PREFERENCES_CONSTANTS.REV_ID));
        audit.setClientId(clientId);
        audit.setImagem(imagem);
        audit.setProductId(productId);
        audit.setAuditorUserId(mSecurityPreferences.getStoredString(Constants.SECURITY_PREFERENCES_CONSTANTS.USER_ID));
        audit.setDescription(mViewHolderProduct.editDescription.getText().toString());

        mNewAuditBusiness.postNewAudit(audit, this);

    }

    @Override
    public void onLocationChanged(Location location) {
        setCordinates(location);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPrevious:
                buttonNextEnabledTrue();
                switch (position) {
                    case 1:
                        onBackPressed();
                        break;
                    case 2:
                        mViewHolder.textStep.setText(getString(R.string.fotografe_preco_irregular));
                        mViewHolderPhoto.imagePhoto.setImageBitmap(Mock.converteBase64Photo(imagem.getImageIrregularPrice()));
                        break;
                    case 3:
                        mViewHolder.textStep.setText(getString(R.string.fotografe_numero_lote));
                        mViewHolderPhoto.imagePhoto.setImageBitmap(Mock.converteBase64Photo(imagem.getImageLotNumber()));
                        mViewHolder.viewStubPDV.setVisibility(View.GONE);
                        mViewHolder.viewStubImage.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        mViewHolder.viewStubPrices.setVisibility(View.GONE);
                        mViewHolder.viewStubPDV.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        mViewHolder.viewStubPrices.setVisibility(View.VISIBLE);
                        mViewHolder.viewStubProduct.setVisibility(View.GONE);
                        mViewHolder.buttonNext.setText("Próximo");
                        break;
                }
                position--;
                break;

            case R.id.buttonNext:
                switch (position) {
                    case 1:
                        if (photos[0]) {
                            if (first[1]) {
                                mViewHolderPhoto.imagePhoto.setImageBitmap(Mock.converteBase64Photo(imagem.getImageLotNumber()));
                            } else {
                                buttonNextEnabledFalse();
                                mViewHolderPhoto.buttonPhotograph.setText(getString(R.string.fotografar));
                                mViewHolderPhoto.imagePhotoInclude.setVisibility(View.GONE);
                                mViewHolderPhoto.textImageDescription.setText("Fotografe o número do lote para visualiza-lo aqui");
                            }

                            mViewHolder.textStep.setText(getString(R.string.fotografe_numero_lote));
                            first[0] = true;
                            position++;
                        }
                        break;
                    case 2:
                        if (photos[1]) {
                            if (first[0]) {
                                mViewHolderPhoto.imagePhoto.setImageBitmap(Mock.converteBase64Photo(imagem.getImageIrregularPrice()));
                            } else {
                                buttonNextEnabledFalse();
                            }

                            mViewHolder.viewStubImage.setVisibility(View.GONE);
                            mViewHolder.viewStubPDV.setVisibility(View.VISIBLE);
                            mViewHolder.textStep.setText(getString(R.string.dados_pdv));
                            first[1] = true;
                            position++;
                        }
                        break;
                    case 3:
                        if (mNewAuditBusiness.clientValidator(mViewHolderPDV.editPDV.getText().toString(), clients)) {
                            step3(mViewHolderPDV.editPDV.getText().toString());
                        } else {
                            Toast.makeText(this, "PDV não existente", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 4:
                        if (mNewAuditBusiness.priceValidation(mViewHolderForm.editTTVCompetitor.getText().toString(),
                                mViewHolderForm.editTTVResale.getText().toString(),
                                mViewHolderForm.editTTCCompetitor.getText().toString(),
                                mViewHolderForm.editTTCResale.getText().toString())) {
                            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mViewHolderForm.editTTCResale.getWindowToken(), 0);
                            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mViewHolderForm.editTTCCompetitor.getWindowToken(), 0);
                            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mViewHolderForm.editTTVResale.getWindowToken(), 0);
                            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mViewHolderForm.editTTVCompetitor.getWindowToken(), 0);
                            mViewHolder.viewStubPrices.setVisibility(View.GONE);
                            mViewHolder.viewStubProduct.setVisibility(View.VISIBLE);
                            position++;
                            mViewHolder.buttonNext.setText("Finalizar");
                            mViewHolder.textStep.setText(getString(R.string.produto_nome));
                        } else {
                            Toast.makeText(this, "insira valores entre 0 e 9999.99", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 5:
                        if (mNewAuditBusiness.productValidator(mViewHolderProduct.editProduct.getText().toString(), products)) {
                            productId = getProductId(mViewHolderProduct.editProduct.getText().toString());
                            setValues();
                            setAudit(audit);
                        } else {
                            Toast.makeText(this, "Produto não existente", Toast.LENGTH_LONG).show();
                        }
                        break;
                }
                break;
            case R.id.buttonPhotograph:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 20);
                break;
            case R.id.buttonComfirmation:
                finish();
                break;
            case R.id.buttonNewPDV:
                mViewHolder.newPDV.setVisibility(View.VISIBLE);
                break;
            case R.id.buttonCancel:
                mViewHolder.newPDV.setVisibility(View.GONE);
                break;
            case R.id.buttonAddPDV:
                Client client = new Client(mViewHolderPDV.editNewPDV.getText().toString(), mViewHolderPDV.editCodePDV.getText().toString(), false, mSecurityPreferences.getStoredString(Constants.SECURITY_PREFERENCES_CONSTANTS.REV_ID));
                mNewAuditBusiness.postNewPDV(client, this);
                startService(new Intent(this, Sync.class));
                break;
        }
    }

    private void step3(String clientName) {
        mViewHolder.viewStubPDV.setVisibility(View.GONE);
        mViewHolder.viewStubPrices.setVisibility(View.VISIBLE);
        mViewHolder.textStep.setText(getString(R.string.precos));
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mViewHolderPDV.editPDV.getWindowToken(), 0);
        position++;
        first[2] = true;
        clientId = getClientId(clientName);
    }


    private String getClientId(String s) {
        for (Client c : clients) {
            if (c.getClientName().equals(s)) {
                return c.getClientId();
            }
        }
        return "";
    }

    private String getProductId(String s) {
        for (Product c : products) {
            if (c.getProductName().equals(s)) {
                return c.getProductId();
            }
        }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        mViewHolderPhoto.imagePhotoInclude.setVisibility(View.VISIBLE);

        try {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mViewHolderPhoto.imagePhoto.setImageBitmap((photo));

            if (position == 1) {
                imagem.setImageIrregularPrice(Mock.convertPhotoBase64(photo));
                photos[0] = true;
            } else if (position == 2) {
                imagem.setImageLotNumber(Mock.convertPhotoBase64(photo));
                photos[1] = true;
            }
            mViewHolderPhoto.buttonPhotograph.setText(getString(R.string.trocar_foto));

            buttonNextEnabledTrue();
        } catch (NullPointerException e) {

        }
    }

    @Override
    public void onBackPressed() {
        if (mViewHolder.newPDV.getVisibility() == View.VISIBLE) {
            mViewHolder.newPDV.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {

        try {
            if (jsonObject.getBoolean("success")) {
                switch (position) {
                    case 5:
                        mViewHolder.comfirmation.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        clients = mNewAuditBusiness.getClients(Constants.URL.URL_CLIENTES, this);
                        autoCompletePDV(clients);
                        mViewHolder.newPDV.setVisibility(View.GONE);
                        break;
                }


            } else {
                StringBuilder message = new StringBuilder();
                JSONArray errors = jsonObject.getJSONArray("errors");
                for (int i = 0; i < errors.length(); i++) {
                    message.append(errors.getString(i));
                    message.append("\n");
                }
                Toast.makeText(this, message.toString(), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(JSONObject jsonObject) {

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
        private TextView textStep;
        private ViewStub viewStubImage;
        private ViewStub viewStubPrices;
        private ViewStub viewStubPDV;
        private ViewStub viewStubProduct;
        private Button buttonPrevius;
        private Button buttonNext;
        private View comfirmation;
        private Button buttonComfirmation;
        private View loadingView;
        private View newPDV;
    }

    private static class ViewHolderPhoto {
        private ImageView imagePhoto;
        private Button buttonPhotograph;
        private TextView textImageDescription;
        private View imagePhotoInclude;
    }

    private static class ViewHolderPDV {
        private AutoCompleteTextView editPDV;
        private EditText editNewPDV;
        private EditText editCodePDV;
        private Button buttonNewPDV;
        private Button buttonAddPDV;
        private Button buttonCancel;
    }

    private static class ViewHolderForm {
        private EditText editTTVCompetitor;
        private EditText editTTVResale;
        private EditText editTTCCompetitor;
        private EditText editTTCResale;
    }

    private static class ViewHolderProduct {
        private AutoCompleteTextView editProduct;
        private EditText editDescription;
        private TextView textCharacter;
    }

}
