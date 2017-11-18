package lyjak.anna.inzynierka.view.fragments.dummy;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import lyjak.anna.inzynierka.service.model.realm.PlannedRoute;
import lyjak.anna.inzynierka.service.model.realm.PointOfRoute;
import lyjak.anna.inzynierka.service.model.realm.RealmLocation;
import lyjak.anna.inzynierka.view.listeners.FindDirectionListener;
import lyjak.anna.inzynierka.service.respository.OnMarkersOperations;
import lyjak.anna.inzynierka.viewmodel.others.RoadFinder;
import lyjak.anna.inzynierka.viewmodel.others.RouteBeetweenTwoPointsDTO;

public class TempCreatePolyline implements FindDirectionListener {

    private static final String TAG = TempCreatePolyline.class.getSimpleName();
//    private GoogleMap mMap;

//    private String km; // the number of kilometers TextView
//    private String dt; // full time to overcome actuall route TextView

    public int fulldistance = 0; // the number of kilometers variable
    public int fullduration = 0; // full time to overcome actuall route variable

    private List<MarkerOptions> originMarkers = new ArrayList<>(); // temporary list of started markers
    private List<MarkerOptions> destinationMarkers = new ArrayList<>(); //temporary list of destiation markers
    private List<Polyline> polylinePaths = new ArrayList<>(); // temporary list of polylines

    private List<List<MarkerOptions>> allOriginMarkers = new ArrayList<>(); // list of all started markers
    private List<List<MarkerOptions>> alldestinationMarkers = new ArrayList<>(); // list of all destination markers
    private List<PolylineOptions> allPolylineOptions = new ArrayList<>(); // list of all polylinesOptions

    private List<List<Polyline>> allpolylinePaths = new ArrayList<>(); // list of all polylines

    private PlannedRoute route;
    private Activity activity;

    public TempCreatePolyline(PlannedRoute route, Activity activity) {
        this.route = route;
        this.activity = activity;
    }

    @Override
    public void onStartFindDirection() {
        Log.i(TAG, "onStartFindDirection");
        // removeRoute origin, destination and polyline markers between 2 points of route
        // and put it into one list for all route
        if (originMarkers != null) {
            allOriginMarkers.add(originMarkers);
//            for (MarkerOptions marker : originMarkers) {
//                marker.removeRoute(); removeRoute from map
//            }
        }
        if (destinationMarkers != null) {
            alldestinationMarkers.add(destinationMarkers);
//            for (MarkerOptions marker : destinationMarkers) {
//                marker.removeRoute();
//            }
        }
        if (polylinePaths != null) {
            allpolylinePaths.add(polylinePaths);
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
        Log.i(TAG, "onStartFindDirection ended");
    }

    @Override
    public void onSucceedFindDirection(List<RouteBeetweenTwoPointsDTO> routes) {
        Log.i(TAG, "onSucceedFindDirection");
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (RouteBeetweenTwoPointsDTO route : routes) {
            fulldistance += route.getDistance();
            fullduration += route.getDuration();
            originMarkers.add(new MarkerOptions().title(route.getStartPlaceName())
                    .position(route.getStartPoint()) // .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
            );
            // if(routes.get(routes.size()-1)==route) {
            destinationMarkers.add(new MarkerOptions()
                    .title(route.getEndPlaceName())  //.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                    .position(route.getEndPoint()));
            // }

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).width(10); //    color(getResources().getColor(R.color.colorPrimary)).

            allPolylineOptions.add(polylineOptions);

            for (int i = 0; i < route.getSizeOfRoutePoints(); i++)
                polylineOptions.add(route.getLatLngFromRoutesPoints(i));

//            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }

        int hour = (fullduration / 60)/60;
        int min = fullduration / 60;
        String distance = String.valueOf(fulldistance);
//        setTextViews(hour, min, distance);
//        helpCounter--;
        Log.i(TAG, "onSucceedFindDirection ended");
    }

    @Override
    public void onStoreFindDirection() {
        Log.i(TAG, "onStoreFindDirection");
        OnMarkersOperations operations = new OnMarkersOperations(activity);
        operations.storePlannedRouteInDatabase(route, this);
        Log.i(TAG, "onStoreFindDirection end");
    }

    /*
    The method of amending Polyline to LatLng's list
     */
    public List<LatLng> getLatLngFromPolyline(List<PolylineOptions> polylines){
        ArrayList<LatLng> list = new ArrayList<>();

        for(PolylineOptions poly : polylines){
            list.addAll(poly.getPoints());
        }
        return list;
    }

    public List<Polyline> mergeAllPolylinesIntoOne() {
        List<Polyline> result = new ArrayList<>();

        for(List<Polyline> list : allpolylinePaths) {
            for(Polyline polylineInLis : list) {
                result.add(polylineInLis);
            }
        }
        return result;
    }


    /**
     * The method selects the currently selected route and creates polyline using RoadFinder
     */
    public void createPolylinePlannedRoute(PlannedRoute route){
        RealmList<PointOfRoute> points = route.getPoints();
        if(points.size() >= 2){
            RealmLocation start = points.get(0).getPoint();
            for(int i = 1; i < points.size(); i++) {
                RealmLocation end = points.get(i).getPoint();
                try {
                    new RoadFinder(this, new LatLng(start.getLatitude(), start.getLongitude()),
                            new LatLng(end.getLatitude(), end.getLongitude())).execute();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error in creating polyline: " + e.getMessage());
                }
                start = points.get(i).getPoint();
            }
        }
    }

    public List<PolylineOptions> getAllPolylineOptions() {
        return allPolylineOptions;
    }
}
