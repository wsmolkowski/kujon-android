package mobi.kujon.utils.geocoding;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

/**
 *
 */

public interface OnLocationSeted {
    Context getContext();
    void setLatLng(LatLng latLng);
    void cantSetLocation();
}
