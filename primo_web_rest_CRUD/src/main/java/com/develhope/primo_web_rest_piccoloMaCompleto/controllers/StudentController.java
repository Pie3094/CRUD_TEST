package com.develhope.primo_web_rest_piccoloMaCompleto.controllers;

import com.develhope.primo_web_rest_piccoloMaCompleto.entities.Student;
import com.develhope.primo_web_rest_piccoloMaCompleto.repositories.IRepositoryStudent;
import com.develhope.primo_web_rest_piccoloMaCompleto.services.StudentService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class StudentController {

    @Autowired
    private StudentService serviceStudent;

    @Autowired
    private IRepositoryStudent repositoryStudent;

    @PostMapping("/create")
    public Student create(@RequestBody Student student) {
        return repositoryStudent.save(student);
    }

    @GetMapping("/getAll")
    public List<Student> getAll() {
        return repositoryStudent.findAll();
    }

    @GetMapping("/getStudent/{id}")
    public Student getStudentById(@PathVariable Long id) {
        Optional<Student> optionalStudent= repositoryStudent.findById(id);
        return optionalStudent.orElse(null);
    }

    @PutMapping("/putStudent/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody @NonNull Student student) {
        student.setId(id);
        return repositoryStudent.save(student);
    }

    @PatchMapping("/isWorking/{id}")
    public Student updateIsWorking(@PathVariable Long id, @RequestParam("working") Boolean working) {
        return serviceStudent.isWorking(id, working).get();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteStudent(@PathVariable Long id) {
        repositoryStudent.deleteById(id);
    }
}
