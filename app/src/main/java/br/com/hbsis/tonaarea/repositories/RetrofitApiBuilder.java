package br.com.hbsis.tonaarea.repositories;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import br.com.hbsis.tonaarea.BuildConfig;
import br.com.hbsis.tonaarea.util.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiBuilder implements API.Builder{

    @Override
    public API build() {
        OkHttpClient client = createOkHttpClient();
        return new Retrofit.Builder()
                .baseUrl(Constants.URL.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build()
                .create(API.class);
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(100, TimeUnit.SECONDS);
        builder.writeTimeout(100, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logInterceptor);
        }
        OkHttpClient client = builder.build();
        return client;
    }
}
