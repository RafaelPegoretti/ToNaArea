package br.com.hbsis.tonaarea.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Calendar;
import java.util.List;
import br.com.hbsis.tonaarea.R;
import br.com.hbsis.tonaarea.entities.AuditDTO;
import br.com.hbsis.tonaarea.util.ActionListDetails;
import br.com.hbsis.tonaarea.util.Constants;
import br.com.hbsis.tonaarea.util.Util;
import br.com.hbsis.tonaarea.util.ViewHolderDetails;

public class AuditsDetailsAdapter extends RecyclerView.Adapter {

    private List<AuditDTO> list;
    private Context context;
    private ActionListDetails actionListDetails;

    public AuditsDetailsAdapter(List<AuditDTO> list, Context context, ActionListDetails actionListDetails) {
        this.list = list;
        this.context = context;
        this.actionListDetails = actionListDetails;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_list_details, viewGroup, false);

        return new ViewHolderDetails(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        ViewHolderDetails holder = (ViewHolderDetails) viewHolder;

        final AuditDTO audit = list.get(i);
        holder.textNameDetails.setText(audit.getName());
        holder.textProductDetails.setText(audit.getProduct());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Util.parseStringToDate(audit.getInstant()));
        int month = calendar.get(Calendar.MONTH)+1;
        String s = calendar.get(Calendar.DAY_OF_MONTH)+"/"+month;
        holder.textDate.setText(s);

        if (audit.getStatus().equals(Constants.STATUS_WORK_FLOW.ON_APPROVAL)){
            holder.imageDetails.setImageResource(R.drawable.ic_list_on_approval);
        }else if (audit.getStatus().equals(Constants.STATUS_WORK_FLOW.APPROVED)) {
            holder.imageDetails.setImageResource(R.drawable.ic_list_approved);
        }else if (audit.getStatus().equals(Constants.STATUS_WORK_FLOW.DISAPPROVED)){
            holder.imageDetails.setImageResource(R.drawable.ic_list_disapproved);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListDetails.onClickItem(audit.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
