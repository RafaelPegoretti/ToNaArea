package br.com.hbsis.tonaarea.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.hbsis.tonaarea.entities.Audit;

public class Util {

    public static String convertPhotoBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] photo = baos.toByteArray();
        return Base64.encodeToString(photo, Base64.NO_WRAP);
    }

    public static Bitmap converteBase64Photo(String s) {
        byte[] photo = Base64.decode(s, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(photo, 0, photo.length);
    }

    public static Integer getPositionList(List<Audit> list, Audit audit){
        int position = -1;
        for (int i = 0; i<list.size();i++) {
            if (list.get(i).equals(audit)){
                position = i;
                break;
            }
        }
        return position;
    }

    public static String parseDateToString(Date date) {
        SimpleDateFormat sd = new SimpleDateFormat();
        return sd.format(date);
    }

    public static Date parseStringToDate(String s){
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            return new Date(sd.parse(s).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void logout(Context context){
        SecurityPreferences mSecurityPreferences = new SecurityPreferences(context);
        mSecurityPreferences.removeStoreString(Constants.SECURITY_PREFERENCES_CONSTANTS.USER_ID);
        mSecurityPreferences.removeStoreString(Constants.SECURITY_PREFERENCES_CONSTANTS.REV_ID);
        mSecurityPreferences.removeStoreString(Constants.SECURITY_PREFERENCES_CONSTANTS.USER_NAME);
        mSecurityPreferences.removeStoreString(Constants.SECURITY_PREFERENCES_CONSTANTS.DATE_ACCESS);
    }

    public static String formateDate(String date){
        String[] date2 = date.split(" ");
        if (date2.length > 1 ){
            String[] date3 = date2[0].split("/");
            String[] date4 = date2[1].split(":");
            date4[1] = (Integer.parseInt(date4[1])+1)+"";
            StringBuilder sb = new StringBuilder();
            sb.append(date3[2]);
            sb.append("-");
            sb.append(date3[1]);
            sb.append("-");
            sb.append(date3[0]);
            sb.append(" ");
            sb.append(date4[0]);
            sb.append(":");
            sb.append(date4[1]);
            sb.append(":");
            sb.append(date4[2]);
            return sb.toString();
        }else{
            return date;
        }
    }

}
