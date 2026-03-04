package com.saas.spring.course;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(value = "SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.lessons",
           countQuery = "SELECT COUNT(c) FROM Course c")
    Page<Course> findAllWithLessonsPaginated(Pageable pageable);
    
    @Query(value = "SELECT c FROM Course c LEFT JOIN FETCH c.lessons")
    List<Course> findAllWithLessons();

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.lessons WHERE c.id = :id")
    public Optional<Course> findByIdWithLessons(Long id);
}
