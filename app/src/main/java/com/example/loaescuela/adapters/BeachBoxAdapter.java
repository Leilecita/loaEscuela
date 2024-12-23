package com.example.loaescuela.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loaescuela.DateHelper;
import com.example.loaescuela.R;
import com.example.loaescuela.ValuesHelper;
import com.example.loaescuela.network.models.BeachBox;
import com.example.loaescuela.types.Constants;

import java.util.List;

public class BeachBoxAdapter extends BaseAdapter<BeachBox,BeachBoxAdapter.ViewHolder> {
    private Context mContext;

    public BeachBoxAdapter(Context context, List<BeachBox> extractions) {
        setItems(extractions);
        mContext = context;
    }

    public BeachBoxAdapter(){

    }

    public List<BeachBox> getListEmployees() {
        return getList();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView number_date;
        public TextView counted_sale;
        public TextView card;
        public TextView total_amount;
        public TextView rest_box;
        public TextView dep;
        public TextView category;
        public TextView deposit;
        public LinearLayout line_photo;
        public LinearLayout principal_line;
        public ImageView vent;
        public ImageView pos;

        public ViewHolder(View v) {
            super(v);
            counted_sale = v.findViewById(R.id.venta_ctdo);
            card = v.findViewById(R.id.card);
            total_amount = v.findViewById(R.id.total_amount);
            rest_box = v.findViewById(R.id.rest_box);
            dep = v.findViewById(R.id.dep);
            date = v.findViewById(R.id.date);
            number_date = v.findViewById(R.id.number_day);
            line_photo = v.findViewById(R.id.line_photos);
            principal_line = v.findViewById(R.id.principal_line);
            category = v.findViewById(R.id.category);
            deposit = v.findViewById(R.id.deposit);
        }
    }

    @Override
    public BeachBoxAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_box, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    private void clearViewHolder(BeachBoxAdapter.ViewHolder vh) {
        if (vh.date != null)
            vh.date.setText(null);
        if (vh.number_date != null)
            vh.number_date.setText(null);
        if (vh.card != null)
            vh.card.setText(null);
        if (vh.counted_sale != null)
            vh.counted_sale.setText(null);
        if (vh.total_amount != null)
            vh.total_amount.setText(null);
        if (vh.rest_box != null)
            vh.rest_box.setText(null);
        if (vh.dep != null)
            vh.dep.setText(null);
        if (vh.category != null)
            vh.category.setText(null);
        if (vh.deposit != null)
            vh.deposit.setText(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        clearViewHolder(holder);

        final BeachBox currentBeachBox = getItem(position);

        holder.counted_sale.setText(ValuesHelper.get().getIntegerQuantity(currentBeachBox.counted_sale));
        holder.card.setText(ValuesHelper.get().getIntegerQuantity(currentBeachBox.card));

        holder.total_amount.setText(ValuesHelper.get().getIntegerQuantity(currentBeachBox.total_box));
        holder.rest_box.setText(ValuesHelper.get().getIntegerQuantity(currentBeachBox.rest_box));
        holder.dep.setText(ValuesHelper.get().getIntegerQuantity(currentBeachBox.deposit));
        holder.category.setText(currentBeachBox.category);
        holder.deposit.setText(ValuesHelper.get().getIntegerQuantity(currentBeachBox.deposit));

        if (currentBeachBox.rest_box != currentBeachBox.total_box - currentBeachBox.deposit) {
            holder.rest_box.setTextColor(mContext.getResources().getColor(R.color.loa_red));
        }else{
            holder.rest_box.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDarkLetter));
        }

        if(currentBeachBox.category.equals(Constants.CATEGORY_ESCUELA)){
            holder.principal_line.setBackgroundColor(mContext.getResources().getColor(R.color.back_buys));
        }else{
            holder.principal_line.setBackgroundColor(mContext.getResources().getColor(R.color.background));
        }


