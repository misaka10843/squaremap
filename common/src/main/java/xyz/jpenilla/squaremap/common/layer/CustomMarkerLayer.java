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
import java.awt.Color;
import java.util.stream.Collectors;
import xyz.jpenilla.squaremap.common.config.MarkerConfig;

@DefaultQualifier(NonNull.class)
public final class CustomMarkerLayer implements LayerProvider {
    private final MarkerConfig.LayerConfig config;
    private final List<Marker> markers;

    public CustomMarkerLayer(final MarkerConfig.LayerConfig config) {
        this.config = config;
        this.markers = new ArrayList<>();
        for (final MarkerConfig.MarkerEntry entry : config.markers()) {
            final Marker marker;
            if ("icon".equalsIgnoreCase(entry.type())) {
                if (entry.point() == null || entry.point().size() < 2 || entry.icon() == null) continue;
                final Point point = Point.of(entry.point().get(0), entry.point().get(1));
                final Key iconKey = Key.of(entry.icon());
                final int sizeX = entry.sizeX() != null ? entry.sizeX() : (entry.size() != null ? entry.size() : 16);
                final int sizeZ = entry.sizeZ() != null ? entry.sizeZ() : (entry.size() != null ? entry.size() : 16);
                marker = Marker.icon(point, iconKey, sizeX, sizeZ);
            } else if ("circle".equalsIgnoreCase(entry.type())) {
                if (entry.point() == null || entry.point().size() < 2 || entry.radius() == null) continue;
                marker = Marker.circle(Point.of(entry.point().get(0), entry.point().get(1)), entry.radius());
            } else if ("polyline".equalsIgnoreCase(entry.type())) {
                if (entry.points() == null || entry.points().isEmpty()) continue;
                marker = Marker.polyline(entry.points().stream().map(p -> Point.of(p.get(0), p.get(1))).collect(Collectors.toList()));
            } else if ("polygon".equalsIgnoreCase(entry.type())) {
                if (entry.points() == null || entry.points().isEmpty()) continue;
                marker = Marker.polygon(entry.points().stream().map(p -> Point.of(p.get(0), p.get(1))).collect(Collectors.toList()));
            } else if ("rectangle".equalsIgnoreCase(entry.type())) {
                if (entry.point1() == null || entry.point1().size() < 2 || entry.point2() == null || entry.point2().size() < 2) continue;
                marker = Marker.rectangle(Point.of(entry.point1().get(0), entry.point1().get(1)), Point.of(entry.point2().get(0), entry.point2().get(1)));
            } else {
                continue;
            }

            final MarkerOptions.Builder options = MarkerOptions.builder();
            if (entry.tooltip() != null) {
                options.hoverTooltip(entry.tooltip());
            }

            if (entry.style() != null) {
                final MarkerConfig.MarkerStyle style = entry.style();
                if (style.strokeColor() != null) options.strokeColor(Color.decode(style.strokeColor()));
                if (style.strokeOpacity() != null) options.strokeOpacity(style.strokeOpacity());
                if (style.strokeWeight() != null) options.strokeWeight(style.strokeWeight());
                if (style.fillColor() != null) options.fillColor(Color.decode(style.fillColor()));
                if (style.fillOpacity() != null) options.fillOpacity(style.fillOpacity());
            }

            marker.markerOptions(options.build());
            this.markers.add(marker);
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
