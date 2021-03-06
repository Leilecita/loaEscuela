package com.example.loaescuela.adapters.assistsResumByDay;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loaescuela.R;
import com.example.loaescuela.adapters.BaseAdapter;
import com.example.loaescuela.network.models.ReportResumPlanilla;

import java.util.List;

public class PlanillaResumAdapter  extends BaseAdapter<ReportResumPlanilla,PlanillaResumAdapter.ViewHolder> {
    private Context mContext;

    public PlanillaResumAdapter(Context context, List<ReportResumPlanilla> planillas){
        setItems(planillas);
        mContext = context;
    }

    public PlanillaResumAdapter(){

    }

    public List<ReportResumPlanilla> getListClient(){
        return getList();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView nombre_planilla;
        public TextView tot_presents;


        public ViewHolder(View v){
            super(v);
            nombre_planilla = v.findViewById(R.id.nombre_planilla);
            tot_presents = v.findViewById(R.id.tot_presents);


        }
    }

    @Override
    public PlanillaResumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View

        View v = LayoutInflater.from(mContext).inflate(R.layout.item_resum_planilla,parent,false);
        PlanillaResumAdapter.ViewHolder vh = new PlanillaResumAdapter.ViewHolder(v);
        return vh;
    }


    private void clearViewHolder(PlanillaResumAdapter.ViewHolder vh){
        if(vh.nombre_planilla!=null)
            vh.nombre_planilla.setText(null);
        if(vh.tot_presents!=null)
            vh.tot_presents.setText(null);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final PlanillaResumAdapter.ViewHolder holder, final int position) {
        clearViewHolder(holder);

        final ReportResumPlanilla current = getItem(position);

        holder.nombre_planilla.setText(current.nombre_planilla);
        holder.tot_presents.setText(String.valueOf(current.cant_presentes));


    }

}

