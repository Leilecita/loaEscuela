package com.example.loaescuela.adapters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loaescuela.DateHelper;
import com.example.loaescuela.R;
import com.example.loaescuela.ValuesHelper;
import com.example.loaescuela.network.ApiClient;
import com.example.loaescuela.network.Error;
import com.example.loaescuela.network.GenericCallback;
import com.example.loaescuela.network.models.ReportOutcome;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OutcomeAdapter extends BaseAdapter<ReportOutcome, OutcomeAdapter.ViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private String groupBy;


    public OutcomeAdapter(Context context, List<ReportOutcome> incomes) {
        setItems(incomes);
        mContext = context;
        groupBy = "day";
    }

    public void setGroupBy(String val){
        this.groupBy = val;
    }

    public OutcomeAdapter() {

    }

    @Override
    public long getHeaderId(int position) {
        if (position >= getItemCount()) {
            return RecyclerView.NO_POSITION;
        } else {
            if(groupBy.equals("day")){
                Date date = DateHelper.get().parseDate(DateHelper.get().onlyDateComplete(getItem(position).created));
                return date.getTime();
            }else{
                Date date = DateHelper.get().parseDate(DateHelper.get().onlyDayMonthComplete(getItem(position).created));
                return date.getTime();
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewheader_extr, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < getItemCount()) {

            LinearLayout linear = (LinearLayout) holder.itemView;
            final ReportOutcome e = getItem(position);

            String dateToShow2 = DateHelper.get().getNameDay(e.created);
            String numberDay = DateHelper.get().numberDay(e.created);
            String month = DateHelper.get().getNameMonth2(DateHelper.get().onlyDate(e.created));
            String year = DateHelper.get().getYear(DateHelper.get().onlyDate(e.created));

            int count = linear.getChildCount();
            View v = null;
            View v2 = null;
            View v3 = null;

            for (int k = 0; k < count; k++) {
                v3 = linear.getChildAt(k);
                if (k == 0) {
                    LinearLayout linear4 = (LinearLayout) v3;

                    int count3 = linear4.getChildCount();

                    for (int i = 0; i < count3; i++) {
                        v = linear4.getChildAt(i);
                        if (i == 0) {

                            TextView t = (TextView) v;
                            t.setText(dateToShow2);
                            if(groupBy.equals("day")){
                                t.setVisibility(View.VISIBLE);
                            }else{
                                t.setVisibility(View.GONE);
                            }


                        } else if (i == 1) {

                            TextView t2 = (TextView) v;
                            t2.setText(numberDay);
                            if(groupBy.equals("day")){
                                t2.setVisibility(View.VISIBLE);
                            }else{
                                t2.setVisibility(View.GONE);
                            }
                        }else if (i == 2) {
                            TextView t2 = (TextView) v;
                            t2.setText(month);
                        }else if (i == 3) {

                            TextView t2 = (TextView) v;
                            t2.setText(year);

                        }
                    }
                }
            }
        }
    }


    private void getAmountByDay(){

    }

    public List<ReportOutcome> getListEmployees() {
        return getList();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView detail;
        public TextView coin_name;
        public TextView value;
        public TextView type;
        public TextView category;


        public LinearLayout line_info_user;
        public TextView user_name;
        public TextView hour;


        public ViewHolder(View v) {
            super(v);

            detail = v.findViewById(R.id.detail);
            value = v.findViewById(R.id.value);
            type = v.findViewById(R.id.type);

//            line_info_user=v.findViewById(R.id.line_info_user);
           // user_name=v.findViewById(R.id.user_name);
         //   hour=v.findViewById(R.id.hour);

            category = v.findViewById(R.id.category);
        }
    }

    @Override
    public OutcomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_outcome, parent, false);
        OutcomeAdapter.ViewHolder vh = new OutcomeAdapter.ViewHolder(v);
        return vh;
    }

    private void clearViewHolder(OutcomeAdapter.ViewHolder vh) {
        if (vh.detail != null)
            vh.detail.setText(null);
        if (vh.type != null)
            vh.type.setText(null);
        if (vh.category != null)
            vh.category.setText(null);
        if (vh.value != null)
            vh.value.setText(null);
        if (vh.coin_name != null)
            vh.coin_name.setText(null);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final OutcomeAdapter.ViewHolder holder, final int position) {
        clearViewHolder(holder);

        final ReportOutcome outcome = getItem(position);

        holder.detail.setText(outcome.observation);
        holder.category.setText(outcome.category);
        holder.type.setText(outcome.type);

        holder.value.setText(  ValuesHelper.get().getIntegerQuantityRounded(outcome.amount));
        // holder.type.setText(firstLettrUpCase(outcome.type));

       // holder.user_name.setText(outcome.user_name);
//        holder.hour.setText(DateHelper.get().onlyHourMinut(DateHelper.get().getOnlyTime(outcome.created)));

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.line_info_user.getVisibility() == View.VISIBLE){
                    holder.line_info_user.setVisibility(View.GONE);
                }else{
                    holder.line_info_user.setVisibility(View.VISIBLE);
                }
            }
        });*/

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Context wrapper = new ContextThemeWrapper(mContext, R.style.PopUpTheme);
                PopupMenu popup = new PopupMenu(wrapper, holder.itemView);

                popup.getMenuInflater().inflate(R.menu.menu_options, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_delete:
                                deleteOutcome(outcome, position);
                                return true;
                            case R.id.menu_edit:
                                //edithOutcome(outcome, position);
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popup.show();
                return false;
            }
        });

    }

    public String firstLettrUpCase(String text){
        if (text.equals("")) {
            return text;
        }else{
            return text.substring(0,1).toUpperCase() + text.substring(1).toLowerCase();
        }
    }

    private void deleteOutcome(final ReportOutcome o, final int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.cuad_delete_outcome, null);
        builder.setView(dialogView);


        final TextView type = dialogView.findViewById(R.id.type);
        final TextView descr = dialogView.findViewById(R.id.description);
        final TextView value = dialogView.findViewById(R.id.value);
        final Button ok = dialogView.findViewById(R.id.ok);
        final TextView cancel = dialogView.findViewById(R.id.cancel);

        type.setText(o.type);
        descr.setText(o.observation);
        value.setText(String.valueOf(o.amount));

        final AlertDialog dialog = builder.create();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ApiClient.get().delteOutcome(o.id, new GenericCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        removeItem(position);
                        Toast.makeText(mContext, "Se ha eliminado el gasto", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Error error) {

                    }
                });

                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
