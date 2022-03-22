#Micronaut R2dbc Postgres Tests

This project consists of several projects designed to isolate or identify workarounds to activating the PostGIS flavor of PostgreSQL database.

Note: While everything here is available per the licensing.  Please use at your own risk.  It is intended for illustrative purposes and should not be used as a production solution.

###Projects

1. r2dbc-geo-geometry - Test usage of GIS Geometry types within a Micronaut MappedEntity 


2. r2dbc-library-compatibility - Demonstrate workaround with R2DBC v1.0 SNAPSHOT build with Micronaut R2dbc 


3. r2dbc-transaction-boundary-violation - Demonstrate transactional commit failure with `@Transactional` annotation in `@Controller` implementations. 


### r2dbc-geo-geometry

Dependencies
- org.locationtech.jts:jts-core:1.18.2
- org.postgresql:r2dbc-postgresql:1.0.0.BUILD-SNAPSHOT

Additions
- Local impl. of PostgeSQL Test Containers that introduces a new QueryOption, `TC_IMAGE_NAME` to override the hard coded `postgres` image the current implementation uses

Current Behaviors
- You can retrieve the count from the Micronaut Crud Repository
- You can saveOne Mapped Entity with a Geometry type
- You can **not** retrieve a Mapped Entity with a Geometry type

### r2dbc-library-compatibility

This compatibility issue is introduced in the SNAPSHOT builds of R2DBC and dependent libraries like R2DBC-POSTGRESQL managed by the org.postgresql team.

The breaking change was introduced on January 27, 2022 with this approved pull request:

```url
https://github.com/r2dbc/r2dbc-spi/commit/03977f5d928a4a4e9e3b2375ae1d9dc6ad9843e4
```

Dependencies
- org.postgresql:r2dbc-postgresql:1.0.0.BUILD-SNAPSHOT

Additions
- Local impl. the Micronaut `DefaultR2dbcRepositoryOperations` operation provider.  When the conditional static ENABLED property is true, it will activate and replace the standard implementation provided by Micronaut

To Activate:
```shell
io.micronaut.data.r2dbc.operations.EnableR2dbcV1Compatibility.ENABLED = true;
```

Current Behaviors
- Tests do not run in sequence because there is no way to properly eject a registered bean initialized.
- Without the V1 Compatible enabled, the test will throw a ClassCastException which a test will catch and verify
- With the V1 Compatability enabled, the object can be safely created and subsequently deleted / updated

### r2dbc-transaction-boundary-violation

TBD