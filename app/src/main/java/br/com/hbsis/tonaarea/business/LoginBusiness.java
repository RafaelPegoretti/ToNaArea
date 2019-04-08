package br.com.hbsis.tonaarea.business;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import br.com.hbsis.tonaarea.entities.Login;
import br.com.hbsis.tonaarea.repositories.CallbackReponse;
import br.com.hbsis.tonaarea.repositories.Repository;
import br.com.hbsis.tonaarea.util.Constants;
import br.com.hbsis.tonaarea.util.SecurityPreferences;
import br.com.hbsis.tonaarea.util.Validator;

public class LoginBusiness {

    private Context context;
    private CallbackReponse callbackReponse;
    private Repository mRepository;
    private SecurityPreferences mSecurityPreferences;

    public LoginBusiness(Context context, CallbackReponse callbackReponse) {
        this.context = context;
        this.callbackReponse = callbackReponse;
        mRepository = new Repository(this.callbackReponse);
        mSecurityPreferences = new SecurityPreferences(context);
    }


    public void login(String cpf, String password) {
        mRepository.postLogin(new Login(cpf, password), context);
    }

    public Validator loginValidation(JSONObject jsonObject) {
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            JSONObject user = data.getJSONObject("user");
            JSONArray claims = user.getJSONArray("claims");
            for (int i = 0; i < claims.length(); i++) {
                JSONObject json = claims.getJSONObject(i);
                if (json.getString("value").toLowerCase().contains("auditor") ||
                        json.getString("value").toLowerCase().contains("revenda")) {
                    handleSave(user);
                    return new Validator(true, null);
                } else {
                    return new Validator(false, "Usuário não possui perfil para acessar o aplicativo");
                }
            }

        } catch (JSONException e) {
            return new Validator(false, "Entre em contato com seu TI para efetuar o cadastro do usuário");
        }
        return new Validator(false, "não foi possivel realizar o login");
    }

    private void handleSave(JSONObject jsonObject) throws JSONException {
        mSecurityPreferences.storeString(Constants.SECURITY_PREFERENCES_CONSTANTS.USER_ID, jsonObject.getString("userid"));
        mSecurityPreferences.storeString(Constants.SECURITY_PREFERENCES_CONSTANTS.REV_ID, jsonObject.getString("revId"));
        mSecurityPreferences.storeString(Constants.SECURITY_PREFERENCES_CONSTANTS.USER_NAME, jsonObject.getString("nome"));

        Long dateAccess = System.currentTimeMillis();
        mSecurityPreferences.storeString(Constants.SECURITY_PREFERENCES_CONSTANTS.DATE_ACCESS, dateAccess.toString());
    }

    public boolean access(){
        String timeString = mSecurityPreferences.getStoredString(Constants.SECURITY_PREFERENCES_CONSTANTS.DATE_ACCESS);
        if (timeString.equals("")){
            return false;
        }
        Long time = Long.parseLong(timeString);
        return System.currentTimeMillis() > time && System.currentTimeMillis() < time+43200000;
    }

}
