package com.example.education;

import reactor.core.publisher.Flux;

import java.util.List;

public class EducationService {

    public Flux<Student> placeStudentsAtSchool(String school, List<String> students) {
        return Flux.empty();
    }
}
