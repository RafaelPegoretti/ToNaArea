package br.com.hbsis.tonaarea.repositories;

import br.com.hbsis.tonaarea.entities.Audit;
import br.com.hbsis.tonaarea.entities.Client;
import br.com.hbsis.tonaarea.entities.Login;
import br.com.hbsis.tonaarea.repositories.api.QuantityApi;
import br.com.hbsis.tonaarea.util.Constants;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {

    @POST(Constants.URL.URL_AUDITORIAS_POST)
    Call<ResponseBody> sendImage(@Body Audit audit);

    @POST(Constants.URL.URL_LOGIN)
    Call<ResponseBody> login(@Body Login login);

    @POST(Constants.URL.URL_CLIENTES_POST)
    Call<ResponseBody> sendClient(@Body Client client);

    @GET("/api/v1/auditorias/quantidade")
    Call<QuantityApi[]> getQuantity(@Query("usuarioId") String userId);

    interface Builder {
        API build(boolean unsafe);
    }
}
