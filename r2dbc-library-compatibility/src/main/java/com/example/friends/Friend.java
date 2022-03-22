package com.example.friends;

import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@MappedEntity("a_friend")
public class Friend {
    @Id
    @AutoPopulated
    @GeneratedValue(GeneratedValue.Type.AUTO)
    private Long id;

    private String name;

    public Friend() {
    }

    public Friend(String name) {
        this();

        this.name = name;
    }
}
