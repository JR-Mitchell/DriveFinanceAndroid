package com.jrmitchell.drivefinance.views.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jrmitchell.drivefinance.R;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    final List<Pair<String,String>> reportEntries;
    final Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Pair<String, String> entry) {
            ((TextView) itemView.findViewById(R.id.reportTitle)).setText(entry.first);
        }
    }

    public ReportAdapter(Context context, List<Pair<String,String>> reportEntries) {
        this.context = context;
        this.reportEntries = reportEntries;
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
        holder.setData(reportEntries.get(position));
    }

    @Override
    public int getItemCount() {
        return reportEntries.size();
    }
}
