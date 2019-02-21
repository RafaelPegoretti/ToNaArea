package br.com.hbsis.tonaarea.business;

import android.content.Context;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import br.com.hbsis.tonaarea.entities.Login;
import br.com.hbsis.tonaarea.repositories.CallbackReponse;
import br.com.hbsis.tonaarea.repositories.Repository;
import br.com.hbsis.tonaarea.util.Constants;
import br.com.hbsis.tonaarea.util.SecurityPreferences;

public class LoginBusiness {

    private Context context;
    private CallbackReponse callbackReponse;
    private Repository mRepository;
    private SecurityPreferences mSecurityPreferences;

    public LoginBusiness(Context context, CallbackReponse callbackReponse){
        this.context = context;
        this.callbackReponse = callbackReponse;
        mRepository = new Repository(this.callbackReponse);
        mSecurityPreferences = new SecurityPreferences(context);
    }


    public void login(String cpf, String password){
        mRepository.postLogin(new Login(cpf, password), context);
    }

    public boolean loginValidation(JSONObject jsonObject){
        boolean valid = false;
        StringBuilder message = new StringBuilder();

        try {
            if (jsonObject.getBoolean("success")){
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject user = data.getJSONObject("user");
                JSONArray claims = user.getJSONArray("claims");
                for (int i = 0; i < claims.length();i++){
                    JSONObject json = claims.getJSONObject(i);
                    if (json.getString("value").toLowerCase().contains("auditor") ||
                            json.getString("value").toLowerCase().contains("revenda")){
                      handleSave(user);
                        valid = true;
                      break;
                    }else {
                        message.append("Seu usuário não possui perfil para efetuar auditoria");
                    }
                }
            }else{
                JSONArray errors = jsonObject.getJSONArray("errors");
                for (int i = 0; i < errors.length(); i++){
                    message.append(errors.getString(i));
                    message.append("\n");
                }
                Toast.makeText(context, message.toString(), Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            Toast.makeText(context, "Entre em contato com seu TI para efetuar o cadastro do usuário", Toast.LENGTH_LONG).show();
        }
        return valid;
    }

    private void handleSave(JSONObject jsonObject) throws JSONException {
        mSecurityPreferences.storeString(Constants.SECURITY_PREFERENCES_CONSTANTS.USER_ID, jsonObject.getString("userid"));
        mSecurityPreferences.storeString(Constants.SECURITY_PREFERENCES_CONSTANTS.REV_ID, jsonObject.getString("revId"));
        mSecurityPreferences.storeString(Constants.SECURITY_PREFERENCES_CONSTANTS.USER_NAME, jsonObject.getString("nome"));
    }

}
