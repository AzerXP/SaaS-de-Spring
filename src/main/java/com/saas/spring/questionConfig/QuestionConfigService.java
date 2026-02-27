package com.saas.spring.questionConfig;

import java.util.List;

import org.springframework.stereotype.Service;

import com.saas.spring.exception.QuestionConfigExceptions;
import com.saas.spring.exception.QuestionExceptions;
import com.saas.spring.question.Question;
import com.saas.spring.question.QuestionRepository;
import com.saas.spring.questionConfig.dto.QuestionConfigInDto;
import com.saas.spring.questionConfig.dto.QuestionConfigOutDto;
import com.saas.spring.questionConfig.dto.QuestionConfigUpdateDto;

import jakarta.transaction.Transactional;

@Service
public class QuestionConfigService {
    private final QuestionConfigRepository questionConfigRepository;
    private final QuestionRepository questionRepository;

    public QuestionConfigOutDto convertToDto(QuestionConfig questionConfig){
        return new QuestionConfigOutDto(
            questionConfig.getId(),
            questionConfig.getConfig()
        );
    }

    public QuestionConfigService(QuestionConfigRepository questionConfigRepository, QuestionRepository questionRepository){
        this.questionConfigRepository = questionConfigRepository;
        this.questionRepository = questionRepository;
    }
    
    public List<QuestionConfigOutDto> getAll(){
        return this.questionConfigRepository.findAll()
        .stream()
        .map(this::convertToDto)
        .toList();
    }

    public QuestionConfig findQuestionConfigById(Long id){
        return questionConfigRepository.findById(id)
        .orElseThrow(() -> new QuestionConfigExceptions.QuestionConfigNotFoundException(id));
    }

    public QuestionConfigOutDto getById(Long id){
        var questionConfig = this.findQuestionConfigById(id);
        return this.convertToDto(questionConfig);
    }

    @Transactional
    public QuestionConfigOutDto createQuestionConfig(QuestionConfigInDto dto){
        Question question = questionRepository.findById(dto.questionId())
        .orElseThrow(() -> new QuestionExceptions.QuestionNotFoundException(dto.questionId()));

        QuestionConfig savedQuestionConfig = this.questionConfigRepository.save(
            QuestionConfig.builder()
            .question(question)
            .config(dto.config())
            .build()
        );

        return this.convertToDto(savedQuestionConfig);
    }

    @Transactional
    public QuestionConfigOutDto updateQuestionConfig(QuestionConfigUpdateDto dto, Long id){
        var questionConfig = this.findQuestionConfigById(id);

        questionConfig.setConfig(
            dto.config() != null ? dto.config() : questionConfig.getConfig()
        );

        questionConfig.setQuestion(
            dto.questionId() != null
            ? questionRepository.findById(dto.questionId())
            .orElseThrow(() -> new QuestionExceptions.QuestionNotFoundException(id))
            :questionConfig.getQuestion()
        );

        return this.convertToDto(questionConfig);
    }

    @Transactional
    public void deleteQuestionConfig(Long id){
        var questionConfig = this.findQuestionConfigById(id);

        this.questionConfigRepository.delete(questionConfig);
    }
}
