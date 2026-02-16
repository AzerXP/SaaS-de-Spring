package com.saas.spring.question;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionInterface extends JpaRepository<Question, Long> {
}
