package com.jrmitchell.drivefinance.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jrmitchell.drivefinance.R;

import java.util.List;

public class PaymentLineAdapter extends RecyclerView.Adapter<PaymentLineAdapter.ViewHolder> {

    final List<String> paymentLines;
    final Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(String string) {
            ((TextView) itemView.findViewById(R.id.reportTitle)).setText(string);
        }
    }

    public PaymentLineAdapter(Context context, List<String> paymentLines) {
        this.context = context;
        this.paymentLines = paymentLines;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(context)
                .inflate(R.layout.report_viewholder_item,parent,false);
        return new ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(paymentLines.get(position));
    }

    @Override
    public int getItemCount() {
        return paymentLines.size();
    }
}
