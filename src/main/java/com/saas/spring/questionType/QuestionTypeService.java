package com.saas.spring.questionType;

import java.util.List;

import org.springframework.stereotype.Service;

import com.saas.spring.questionType.dto.QuestionTypeInDto;
import com.saas.spring.questionType.dto.QuestionTypeOutDto;
import com.saas.spring.questionType.dto.QuestionTypeUpdateDto;

@Service
public class QuestionTypeService {

    private QuestionTypeRepository questionTypeRepository;

    public QuestionTypeService(QuestionTypeRepository questionTypeRepository){
        this.questionTypeRepository = questionTypeRepository;
    }

    private QuestionTypeOutDto convertToDto(QuestionType questionType){
        return new QuestionTypeOutDto(
            questionType.getId(),
            questionType.getName()
        );
    }

    public List<QuestionTypeOutDto> getAllQuestionTypes(){
        return this.questionTypeRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public QuestionType findQuestionTypeById(Long id){
        return this.questionTypeRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Tipo de pregunta no encontrada"));
    }
    
    public QuestionTypeOutDto getById(Long id){
        QuestionType questionType = this.findQuestionTypeById(id);
        return this.convertToDto(questionType);
    }

    public QuestionTypeOutDto createQuestionType(QuestionTypeInDto dto){
        QuestionType questionType = this.questionTypeRepository.save(
            QuestionType.builder()
            .name(dto.name())
            .build()
        );

        return this.convertToDto(questionType);
    }

    public QuestionTypeOutDto updateQuestionType(QuestionTypeUpdateDto dto, Long id){
        QuestionType questionType = this.findQuestionTypeById(id);

        questionType.setName(
            dto.name() != null ? dto.name() : questionType.getName()
        );

        return this.convertToDto(this.questionTypeRepository.save(questionType));
    }

    public void deleteQuestionType(Long id){
        QuestionType questionType = this.findQuestionTypeById(id);

        this.questionTypeRepository.delete(questionType);
    }
}
