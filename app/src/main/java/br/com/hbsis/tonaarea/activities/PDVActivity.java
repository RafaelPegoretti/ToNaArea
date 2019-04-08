package br.com.hbsis.tonaarea.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.hbsis.tonaarea.R;
import br.com.hbsis.tonaarea.business.NewAuditBusiness;
import br.com.hbsis.tonaarea.dao.Sync;
import br.com.hbsis.tonaarea.entities.Client;
import br.com.hbsis.tonaarea.repositories.CallbackReponse;
import br.com.hbsis.tonaarea.util.Constants;
import br.com.hbsis.tonaarea.util.SecurityPreferences;
import br.com.hbsis.tonaarea.util.Util;

public class PDVActivity extends AppCompatActivity implements View.OnClickListener, CallbackReponse {

    ViewHolder mViewHolder = new ViewHolder();
    private NewAuditBusiness mNewAuditBusiness;
    private SecurityPreferences mSecurityPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdv);

        mNewAuditBusiness = new NewAuditBusiness(this, this);
        mSecurityPreferences = new SecurityPreferences(this);

        this.mViewHolder.buttonAddPDV = findViewById(R.id.buttonAddPDV);
        this.mViewHolder.buttonCancel = findViewById(R.id.buttonCancel);
        this.mViewHolder.editNewPDV = findViewById(R.id.editNewPDV);
        this.mViewHolder.editCodePDV = findViewById(R.id.editCodePDV);
        this.mViewHolder.textLogout = findViewById(R.id.textLogout);


        this.mViewHolder.buttonAddPDV.setOnClickListener(this);
        this.mViewHolder.buttonCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAddPDV:
                Client client = new Client(mViewHolder.editNewPDV.getText().toString(), mViewHolder.editCodePDV.getText().toString(), true, mSecurityPreferences.getStoredString(Constants.SECURITY_PREFERENCES_CONSTANTS.REV_ID));
                mNewAuditBusiness.postNewPDV(client);
                startService(new Intent(this, Sync.class));
                mViewHolder.editCodePDV.setText("");
                mViewHolder.editNewPDV.setText("");
                break;
            case R.id.textLogout:
                Util.logout(this);
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.buttonCancel:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        try {
            if (!jsonObject.getBoolean("success")) {
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

    private class ViewHolder{
        public TextView textLogout;
        private Button buttonAddPDV;
        private Button buttonCancel;
        private EditText editNewPDV;
        private EditText editCodePDV;
    }

}
