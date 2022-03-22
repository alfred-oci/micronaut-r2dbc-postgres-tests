package com.example.converters;

import jakarta.inject.Singleton;
import org.locationtech.jts.geom.Point;

@Singleton
public class PointConverter extends AbstractGeometryWKBConverter<Point> {
}
