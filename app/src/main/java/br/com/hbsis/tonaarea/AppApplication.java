package br.com.hbsis.tonaarea;

import android.app.Application;

import br.com.hbsis.tonaarea.repositories.API;
import br.com.hbsis.tonaarea.repositories.RetrofitApiBuilder;

public class AppApplication extends Application {

    private API api;
    private static AppApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

    }

    public static AppApplication getInstance() {
        return INSTANCE;
    }

    public API getApi() {
        if(api == null){
            api = new RetrofitApiBuilder().build();
        }
        return api;
    }
}
