package com.example;

import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.runtime.config.DataSettings;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Result;
import org.slf4j.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@R2dbcRepository
public class PostGisInitializer {
    private static final Logger QUERY_LOG = DataSettings.QUERY_LOG;

    private static final List<String> initializers = List.of(
//            "CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\";",
            "CREATE TABLE IF NOT EXISTS \"water_facility\" (\"id\" BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, \"name\" VARCHAR(255) NOT NULL,\"geom\" GEOMETRY(POINT, 4326) NOT NULL);",
            "INSERT INTO \"water_facility\" (\"name\",\"geom\") VALUES ('Day Star Facility','SRID=4326;POINT(-93.373233615936 37.2208867239858)')"
    );

    private final ConnectionFactory connectionFactory;

    public PostGisInitializer(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Mono<Long> initialize() {
        return Flux.from(connectionFactory.create())
                .flatMap(connection -> Flux.just(connection.createBatch())
                        .flatMap(batch -> {

                            for(String sql : initializers) {
                                if (QUERY_LOG.isDebugEnabled()) {
                                    QUERY_LOG.debug("Executing SQL: {}", sql);
                                }

                                batch.add(sql);
                            }

                            return batch.execute();
                        })
                        .flatMap(Result::getRowsUpdated)
                        .doFinally((st) -> {
                            if (QUERY_LOG.isTraceEnabled()) {
                                QUERY_LOG.trace("Closing connection {} {}",
                                        connection.getMetadata().getDatabaseProductName(),
                                        connection.getMetadata().getDatabaseVersion());
                            }
                            connection.close();
                        })
                ).reduce(0L, Long::sum);
    }
}
