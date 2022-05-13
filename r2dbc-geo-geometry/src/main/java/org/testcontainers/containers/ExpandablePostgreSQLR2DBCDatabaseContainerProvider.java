package org.testcontainers.containers;

import com.google.auto.service.AutoService;
import io.r2dbc.spi.ConnectionFactoryMetadata;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;
import org.testcontainers.r2dbc.R2DBCDatabaseContainer;
import org.testcontainers.r2dbc.R2DBCDatabaseContainerProvider;
import org.testcontainers.utility.DockerImageName;

import javax.annotation.Nullable;
import java.util.Optional;

@AutoService(R2DBCDatabaseContainerProvider.class)
public class ExpandablePostgreSQLR2DBCDatabaseContainerProvider implements R2DBCDatabaseContainerProvider {
    Option<String> IMAGE_NAME_OPTION = Option.valueOf("TC_IMAGE_NAME");

    static final String DRIVER = PostgreSQLR2DBCDatabaseContainerProvider.DRIVER;

    @Override
    public boolean supports(ConnectionFactoryOptions options) {
        return DRIVER.equals(options.getRequiredValue(ConnectionFactoryOptions.DRIVER));
    }

    @Override
    public R2DBCDatabaseContainer createContainer(ConnectionFactoryOptions options) {
        String rawImageName =
                Optional.ofNullable(options.getValue(IMAGE_NAME_OPTION)).orElse(PostgreSQLContainer.IMAGE) +
                ":" + options.getRequiredValue(R2DBCDatabaseContainerProvider.IMAGE_TAG_OPTION);

        DockerImageName dockerImageName =
                DockerImageName.parse(rawImageName).asCompatibleSubstituteFor(DockerImageName.parse("postgres"));

        PostgreSQLContainer<?> container = new PostgreSQLContainer<>(dockerImageName)
                .withDatabaseName(options.getRequiredValue(ConnectionFactoryOptions.DATABASE).toString());

        if (Boolean.TRUE.equals(options.getValue(R2DBCDatabaseContainerProvider.REUSABLE_OPTION))) {
            container.withReuse(true);
        }
        return new PostgreSQLR2DBCDatabaseContainer(container);
    }

    @Nullable
    @Override
    public ConnectionFactoryMetadata getMetadata(ConnectionFactoryOptions options) {
        ConnectionFactoryOptions.Builder builder = options.mutate();
        if (!options.hasOption(ConnectionFactoryOptions.USER)) {
            builder.option(ConnectionFactoryOptions.USER, PostgreSQLContainer.DEFAULT_USER);
        }
        if (!options.hasOption(ConnectionFactoryOptions.PASSWORD)) {
            builder.option(ConnectionFactoryOptions.PASSWORD, PostgreSQLContainer.DEFAULT_PASSWORD);
        }
        return R2DBCDatabaseContainerProvider.super.getMetadata(builder.build());
    }
}
