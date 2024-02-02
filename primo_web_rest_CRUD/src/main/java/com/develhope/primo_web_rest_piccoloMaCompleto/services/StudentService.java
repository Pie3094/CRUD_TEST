package com.develhope.primo_web_rest_piccoloMaCompleto.services;

import com.develhope.primo_web_rest_piccoloMaCompleto.entities.Student;
import com.develhope.primo_web_rest_piccoloMaCompleto.repositories.IRepositoryStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private IRepositoryStudent student;


    public Optional<Student> isWorking(Long id, boolean isWorking) {

        Optional<Student> optionalStudent = student.findById(id);

        if (optionalStudent.isEmpty()){
            return Optional.empty();
        }
        optionalStudent.get().setWorking(isWorking);

        return optionalStudent;

    }





}
