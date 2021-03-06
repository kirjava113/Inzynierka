package lyjak.anna.inzynierka.view.callbacks;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.databinding.DialogActualRouteCardClickBinding;
import lyjak.anna.inzynierka.model.realmObjects.Route;
import lyjak.anna.inzynierka.view.activities.MainActivity;
import lyjak.anna.inzynierka.view.activities.MapsActivity;
import lyjak.anna.inzynierka.view.fragments.TransportSelectionFragment;
import lyjak.anna.inzynierka.viewmodel.ActualRouteListViewModel;
import lyjak.anna.inzynierka.viewmodel.MapsViewModel;
import lyjak.anna.inzynierka.viewmodel.report.GenerateActualRouteReport;

/**
 *
 * Created by Anna on 19.11.2017.
 */

public class ListActualRouteCallback implements ActualRouteCallback {

    private Activity activity;
    private ActualRouteListViewModel viewModel;

    public ListActualRouteCallback(Activity activity, ActualRouteListViewModel viewModel) {
        this.activity = activity;
        this.viewModel = viewModel;
    }

    @Override
    public void onClick(Route route) {
        final Dialog dialog = new Dialog(activity, R.style.SettingsDialogStyle);
        LayoutInflater layoutInflater = LayoutInflater.from(activity.getApplicationContext());
        DialogActualRouteCardClickBinding viewDataBinding = DataBindingUtil
                .inflate(layoutInflater,
                        R.layout.dialog_actual_route_card_click,
                        null, false);
        viewDataBinding.buttonGenerateReport.setOnClickListener(v2 -> {
            dialog.dismiss();
            final TransportSelectionFragment fragment = TransportSelectionFragment
                    .newInstance(route);
            MainActivity.attachNewFragment(fragment);
        });
        viewDataBinding.buttonGenerateAcctualRouteReport.setOnClickListener(v -> {
            dialog.dismiss();

            GenerateActualRouteReport report = new GenerateActualRouteReport(route);
            Intent openMapIntent = new Intent(activity,
                    MapsActivity.class);
            MapsViewModel.reportAcctualRoute = report;
            Bundle bundle = new Bundle();
            bundle.putString("title", "@ACTUALL_ROUTE@");
            bundle.putBoolean("REPORT", true);
            openMapIntent.putExtras(bundle);
            activity.startActivity(openMapIntent);
        });
        viewDataBinding.buttonShowRouteOnMap.setOnClickListener(v12 -> {
            dialog.dismiss();
            Intent openMapIntent = new Intent(dialog.getContext(),
                    MapsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("title", "@ACTUALL_ROUTE@");
            bundle.putBoolean("editActuallRoute", true);
            bundle.putLong("date", route.getDate().getTime());
            if (route.getStartDate() != null) {
                bundle.putLong("startDate", route.getStartDate().getTime());
            }
            if (route.getEndDate() != null) {
                bundle.putLong("endDate", route.getEndDate().getTime());
            }
            openMapIntent.putExtras(bundle);
            activity.startActivity(openMapIntent);
        });
        viewDataBinding.buttonDeleteRoute.setOnClickListener(view2 -> {
            dialog.dismiss();
            viewModel.removeRoute(route);
            if (activity instanceof MainActivity) {
                ((MainActivity) activity).notyfyDataSetChange();
            }
        });
        viewDataBinding.buttonAnuluj.setOnClickListener(v2 -> dialog.dismiss());

        dialog.setContentView(viewDataBinding.getRoot());
        dialog.show();
    }

}
