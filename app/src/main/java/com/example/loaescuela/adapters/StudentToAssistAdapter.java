package com.example.loaescuela.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    public void setOnSelectStudent(OnSelectStudent listener){
        this.onSelectStudent = listener;
    }


    public void setPlanillaId(Long planillaId){
        this.mPlanillaId = planillaId;
    }

    public StudentToAssistAdapter(Context context, List<Student> clients) {
        setItems(clients);
        mContext = context;

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

        public TextView firstLetter;
        public LinearLayout select;


        public ViewHolder(View v) {
            super(v);
            text_name = v.findViewById(R.id.text_name);
            text_name2 = v.findViewById(R.id.name2_student);
            select = v.findViewById(R.id.select);
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


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final StudentToAssistAdapter.ViewHolder holder, final int position) {
        clearViewHolder(holder);

        final Student current = getItem(position);

        holder.text_name.setText(current.nombre + " " + current.apellido);

        holder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudentToAssist(current.id);

                if(onSelectStudent!=null){
                    onSelectStudent.onSelectStudent();
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

