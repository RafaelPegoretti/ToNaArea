package br.com.hbsis.tonaarea.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import br.com.hbsis.tonaarea.R;
import br.com.hbsis.tonaarea.business.DetailsBusiness;
import br.com.hbsis.tonaarea.entities.Audit;
import br.com.hbsis.tonaarea.util.Constants;
import br.com.hbsis.tonaarea.util.Mock;
import br.com.hbsis.tonaarea.util.SecurityPreferences;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();
    private Audit audit;
    private String id;
    private DetailsBusiness mDetailsBusiness = new DetailsBusiness();
    List<String> list;
    int position;
    SecurityPreferences mSecurityPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mSecurityPreferences = new SecurityPreferences(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getString("id");

        list = mDetailsBusiness.getAuditsIds(Constants.URL.URL_AUDITORIAS, mSecurityPreferences.getStoredString(Constants.SECURITY_PREFERENCES_CONSTANTS.USER_ID), 9, true, this);

        position = list.size();
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

        listeners();

        audit = mDetailsBusiness.getAudit(Constants.URL.URL_AUDITORIAS, id, this);

        setValues();
        blockButtonNext();
        buttonNextIsBlock();

    }

    public void listeners() {
        mViewHolder.buttonPrevious.setOnClickListener(this);
        mViewHolder.buttonNext.setOnClickListener(this);
        mViewHolder.imageIrregularPrice.setOnClickListener(this);
        mViewHolder.imageLotNumber.setOnClickListener(this);
        mViewHolder.imagePhoto.setOnClickListener(this);
    }

    private void setValues() {
        mViewHolder.textSalePointName.setText(audit.getName());
        mViewHolder.textProductName.setText(audit.getProduct());
        mViewHolder.textTTVCompetitorPrice.setText(audit.getTTVConcorrente());
        mViewHolder.textTTVResalePrice.setText(audit.getTTVRevenda());
        mViewHolder.textTTCCompetitorPrice.setText(audit.getTTCConcorrente());
        mViewHolder.textTTCResalePrice.setText(audit.getTTCRevenda());
        mViewHolder.imageLotNumber.setImageBitmap(Mock.converteBase64Photo(audit.getImagem().getImageLotNumber()));
        mViewHolder.imageIrregularPrice.setImageBitmap(Mock.converteBase64Photo(audit.getImagem().getImageIrregularPrice()));
    }

    private void blockButtonNext() {
        if (id.equals(list.get(list.size() - 1))) {
            mViewHolder.buttonNext.setEnabled(false);
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
                audit = mDetailsBusiness.getAudit(Constants.URL.URL_AUDITORIAS, id, this);

                setValues();
                buttonNextIsBlock();
                break;
            case R.id.buttonNext:
                getNextId();
                audit = mDetailsBusiness.getAudit(Constants.URL.URL_AUDITORIAS, id, this);
                setValues();
                blockButtonNext();
                buttonNextIsBlock();
                break;
            case R.id.imageIrregularPrice:
                mViewHolder.imageInclude.setVisibility(View.VISIBLE);
                mViewHolder.imagePhoto.setImageBitmap(Mock.converteBase64Photo(audit.getImagem().getImageIrregularPrice()));
                break;
            case R.id.imageLotNumber:
                mViewHolder.imageInclude.setVisibility(View.VISIBLE);
                mViewHolder.imagePhoto.setImageBitmap(Mock.converteBase64Photo(audit.getImagem().getImageLotNumber()));
                break;
            case R.id.imagePhoto:
                mViewHolder.imageInclude.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mViewHolder.imageInclude.getVisibility() == View.VISIBLE){
            mViewHolder.imageInclude.setVisibility(View.GONE);
        }else{
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
    }

}