/*
    private void edithOutcome(final ReportOutcome o,final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.edith_outcome, null);
        builder.setView(dialogView);

        final TextView date= dialogView.findViewById(R.id.date);
        final EditText type= dialogView.findViewById(R.id.type);
        final EditText descr= dialogView.findViewById(R.id.description);
        final EditText value= dialogView.findViewById(R.id.value);
        final Spinner spinnerCoin= dialogView.findViewById(R.id.coin);

        final TextView cancel= dialogView.findViewById(R.id.cancel);
        final Button ok= dialogView.findViewById(R.id.ok);

        type.setText(o.type);
        descr.setText(o.description);
        value.setText(String.valueOf(o.value));
        date.setText(o.created);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate(date);
            }
        });

        adapterCoin = new SpinnerAdapter(mContext, R.layout.item_custom_spend, FlagHelper.get().createArrayCoins(mCoins), FlagHelper.get().createFlags(mCoins), "dialog");
        spinnerCoin.setAdapter(adapterCoin);

        spinnerCoin.setSelection(FlagHelper.get().getPositionAdapter(o.coin_name,mCoins));

        final AlertDialog dialog = builder.create();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String outType=type.getText().toString().trim();
                String outDescr=descr.getText().toString().trim();
                String outValue=value.getText().toString().trim();
                String selectedDate = date.getText().toString().trim();

                Double outValuDouble = Double.valueOf(outValue);

                final String itemSelected = spinnerCoin.getSelectedItem().toString().trim();

                Outcome outcome = new Outcome(outType,outDescr,Double.valueOf(outValue),FlagHelper.get().getId(itemSelected,mCoins));
                outcome.id=o.outcome_id;
                outcome.created = outCreated;



                ApiClient.get().putOutcome(outcome, new GenericCallback<Outcome>() {
                    @Override
                    public void onSuccess(Outcome data) {

                        if(!data.created.equals(o.created)){
                            if(onRefresh!=null){
                                onRefresh.onRefresh();
                            }
                        }
                        Toast.makeText(dialogView.getContext(), " El gasto "+data.description +" ha sido modificado ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Error error) {

                    }
                });
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    */

    private void selectDate(final TextView date){
        final DatePickerDialog datePickerDialog;
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog
        datePickerDialog = new DatePickerDialog(mContext,R.style.datepicker,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        String sdayOfMonth = String.valueOf(dayOfMonth);
                        if (sdayOfMonth.length() == 1) {
                            sdayOfMonth = "0" + dayOfMonth;
                        }

                        String smonthOfYear = String.valueOf(monthOfYear + 1);
                        if (smonthOfYear.length() == 1) {
                            smonthOfYear = "0" + smonthOfYear;
                        }

                        String time= DateHelper.get().getOnlyTime(DateHelper.get().getActualDate());

                        String datePicker=year + "-" + smonthOfYear + "-" +  sdayOfMonth +" "+time ;
                        date.setText(datePicker);

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }
}
