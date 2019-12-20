package com.canadainformation.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.canadainformation.R;
import com.canadainformation.base.BaseView;
import com.canadainformation.di.scopes.ActivityScoped;
import com.canadainformation.utils.ItemDecoration;
import com.canadainformation.view.adapter.InformationAdapter;
import com.canadainformation.view.information.InfoItem;
import com.canadainformation.view.information.InformationUiModel;
import com.canadainformation.view.information.NoInfoModel;
import com.canadainformation.view.viewmodel.InformationViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created By Akash
 * on 17,Dec,2019 : 2:47 PM
 */
@ActivityScoped
public class InformationFragment extends Fragment implements BaseView {

    private static final String TAG = InformationFragment.class.getSimpleName();

    private InformationViewModel mViewModel;

    private InformationAdapter mInformationAdapter;

    private LinearLayout mNoInformationView;

    private TextView mNoInformationMainView;

    private CompositeDisposable mSubscription = new CompositeDisposable();

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    public InformationFragment() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(InformationViewModel.class);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_information, container, false);

        mInformationAdapter = new InformationAdapter(new ArrayList<>());

        // Set up Information view
        RecyclerView recyclerView = root.findViewById(R.id.rv_info_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemDecoration itemDecoration=new ItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_margin),1);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(mInformationAdapter);

        setupNoInformationView(root);
        setupSwipeRefreshLayout(root);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        bindViewModel();
    }

    @Override
    public void onPause() {
        super.onPause();
        unbindViewModel();
    }

    private void setupNoInformationView(View view) {
        mNoInformationView = view.findViewById(R.id.noInformationLayout);
        mNoInformationMainView = view.findViewById(R.id.noInformationFoundTextView);

    }

    private void setupSwipeRefreshLayout(View view){

        SwipeRefreshLayout swipeRefreshLayout=view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors( ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.colorPrimary),
                ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.colorAccent),
                ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.colorPrimaryDark));

        swipeRefreshLayout.setOnRefreshListener(this::forceUpdate);
    }

    private void forceUpdate() {
        mSubscription.add(mViewModel.getUiModel(true)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        //onNext
                        this::updateView,
                        //onError
                        error -> Log.d(TAG, "Error loading Information")
                ));
    }

    private void updateView(InformationUiModel model){
        int ratesListVisibility = model.ismIsNoInfoAvailable() ? View.VISIBLE : View.GONE;
        int noInformaionViewVisibility = model.ismIsNoInfoViewVisible() ? View.VISIBLE : View.GONE;
        mNoInformationView.setVisibility(noInformaionViewVisibility);

        if (model.ismIsNoInfoAvailable()) {
            showInformation(model.getmItemList());
        }
        if (model.ismIsNoInfoViewVisible() && model.getmNoInfoModel() != null) {
            showNoInformation(model.getmNoInfoModel());
        }
    }


    private void showNoInformation(NoInfoModel model) {
        mNoInformationMainView.setText(model.getText());
    }

    private void showInformation(List<InfoItem> itemList) {
        mInformationAdapter.setInfoItemList(itemList);
    }

    private void showSnackbar(@StringRes int message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    private void setLoadingIndicatorVisibility(final boolean isVisible) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl = getView().findViewById(R.id.swipe_refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(() -> srl.setRefreshing(isVisible));
    }

    @Override
    public void bindViewModel() {

        // using a CompositeDisposable to gather all the subscriptions, so all of them can be
        // later unsubscribed together
        mSubscription = new CompositeDisposable();

        // The ViewModel holds an observable containing the state of the UI.
        // subscribe to the emissions of the Ui Model
        // update the view at every emission of the Ui Model
        mSubscription.add(mViewModel.getUiModel(false)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        //onNext
                        this::updateView,
                        //onError
                        error -> Log.d(TAG, "Error loading information")
                ));

        // subscribe to the emissions of the snackbar text
        // every time the snackbar text emits, show the snackbar
        mSubscription.add(mViewModel.getSnackbarMessage()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        //onNext
                        this::showSnackbar,
                        //onError
                        error -> Log.d(TAG, "Error showing snackbar", error)
                ));

        // subscribe to the emissions of the loading indicator visibility
        // for every emission, update the visibility of the loading indicator
        mSubscription.add(mViewModel.getLoadingIndicatorVisibility()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        //onNext
                        this::setLoadingIndicatorVisibility,
                        //onError
                        error -> Log.d(TAG, "Error showing loading indicator", error)
                ));



    }

    @Override
    public void unbindViewModel() {

    }
}
