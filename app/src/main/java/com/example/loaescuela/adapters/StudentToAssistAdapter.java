package com.example.loaescuela.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loaescuela.Interfaces.OnSelectStudent;
import com.example.loaescuela.R;
import com.example.loaescuela.network.ApiClient;
import com.example.loaescuela.network.Error;
import com.example.loaescuela.network.GenericCallback;
import com.example.loaescuela.network.models.PlanillaAlumno;
import com.example.loaescuela.network.models.ReportStudentAsist;
import com.example.loaescuela.network.models.Student;
import com.example.loaescuela.types.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StudentToAssistAdapter extends BaseAdapter<Student,StudentToAssistAdapter.ViewHolder> {
    private Context mContext;
    private ArrayAdapter<String> adapter;

    List<String> listColor = new ArrayList<>();
    Random random;
    private Long mPlanillaId = -1l;

    private OnSelectStudent onSelectStudent = null;
    private String typeActivity;
    private String paymentPlace;
    private String categoryActivity;

    public void setOnSelectStudent(OnSelectStudent listener){
        this.onSelectStudent = listener;
    }



    public void setCategoryActivity(String category){
        this.categoryActivity = category;
    }

    public void setPlanillaId(Long planillaId){
        this.mPlanillaId = planillaId;
    }

    public StudentToAssistAdapter(Context context, List<Student> clients) {
        setItems(clients);
        mContext = context;
        typeActivity = "PLANILLA";
        categoryActivity = "escuela";
        paymentPlace = "escuela";

        listColor.add("#685c85");
        listColor.add("#4f426b");
        listColor.add("#A49FB8");
        listColor.add("#817699");
        listColor.add("#a197b8");
        listColor.add("#9088a3");
        listColor.add("#C6BCDA");
        random = new Random();

    }

    public StudentToAssistAdapter() {

    }

    public List<Student> getListClient() {
        return getList();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text_name;
        public TextView text_name2;
        public TextView text_year;
        public TextView category;
        public TextView dni;

        public TextView firstLetter;
        public TextView cuad_text;
        public LinearLayout select;

        public ImageView cuad_image;

        public TextView nombre_mama;
        public TextView tel_mama;
        public TextView nombre_papa;
        public TextView tel_papa;
        public LinearLayout info_student;
        public LinearLayout line_mama;
        public LinearLayout line_papa;


        public ViewHolder(View v) {
            super(v);
            text_name = v.findViewById(R.id.text_name);
            text_name2 = v.findViewById(R.id.name2_student);
            select = v.findViewById(R.id.select);
            //category = v.findViewById(R.id.category);
            cuad_image = v.findViewById(R.id.cuad_image);
            cuad_text = v.findViewById(R.id.cuad_text);

            firstLetter = v.findViewById(R.id.firstLetter);
            dni = v.findViewById(R.id.dni);

            nombre_mama = v.findViewById(R.id.nombre_mama);
            nombre_papa = v.findViewById(R.id.nombre_papa);
            tel_mama = v.findViewById(R.id.tel_mama);
            tel_papa = v.findViewById(R.id.tel_papa);
            line_mama = v.findViewById(R.id.line_mama);
            line_papa = v.findViewById(R.id.line_papa);
            info_student = v.findViewById(R.id.info_student);
        }
    }

    @Override
    public StudentToAssistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new View

        View v = LayoutInflater.from(mContext).inflate(R.layout.item_student_to_assists, parent, false);
        StudentToAssistAdapter.ViewHolder vh = new StudentToAssistAdapter.ViewHolder(v);
        return vh;
    }


    private void clearViewHolder(StudentToAssistAdapter.ViewHolder vh) {
        if (vh.text_name != null)
            vh.text_name.setText(null);
        if (vh.text_year != null)
            vh.text_year.setText(null);
        if (vh.dni != null)
            vh.dni.setText(null);
    }

    public void setTypeActivity(String type){
        this.typeActivity = type;
    }

    public void setPaymentPLace(String place){
        this.paymentPlace = place;
    }

    public String getPaymentPlace(){
        return this.paymentPlace;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final StudentToAssistAdapter.ViewHolder holder, final int position) {
        clearViewHolder(holder);

        final Student current = getItem(position);

        System.out.println("acaaa assist");
        System.out.println(paymentPlace);

        holder.text_name.setText(current.nombre + " " + current.apellido);
       // holder.category.setText(current.category);
        holder.dni.setText(current.dni);

        holder.nombre_mama.setText(current.nombre_mama);
        holder.nombre_papa.setText(current.nombre_papa);
        holder.tel_mama.setText(current.tel_mama);
        holder.tel_papa.setText(current.tel_papa);


        if(current.nombre_mama != null && current.nombre_mama.matches("") && current.tel_mama.matches("") ){
            holder.line_mama.setVisibility(View.GONE);
        }else{
            holder.line_mama.setVisibility(View.VISIBLE);
        }

        if(current.nombre_papa != null && current.nombre_papa.matches("") && current.tel_papa.matches("") ){
            holder.line_papa.setVisibility(View.GONE);
        }else{
            holder.line_papa.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.info_student.getVisibility() == View.VISIBLE){
                    holder.info_student.setVisibility(View.GONE);
                }else{
                    holder.info_student.setVisibility(View.VISIBLE);
                }
            }
        });

        if(typeActivity.equals("PAGOS")){
            holder.cuad_text.setText("cargar pago");
        }else{
            holder.cuad_text.setText(" asignar  a  planilla");
        }

        if(current.category.equals(Constants.CATEGORY_COLONIA)){
            holder.cuad_image.setColorFilter(Color.parseColor(Constants.COLOR_COLONIA), PorterDuff.Mode.SRC_ATOP);
        }else if(current.category.equals(Constants.CATEGORY_ESCUELA)){
            holder.cuad_image.setColorFilter(Color.parseColor(Constants.COLOR_ESCUELA), PorterDuff.Mode.SRC_ATOP);
        }else{
            holder.cuad_image.setColorFilter(Color.parseColor(Constants.COLOR_HIGHSCHOOL), PorterDuff.Mode.SRC_ATOP);
        }

        if(current.nombre.length() > 0){
            holder.firstLetter.setText(String.valueOf(current.nombre.trim().charAt(0)));
        }


        holder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudentToAssist(current.id);

                if(onSelectStudent!=null){
                    //onSelectStudent.onSelectStudent(current.id, current.nombre, current.apellido, current.category);

                    onSelectStudent.onSelectStudent(current.id, current.nombre, current.apellido, categoryActivity);
                }

            }
        });
    }

    private void addStudentToAssist(Long id){

        PlanillaAlumno p = new PlanillaAlumno();
        p.alumno_id = id;
        p.planilla_id = mPlanillaId;

        ApiClient.get().postPlanillaAlumno(p, new GenericCallback<PlanillaAlumno>() {
            @Override
            public void onSuccess(PlanillaAlumno data) {

            }

            @Override
            public void onError(Error error) {

            }
        });

    }
}

