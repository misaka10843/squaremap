package xyz.jpenilla.squaremap.common.config;

import io.leangen.geantyref.TypeToken;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.common.data.DirectoryProvider;

@DefaultQualifier(NonNull.class)
public final class MarkerConfig extends AbstractConfig {
    private static MarkerConfig instance;

    public MarkerConfig(final Path dataDirectory) {
        super(dataDirectory, MarkerConfig.class, "markers.yml", 1);
    }

    public static void reload(final DirectoryProvider directoryProvider) {
        instance = new MarkerConfig(directoryProvider.dataDirectory());
        instance.readConfig(MarkerConfig.class, instance);
    }

    public static MarkerConfig instance() {
        return instance;
    }

    private void settings() {
        this.get(new TypeToken<Map<String, IconConfig>>() {}, "icons", Collections.emptyMap());
        this.get(new TypeToken<Map<String, List<LayerConfig>>>() {}, "worlds", Collections.emptyMap());
    }

    public Map<String, IconConfig> icons() {
        return this.get(new TypeToken<Map<String, IconConfig>>() {}, "icons", Collections.emptyMap());
    }

    public Map<String, List<LayerConfig>> worlds() {
        return this.get(new TypeToken<Map<String, List<LayerConfig>>>() {}, "worlds", Collections.emptyMap());
    }

    @ConfigSerializable
    public record IconConfig(
        String file,
        String url
    ) {}

    @ConfigSerializable
    public record LayerConfig(
        String label,
        boolean showControls,
        boolean defaultHidden,
        int layerPriority,
        int zIndex,
        List<MarkerEntry> markers
    ) {
        public LayerConfig {
            if (label == null) label = "Custom Layer";
        }
    }

    @ConfigSerializable
    public record MarkerEntry(
        String type,
        List<Double> point,      // For Icon, Circle
        List<List<Double>> points, // For Polyline, Polygon
        List<Double> point1,     // For Rectangle
        List<Double> point2,     // For Rectangle
        Double radius,           // For Circle
        String icon,             // For Icon
        Integer size,            // For Icon
        Integer sizeX,           // For Icon
        Integer sizeZ,           // For Icon
        String tooltip,
        MarkerStyle style
    ) {}

    @ConfigSerializable
    public record MarkerStyle(
        String strokeColor,
        Double strokeOpacity,
        Integer strokeWeight,
        String fillColor,
        Double fillOpacity
    ) {}
}
