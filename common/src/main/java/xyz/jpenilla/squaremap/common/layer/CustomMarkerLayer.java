package xyz.jpenilla.squaremap.common.layer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.LayerProvider;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;
import xyz.jpenilla.squaremap.common.config.MarkerConfig;

@DefaultQualifier(NonNull.class)
public final class CustomMarkerLayer implements LayerProvider {
    private final MarkerConfig.LayerConfig config;
    private final List<Marker> markers;

    public CustomMarkerLayer(final MarkerConfig.LayerConfig config) {
        this.config = config;
        this.markers = new ArrayList<>();
        for (final MarkerConfig.MarkerEntry entry : config.markers()) {
            if ("icon".equalsIgnoreCase(entry.type())) {
                if (entry.point() == null || entry.point().size() < 2 || entry.icon() == null) {
                    continue;
                }
                final Point point = Point.of(entry.point().get(0), entry.point().get(1));
                final Key iconKey = Key.of(entry.icon());
                final int sizeX = entry.sizeX() != null ? entry.sizeX() : (entry.size() != null ? entry.size() : 16);
                final int sizeZ = entry.sizeZ() != null ? entry.sizeZ() : (entry.size() != null ? entry.size() : 16);
                
                final Marker marker = Marker.icon(point, iconKey, sizeX, sizeZ);
                if (entry.tooltip() != null) {
                    marker.markerOptions(MarkerOptions.builder().hoverTooltip(entry.tooltip()).build());
                }
                this.markers.add(marker);
            }
            // Can add other marker types here if needed
        }
    }

    @Override
    public String getLabel() {
        return this.config.label();
    }

    @Override
    public boolean showControls() {
        return this.config.showControls();
    }

    @Override
    public boolean defaultHidden() {
        return this.config.defaultHidden();
    }

    @Override
    public int layerPriority() {
        return this.config.layerPriority();
    }

    @Override
    public int zIndex() {
        return this.config.zIndex();
    }

    @Override
    public Collection<Marker> getMarkers() {
        return this.markers;
    }
}
