package com.saas.spring.question;

import com.saas.spring.question.dto.QuestionInDto;
import com.saas.spring.question.dto.QuestionOutDto;
import com.saas.spring.question.dto.QuestionUpdateDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository){
        this.questionRepository = questionRepository;
    }

    private QuestionOutDto convertToDto(Question question) {
        return new QuestionOutDto(
                question.getId(),
                question.getText()
        );
    }

    private Question findQuestionById(Long id) {
        return questionRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Pregunta no encontrada con id: " + id));
    }

    public List<QuestionOutDto> getAllQuestions(){
        return this.questionRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public QuestionOutDto getById(Long id){
        var question = this.findQuestionById(id);
        return this.convertToDto(question);
    }

    public QuestionOutDto createQuestion(QuestionInDto dto){
        var question = this.questionRepository.save(
                Question.builder()
                    .text(dto.text())
                        .build()
        );

        return this.convertToDto(question);
    }

    public QuestionOutDto updateQuestion(QuestionUpdateDto dto, Long id){
        var question = this.findQuestionById(id);

        question.setText(
                dto.text() != null ? dto.text() : question.getText()
        );

        return this.convertToDto(this.questionRepository.save(question));
    }

    public void deleteQuestion(Long id){
        var question = this.findQuestionById(id);

        this.questionRepository.delete(question);
    }
}