        if(currentBeachBox.total_box != currentBeachBox.rest_box_ant + currentBeachBox.counted_sale){
            holder.total_amount.setTextColor(mContext.getResources().getColor(R.color.loa_red));
        }else{
            holder.total_amount.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDarkLetter));
        }

        holder.dep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent= new Intent(mContext, ExtractionsActivity.class);
                intent.putExtra("DATE",currentBeachBox.created);
                intent.putExtra("NEXTDATE",DateHelper.get().getNextDay(currentBeachBox.created));
                mContext.startActivity(intent);*/
            }
        });
        final String dateToShow= DateHelper.get().getOnlyDate(DateHelper.get().changeFormatDate(currentBeachBox.created));

        holder.date.setText(DateHelper.get().getNameMonth2(currentBeachBox.created).substring(0,3));
        holder.number_date.setText(DateHelper.get().numberDay(currentBeachBox.created));

        holder.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,DateHelper.get().onlyDayMonth(dateToShow)+"/"+ DateHelper.get().getOnlyYear(dateToShow),Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.line_photo.getVisibility() == View.GONE){
                    holder.line_photo.setVisibility(View.VISIBLE);
                }else{
                    holder.line_photo.setVisibility(View.GONE);
                }
            }
        });



        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

               /* AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.cuad_information_box, null);

                builder.setView(dialogView);
                final AlertDialog dialog = builder.create();

                final ImageView delete=  dialogView.findViewById(R.id.delete);
                ImageView edith=  dialogView.findViewById(R.id.edith);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //deleteBeachBox(currentBeachBox,position);
                        dialog.dismiss();
                    }
                });

                edith.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // edithBeachBox(currentBeachBox,position);
                        dialog.dismiss();
                    }
                });

                TextView desc=  dialogView.findViewById(R.id.detail);
                desc.setText(currentBeachBox.detail);
                TextView countedSale=  dialogView.findViewById(R.id.counted_sale);
                TextView creditCard=  dialogView.findViewById(R.id.credit_card);
                TextView total_box=  dialogView.findViewById(R.id.total_box);
                TextView rest_box=  dialogView.findViewById(R.id.rest_box);
                TextView rest_box_ant=  dialogView.findViewById(R.id.rest_box_ant);
                TextView deposit=  dialogView.findViewById(R.id.deposit);

                countedSale.setText(String.valueOf(currentBeachBox.counted_sale));
                creditCard.setText(String.valueOf(currentBeachBox.credit_card));
                total_box.setText(String.valueOf(currentBeachBox.total_box));
                rest_box.setText(String.valueOf(currentBeachBox.rest_box));
                rest_box_ant.setText(String.valueOf(currentBeachBox.rest_box_ant));
                deposit.setText(String.valueOf(currentBeachBox.deposit));
                TextView date=  dialogView.findViewById(R.id.date);

                // date.setText(DateHelper.get().serverToUserFormatted(currentBeachBox.created));
                date.setText(DateHelper.get().changeFormatDate(currentBeachBox.created));
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();*/
                return false;
            }
        });
    }

    /*private void deleteBeachBox(final BeachBox b,final Integer position){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.cuad_delete_box, null);
        builder.setView(dialogView);

        final TextView date= dialogView.findViewById(R.id.date);
        final TextView desc= dialogView.findViewById(R.id.detail);

        date.setText(b.created);
        desc.setText(b.detail);

        final TextView cancel =dialogView.findViewById(R.id.cancel);
        final Button ok =dialogView.findViewById(R.id.ok);

        final AlertDialog dialog = builder.create();

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ApiClient.get().deleteBeachBox(b.id, new GenericCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        Toast.makeText(mContext,"Se elimina la caja "+b.detail,Toast.LENGTH_LONG).show();
                        removeItem(position);
                    }

                    @Override
                    public void onError(Error error) {
                        DialogHelper.get().showMessage("Error", "No se pudo eliminar la caja",mContext);
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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void edithBeachBox(final BeachBox b, final Integer position){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.cuad_model_edith_box, null);
        builder.setView(dialogView);

        final TextView counted_sale= dialogView.findViewById(R.id.counted_sale);
        final TextView credit_card= dialogView.findViewById(R.id.credit_card);
        final TextView total_box= dialogView.findViewById(R.id.total_box);
        final TextView rest_box =dialogView.findViewById(R.id.rest_box);
        final TextView rest_box_ant =dialogView.findViewById(R.id.rest_box_ant);
        final TextView date =dialogView.findViewById(R.id.date);
        final TextView dep =dialogView.findViewById(R.id.deposit);
        final TextView cancel =dialogView.findViewById(R.id.cancel);
        final Button ok =dialogView.findViewById(R.id.ok);

        counted_sale.setText(String.valueOf(ValuesHelper.get().getIntegerQuantity(b.counted_sale)));
        credit_card.setText(String.valueOf(ValuesHelper.get().getIntegerQuantity(b.credit_card)));
        total_box.setText(String.valueOf(ValuesHelper.get().getIntegerQuantity(b.total_box)));
        rest_box.setText(String.valueOf(ValuesHelper.get().getIntegerQuantity(b.rest_box)));
        rest_box_ant.setText(String.valueOf(ValuesHelper.get().getIntegerQuantity(b.rest_box_ant)));
        dep.setText(String.valueOf(ValuesHelper.get().getIntegerQuantity(b.deposit)));

        dep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, " Se modifica de la pantalla 'Extracciones' ",Toast.LENGTH_LONG).show();
            }
        });

        date.setHint(DateHelper.get().getOnlyDate((DateHelper.get().getOnlyDate(b.created))));
        date.setHintTextColor(mContext.getResources().getColor(R.color.colorDialogButton));

        final AlertDialog dialog = builder.create();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ValidatorHelper.get().isTypeDouble(counted_sale.getText().toString().trim())
                        && ValidatorHelper.get().isTypeDouble(credit_card.getText().toString().trim())
                        && ValidatorHelper.get().isTypeDouble(total_box.getText().toString().trim())
                        && ValidatorHelper.get().isTypeDouble(rest_box.getText().toString().trim())
                        && ValidatorHelper.get().isTypeDouble(rest_box_ant.getText().toString().trim())
                ){

                    b.counted_sale=Double.valueOf(counted_sale.getText().toString().trim());
                    b.credit_card=Double.valueOf(credit_card.getText().toString().trim());
                    b.total_box=Double.valueOf(total_box.getText().toString().trim());
                    b.rest_box=Double.valueOf(rest_box.getText().toString().trim());
                    b.rest_box_ant=Double.valueOf(rest_box_ant.getText().toString().trim());
                    // b.deposit=Double.valueOf(dep.getText().toString().trim());

                    ApiClient.get().putBeachBox(b, new GenericCallback<BeachBox>() {
                        @Override
                        public void onSuccess(BeachBox data) {
                            updateItem(position,b);
                            Toast.makeText(mContext,"La caja se ha modificado con éxito" ,Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(Error error) {
                            DialogHelper.get().showMessage("Error","No se pudo modificar la caja",mContext);
                        }
                    });
                    dialog.dismiss();
                    notifyDataSetChanged();
                }else{
                    Toast.makeText(dialogView.getContext(), " Tipo de valor no valido ", Toast.LENGTH_LONG).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }*/
}

