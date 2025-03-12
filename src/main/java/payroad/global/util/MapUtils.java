package payroad.global.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import payroad.domain.consumption.Consumption;

public abstract class MapUtils {

    private static final GeometryFactory geometryFactory = new GeometryFactory();
    private static final int TILE_SIZE = 256;


    public static List<Consumption> getConsumptionByDistance(List<Consumption> consumptions,
        double radius, double latitude, double longitude
    ) {
        // 기준점 (서울, 위도/경도)
        Coordinate centerCoord = new Coordinate(longitude, latitude);
        Point centerPoint = geometryFactory.createPoint(centerCoord);

        // 반경 (단위: degrees, WGS84 사용 시 1도 ≈ 111km)
        // 0.1은 대략 11km 반경
        double radiusDegrees = radius / 111.0;
        // 반경 내의 영역 (Polygon 생성)
        Geometry buffer = centerPoint.buffer(radiusDegrees);

        return consumptions.stream()
            .filter(consumption -> buffer.contains(consumption.getMapEntity().getLocation()))
            .collect(Collectors.toList());
    }

    public static int[] getTileCoordinates(double latitude, double longitude, int zoom) {
        double scale = 1 << zoom;
        int x = (int) Math.floor((longitude + 180.0) / 360.0 * Math.pow(2, scale));
        int y = (int) Math.floor((1 - Math.log(Math.toRadians(latitude)) +
            1 / Math.cos((Math.toRadians(latitude))) / Math.PI) / 2 * Math.pow(2, scale));

        return new int[]{x, y};
    }

}
