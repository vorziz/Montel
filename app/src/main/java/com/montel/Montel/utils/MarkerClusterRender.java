package com.montel.Montel.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.montel.Montel.R;

public class MarkerClusterRender extends DefaultClusterRenderer<MarkerClusterItem> {

    private static final int MARKER_DIMENSION = 0;

    private final IconGenerator iconGenerator;
    private final ImageView markerImageView;
    Context contexts;


    public MarkerClusterRender(Context context, GoogleMap map, ClusterManager<MarkerClusterItem> clusterManager) {
        super(context, map, clusterManager);

        contexts = context;

        iconGenerator = new IconGenerator(context);  // 3
        markerImageView = new ImageView(context);
        markerImageView.setLayoutParams(new ViewGroup.LayoutParams(MARKER_DIMENSION, MARKER_DIMENSION));
        iconGenerator.setContentView(markerImageView);  // 4
    }

    @Override
    protected void onBeforeClusterItemRendered(MarkerClusterItem item, MarkerOptions markerOptions) { // 5
        markerImageView.setImageResource(R.drawable.marker_montel_02);  // 6
        Bitmap icon = iconGenerator.makeIcon();  // 7

        int height = 150;
        int width = 150;
         Bitmap b = BitmapFactory.decodeResource(contexts.getResources(), R.drawable.marker_montel_02);
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);


        markerOptions.icon(smallMarkerIcon);  // 8
        markerOptions.title(item.getTitle());
    }


}
