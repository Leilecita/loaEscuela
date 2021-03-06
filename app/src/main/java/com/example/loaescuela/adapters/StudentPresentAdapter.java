package com.example.loaescuela.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loaescuela.DateHelper;
import com.example.loaescuela.R;
import com.example.loaescuela.adapters.assistsResumByDay.AssistResumAdapter;
import com.example.loaescuela.adapters.assistsResumByDay.PlanillaResumAdapter;
import com.example.loaescuela.network.models.ClassCourse;
import com.example.loaescuela.network.models.ReportPresent;
import com.example.loaescuela.network.models.ReportResumAsist;
import com.example.loaescuela.network.models.ReportResumPlanilla;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentPresentAdapter extends BaseAdapter<ReportPresent, StudentPresentAdapter.ViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>  {
    private Context mContext;


    public StudentPresentAdapter(Context context, List<ReportPresent> planillas){
        setItems(planillas);
        mContext = context;
    }

    public StudentPresentAdapter(){

    }

    @Override
    public long getHeaderId(int position) {
        if (position >= getItemCount()) {
            return -1;
        } else {
            Date date = DateHelper.get().parseDate(DateHelper.get().onlyDateComplete(getItem(position).fecha_presente));
            return date.getYear();
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_header_assists, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position < getItemCount()) {

            LinearLayout linear = (LinearLayout) holder.itemView;
            final ReportPresent e = getItem(position);

            String date = DateHelper.get().getOnlyDate(e.fecha_presente);
            String year = DateHelper.get().getYear(date);

            int count = linear.getChildCount();
            View v = null;
            View v2 = null;

            for (int k = 0; k < count; k++) {
                v = linear.getChildAt(k);
                if (k == 0) {

                    TextView t = (TextView) v;
                    t.setText(year);
                }
            }
        }
    }

    public List<ReportPresent> getListClient(){
        return getList();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView fecha_presente;
        public TextView nombre_planilla;



        public ViewHolder(View v){
            super(v);
            fecha_presente = v.findViewById(R.id.fecha_presente);
            nombre_planilla = v.findViewById(R.id.planilla);

        }
    }

    @Override
    public StudentPresentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View

        View v = LayoutInflater.from(mContext).inflate(R.layout.item_report_present,parent,false);
        StudentPresentAdapter.ViewHolder vh = new StudentPresentAdapter.ViewHolder(v);
        return vh;
    }


    private void clearViewHolder(StudentPresentAdapter.ViewHolder vh){
        if(vh.fecha_presente!=null)
            vh.fecha_presente.setText(null);
        if(vh.nombre_planilla!=null)
            vh.nombre_planilla.setText(null);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final StudentPresentAdapter.ViewHolder holder, final int position) {
        clearViewHolder(holder);

        final ReportPresent current = getItem(position);

        holder.fecha_presente.setText(current.fecha_presente);
        holder.nombre_planilla.setText(current.planilla);

    }

}

