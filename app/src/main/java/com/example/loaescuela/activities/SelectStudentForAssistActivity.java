package com.example.loaescuela.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loaescuela.CustomLoadingListItemCreator;
import com.example.loaescuela.Interfaces.OnSelectStudent;
import com.example.loaescuela.R;
import com.example.loaescuela.adapters.StudentAdapter;
import com.example.loaescuela.adapters.StudentToAssistAdapter;
import com.example.loaescuela.network.ApiClient;
import com.example.loaescuela.network.Error;
import com.example.loaescuela.network.GenericCallback;
import com.example.loaescuela.network.models.Student;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemSpanLookup;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SelectStudentForAssistActivity extends BaseActivity implements Paginate.Callbacks, OnSelectStudent {

    private RecyclerView mRecyclerView;
    private StudentToAssistAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //pagination
    private boolean loadingInProgress;
    private Integer mCurrentPage;
    private Paginate paginate;
    private boolean hasMoreItems;
    private String mQuery = "";
    private String token = "";

    private LinearLayout button;
    private StickyRecyclerHeadersDecoration headersDecor;

    private LinearLayout home;

    private LinearLayout bottomSheet;
    private LinearLayout escuelaFilter;
    private LinearLayout coloniaFilter;
    private LinearLayout highschoolFilter;
    private LinearLayout allFilter;
    private String mCategory = "todos";

    private Long mPlanillaId = -1l;
    private String mCategoria = "";
    private String mSubCategoria = "";

    private Button orderByCreated;
    private Button orderByName;
    private String mOrderBy;
    private String typeActivity;
    private String fragmentCategory;
    private String paymentPlace;

    private LinearLayout add;

    @Override
    public void onSelectStudent(Long id, String name, String surname, String category){

        if(typeActivity.equals("PAGOS")){
            System.out.println(mAdapter.getPaymentPlace());
            System.out.println(mAdapter.getPaymentPlace());
            AssistsCoursesIncomesByStudentActivity.start(this, id, name, surname, category, typeActivity, mAdapter.getPaymentPlace());
        }

        Intent intent=new Intent();
        intent.putExtra("CATEGORIA", category);
        intent.putExtra("SUBCATEGORIA", mSubCategoria);
        intent.putExtra("STUDENTID", id);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.list_student_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        home = findViewById(R.id.line_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        mPlanillaId = getIntent().getLongExtra("ID", -1);
        mCategoria = getIntent().getStringExtra("CATEGORIA");
        mSubCategoria = getIntent().getStringExtra("SUBCATEGORIA");
        typeActivity = getIntent().getStringExtra("TYPE");
        fragmentCategory = getIntent().getStringExtra("FRAGMENTCATEGORY");
        paymentPlace = getIntent().getStringExtra("PAYMENTPLACE");

        System.out.println("acaaa");
        System.out.println(paymentPlace);

        mRecyclerView = findViewById(R.id.list_users);
        layoutManager = new LinearLayoutManager(this    );
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new StudentToAssistAdapter(this, new ArrayList<Student>());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnSelectStudent(this);
        mAdapter.setCategoryActivity(getIntent().getStringExtra("CATEGORIA"));
        mAdapter.setTypeActivity(typeActivity);
        mAdapter.setPaymentPLace(paymentPlace);

        mAdapter.setPlanillaId(mPlanillaId);

        button = findViewById(R.id.fab);
        orderByCreated = findViewById(R.id.orderClientByCreated);
        orderByName = findViewById(R.id.orderClientBy);
        mOrderBy = "";

        final SearchView searchView= findViewById(R.id.searchView);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconified(false);
            }
        });

        searchView.setQueryHint("Buscar");
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                mCurrentPage = 0;
                mAdapter.clear();
                listClients("");
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.trim().toLowerCase().equals(mQuery)) {
                    mCurrentPage = 0;
                    mAdapter.clear();
                    listClients(newText.trim().toLowerCase());
                }
                return false;
            }
        });

        orderByCreated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderBy = "created";
                clearView();
            }
        });

        orderByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderBy = "nombre";
                clearView();
            }
        });


        implementsPaginate();
/*
        headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
        mRecyclerView.addItemDecoration(headersDecor);

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });*/

        bottomSheet = this.findViewById(R.id.bottomSheet);
        //trae los empleados

        topBarListener(bottomSheet);

        add = findViewById(R.id.add_new_student);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), CreateStudentActivity.class));
            }
        });
    }

    private void topBarListener(View bottomSheet){
        escuelaFilter = bottomSheet.findViewById(R.id.escuelaFilter);
        coloniaFilter = bottomSheet.findViewById(R.id.coloniaFilter);
        highschoolFilter = bottomSheet.findViewById(R.id.highschoolFilter);
        allFilter = bottomSheet.findViewById(R.id.all);

        allFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategory = "todos";
                clearView();
            }
        });

        escuelaFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategory = "escuela";
                clearView();
            }
        });

        coloniaFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategory = "colonia";
                clearView();
            }
        });

        highschoolFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategory = "highschool";
                clearView();
            }
        });
    }

    private void clearView(){
        mCurrentPage = 0;
        mAdapter.clear();
        hasMoreItems=true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isLoading()) {
            mCurrentPage = 0;
            mAdapter.clear();
            hasMoreItems=true;
        }
    }

    public void listClients(final String query){
        loadingInProgress=true;
        this.mQuery = query;
        final String newToken = UUID.randomUUID().toString();
        this.token =  newToken;

        ApiClient.get().getStudents(query, mCurrentPage, mCategory, mOrderBy,new GenericCallback<List<Student>>() {
            @Override
            public void onSuccess(List<Student> data) {
                if(token.equals(newToken)){
                    Log.e("TOKEN", "Llega token: " + newToken);
                    System.out.println("IMPRIME"+mCurrentPage+" data size "+data.size());
                    if (query == mQuery) {

                        if (data.size() == 0) {
                            hasMoreItems = false;
                        }else{
                            int prevSize = mAdapter.getItemCount();
                            mAdapter.pushList(data);
                            mCurrentPage++;
                            if(prevSize == 0){
                                layoutManager.scrollToPosition(0);
                            }
                        }
                        loadingInProgress = false;
                    }
                }else{
                    Log.e("TOKEN", "Descarta token: " + newToken);
                }
            }

            @Override
            public void onError(Error error) {
                loadingInProgress = false;
            }
        });
    }

    private void implementsPaginate(){

        loadingInProgress=false;
        mCurrentPage=0;
        hasMoreItems = true;

        paginate= Paginate.with(mRecyclerView, this)
                .setLoadingTriggerThreshold(2)
                .addLoadingListItem(true)
                .setLoadingListItemCreator(new CustomLoadingListItemCreator())
                .setLoadingListItemSpanSizeLookup(new LoadingListItemSpanLookup() {
                    @Override
                    public int getSpanSize() {
                        return 0;
                    }
                })
                .build();
    }

    @Override
    public void onLoadMore() {
        listClients(mQuery);
    }

    @Override
    public boolean isLoading() {
        return loadingInProgress;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return !hasMoreItems;
    }

}
