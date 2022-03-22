package com.example.utilities;

import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Mono;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface WaterFacilityRepository extends ReactorCrudRepository<WaterFacility, Long> {

    Mono<WaterFacility> findByName(String name);

}
