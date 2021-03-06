package lyjak.anna.inzynierka.viewmodel.report;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.File;

import lyjak.anna.inzynierka.model.realmObjects.PlannedRoute;
import lyjak.anna.inzynierka.model.modelDTO.PlannedRouteForReportDTO;

/**
 *
 * Created by Anna on 25.11.2017.
 */

public class GeneratePlannedRouteReport {

    private static final String TAG = GenerateActualRouteReport.class.getSimpleName();
    private PlannedRoute plannedRoute;

    public GeneratePlannedRouteReport(PlannedRoute plannedRoute) {
        this.plannedRoute = plannedRoute;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createPdf(Activity activity, Bitmap bitmap) {
        String targetPdf = "/LogMilesPlannedRouteRaport" + System.currentTimeMillis() + ".pdf";
        GeneratePdf pdf = new GeneratePdf(activity);
        pdf.setBitmap(bitmap);
        PlannedRouteForReportDTO prfr = PlannedRouteForReportDTO.getInstance(plannedRoute);
        ProgressDialog progressDialog = ProgressDialog.show(activity,
                "Please wait ...",  "Task in progress ...", true);
        progressDialog.setCancelable(true);
        new Thread(() -> {
            try {
                File savedFile = pdf.generatePlannedRouteReport(prfr, targetPdf);
                sendFile(activity, savedFile);
            } catch (Exception e) {
                Log.e("error: ", e.getMessage());
            }
            progressDialog.dismiss();
        }).start();
    }

    private void sendFile(Activity activity, File file) {
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        if(file.exists()) {
            intentShareFile.setType("application/pdf");
            Log.i(TAG, file.getAbsolutePath());
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file.getAbsolutePath()));

            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Raport");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Witam, przesyłam raport zaplanowanej trasy. Z poważaniem, ");

            activity.startActivity(Intent.createChooser(intentShareFile, "Share File"));
        } else {
            Log.i(TAG, "Plik nie istnieje");
        }
    }

    public PlannedRoute getPlannedRoute() {
        return plannedRoute;
    }
}
