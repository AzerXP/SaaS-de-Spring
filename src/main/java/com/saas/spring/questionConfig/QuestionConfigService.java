package com.saas.spring.questionConfig;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.saas.spring.exception.QuestionConfigExceptions;
import com.saas.spring.exception.QuestionExceptions;
import com.saas.spring.question.Question;
import com.saas.spring.question.QuestionRepository;
import com.saas.spring.questionConfig.dto.QuestionConfigInDto;
import com.saas.spring.questionConfig.dto.QuestionConfigOutDto;
import com.saas.spring.questionConfig.dto.QuestionConfigUpdateDto;
import com.saas.spring.questionType.QuestionType;

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

    public QuestionConfigService(
        QuestionConfigRepository questionConfigRepository, 
        QuestionRepository questionRepository
    ){
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

    /**
     * Valida que el config JSON cumpla con el schema definido en el QuestionType
     */
    private void validateConfigAgainstSchema(Map<String, Object> config, QuestionType questionType) {
        if (config == null || config.isEmpty()) {
            return; // Config opcional, no se valida
        }

        Map<String, Object> schemaMap = questionType.getConfig_schema();
        if (schemaMap == null || schemaMap.isEmpty()) {
            return; // No hay schema definido, no se valida
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // Convertir Map a JsonNode
            JsonNode schemaNode = objectMapper.convertValue(schemaMap, JsonNode.class);
            JsonNode configNode = objectMapper.convertValue(config, JsonNode.class);

            // Crear schema y validar
            JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)
                .getSchema(schemaNode);
            
            java.util.Set<ValidationMessage> errors = schema.validate(configNode);
            
            if (!errors.isEmpty()) {
                StringBuilder errorMsg = new StringBuilder();
                errors.forEach(err -> errorMsg.append(err.getMessage()).append("; "));
                throw new QuestionConfigExceptions.InvalidConfigSchemaException(errorMsg.toString());
            }
        } catch (Exception e) {
            if (e instanceof QuestionConfigExceptions.InvalidConfigSchemaException) {
                throw e;
            }
            throw new QuestionConfigExceptions.InvalidConfigSchemaException(
                "Error al validar el schema: " + e.getMessage(), 
                e
            );
        }
    }

    @Transactional
    public QuestionConfigOutDto createQuestionConfig(QuestionConfigInDto dto){
        Question question = questionRepository.findById(dto.questionId())
        .orElseThrow(() -> new QuestionExceptions.QuestionNotFoundException(dto.questionId()));

        // Validar config contra el schema del QuestionType
        validateConfigAgainstSchema(dto.config(), question.getQuestionType());

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

        // Si se está actualizando el config, validar contra el schema
        if (dto.config() != null) {
            Question question = questionConfig.getQuestion();
            // Si también se cambia la pregunta, usar el nuevo QuestionType
            if (dto.questionId() != null) {
                question = questionRepository.findById(dto.questionId())
                    .orElseThrow(() -> new QuestionExceptions.QuestionNotFoundException(dto.questionId()));
            }
            validateConfigAgainstSchema(dto.config(), question.getQuestionType());
        }

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
