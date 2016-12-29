package mobi.kujon.utils.geocoding;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

/**
 *
 */

public class GeocodeTask extends AsyncTask<Void,Void,Void> {
    private WeakReference<OnLocationSeted> reference;
    private String addressLine;

    private LatLng latLng;
    public GeocodeTask(WeakReference<OnLocationSeted> reference, String addressLine) {
        this.reference = reference;
        this.addressLine = addressLine;
    }



    @Override
    protected Void doInBackground(Void... params) {
        if(reference.get() != null){
            Geocoder geocoder = new Geocoder(reference.get().getContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocationName(addressLine,1);
                if(addresses.size()>0){
                   double lat = addresses.get(0).getLatitude();
                   double lng = addresses.get(0).getLongitude();
                   this.latLng = new LatLng(lat,lng);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(reference.get() !=null){
            if(latLng !=null){
                reference.get().setLatLng(latLng);
            }else {
                reference.get().cantSetLocation();
            }
        }
    }
}
