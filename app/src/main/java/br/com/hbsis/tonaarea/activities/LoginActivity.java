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

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import org.json.JSONObject;
import br.com.hbsis.tonaarea.business.LoginBusiness;
import br.com.hbsis.tonaarea.R;
import br.com.hbsis.tonaarea.dao.ClientRepository;
import br.com.hbsis.tonaarea.dao.ProductRepository;
import br.com.hbsis.tonaarea.db.DataOpenHelper;
import br.com.hbsis.tonaarea.repositories.CallbackReponse;
import br.com.hbsis.tonaarea.util.Validator;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CallbackReponse {

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

        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        mViewHolder.editUser.addTextChangedListener(new MaskTextWatcher(mViewHolder.editUser, smf));

        getRepositories();
        listeners();

        if (mLoginBusiness.access()){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }

    public void listeners() {
        mViewHolder.buttonLogin.setOnClickListener(this);
        mViewHolder.textForgotPassword.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                mLoginBusiness.login(mViewHolder.editUser.getText().toString().replace(".", "").replace("-", ""), mViewHolder.editPassword.getText().toString());
                break;

            case R.id.textForgotPassword:
                Toast.makeText(this, "Entre em contato com seu TI para efetuar o reset da senha", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        Validator validator = mLoginBusiness.loginValidation(jsonObject);
        if (validator.isValid()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else {
            Toast.makeText(this, validator.getMessage(), Toast.LENGTH_LONG).show();
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
    }




}


