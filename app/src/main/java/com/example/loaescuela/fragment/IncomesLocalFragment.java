package com.example.loaescuela.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loaescuela.CustomLoadingListItemCreator;
import com.example.loaescuela.R;
import com.example.loaescuela.activities.GeneralAssistActivity;
import com.example.loaescuela.activities.IncomesActivity;
import com.example.loaescuela.activities.SelectStudentForAssistActivity;
import com.example.loaescuela.activities.StudentsListActivity;
import com.example.loaescuela.adapters.IncomeStudentAdapter;
import com.example.loaescuela.network.ApiClient;
import com.example.loaescuela.network.Error;
import com.example.loaescuela.network.GenericCallback;
import com.example.loaescuela.network.models.ReportIncomeStudent;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemSpanLookup;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

public class IncomesLocalFragment extends BaseFragment implements Paginate.Callbacks {

    private RecyclerView mRecyclerView;
    private IncomeStudentAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private View mRootView;

    //pagination
    private boolean loadingInProgress;
    private Integer mCurrentPage;
    private Paginate paginate;
    private boolean hasMoreItems;

    private String selectedDate;
    private LinearLayout home;
    private LinearLayout add;

    private String mQuery = "";
    private String paymentPlace = "negocio";

    public void refreshList(Long student_id){
        clearView();
        //Toast.makeText(getContext(), " crear cuadro dialogo con id"+student_id,Toast.LENGTH_LONG).show();

    }

    public void onClickButton(){
        ((IncomesActivity) requireActivity()).startAddStudentActivity("negocio");
    }

    public int getVisibility(){
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.incomes_day_activity, container, false);

        home= mRootView.findViewById(R.id.line_home);
        paymentPlace = "negocio";

        mRecyclerView = mRootView.findViewById(R.id.list_events);
        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new IncomeStudentAdapter(getContext(), new ArrayList<ReportIncomeStudent>());
        mRecyclerView.setAdapter(mAdapter);

        //STICKY
        // Add the sticky headers decoration
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
        mRecyclerView.addItemDecoration(headersDecor);


        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });

        registerForContextMenu(mRecyclerView);

        implementsPaginate();

        return mRootView;
    }


    private void clearView(){
        mCurrentPage = 0;
        mAdapter.clear();
        hasMoreItems=true;
    }

    private void clearAndList(){
        clearView();
        listEvents();
    }

    private void listEvents(){

        loadingInProgress=true;
        ApiClient.get().getAllIncomes(mCurrentPage, paymentPlace, new GenericCallback<List<ReportIncomeStudent>>() {
            @Override
            public void onSuccess(List<ReportIncomeStudent> data) {

                System.out.println("aca");
                System.out.println(data.size());

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
        listEvents();
    }

    @Override
    public boolean isLoading() {
        return loadingInProgress;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return !hasMoreItems;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                //setResult(RESULT_CANCELED);
                //finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
