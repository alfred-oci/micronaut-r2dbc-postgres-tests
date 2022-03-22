package com.example.utilities;

import io.micronaut.data.annotation.*;
import lombok.Data;
import org.locationtech.jts.geom.Point;

@Data
@MappedEntity("water_facility")
public class WaterFacility {

    @Id
    @AutoPopulated
    @GeneratedValue(GeneratedValue.Type.AUTO)
    private Long id;

    private String name;

    private Point geom;

    public WaterFacility() {
    }

    public WaterFacility(String name, Point geom) {
        this();

        this.name = name;
        this.geom = geom;
    }
}
