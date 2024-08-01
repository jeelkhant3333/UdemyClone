package com.techspine.cms.repository;


import com.techspine.cms.entity.Course;
import com.techspine.cms.entity.Instructor;
import com.techspine.cms.entity.InstructorDetail;
import com.techspine.cms.entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class AppDAOImpl implements AppDAO {
    private final EntityManager entityManager;

    @Autowired
    public AppDAOImpl(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }

    @Override
    public Student findStudentById(int id) {
        return entityManager.find(Student.class, id);
    }

    @Override
    public Instructor findInstructorById(int theId) {
        return entityManager.find(Instructor.class, theId);
    }

    @Override
    public InstructorDetail findInstructorDetailById(int theId) {
        return entityManager.find(InstructorDetail.class, theId);
    }

    @Override
    public Course findCourseById(int id) {
        return entityManager.find(Course.class, id);
    }


    @Override
    @Transactional
    public void addStudent(Student student) {
        entityManager.persist(student);
    }

    @Override
    @Transactional
    public void addInstructor(Instructor instructor) {
        entityManager.persist(instructor);
    }

    @Override
    @Transactional
    public void addInstructorDetails(InstructorDetail instructorDetail) {
        entityManager.persist(instructorDetail);
    }

    @Override
    @Transactional
    public void addCourse(Course course) {
        entityManager.persist(course);
    }

    @Override
    @Transactional
    public void updateStudent(Student student) {
        entityManager.merge(student);
    }

    @Override
    @Transactional
    public void updateInstructor(Instructor instructor) {
        entityManager.merge(instructor);
    }

    @Override
    @Transactional
    public void updateCourse(Course course) {
        entityManager.merge(course);
    }

    @Override
    @Transactional
    public String deleteStudentById(int id) {
        Student student = entityManager.find(Student.class, id);
        entityManager.remove(student);
        return "Student Deleted";
    }

    @Override
    @Transactional
    public String deleteInstructorById(int id) {
        Instructor instructor = entityManager.find(Instructor.class, id);
        List<Course> courses = instructor.getCourses();
        for (Course course : courses) {
            course.setInstructor(null);
        }
        entityManager.remove(instructor);
        return "Instructor Deleted";
    }

    @Override
    @Transactional
    public String deleteInstructorDetail(int theId) {
        InstructorDetail instructorDetail = entityManager.find(InstructorDetail.class, theId);
        instructorDetail.getInstructor().setInstructorDetail(null);
        entityManager.remove(instructorDetail);
        return "InstructorDetail Deleted";
    }

    @Override
    @Transactional
    public String deleteCourseById(int id) {
        Course course = entityManager.find(Course.class, id);
        entityManager.remove(course);
        return "Course Deleted";
    }

    @Override
    public List<Course> findCoursesByInstructorId(int theId) {
        TypedQuery<Course> query = entityManager.createQuery("from Course where instructor.id=:data", Course.class);
        query.setParameter("data", theId);
        return query.getResultList();
    }

    @Override
    public Instructor findInstructorByIdJoinFetch(int theId) {
        TypedQuery<Instructor> query = entityManager.createQuery(
                "select i from Instructor i "
                        + "JOIN FETCH i.courses " + "JOIN FETCH i.instructorDetail " + "where i.id= :data ", Instructor.class);
        query.setParameter("data", theId);
        return query.getSingleResult();
    }


    @Override
    public Course findCourseAndReview(int theId) {
        TypedQuery<Course> query = entityManager.createQuery(
                "select c from Course c "
                        + "JOIN FETCH c.reviews "
                        + "where c.id=:data", Course.class);
        query.setParameter("data", theId);
        return query.getSingleResult();
    }

    @Override
    public Course findCourseAndStudent(int theId) {
        TypedQuery<Course> query = entityManager.createQuery(
                "select c from Course c "
                        + "JOIN FETCH c.students "
                        + "where c.id=:data", Course.class);
        query.setParameter("data", theId);
        return query.getSingleResult();
    }

    @Override
    public Student findStudentAndCourse(int theId) {
        TypedQuery<Student> query = entityManager.createQuery(
                "select c from Student c "
                        + "JOIN FETCH c.courses "
                        + "where c.id=:data", Student.class);
        query.setParameter("data", theId);
        return query.getSingleResult();
    }

}
