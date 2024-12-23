package com.example.loaescuela.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.loaescuela.DateHelper;
import com.example.loaescuela.R;
import com.example.loaescuela.ValidatorHelper;
import com.example.loaescuela.ValuesHelper;
import com.example.loaescuela.fragment.BaseFragment;
import com.example.loaescuela.fragment.BoxBeachFragment;
import com.example.loaescuela.fragment.BoxLocalFragment;
import com.example.loaescuela.fragment.IncomesBeachFragment;
import com.example.loaescuela.fragment.IncomesLocalFragment;
import com.example.loaescuela.network.ApiClient;
import com.example.loaescuela.network.Error;
import com.example.loaescuela.network.GenericCallback;
import com.example.loaescuela.network.models.BeachBox;
import com.example.loaescuela.network.models.ReportBox;
import com.example.loaescuela.network.models.ReportNewBox;
import com.example.loaescuela.types.Constants;

import java.util.Calendar;

public class CreateBoxActivity extends BaseActivity {

    private TextView counted_sale;
    private TextView credit_card;
    private EditText total_amount;
    private EditText rest_box;
    private EditText deposit;
    private EditText detail;
    private TextView date;

    private String mSelectDate;
    private String mRestBoxDayBefore;
    private RelativeLayout date_picker;


    private Boolean mDetailNoNUll=false;
    private TextView tot_extractions;
    private TextView rest_box_day_before;

    private Double mRestBoxDayBeforeValue;
    private Double mTotalExtractionsByDay;
    private Double mTotalBox=0.0;
    private Double mRestBox=0.0;
    private Double mCountedSale=0.0;
    private Double mCreditCard=0.0;

    private TextView day;
    private TextView month;

    private TextView tot_esc_ef;
    private TextView tot_formatos;

    private BeachBox mBox;
    private LinearLayout line_photos;
    private LinearLayout line_top_info;
    private LinearLayout finish_save;
    private LinearLayout fab_save;

    private LinearLayout home;
    private LinearLayout mChange_category;

    private String mPaymentPlace;
    private String mCategory;

    private FrameLayout porcentaje;
    private LinearLayout colonia;
    private LinearLayout load_extraction;
    private TextView text_colonia;
    private TextView text_porcentaje;
    private static final int CREATE_BOX_ACTIVITY_OUT = 1329;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_create_box;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPaymentPlace = getIntent().getStringExtra("PAYMENTPLACE");
        System.out.println("acaaaaa");
        System.out.println(mPaymentPlace);
        mCategory = Constants.CATEGORY_ESCUELA;

