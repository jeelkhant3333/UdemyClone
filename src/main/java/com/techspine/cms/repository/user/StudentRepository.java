package com.techspine.cms.repository.user;

import com.techspine.cms.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Integer> {

    public Student findByEmail(String email);
}
