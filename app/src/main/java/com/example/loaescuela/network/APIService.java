package com.example.loaescuela.network;

import com.example.loaescuela.adapters.PlanillaAdapter;
import com.example.loaescuela.network.models.ClassCourse;
import com.example.loaescuela.network.models.DataIncomeCourse;
import com.example.loaescuela.network.models.Income;
import com.example.loaescuela.network.models.Planilla;
import com.example.loaescuela.network.models.PlanillaAlumno;
import com.example.loaescuela.network.models.PlanillaPresente;
import com.example.loaescuela.network.models.ReportClassCourse;
import com.example.loaescuela.network.models.ReportIncomeStudent;
import com.example.loaescuela.network.models.ReportPresent;
import com.example.loaescuela.network.models.ReportResumAsist;
import com.example.loaescuela.network.models.ReportStudentAsist;
import com.example.loaescuela.network.models.ReportStudentValue;
import com.example.loaescuela.network.models.Student;
import com.example.loaescuela.network.models.User;
import com.example.loaescuela.network.models.UserToken;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;

public interface APIService {

    //PLANILLAS_ALUMNOS

    @POST("planillas_alumnos.php")
    Observable<Response<PlanillaAlumno>> postAlumnoPlanilla(@Body PlanillaAlumno c);

    //PLANILLAS_PRESENTES

    @GET("planillas_presentes.php")
    Observable<Response<List<ReportPresent>>> getAssistsByStudent(@Query("method") String method, @Query("page") Integer page, @Query("id") Long alumno_id);

    @GET("planillas_presentes.php")
    Observable<Response<List<ReportResumAsist>>> getAssistsResumByDay(@Query("method") String method, @Query("page") Integer page);

    @POST("planillas_presentes.php")
    Observable<Response<PlanillaPresente>> postPresentePlanilla(@Body PlanillaPresente c);

    @PUT("planillas_presentes.php")
    Observable<Response<PlanillaPresente>> putPresentePlanilla(@Body PlanillaPresente c);

    @DELETE("planillas_presentes.php")
    Observable<ResponseBody>  deletePlanillaPresente(@Query("id") Long id);


    //CURSOS

    @POST("class_courses.php")
    Observable<Response<ClassCourse>> postClassCourse(@Body ClassCourse c);

    @GET("class_courses.php")
    Observable<Response<List<ReportClassCourse>>> getCoursesByStudentId(@Query("method") String method, @Query("student_id") Long id);

    @GET("class_courses.php")
    Observable<Response<List<ReportClassCourse>>> getCourseById(@Query("method") String method, @Query("student_id") Long id,  @Query("course_id") Long course_id);




    //INCOMES
    @POST("incomes_class_courses.php")
    Observable<Response<Income>> postIncomeCourse(@Body DataIncomeCourse inc);

    @GET("incomes.php")
    Observable<Response<List<ReportIncomeStudent>>> getAllIncomes(@Query("method") String method, @Query("page") Integer page);




    @DELETE("incomes.php")
    Observable<ResponseBody>  deleteIncome(@Query("id") Long id);

    @PUT("incomes.php")
    Observable<Response<Income>> putIncome(@Body Income income);


    //PLANILLAS
    @GET("planillas.php")
    Observable<Response<List<Planilla>>> getPlanillas(@Query("method") String method ,@Query("page") Integer page );

    @POST("planillas.php")
    Observable<Response<Planilla>> postPlanilla(@Body Planilla c);

    @DELETE("planillas.php")
    Observable<ResponseBody>  deletePlanilla(@Query("id") Long id);

    //STUDENTS

    @POST("students.php")
    Observable<Response<Student>> postStudent(@Body Student c);

    @GET("students.php")
    Observable<Response<List<Student>>> getStudents(@Query("page") Integer page, @Query("query") String query, @Query("method") String method , @Query("category") String category, @Query("order") String orderby );

    @GET("students.php")
    Observable<Response<ReportStudentAsist>> getStudentsAsists(@Query("method") String method , @Query("query") String query, @Query("page") Integer page, @Query("category") String category, @Query("categoria") String categoria, @Query("subcategoria") String subcategoria ,@Query("date") String datepresent, @Query("onlyPresents") String onlyP );

    @GET("students.php")
    Observable<Response<ReportStudentValue>> getStudentsValues(@Query("method") String method , @Query("category") String category, @Query("categoria") String categoria, @Query("subcategoria") String subcategoria , @Query("date") String datepresent );


    //USERS
    @GET("login.php")
    Observable<Response<UserToken>> login(@Query("name") String name, @Query("hash_password") String password,
                                          @Query("method") String method);

    @POST("login.php")
    Observable<Response<User>> register(@Body User u, @Query("key_access") String key, @Query("method") String method);

}
