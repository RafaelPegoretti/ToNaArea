package br.com.hbsis.tonaarea.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.hbsis.tonaarea.R;

public class ViewHolderDetails extends RecyclerView.ViewHolder {

    public final ImageView imageDetails;
    public final TextView textNameDetails;
    public final TextView textProductDetails;
    public final TextView textDate;
    public final Context context;

    public ViewHolderDetails(@NonNull View itemView) {
        super(itemView);
        imageDetails = itemView.findViewById(R.id.imageDetails);
        textNameDetails = itemView.findViewById(R.id.textNameDetails);
        textProductDetails = itemView.findViewById(R.id.textProductDetails);
        textDate = itemView.findViewById(R.id.textDate);
        this.context = itemView.getContext();


    }
}
