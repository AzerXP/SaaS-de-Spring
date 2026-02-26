package com.saas.spring.question;

import com.saas.spring.User.User;
import com.saas.spring.questionConfig.QuestionConfig;
import com.saas.spring.questionType.QuestionType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_type_id", nullable = false)
    private QuestionType questionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(
        mappedBy = "question",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY
    )
    private QuestionConfig questionConfig;
}
