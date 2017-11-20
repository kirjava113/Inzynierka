package lyjak.anna.inzynierka.viewmodel;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmResults;
import lyjak.anna.inzynierka.service.model.realm.PlannedRoute;
import lyjak.anna.inzynierka.service.respository.RouteService;

/**
 * Created by Anna on 19.11.2017.
 */

public class PlannedRoutesCardListViewModel extends MainViewModel {

    private List<PlannedRoute> dataset;

    public PlannedRoutesCardListViewModel(Context context) {
        super(context);
    }

    public List<PlannedRoute> getPlannedRoutesFromDatabase() {
        dataset = new ArrayList<>(service.getAllPlannedRoutes());
        return dataset;
    }

    public PlannedRoute getPlannedRoute(int position) {
        return dataset.get(position);
    }

    public int getDatasetSize() {
        if (dataset != null) {
            return dataset.size();
        } else {
            return 0;
        }
    }

    public void removePlannedRoute(int position) {
        PlannedRoute routeToRemove = getPlannedRoute(position);
        service.removePlannedRouteFromDatabase(routeToRemove);
    }

    public void removePlannedRoute(PlannedRoute routeToRemove) {
        service.removePlannedRouteFromDatabase(routeToRemove);
    }
}
