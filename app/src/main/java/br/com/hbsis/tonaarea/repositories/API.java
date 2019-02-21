package br.com.hbsis.tonaarea.repositories;

import br.com.hbsis.tonaarea.entities.Audit;
import br.com.hbsis.tonaarea.entities.Client;
import br.com.hbsis.tonaarea.entities.Login;
import br.com.hbsis.tonaarea.util.Constants;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface API {

    @POST(Constants.URL.URL_AUDITORIAS_POST)
    Call<ResponseBody> sendImage(@Body Audit audit);

    @POST(Constants.URL.URL_LOGIN)
    Call<ResponseBody> login(@Body Login login);

    @POST(Constants.URL.URL_CLIENTES_POST)
    Call<ResponseBody> sendClient(@Body Client client);


    interface Builder {
        API build();
    }
}
