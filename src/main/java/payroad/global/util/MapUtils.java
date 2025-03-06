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

}
