package com.saas.spring.question;

import com.saas.spring.exception.QuestionExceptions;
import com.saas.spring.exception.QuestionTypeExceptions;
import com.saas.spring.question.dto.QuestionInDto;
import com.saas.spring.question.dto.QuestionOutDto;
import com.saas.spring.question.dto.QuestionUpdateDto;
import com.saas.spring.questionType.QuestionTypeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionTypeRepository questionTypeRepository;

    public QuestionService(QuestionRepository questionRepository, QuestionTypeRepository questionTypeRepository){
        this.questionRepository = questionRepository;
        this.questionTypeRepository = questionTypeRepository;
    }

    private QuestionOutDto convertToDto(Question question) {
        return new QuestionOutDto(
                question.getId(),
                question.getText(),
                question.getQuestionType().getId()
        );
    }

    private Question findQuestionById(Long id) {
        return questionRepository.findByIdWithQuestionType(id).
                orElseThrow(() -> new QuestionExceptions.QuestionNotFoundException(id));
    }

    public List<QuestionOutDto> getAllQuestions(){
        return this.questionRepository.findAllWithQuestionType()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public QuestionOutDto getById(Long id){
        var question = this.findQuestionById(id);
        return this.convertToDto(question);
    }

    @Transactional
    public QuestionOutDto createQuestion(QuestionInDto dto){
        var questionType = questionTypeRepository.findById(dto.questionTypeId())
            .orElseThrow(() -> new QuestionTypeExceptions.QuestionTypeNotFoundException(dto.questionTypeId()));

        Question question = this.questionRepository.save(
                Question.builder()
                    .text(dto.text())
                    .questionType(questionType)
                        .build()
        );

        return this.convertToDto(question);
    }

    @Transactional
    public QuestionOutDto updateQuestion(QuestionUpdateDto dto, Long id){
        var question = this.findQuestionById(id);

        question.setText(
            dto.text() != null ? dto.text() : question.getText()
        );

        question.setQuestionType(
            dto.questionTypeId() != null 
                ? questionTypeRepository.findById(dto.questionTypeId())
                    .orElseThrow(() -> new QuestionTypeExceptions.QuestionTypeNotFoundException(id))
                : question.getQuestionType()
        );

        return this.convertToDto(question);
    }

    @Transactional
    public void deleteQuestion(Long id){
        var question = this.findQuestionById(id);

        this.questionRepository.delete(question);
    }
}
