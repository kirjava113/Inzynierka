package lyjak.anna.inzynierka.view.callbacks;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.databinding.DialogAddingRouteToReportConfirmBinding;
import lyjak.anna.inzynierka.model.realmObjects.PlannedRoute;
import lyjak.anna.inzynierka.view.activities.MapsActivity;
import lyjak.anna.inzynierka.viewmodel.MapsViewModel;
import lyjak.anna.inzynierka.viewmodel.report.GenerateStandardReport;

/**
 * Created by Anna on 20.11.2017.
 */

public class ReportPlannedRouteCallback implements PlannedRouteCallback {

    Activity activity;
    private GenerateStandardReport generateStandardReport;

    public ReportPlannedRouteCallback(Activity activity, GenerateStandardReport generateStandardReport) {
        this.activity = activity;
        this.generateStandardReport = generateStandardReport;
    }

    @Override
    public void onClick(PlannedRoute route) {
        final Dialog dialog = new Dialog(activity, R.style.SettingsDialogStyle);
        LayoutInflater layoutInflater = LayoutInflater.from(activity.getApplicationContext());
        DialogAddingRouteToReportConfirmBinding viewDataBinding = DataBindingUtil
                .inflate(layoutInflater,
                        R.layout.dialog_adding_route_to_report_confirm,
                        null, false);
        viewDataBinding.buttonNo.setOnClickListener(v2 -> dialog.dismiss());
        viewDataBinding.buttonYes.setOnClickListener(v2 -> {
            dialog.dismiss();
            generateStandardReport.setPlannedRoute(route);
            Intent openMapIntent = new Intent(activity,
                    MapsActivity.class);
            MapsViewModel.report = generateStandardReport;
            Bundle bundle = new Bundle();
            bundle.putString("STANDARDREPORT", "@STANDARD_REPORT@");
            bundle.putBoolean("REPORT", true);
            openMapIntent.putExtras(bundle);
            activity.startActivity(openMapIntent);
        });
        dialog.setContentView(viewDataBinding.getRoot());
        dialog.show();
    }
}
