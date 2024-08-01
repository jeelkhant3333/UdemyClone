package com.techspine.cms.controller;


import com.techspine.cms.entity.Course;
import com.techspine.cms.entity.Instructor;
import com.techspine.cms.entity.InstructorDetail;
import com.techspine.cms.entity.Student;
import com.techspine.cms.repository.AppDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {
    @Autowired
    private AppDAOImpl appDAO;


    @GetMapping("/student/{id}")
    public Student findStudentById(@PathVariable("id") int id){
        return appDAO.findStudentById(id);
    }

    @GetMapping("/instructor/{id}")
    public Instructor findInstructorById(@PathVariable("id") int id) {
        return appDAO.findInstructorById(id);
    }

    @GetMapping("/instructorDetail/{id}")
    public InstructorDetail findInstructorDetailById(@PathVariable("id") int id) {
        return appDAO.findInstructorDetailById(id);
    }

    @GetMapping("/course/{id}")
    public Course findCourseById(@PathVariable("id") int id) {
        return appDAO.findCourseById(id);
    }


}
