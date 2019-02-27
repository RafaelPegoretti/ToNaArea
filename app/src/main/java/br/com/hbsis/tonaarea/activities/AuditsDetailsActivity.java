package br.com.hbsis.tonaarea.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.hbsis.tonaarea.R;
import br.com.hbsis.tonaarea.adapters.AuditsDetailsAdapter;
import br.com.hbsis.tonaarea.business.AuditsDetailsBusiness;
import br.com.hbsis.tonaarea.entities.Audit;
import br.com.hbsis.tonaarea.entities.AuditDTO;
import br.com.hbsis.tonaarea.repositories.API;
import br.com.hbsis.tonaarea.repositories.CallbackReponse;
import br.com.hbsis.tonaarea.util.ActionListDetails;
import br.com.hbsis.tonaarea.util.Constants;
import br.com.hbsis.tonaarea.util.Mock;
import br.com.hbsis.tonaarea.util.SecurityPreferences;

public class AuditsDetailsActivity extends AppCompatActivity implements ActionListDetails, View.OnClickListener, CallbackReponse {

    private ViewHolder mViewHolder = new ViewHolder();
    private AuditsDetailsBusiness mAuditsDetailsBusiness;
    private List<AuditDTO> list = new ArrayList<>();
    private SecurityPreferences mSecurityPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audits_details);

        mSecurityPreferences = new SecurityPreferences(this);
        mAuditsDetailsBusiness = new AuditsDetailsBusiness(this, this);
        mAuditsDetailsBusiness.getAudits();

        this.mViewHolder.listAuditsDetails = findViewById(R.id.listAuditsDetails);
        this.mViewHolder.imageAll = findViewById(R.id.imageAll);
        this.mViewHolder.imageOnApproval = findViewById(R.id.imageOnApproval);
        this.mViewHolder.imageApproved = findViewById(R.id.imageApproved);
        this.mViewHolder.imageDisapproved = findViewById(R.id.imageDisapproved);
        this.mViewHolder.textAll = findViewById(R.id.textAll);
        this.mViewHolder.textOnApproval = findViewById(R.id.textOnApproval);
        this.mViewHolder.textApproved = findViewById(R.id.textApproved);
        this.mViewHolder.textDisapproved = findViewById(R.id.textDisapproved);

        listeners();
    }


    private void listeners() {
        this.mViewHolder.imageAll.setOnClickListener(this);
        this.mViewHolder.imageOnApproval.setOnClickListener(this);
        this.mViewHolder.imageApproved.setOnClickListener(this);
        this.mViewHolder.imageDisapproved.setOnClickListener(this);
    }


    @Override
    public void onClickItem(String id) {

        Bundle bundle = new Bundle();
        bundle.putString("id", id);

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        int textColor = Color.parseColor("#2870b2");

        switch (v.getId()) {
            case R.id.imageAll:
                mViewHolder.listAuditsDetails.setAdapter(new AuditsDetailsAdapter(list, this, this));
                disableImages();
                mViewHolder.imageAll.setImageResource(R.drawable.ic_small_all_blue);
                mViewHolder.textAll.setTextColor(textColor);
                break;
            case R.id.imageOnApproval:
                mViewHolder.listAuditsDetails.setAdapter(new AuditsDetailsAdapter(mAuditsDetailsBusiness.filter(Constants.STATUS_WORK_FLOW.ON_APPROVAL, list), this, this));
                disableImages();
                mViewHolder.imageOnApproval.setImageResource(R.drawable.ic_small_on_approval_blue);
                mViewHolder.textOnApproval.setTextColor(textColor);
                break;
            case R.id.imageApproved:
                mViewHolder.listAuditsDetails.setAdapter(new AuditsDetailsAdapter(mAuditsDetailsBusiness.filter(Constants.STATUS_WORK_FLOW.APPROVED, list), this, this));
                disableImages();
                mViewHolder.imageApproved.setImageResource(R.drawable.ic_small_approved_blue);
                mViewHolder.textApproved.setTextColor(textColor);
                break;
            case R.id.imageDisapproved:
                mViewHolder.listAuditsDetails.setAdapter(new AuditsDetailsAdapter(mAuditsDetailsBusiness.filter(Constants.STATUS_WORK_FLOW.DISAPPROVED, list), this, this));
                disableImages();
                mViewHolder.imageDisapproved.setImageResource(R.drawable.ic_small_disapproved_blue);
                mViewHolder.textDisapproved.setTextColor(textColor);
                break;
        }
    }

    private void disableImages() {

        int textColor = Color.parseColor("#95a3b7");

        mViewHolder.imageAll.setImageResource(R.drawable.ic_small_all);
        mViewHolder.imageOnApproval.setImageResource(R.drawable.ic_small_on_approval);
        mViewHolder.imageApproved.setImageResource(R.drawable.ic_small_approved);
        mViewHolder.imageDisapproved.setImageResource(R.drawable.ic_small_disapproved);

        mViewHolder.textAll.setTextColor(textColor);
        mViewHolder.textOnApproval.setTextColor(textColor);
        mViewHolder.textApproved.setTextColor(textColor);
        mViewHolder.textDisapproved.setTextColor(textColor);

    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        AuditDTO audit = new AuditDTO();
        try {
            audit.setId(jsonObject.getString("id"));
            audit.setNameRevenda(jsonObject.getString("nomeRevenda"));
            audit.setName(jsonObject.getString("nomeCliente"));
            audit.setProduct(jsonObject.getString("nomeProduto"));
            audit.setNameAuditor(jsonObject.getString("nomeAuditor"));
            audit.setStatus(jsonObject.getString("statusWorkflow"));
            audit.setInstant(jsonObject.getString("dataAuditoria"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        list.add(audit);
        //Collections.sort(list);
        mViewHolder.listAuditsDetails.setAdapter(new AuditsDetailsAdapter(list, this, this));
    }

    @Override
    public void onError(JSONObject jsonObject) {

    }

    private static class ViewHolder {
        private RecyclerView listAuditsDetails;
        private ImageView imageAll;
        private ImageView imageOnApproval;
        private ImageView imageApproved;
        private ImageView imageDisapproved;
        private TextView textAll;
        private TextView textOnApproval;
        private TextView textApproved;
        private TextView textDisapproved;
    }
}
