package com.example.loaescuela.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loaescuela.DateHelper;
import com.example.loaescuela.Interfaces.OnSelectStudent;
import com.example.loaescuela.Interfaces.OrderFragmentListener;
import com.example.loaescuela.R;
import com.example.loaescuela.activities.AssistsCoursesIncomesByStudentActivity;
import com.example.loaescuela.data.SessionPrefs;
import com.example.loaescuela.network.ApiClient;
import com.example.loaescuela.network.Error;
import com.example.loaescuela.network.GenericCallback;
import com.example.loaescuela.network.models.ClassCourse;
import com.example.loaescuela.network.models.Planilla;
import com.example.loaescuela.network.models.PlanillaAlumno;
import com.example.loaescuela.network.models.PlanillaPresente;
import com.example.loaescuela.network.models.ReportSeasonPresent;
import com.example.loaescuela.network.models.ReportStudentAsist;
import com.example.loaescuela.network.models.ReportStudentAsistItem;
import com.example.loaescuela.types.CategoryType;
import com.example.loaescuela.types.Constants;
import com.example.loaescuela.types.SubCategoryType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StudentAssistAdapter extends BaseAdapter<ReportStudentAsistItem,StudentAssistAdapter.ViewHolder> {
    private Context mContext;
    private ArrayAdapter<String> adapter;

    List<String> listColor = new ArrayList<>();
    Random random;

    private String datePresent;
    private String category="";
    private Long planilla_id;

    private OnSelectStudent onSelectStudent = null;

    private Boolean enablePresent = true;
    private OrderFragmentListener onOrderFragmentLister= null;

    public void setOnOrderFragmentLister(OrderFragmentListener lister){
        onOrderFragmentLister=lister;
    }

    public void setOnSelectStudent(OnSelectStudent listener){
        this.onSelectStudent = listener;
    }

    public StudentAssistAdapter(Context context, List<ReportStudentAsistItem> clients) {
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

    public void setCategory(String category){
        this.category = category;
    }
    public StudentAssistAdapter() {
    }

    public void setEnablePresent(Boolean val){
        this.enablePresent = val;
    }

    public void setDatePresent(String date){
        this.datePresent = date;
    }

    public void setPlanillaId(Long planillaid){
        this.planilla_id = planillaid;
    }

    public List<ReportStudentAsistItem> getListClient() {
        return getList();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text_name;
        public TextView text_name2;
        public TextView text_year;
        public TextView taken_clases;
        public TextView taken_clases_2;
        public TextView last_present;
        public TextView buyed_classes;
        public TextView debt_amount;
        public TextView dni;

        public TextView firstLetter;
        public CheckBox check_presente;

        public LinearLayout line_info_student;
        public LinearLayout more_info;

        public ImageView cuad_image;

        public LinearLayout call_mama;
        public LinearLayout call_papa;
        public LinearLayout call_alumno;
        public LinearLayout whatsapp_mama;
        public LinearLayout whatsapp_papa;
        public LinearLayout whatsapp_alumno;

        public TextView nombre_mama;
        public TextView nombre_adulto;
        public TextView tel_mama;
        public TextView nombre_papa;
        public TextView tel_papa;
        public TextView student_observation;

        public LinearLayout line_mama;
        public LinearLayout line_papa;
        public LinearLayout line_tel_student;


        public ViewHolder(View v) {
            super(v);
            text_name = v.findViewById(R.id.text_name);
            text_name2 = v.findViewById(R.id.name2_student);
            check_presente = v.findViewById(R.id.check_presente);
            line_info_student = v.findViewById(R.id.info_student);
            taken_clases = v.findViewById(R.id.taken_classes);
            taken_clases_2 = v.findViewById(R.id.taken_classes_2);
            more_info = v.findViewById(R.id.more_info);
            buyed_classes = v.findViewById(R.id.buyed_classes);
            debt_amount = v.findViewById(R.id.debt_amount);
            cuad_image = v.findViewById(R.id.cuad_image);
            firstLetter = v.findViewById(R.id.firstLetter);
            dni = v.findViewById(R.id.dni);

            call_mama = v.findViewById(R.id.call_mama);
            call_papa = v.findViewById(R.id.call_papa);
            call_alumno = v.findViewById(R.id.call_student);
            whatsapp_mama = v.findViewById(R.id.wh_mam);
            whatsapp_papa = v.findViewById(R.id.wh_pap);
            whatsapp_alumno = v.findViewById(R.id.wh_student);

            nombre_mama = v.findViewById(R.id.nombre_mama);
            nombre_adulto = v.findViewById(R.id.nombre_adulto);
            nombre_papa = v.findViewById(R.id.nombre_papa);
            nombre_papa = v.findViewById(R.id.nombre_papa);

            tel_mama = v.findViewById(R.id.tel_mama);
            tel_papa = v.findViewById(R.id.tel_papa);

            line_mama = v.findViewById(R.id.line_mama);
            line_papa = v.findViewById(R.id.line_papa);
            line_tel_student = v.findViewById(R.id.line_tel_student);
            student_observation = v.findViewById(R.id.student_observation);
            last_present = v.findViewById(R.id.last_present);

        }
    }

    @Override
    public StudentAssistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new View

        View v = LayoutInflater.from(mContext).inflate(R.layout.item_student_assist, parent, false);
        StudentAssistAdapter.ViewHolder vh = new StudentAssistAdapter.ViewHolder(v);
        return vh;
    }


    private void clearViewHolder(StudentAssistAdapter.ViewHolder vh) {
        if (vh.text_name != null)
            vh.text_name.setText(null);
        if (vh.text_name2 != null)
            vh.text_name2.setText(null);
        if (vh.text_year != null)
            vh.text_year.setText(null);
        if (vh.taken_clases != null)
            vh.taken_clases.setText(null);
        if (vh.taken_clases_2 != null)
            vh.taken_clases_2.setText(null);

        if (vh.buyed_classes != null)
            vh.buyed_classes.setText(null);

        if (vh.debt_amount != null)
            vh.debt_amount.setText(null);
        if (vh.dni != null)
            vh.dni.setText(null);
        if (vh.student_observation != null)
            vh.student_observation.setText(null);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final StudentAssistAdapter.ViewHolder holder, final int position) {
        clearViewHolder(holder);

        final ReportStudentAsistItem currentClient = getItem(position);

        if(currentClient.nombre_mama != null && currentClient.nombre_mama.matches("") && currentClient.tel_mama.matches("") ){
            holder.line_mama.setVisibility(View.GONE);
        }else{
            holder.line_mama.setVisibility(View.VISIBLE);
        }

        if(currentClient.nombre_papa != null && currentClient.nombre_papa.matches("") && currentClient.tel_papa.matches("") ){
            holder.line_papa.setVisibility(View.GONE);
        }else{
            holder.line_papa.setVisibility(View.VISIBLE);
        }

        holder.nombre_adulto.setText(currentClient.nombre);
        holder.nombre_mama.setText(currentClient.nombre_mama);
        holder.nombre_papa.setText(currentClient.nombre_papa);
        holder.tel_mama.setText(currentClient.tel_mama);
        holder.tel_papa.setText(currentClient.tel_papa);

        if(currentClient.student_observation.length() > 0){
            holder.student_observation.setText(currentClient.student_observation);
            holder.student_observation.setVisibility(View.VISIBLE);

        }else{
            holder.student_observation.setVisibility(View.GONE);
        }



        if(currentClient.tel_adulto.matches("")){
            holder.line_tel_student.setVisibility(View.GONE);
        }else{
            holder.line_tel_student.setVisibility(View.VISIBLE);
        }

        holder.call_papa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(currentClient.tel_papa);
            }
        });

        holder.call_mama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(currentClient.tel_mama);
            }
        });

        holder.call_alumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(currentClient.tel_adulto);
            }
        });

        holder.whatsapp_papa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendWhatsapp("Hola", currentClient.tel_papa);
            }
        });

        holder.whatsapp_mama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendWhatsapp("Hola", currentClient.tel_mama);
            }
        });
        holder.whatsapp_alumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendWhatsapp("Hola", currentClient.tel_adulto);
            }
        });



        holder.text_name.setText(currentClient.nombre + " " + currentClient.apellido);
        holder.firstLetter.setText(String.valueOf(currentClient.nombre.charAt(0)));
        holder.dni.setText(currentClient.dni);

        if(this.category.equals(Constants.CATEGORY_COLONIA)){
            holder.cuad_image.setColorFilter(Color.parseColor(Constants.COLOR_COLONIA), PorterDuff.Mode.SRC_ATOP);
        }else if(this.category.equals(Constants.CATEGORY_ESCUELA)){
            holder.cuad_image.setColorFilter(Color.parseColor(Constants.COLOR_ESCUELA), PorterDuff.Mode.SRC_ATOP);
        }else{
            holder.cuad_image.setColorFilter(Color.parseColor(Constants.COLOR_HIGHSCHOOL), PorterDuff.Mode.SRC_ATOP);
        }


        if(currentClient.taken_classes.size() > 0){

            holder.taken_clases.setText(String.valueOf(currentClient.taken_classes.get(0).cant_presents));
            holder.taken_clases_2.setText(String.valueOf(currentClient.taken_classes.get(0).cant_presents));
            holder.buyed_classes.setText(String.valueOf(currentClient.taken_classes.get(0).cant_buyed_classes));
            holder.debt_amount.setText(String.valueOf(currentClient.taken_classes.get(0).tot_amount - currentClient.taken_classes.get(0).tot_paid_amount));
        }


        if(currentClient.presente.equals("si")){
            holder.check_presente.setChecked(true);
        }else{
            holder.check_presente.setChecked(false);
        }

        if(!enablePresent){
            if(!SessionPrefs.get(mContext).getName().equals("lei")){
                holder.check_presente.setEnabled(false);
            }

            holder.check_presente.setAlpha(.2f);
            holder.cuad_image.setAlpha(.2f);

        } else{
            holder.check_presente.setEnabled(true);
            holder.check_presente.setAlpha(1.f);
            holder.cuad_image.setAlpha(1.f);
        }

        holder.check_presente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(enablePresent || SessionPrefs.get(mContext).getName().equals("lei")){

                    if(!holder.check_presente.isChecked()){
                        holder.check_presente.setChecked(true);
                        quitPresent(holder,currentClient.nombre+" "+currentClient.apellido, currentClient, position);
                    }else{
                        addPresent(currentClient, position, holder);
                    }

                }else{
                    Toast.makeText(mContext, "No es posible modificar presentes de días anteriores", Toast.LENGTH_LONG).show();
                }
            }
        });

        if(currentClient.isOpen){
            holder.line_info_student.setVisibility(View.VISIBLE);
        }else{
            holder.line_info_student.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.line_info_student.getVisibility() == View.VISIBLE){
                    holder.line_info_student.setVisibility(View.GONE);
                }else{
                    holder.line_info_student.setVisibility(View.VISIBLE);
                }

            }
        });

        holder.more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SessionPrefs.get(mContext).getLevel().equals("admin")){

                    startEdithOrderActivity(currentClient,position, category);
                }else{
                    Toast.makeText(mContext,"consultar a Lei",Toast.LENGTH_LONG).show();
                }




               // AssistsCoursesIncomesByStudentActivity.start(mContext,currentClient.student_id, currentClient.nombre, currentClient.apellido, category, "ASISTENCIA");
            }
        });

        if(SessionPrefs.get(mContext).getLevel().equals("admin")) {
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
                                    if (SessionPrefs.get(mContext).getLevel().equals("admin")) {
                                        deleteAlumnoDePLanilla(currentClient, position, "");
                                    } else {
                                        Toast.makeText(mContext, "pedir autorización a Lei", Toast.LENGTH_LONG).show();
                                    }

                                    return true;
                                case R.id.menu_edit:
                                    currentStudent(currentClient,position,"");
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
    }

    private void deleteAlumnoDePLanilla(final ReportStudentAsistItem o, final int position,final String planilla){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.cuad_delete_alum_asist, null);
        builder.setView(dialogView);

        final TextView nombre = dialogView.findViewById(R.id.nombre);
        final TextView planila = dialogView.findViewById(R.id.planilla);
        final Button ok = dialogView.findViewById(R.id.ok);
        final TextView cancel = dialogView.findViewById(R.id.cancel);

        nombre.setText(o.nombre);
        planila.setText(planilla);

        final AlertDialog dialog = builder.create();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ApiClient.get().deleteAlumnoPlanilla(o.planilla_alumno_id, new GenericCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        removeItem(position);
                        Toast.makeText(mContext, "Se ha eliminado el alumno de la planilla", Toast.LENGTH_LONG).show();
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

    private void currentStudent(final ReportStudentAsistItem o, final int position,final String planilla){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.current_student_dialog, null);
        builder.setView(dialogView);

        final TextView nombre = dialogView.findViewById(R.id.nombre);
        final TextView planila = dialogView.findViewById(R.id.planilla);
        final Button ok = dialogView.findViewById(R.id.ok);
        final Button no = dialogView.findViewById(R.id.no);
        final TextView cancel = dialogView.findViewById(R.id.cancel);

        nombre.setText(o.nombre);
        planila.setText(planilla);

        final AlertDialog dialog = builder.create();

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlanillaAlumno p = new PlanillaAlumno();
                p.id = o.planilla_alumno_id;
                p.current_student = "no";

                ApiClient.get().putPlanillaAlumno(p, new GenericCallback<PlanillaAlumno>() {
                    @Override
                    public void onSuccess(PlanillaAlumno data) {

                    }

                    @Override
                    public void onError(Error error) {

                    }
                });
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlanillaAlumno p = new PlanillaAlumno();
                p.id = o.planilla_alumno_id;
                p.current_student = "si";

                ApiClient.get().putPlanillaAlumno(p, new GenericCallback<PlanillaAlumno>() {
                    @Override
                    public void onSuccess(PlanillaAlumno data) {

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

    private void startEdithOrderActivity(ReportStudentAsistItem order,Integer pos, String category){
        if(onOrderFragmentLister!=null){
            onOrderFragmentLister.startNewActivityFragment(order,pos, category);
        }
    }

    private void updatePresentStudent(ReportStudentAsistItem r, Integer pos){

    }

    public void updateItem(final Integer position){
        final ReportStudentAsistItem r = getItem(position);

        ApiClient.get().getResumInfoByStudent(r.student_id, new GenericCallback<List<ReportSeasonPresent>>() {
            @Override
            public void onSuccess(List<ReportSeasonPresent> data) {

                if(data.size() > 0 ) {
                    r.taken_classes = data;

               /* if(data.size() > 0 && r.taken_classes.size() > 0){
                    ReportSeasonPresent d = data.get(0);
                    r.taken_classes.get(0).cant_presents = d.cant_presents;
                    r.taken_classes.get(0).cant_buyed_classes = d.cant_buyed_classes;
                    r.taken_classes.get(0).tot_paid_amount = d.tot_paid_amount;
                    r.taken_classes.get(0).tot_amount = d.tot_amount;
                } */
                }

                r.isOpen = true;

                updateItem(position, r);
            }

            @Override
            public void onError(Error error) {

            }
        });



      /*  ApiClient.get().getReportOrderByOrderId(r.order_id, new GenericCallback<ReportOrder>() {
            @Override
            public void onSuccess(ReportOrder data) {
                r.amount_order=data.amount_order;

                r.items=data.items;
                r.items_add=data.items_add;
                r.items_rem=data.items_rem;

                r.delivery_date=data.delivery_date;
                r.order_obs=data.order_obs;

                r.products_cant = data.products_cant;

                r.prepared_by = data.prepared_by;

                r.assigned_zone = data.assigned_zone;

                r.process_user_name = data.process_user_name;

                updateItem(position,r);

                r.isOpen = true;
            }

            @Override
            public void onError(Error error) {

            }
        });*/
    }


    private void call(String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public void sendWhatsapp(String text, String phone){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+549"+phone));
            mContext.startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    private void addPresent(ReportStudentAsistItem r, Integer pos, ViewHolder holder){

        PlanillaPresente p = new PlanillaPresente();
        p.alumno_id = r.student_id;
        p.planilla_id = r.planilla_id;
        p.fecha_presente = datePresent;

        if(r.planilla_presente_id >= 0){
            ApiClient.get().deletePlanillaPresente(r.planilla_presente_id, new GenericCallback<Void>() {
                @Override
                public void onSuccess(Void data) {

                    holder.check_presente.setChecked(false);

                    r.presente = "no";
                    r.planilla_presente_id = -1l;

                    //updateItem(pos, r); lo saque porque me modifica el padding bottom

                    r.taken_classes.get(0).cant_presents -= 1;
                    updateItem(pos, r);

                    if(onSelectStudent!=null){
                        onSelectStudent.onSelectStudent(-1l,"","","");
                    }
                }

                @Override
                public void onError(Error error) {

                }
            });

        }else{

            ApiClient.get().postPlanillaPresente(p , new GenericCallback<PlanillaPresente>() {
                @Override
                public void onSuccess(PlanillaPresente data) {

                    r.presente = "si";
                    r.planilla_presente_id = data.id;

                   // updateItem(pos, r);

                    r.taken_classes.get(0).cant_presents += 1;

                    updateItem(pos, r);

                    if(onSelectStudent!=null){
                        onSelectStudent.onSelectStudent(-1l,"","","");
                    }
                }

                @Override
                public void onError(Error error) {
                    Toast.makeText(mContext, error.message,Toast.LENGTH_LONG).show();

                }
            });
        }
    }


    private void quitPresent(ViewHolder holder, String name,ReportStudentAsistItem r, Integer pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.quit_present, null);
        builder.setView(dialogView);

        final TextView name_text =  dialogView.findViewById(R.id.name);
        name_text.setText(name);

        final TextView cancel=  dialogView.findViewById(R.id.cancel);
        final Button ok=  dialogView.findViewById(R.id.ok);

        final AlertDialog dialog = builder.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addPresent(r, pos, holder);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }



}

