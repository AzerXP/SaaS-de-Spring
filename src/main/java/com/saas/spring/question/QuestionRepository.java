package com.saas.spring.question;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT q FROM Question q JOIN FETCH q.questionType qt ORDER BY q.id")
    public List<Question> findAllWithQuestionType();

    @Query("SELECT q FROM Question q JOIN FETCH q.questionType qt WHERE q.id = :id")
    public Optional<Question> findByIdWithQuestionType(Long id);
}
