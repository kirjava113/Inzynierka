package lyjak.anna.inzynierka.view.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.databinding.FragmentPointsBinding;
import lyjak.anna.inzynierka.model.realmObjects.PlannedRoute;
import lyjak.anna.inzynierka.view.adapters.PointsAdapter;
import lyjak.anna.inzynierka.viewmodel.PointsCardListViewModel;
import lyjak.anna.inzynierka.viewmodel.listeners.OnStartDragListener;
import lyjak.anna.inzynierka.view.callbacks.SimpleItemTouchHelperCallback;

public class PointsFragment extends Fragment implements OnStartDragListener {

    private FragmentPointsBinding binding;
    private PointsAdapter mAdapter;
    private PointsCardListViewModel viewModel;

    private ItemTouchHelper mItemTouchHelper;

    public PointsFragment() {
        // Required empty public constructor
    }

    public static PointsFragment newInstance(PlannedRoute plannedRoute) {
        PointsFragment fragment = new PointsFragment();
        fragment.viewModel = new PointsCardListViewModel(fragment.getActivity(), plannedRoute);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_points, container, false);
        if (viewModel != null) {
            mAdapter = new PointsAdapter(this, viewModel);

            final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            binding.recycleViewPoints.setLayoutManager(mLayoutManager);
            binding.recycleViewPoints.setItemAnimator(new DefaultItemAnimator());
            binding.recycleViewPoints.setAdapter(mAdapter);
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle save) {
        super.onViewCreated(view, save);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(binding.recycleViewPoints);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
