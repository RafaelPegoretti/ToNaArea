package br.com.hbsis.tonaarea.repositories;

import com.google.gson.GsonBuilder;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import br.com.hbsis.tonaarea.BuildConfig;
import br.com.hbsis.tonaarea.util.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiBuilder implements API.Builder{

    @Override
    public API build(boolean unsafe) {
        OkHttpClient client;
        if(unsafe){
            client = createOkHttpClientUnsafe();
        }else{
            client = createOkHttpClient();
        }


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

    private OkHttpClient createOkHttpClientUnsafe() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(100, TimeUnit.SECONDS);
            builder.writeTimeout(100, TimeUnit.SECONDS);
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
                logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(logInterceptor);
            }
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });



            return builder.build();
        }catch (Exception e){
            return createOkHttpClient();
        }
    }

}
