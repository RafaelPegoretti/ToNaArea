package br.com.hbsis.tonaarea.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.hbsis.tonaarea.R;
import br.com.hbsis.tonaarea.business.DetailsBusiness;
import br.com.hbsis.tonaarea.entities.Audit;
import br.com.hbsis.tonaarea.repositories.CallbackReponse;
import br.com.hbsis.tonaarea.util.SecurityPreferences;
import br.com.hbsis.tonaarea.util.Util;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener, CallbackReponse {

    private ViewHolder mViewHolder = new ViewHolder();
    private Audit audit = new Audit();
    private String id;
    private DetailsBusiness mDetailsBusiness;
    List<String> list = new ArrayList<>();
    int position;
    SecurityPreferences mSecurityPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mDetailsBusiness = new DetailsBusiness(this, this);
        mSecurityPreferences = new SecurityPreferences(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getString("id");

        this.mViewHolder.textSalePointName = findViewById(R.id.textSalePointName);
        this.mViewHolder.textProductName = findViewById(R.id.textProductName);
        this.mViewHolder.textTTVCompetitorPrice = findViewById(R.id.textTTVCompetitorPrice);
        this.mViewHolder.textTTCCompetitorPrice = findViewById(R.id.textTTCCompetitorPrice);
        this.mViewHolder.textTTVResalePrice = findViewById(R.id.textTTVResalePrice);
        this.mViewHolder.textTTCResalePrice = findViewById(R.id.textTTCResalePrice);
        this.mViewHolder.imageIrregularPrice = findViewById(R.id.imageIrregularPrice);
        this.mViewHolder.imageLotNumber = findViewById(R.id.imageLotNumber);
        this.mViewHolder.buttonPrevious = findViewById(R.id.buttonPrevious);
        this.mViewHolder.buttonNext = findViewById(R.id.buttonNext);
        this.mViewHolder.imagePhoto = findViewById(R.id.imagePhoto);
        this.mViewHolder.imageInclude = findViewById(R.id.imageInclude);
        this.mViewHolder.textLogout = findViewById(R.id.textLogout);


        listeners();

        mDetailsBusiness.getAudit(id, this);
        //audit = mDetailsBusiness.getAudit(Constants.URL.URL_AUDITORIAS, id, this);
    }

    public void listeners() {
        mViewHolder.buttonPrevious.setOnClickListener(this);
        mViewHolder.buttonNext.setOnClickListener(this);
        mViewHolder.imageIrregularPrice.setOnClickListener(this);
        mViewHolder.imageLotNumber.setOnClickListener(this);
        mViewHolder.imagePhoto.setOnClickListener(this);
        mViewHolder.textLogout.setOnClickListener(this);

    }

    private void setValues() {
        mViewHolder.textSalePointName.setText(audit.getName());
        mViewHolder.textProductName.setText(audit.getProduct());
        mViewHolder.textTTVCompetitorPrice.setText(audit.getTTVConcorrente());
        mViewHolder.textTTVResalePrice.setText(audit.getTTVRevenda());
        mViewHolder.textTTCCompetitorPrice.setText(audit.getTTCConcorrente());
        mViewHolder.textTTCResalePrice.setText(audit.getTTCRevenda());
        mViewHolder.imageLotNumber.setImageBitmap(Util.converteBase64Photo(audit.getImagem().getImageLotNumber()));
        mViewHolder.imageIrregularPrice.setImageBitmap(Util.converteBase64Photo(audit.getImagem().getImageIrregularPrice()));
    }

    private void blockButtonNext() {
        try {
            if (id.equals(list.get(list.size() - 1))) {
                mViewHolder.buttonNext.setEnabled(false);
            }
        } catch (Exception e) {
        }
    }

    private void buttonNextIsBlock() {
        if (mViewHolder.buttonNext.isEnabled()) {
            mViewHolder.buttonNext.setBackgroundResource(R.drawable.button_shape_blue);
        } else {
            mViewHolder.buttonNext.setBackgroundResource(R.drawable.button_shape_blue_enabled);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPrevious:
                if (list.get(0).equals(id)) {
                    onBackPressed();
                }
                mViewHolder.buttonNext.setEnabled(true);
                getPreviusId();
                //audit = mDetailsBusiness.getAudit(Constants.URL.URL_AUDITORIAS, id, this);
                mDetailsBusiness.getAudit(id, this);
                setValues();
                buttonNextIsBlock();
                break;
            case R.id.buttonNext:
                getNextId();
                //audit = mDetailsBusiness.getAudit(Constants.URL.URL_AUDITORIAS, id, this);
                mDetailsBusiness.getAudit(id, this);
                setValues();
                blockButtonNext();
                buttonNextIsBlock();
                break;
            case R.id.imageIrregularPrice:
                mViewHolder.imageInclude.setVisibility(View.VISIBLE);
                mViewHolder.imagePhoto.setImageBitmap(Util.converteBase64Photo(audit.getImagem().getImageIrregularPrice()));
                break;
            case R.id.imageLotNumber:
                mViewHolder.imageInclude.setVisibility(View.VISIBLE);
                mViewHolder.imagePhoto.setImageBitmap(Util.converteBase64Photo(audit.getImagem().getImageLotNumber()));
                break;
            case R.id.imagePhoto:
                mViewHolder.imageInclude.setVisibility(View.GONE);
                break;
            case R.id.textLogout:
                Util.logout(this);
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mViewHolder.imageInclude.getVisibility() == View.VISIBLE) {
            mViewHolder.imageInclude.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    private void getNextId() {
        int i;
        for (i = 0; i < list.size(); i++) {
            if (list.get(i).equals(id)) {
                break;
            }
        }
        if (position != i + 1) {
            id = list.get(i + 1);
        }
    }

    private void getPreviusId() {
        int i;
        for (i = 0; i < list.size(); i++) {
            if (list.get(i).equals(id)) {
                break;
            }
        }
        if (i - 1 >= 0) {
            id = list.get(i - 1);
        }
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {

        try {
            audit.setId(jsonObject.getString("id"));
            audit.setNameRevenda(jsonObject.getString("nomeRevenda"));
            audit.setName(jsonObject.getString("nomeCliente"));
            audit.setProduct(jsonObject.getString("nomeProduto"));
            audit.setTTCRevenda(jsonObject.getString("valorTtcRevenda"));
            audit.setTTVRevenda(jsonObject.getString("valorTtvRevenda"));
            audit.setTTCConcorrente(jsonObject.getString("valorTtcAuditado"));
            audit.setTTVConcorrente(jsonObject.getString("valorTtvAuditado"));
            audit.setDescription(jsonObject.getString("descricao"));
            audit.setCoodinates(jsonObject.getString("coordenadas"));
            audit.setNameAuditor(jsonObject.getString("nomeAuditor"));
            audit.setInstant(jsonObject.getString("dataAuditoria"));
            audit.setStatus(jsonObject.getString("statusWorkFlow"));

            JSONObject jsonImage = jsonObject.getJSONObject("imagem");
            audit.getImagem().setImageId(jsonImage.getString("id"));
            audit.getImagem().setImageIrregularPrice(jsonImage.getString("precoIrregularBase64"));
            audit.getImagem().setImageLotNumber(jsonImage.getString("loteBase64"));

            try {
                audit.setDateWorkFlow(jsonObject.getString("dataWorkflow"));
                audit.setNameUserWorkFlow(jsonObject.getString("nomeUsuarioWorkflow"));
            } catch (JSONException e) {
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        list = mDetailsBusiness.getIds();
        position = list.size();
        setValues();
        blockButtonNext();
        buttonNextIsBlock();
    }

    @Override
    public void onError(JSONObject jsonObject) {

    }

    private static class ViewHolder {
        private TextView textSalePointName;
        private TextView textProductName;
        private TextView textTTVCompetitorPrice;
        private TextView textTTCCompetitorPrice;
        private TextView textTTVResalePrice;
        private TextView textTTCResalePrice;
        private ImageView imageIrregularPrice;
        private ImageView imageLotNumber;
        private Button buttonPrevious;
        private Button buttonNext;
        private ImageView imagePhoto;
        private View imageInclude;
        private TextView textLogout;
    }

}
