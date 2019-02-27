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

    @POST(Constants.URL.URL_AUDITORIAS)
    Call<ResponseBody> sendImage(@Body Audit audit);

    @POST(Constants.URL.URL_LOGIN)
    Call<ResponseBody> login(@Body Login login);

    @POST(Constants.URL.URL_CLIENTES)
    Call<ResponseBody> sendClient(@Body Client client);

    @GET(Constants.URL.URL_AUDITORIAS_QUANTIDADE)
    Call<ResponseBody> getQuantity(@Query("usuarioId") String userId);

    @GET(Constants.URL.URL_AUDITORIAS)
    Call<ResponseBody> getAudits(@Query("usuarioId") String userId, @Query("statusWorkflow") int status, @Query("ultimos30dias") boolean last30days);

    @GET(Constants.URL.URL_AUDITORIAS+"/{id}")
    Call<ResponseBody> getAudit(@Path("id") String id);

    @GET (Constants.URL.URL_CLIENTES)
    Call<ResponseBody> getClients(@Query("usuarioId") String UserId, @Query("revendaId") String revId, @Query("dataInicial") String startDate, @Query("dataFinal") String endDate);

    @GET (Constants.URL.URL_PRODUTOS)
    Call<ResponseBody> getProducts(@Query("dataInicial") String startDate, @Query("dataFinal") String endDate);

    @GET (Constants.URL.URL_CLIENTES_ULTIMA_ATUALIZACAO)
    Call<ResponseBody> getLastUpdateClient(@Query("revendaId") String revId);

    @GET (Constants.URL.URL_PRODUTOS_ULTIMA_ATUALIZACAO)
    Call<ResponseBody> getLastUpdateProduct();

    interface Builder {
        API build(boolean unsafe);
    }
}