        home = findViewById(R.id.line_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("PAYMENTPLACE", mPaymentPlace);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        mChange_category = findViewById(R.id.change_category);
        text_porcentaje = findViewById(R.id.text_porcentaje);
        text_colonia = findViewById(R.id.text_comision);
        porcentaje = findViewById(R.id.gen);
        colonia = findViewById(R.id.comision);

        mChange_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(mCategory.equals(Constants.CATEGORY_ESCUELA)){
                        mCategory = Constants.CATEGORY_COLONIA;

                        porcentaje.setBackgroundResource(R.drawable.custom_button_3);
                        colonia.setBackgroundResource(R.drawable.custom_button_3a);
                        text_colonia.setTextColor(getResources().getColor(R.color.white));
                        text_porcentaje.setTextColor(getResources().getColor(R.color.colorbutton));

                        clearAllFields();
                    }else{
                        mCategory = Constants.CATEGORY_ESCUELA;

                        porcentaje.setBackgroundResource(R.drawable.custom_button_3a);
                        colonia.setBackgroundResource(R.drawable.custom_button_3);
                        text_colonia.setTextColor(getResources().getColor(R.color.colorbutton));
                        text_porcentaje.setTextColor(getResources().getColor(R.color.white));

                        clearAllFields();
                    }
                }
        });



        load_extraction = findViewById(R.id.load_ext);
        load_extraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateBoxActivity.this, OutcomesActivity.class);
                startActivityForResult(i, CREATE_BOX_ACTIVITY_OUT);
            }
        });
        line_top_info = findViewById(R.id.line_top);
        finish_save = findViewById(R.id.finish_save);
        fab_save = findViewById(R.id.fab_save);
        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mDetailNoNUll){
                    BeachBox b = loadInfoBox();

                    b.created= mSelectDate;
                    b.payment_place = mPaymentPlace;

                    ApiClient.get().postBeachBox(b, new GenericCallback<BeachBox>() {
                        @Override
                        public void onSuccess(BeachBox data) {
                            Toast.makeText(getBaseContext(),"La caja ha sido guardada"+data.id, Toast.LENGTH_SHORT).show();
                            mBox = data;

                            clearAllFields();
                        }

                        @Override
                        public void onError(Error error) {
                            // DialogHelper.get().showMessage("Error", "No se pudo crear la caja",getBaseContext());
                        }
                    });


                }else{
                    Toast.makeText(getBaseContext(),"Ingrese detalle de caja", Toast.LENGTH_LONG).show();
                }
            }
        });
        finish_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("PAYMENTPLACE", mPaymentPlace);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        day=findViewById(R.id.day);
        month=findViewById(R.id.month);

        rest_box_day_before = findViewById(R.id.res_box_day_before);
        tot_extractions = findViewById(R.id.tot_extract);

        counted_sale=  findViewById(R.id.counted_sale);
        credit_card=  findViewById(R.id.credit_card);
        total_amount=  findViewById(R.id.total_amount);
        rest_box=  findViewById(R.id.rest_box);
        deposit=  findViewById(R.id.deposit);
        detail=  findViewById(R.id.detail);
        date=  findViewById(R.id.date);
        date_picker=  findViewById(R.id.date_picker);

        mSelectDate = DateHelper.get().getActualDate2();
        date.setText(DateHelper.get().getOnlyDate(mSelectDate));
        day.setText(DateHelper.get().numberDay(mSelectDate));
        month.setText(DateHelper.get().getNameMonth2(mSelectDate).substring(0,3));

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog;
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(CreateBoxActivity.this,R.style.datepicker,
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
                                date.setText(DateHelper.get().getOnlyDate(datePicker));
                                mSelectDate=datePicker;
                                deposit.setText("");

                                getPreviousBox();
                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });

        getPreviousBox();
    }

    private void clearAllFields(){
        tot_extractions.setText("");
        counted_sale.setText("");
        credit_card.setText("");
        rest_box_day_before.setText("");
        rest_box.setText("");
        total_amount.setText("");

        getPreviousBox();
    }

    private void getPreviousBox(){
        ApiClient.get().getPreviousBox(DateHelper.get().getOnlyDate(mSelectDate),DateHelper.get().getOnlyDateComplete(mSelectDate),
                DateHelper.get().getOnlyDateComplete(DateHelper.get().getNextDay(mSelectDate)), this.mPaymentPlace, this.mCategory,new GenericCallback<ReportNewBox>() {
                    @Override
                    public void onSuccess(ReportNewBox data) {
                        mRestBoxDayBefore = ValuesHelper.get().getIntegerQuantity(data.lastBox.rest_box);
                        mRestBoxDayBeforeValue = Double.valueOf(mRestBoxDayBefore);
                        rest_box_day_before.setText(String.valueOf(mRestBoxDayBefore));

                        mTotalExtractionsByDay = data.amountExtractions;
                        tot_extractions.setText(ValuesHelper.get().getIntegerQuantity(data.amountExtractions));

                        loadInfo();
                    }

                    @Override
                    public void onError(Error error) {
                    }
                });
    }



    private void loadInfo(){
        loadAmountByDay();   //mCountedSale=

        counted_sale.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {

                if(!counted_sale.getText().toString().trim().equals("") && ValidatorHelper.get().isTypeDouble(counted_sale.getText().toString().trim())){
                    Double d=Double.valueOf(counted_sale.getText().toString().trim());
                    Double d2=Double.valueOf(mRestBoxDayBefore);

                    total_amount.setText(String.valueOf(d+d2));
                    rest_box.setText(String.valueOf(d+d2-mTotalExtractionsByDay));
                }else{
                    total_amount.setText("");
                    rest_box.setText("");
                }
            }
        });

        total_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {

                System.out.println("ejecuta amount");
                if(!total_amount.getText().toString().trim().equals("") && ValidatorHelper.get().isTypeDouble(total_amount.getText().toString().trim())){
                    Double total=Double.valueOf(total_amount.getText().toString().trim());
                    Double restDaybefore=Double.valueOf(mRestBoxDayBefore);
                    Double countedsale=Double.valueOf(counted_sale.getText().toString().trim());

                    Double rest_b=total - mTotalExtractionsByDay;
                    rest_box.setText(String.valueOf(rest_b));

                    if(total != restDaybefore+countedsale){
                        total_amount.setTextColor(getResources().getColor(R.color.loa_red));
                        mDetailNoNUll=true;
                    }else{
                        total_amount.setTextColor(getResources().getColor(R.color.colorPrimaryDarkLetter));
                        mDetailNoNUll=false;
                    }
                }
            }
        });

        rest_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {


                System.out.println("ejecuta box");
                if(!rest_box.getText().toString().trim().equals("") && ValidatorHelper.get().isTypeDouble(rest_box.getText().toString().trim())){
                    Double restbox=Double.valueOf(rest_box.getText().toString().trim());
                    Double total=Double.valueOf(total_amount.getText().toString().trim());
                    Double totalextr=Double.valueOf(mTotalExtractionsByDay);

                    System.out.println("caja  BOXX");
                    if(restbox != total-totalextr){
                        rest_box.setTextColor(getResources().getColor(R.color.loa_red));
                        mDetailNoNUll=true;
                    }else{
                        rest_box.setTextColor(getResources().getColor(R.color.colorPrimaryDarkLetter));
                        mDetailNoNUll=false;
                    }
                }
            }
        });

        detail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(!detail.getText().toString().trim().equals("") ){
                    mDetailNoNUll=false;
                }else{
                    mDetailNoNUll=true;
                }
            }
        });

    }

    private BeachBox loadInfoBox(){

        Double countedSale=Double.valueOf((counted_sale.getText().toString().trim().equals("")?"0":counted_sale.getText().toString().trim()));
        Double creditCard=Double.valueOf(credit_card.getText().toString().trim().equals("")?"0":credit_card.getText().toString().trim());
        Double totalAmount= Double.valueOf(total_amount.getText().toString().trim());
        Double restBox= Double.valueOf(rest_box.getText().toString().trim());
        String det=detail.getText().toString().trim();

       // BeachBox b= new BeachBox(countedSale,creditCard,totalAmount,restBox,mTotalExtractionsByDay,det,mRestBoxDayBeforeValue);
        BeachBox b= new BeachBox();
        b.card = creditCard;
        b.total_box = totalAmount;
        b.counted_sale = countedSale;
        b.rest_box = restBox;
        b.category = mCategory;
        b.detail = det;
        b.deposit = mTotalExtractionsByDay;

        return b;
    }


    //aca dividir si es escuela o colonia

    private void loadAmountByDay(){

        System.out.println("pyment place aaaa");
        System.out.println(this.mPaymentPlace);
        ApiClient.get().getPaidAmountByDay(mSelectDate, this.mPaymentPlace, this.mCategory,new GenericCallback<ReportBox>() {
            @Override
            public void onSuccess(ReportBox data) {

                counted_sale.setText(ValuesHelper.get().getIntegerQuantity(data.tot_ef));
                mCountedSale = data.tot_ef;
                credit_card.setText(ValuesHelper.get().getIntegerQuantity(data.tot_tarjeta));
                mCreditCard = data.tot_tarjeta; //digital

                mTotalExtractionsByDay = data.amount_outcomes;
                tot_extractions.setText(ValuesHelper.get().getIntegerQuantity(data.amount_outcomes));

                total_amount.setText(ValuesHelper.get().getIntegerQuantity(mCountedSale + mRestBoxDayBeforeValue));
                rest_box.setText(ValuesHelper.get().getIntegerQuantity(mCountedSale + mRestBoxDayBeforeValue - mTotalExtractionsByDay));
            }

            @Override
            public void onError(Error error) {

            }
        });
       /* ApiClient.get().getAmountSalesByDay(mSelectDate, new GenericCallback<AmountResult>() {
            @Override
            public void onSuccess(AmountResult data) {
                counted_sale.setText(String.valueOf(data.total));
                mCountedSale=data.total;
            }

            @Override
            public void onError(Error error) {
            }
        });*/
    }

    private void loadAmountSalesCard(){
       /* ApiClient.get().getAmountSalesByDayCard(mSelectDate, new GenericCallback<AmountResult>() {
            @Override
            public void onSuccess(AmountResult data) {
                credit_card.setText(String.valueOf(data.total));
                mCreditCard=data.total;
            }

            @Override
            public void onError(Error error) {
            }
        });*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CREATE_BOX_ACTIVITY_OUT ){
            getPreviousBox();
        }
    }

}
