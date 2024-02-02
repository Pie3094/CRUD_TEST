package com.develhope.primo_web_rest_piccoloMaCompleto.repositories;

import com.develhope.primo_web_rest_piccoloMaCompleto.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRepositoryStudent extends JpaRepository<Student, Long> {
}
