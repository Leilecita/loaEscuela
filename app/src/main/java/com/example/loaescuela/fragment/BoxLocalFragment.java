package com.example.loaescuela.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loaescuela.CustomLoadingListItemCreator;
import com.example.loaescuela.R;
import com.example.loaescuela.activities.IncomesActivity;
import com.example.loaescuela.adapters.BeachBoxAdapter;
import com.example.loaescuela.network.ApiClient;
import com.example.loaescuela.network.Error;
import com.example.loaescuela.network.GenericCallback;
import com.example.loaescuela.network.models.BeachBox;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemSpanLookup;

import java.util.ArrayList;
import java.util.List;

public class BoxLocalFragment extends BaseFragment implements Paginate.Callbacks {

    private RecyclerView mRecyclerView;
    private BeachBoxAdapter mAdapter;
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

    public void refreshList(Long student_id){
        clearView();
        //Toast.makeText(getContext(), " crear cuadro dialogo con id"+student_id,Toast.LENGTH_LONG).show();
    }

    public void onClickButton(){
        ((IncomesActivity) requireActivity()).startCreateBoxActivity("negocio");
    }

    public int getVisibility(){
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_box, container, false);

        home= mRootView.findViewById(R.id.line_home);

        mRecyclerView = mRootView.findViewById(R.id.list_box_month);
        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new BeachBoxAdapter(getContext(), new ArrayList<BeachBox>());
        mRecyclerView.setAdapter(mAdapter);

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
    }

    private void listEvents(){

        loadingInProgress=true;
        ApiClient.get().getBoxes(mCurrentPage, "negocio", "Todas",new GenericCallback<List<BeachBox>>() {
            @Override
            public void onSuccess(List<BeachBox> data) {

                System.out.println("aca estoyyy");
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

