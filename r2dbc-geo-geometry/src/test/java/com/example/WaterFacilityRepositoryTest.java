package com.example;

import com.example.utilities.WaterFacility;
import com.example.utilities.WaterFacilityRepository;
import org.testcontainers.containers.PostgisContainerProvider;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.PostgreSQLR2DBCDatabaseContainer;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WaterFacilityRepositoryTest {
    final private GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);

//    @Container
//    private static final PostgreSQLContainer JDBC_CONTAINER;

//    @Container
//    private static final PostgreSQLR2DBCDatabaseContainer R2DBC_CONTAINER;

//    static {
//        PostgreSQLContainer container = (PostgreSQLContainer)new PostgisContainerProvider()
//                .newInstance("latest");
//
//        PostgreSQLR2DBCDatabaseContainer r2dbcContainer = new PostgreSQLR2DBCDatabaseContainer(container);
//
//        R2DBC_CONTAINER = r2dbcContainer;
//    }


    @Inject
    EmbeddedApplication<?> application;

    @Inject
    PostGisInitializer postgisInitializer;

    @Inject
    WaterFacilityRepository repository;

    @BeforeAll
    public void setup() {
        postgisInitializer.initialize().block();
    }

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

    @Test
    public void testGetCount() {
        Long count = repository.count().block();

        assertNotNull(count);
        assertEquals(1L, count);
    }

    @Test
    public void testGetOneByName() {
        final Point geom = gf.createPoint(new CoordinateXY(-93.373233615936, 37.2208867239858));
        WaterFacility retrievedWaterFacility = repository.findByName("Day Star Facility").block();

        assertNotNull(retrievedWaterFacility);
        assertEquals("Day Star Facility", retrievedWaterFacility.getName());
        assertEquals(geom, retrievedWaterFacility.getGeom());
    }

    @Test
    public void testSaveOneAndGetOne() {
        final Point geom = gf.createPoint(new CoordinateXY(38.590172, -89.910858));
        WaterFacility savedWaterFacility =
                repository.save(new WaterFacility(
                        "Magna Seating", geom)
                ).block();

        assertNotNull(savedWaterFacility);
        assertNotNull(savedWaterFacility.getId());
        assertEquals("Magna Seating", savedWaterFacility.getName());
        assertEquals(geom, savedWaterFacility.getGeom());

        WaterFacility retrievedWaterFacility = repository.findById(savedWaterFacility.getId()).block();

        assertNotNull(retrievedWaterFacility);
        assertEquals("Magna Seating", retrievedWaterFacility.getName());
        assertEquals(geom, retrievedWaterFacility.getGeom());
    }
}
