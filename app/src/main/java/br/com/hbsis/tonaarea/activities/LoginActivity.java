package br.com.hbsis.tonaarea.activities;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import br.com.hbsis.tonaarea.business.LoginBusiness;
import br.com.hbsis.tonaarea.R;
import br.com.hbsis.tonaarea.dao.ClientRepository;
import br.com.hbsis.tonaarea.dao.ProductRepository;
import br.com.hbsis.tonaarea.db.DataOpenHelper;
import br.com.hbsis.tonaarea.repositories.CallbackReponse;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, CallbackReponse {

    private LoginBusiness mLoginBusiness;
    private ViewHolder mViewHolder = new ViewHolder();
    private ClientRepository clientRepository;
    private ProductRepository productRepository;
    private SQLiteDatabase connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginBusiness = new LoginBusiness(this, this);
        this.mViewHolder.editUser = findViewById(R.id.editUser);
        this.mViewHolder.editPassword = findViewById(R.id.editPassword);
        this.mViewHolder.buttonLogin = findViewById(R.id.buttonLogin);
        this.mViewHolder.textForgotPassword = findViewById(R.id.textForgotPassword);
        this.mViewHolder.layoutBackgroundLogin = findViewById(R.id.layoutBackgroundLogin);
        this.mViewHolder.layoutFormLogin = findViewById(R.id.layoutFormLogin);

        getRepositories();
        listeners();
    }

    public void listeners() {
        mViewHolder.buttonLogin.setOnClickListener(this);
        mViewHolder.textForgotPassword.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                mLoginBusiness.login(mViewHolder.editUser.getText().toString(), mViewHolder.editPassword.getText().toString());
                break;

            case R.id.textForgotPassword:
                Toast.makeText(this, "Entre em contato com seu TI para efetuar o reset da senha", Toast.LENGTH_LONG).show();
                break;
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (MotionEvent.ACTION_UP == event.getAction()) {
            ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) mViewHolder.layoutBackgroundLogin.getLayoutParams();
            newLayoutParams.bottomMargin = 1200;
            mViewHolder.layoutBackgroundLogin.setLayoutParams(newLayoutParams);

            newLayoutParams = (ConstraintLayout.LayoutParams) mViewHolder.layoutFormLogin.getLayoutParams();
            newLayoutParams.bottomMargin = 700;
            mViewHolder.layoutFormLogin.setLayoutParams(newLayoutParams);
        }

        return false;
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        if (mLoginBusiness.loginValidation(jsonObject)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onError(JSONObject jsonObject) {
        Toast.makeText(this, "Erro ao conectar com o servidor", Toast.LENGTH_LONG).show();
    }

    private void getRepositories() {
        try {
            DataOpenHelper dataOpenHelper = new DataOpenHelper(this);
            connection = dataOpenHelper.getWritableDatabase();
            productRepository = new ProductRepository(connection);
            clientRepository = new ClientRepository(connection);
        } catch (SQLException e) {
        }
    }

    private static class ViewHolder {
        private EditText editUser;
        private EditText editPassword;
        private Button buttonLogin;
        private TextView textForgotPassword;
        private ConstraintLayout layoutBackgroundLogin;
        private ConstraintLayout layoutFormLogin;
    }




}


