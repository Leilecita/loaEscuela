package com.example.loaescuela.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loaescuela.DateHelper;
import com.example.loaescuela.R;
import com.example.loaescuela.ValuesHelper;
import com.example.loaescuela.network.models.ReportIncomeStudent;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.Date;
import java.util.List;

public class IncomeStudentAdapter extends BaseAdapter<ReportIncomeStudent, IncomeStudentAdapter.ViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
    private Context mContext;


    public IncomeStudentAdapter(Context context, List<ReportIncomeStudent> events){
        setItems(events);
        mContext = context;
    }

    public IncomeStudentAdapter(){

    }

    @Override
    public long getHeaderId(int position) {
        if (position >= getItemCount()) {
            return -1;
        } else {
            Date date = DateHelper.get().parseDate(DateHelper.get().onlyDateComplete(getItem(position).income_created));
            return date.getTime();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_header_income_event_day, parent, false);

        return new RecyclerView.ViewHolder(view) {

        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < getItemCount()) {

            LinearLayout linear = (LinearLayout) holder.itemView;

            final ReportIncomeStudent e = getItem(position);

            String dateToShow2 = DateHelper.get().getDayEvent(e.income_created);

            String month =DateHelper.get().getNameMonth(DateHelper.get().getOnlyDate(e.income_created));

            int count = linear.getChildCount();
            View v = null;
            View v2 = null;
            View v3 = null;

            for(int k=0; k<count; k++) {
                v3 = linear.getChildAt(k);
                if(k==1){
                    LinearLayout linear4= (LinearLayout) v3;
                    int count3 = linear4.getChildCount();

                    for(int i=0; i<count3; i++) {
                        v = linear4.getChildAt(i);
                        if(i==0){
                            LinearLayout linear2= (LinearLayout) v;

                            int count2 = linear2.getChildCount();

                            for(int j=0; j<count2; j++) {

                                v2 = linear2.getChildAt(j);

                                if(j==0){
                                    TextView t2= (TextView) v2;
                                    t2.setText(month.substring(0,3));
                                }else if(j == 1){


                                    TextView t= (TextView) v2;
                                    t.setText(dateToShow2);
                                }
                            }
                        }
                    }

                }
            }
        }
    }



    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView hour;

        public TextView student_name;
        public TextView observation;
        public TextView amount;
        public TextView type;

        public LinearLayout lineDetail;
        public TextView circlePendient;
        public LinearLayout moreInfo;

        public ImageView image;

        public LinearLayout linePayment;
        public TextView textPayment;
        public TextView valPayment;


        public RecyclerView list_events;

        public ViewHolder(View v){
            super(v);

            hour= v.findViewById(R.id.hour);
            student_name= v.findViewById(R.id.student_name);
            amount= v.findViewById(R.id.amount);
            type= v.findViewById(R.id.type);
            lineDetail= v.findViewById(R.id.line_detail);
            circlePendient= v.findViewById(R.id.circle_pendient);
            image= v.findViewById(R.id.image);

            linePayment= v.findViewById(R.id.line_payment);
            textPayment= v.findViewById(R.id.textpayment);
            valPayment= v.findViewById(R.id.valpayment);


            list_events = v.findViewById(R.id.list_events);
        }
    }

    @Override
    public IncomeStudentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_income_student_day,parent,false);
        IncomeStudentAdapter.ViewHolder vh = new IncomeStudentAdapter.ViewHolder(v);

        return vh;
    }

    private void clearViewHolder(IncomeStudentAdapter.ViewHolder vh){
        if(vh.amount != null)
            vh.amount.setText(null);
        if(vh.student_name!=null)
            vh.student_name.setText(null);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final IncomeStudentAdapter.ViewHolder holder, final int position) {
        clearViewHolder(holder);

        final ReportIncomeStudent current_general = getItem(position);

        holder.amount.setText(ValuesHelper.get().getIntegerQuantity(current_general.amount));

        holder.student_name.setText(current_general.description);

        if(current_general.income_created == null){
            holder.hour.setText("");
        }else{
            holder.hour.setText(DateHelper.get().getOnlyTimeFromCreated(current_general.income_created));
        }

        holder.lineDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent=new Intent(mContext, ReportByStudentActivity.class);
                intent.putExtra("NAME",current_general.name);
                intent.putExtra("SURNAME",current_general.surname);
                intent.putExtra("ID",current_general.student_id);

                mContext.startActivity(intent);*/
            }
        });


    }


}
