package com.example.converters;

import io.micronaut.core.convert.ConversionContext;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.model.runtime.convert.AttributeConverter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKBWriter;

public abstract class AbstractGeometryWKBConverter<T extends Geometry> implements AttributeConverter<T, String> {
    private final WKBReader wkbReader;
    private final WKBWriter wkbWriter;

    public AbstractGeometryWKBConverter() {
        this.wkbReader = new WKBReader();
        this.wkbWriter = new WKBWriter();
    }

    @Override
    public String convertToPersistedValue(Geometry entityValue, ConversionContext context) {
        if(null == entityValue) {
            return null;
        }

        return WKBWriter.toHex(wkbWriter.write(entityValue));
    }

    @Override
    public T convertToEntityValue(String persistedValue, ConversionContext context) {
        if(null == persistedValue) {
            return null;
        }

        try {
            return (T) wkbReader.read(WKBReader.hexToBytes(persistedValue));
        } catch (ParseException e) {
            throw new DataAccessException("cannot read geometry persisted value");
        }
    }
}
