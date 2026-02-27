package com.saas.spring.questionConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionConfigRepository extends JpaRepository<QuestionConfig, Long>{

}
