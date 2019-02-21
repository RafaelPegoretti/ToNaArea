package br.com.hbsis.tonaarea.repositories;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import br.com.hbsis.tonaarea.entities.APIObject;
import br.com.hbsis.tonaarea.util.Constants;

public class APIRepository extends AsyncTask<APIObject, Void, String>{

    private Context context;

    public APIRepository(Context context) {
        this.context = context;
    }



    private static String converterInputStreamToString(InputStream is){
        StringBuffer buffer = new StringBuffer();
        try{
            BufferedReader br;
            String line;

            br = new BufferedReader(new InputStreamReader(is));
            while((line = br.readLine())!=null){
                buffer.append(line);
            }

            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        return buffer.toString();


    }

    @Override
    protected String doInBackground(APIObject... apiObjects) {
        String s = "";

        try {
            int responseCode;
            HttpURLConnection connection;
            InputStream is;
            URL apiEnd;

            if (apiObjects[0].getMethod().equals(Constants.OPERATION_METHOD.GET)){
                apiEnd = new URL(apiObjects[0].getURL());
            }else{
                apiEnd = new URL(apiObjects[0].getURL());
            }


            connection = (HttpURLConnection) apiEnd.openConnection();
            connection.setRequestMethod(apiObjects[0].getMethod());
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setUseCaches(false);
            connection.connect();

            if (!apiObjects[0].getMethod().equals(Constants.OPERATION_METHOD.GET)){
                OutputStreamWriter request = new OutputStreamWriter(connection.getOutputStream());
                request.write(apiObjects[0].getJson().toString());
                request.flush();
                request.close();
                String line;
                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                return sb.toString();

            }


            responseCode = connection.getResponseCode();
            if(responseCode < HttpURLConnection.HTTP_BAD_REQUEST){
                is = connection.getInputStream();
            }else{
                is = connection.getErrorStream();
            }

            s = converterInputStreamToString(is);
            is.close();
            connection.disconnect();


        }catch (MalformedURLException e) {
            e.printStackTrace();
            return e.toString();
        } catch (ProtocolException e) {
            e.printStackTrace();
            return e.toString();
        } catch (ConnectException e){
            return "Sem conecção com a internet";
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }

        return s;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);



    }
}
