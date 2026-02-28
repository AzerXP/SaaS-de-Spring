package com.saas.spring.questionType;

import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class QuestionTypeSeeder implements CommandLineRunner {

    private final QuestionTypeRepository questionTypeRepository;

    public QuestionTypeSeeder(QuestionTypeRepository questionTypeRepository) {
        this.questionTypeRepository = questionTypeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        createQuestionTypesIfNotExists();
    }

    private void createQuestionTypesIfNotExists() {
        createMultipleChoiceType();
        createBlankSpacesType();
    }

    private void createMultipleChoiceType() {
        String name = "MULTIPLE_CHOICE";
        
        if (questionTypeRepository.findByName(name).isPresent()) {
            return;
        }

        Map<String, Object> schema = Map.of(
            "type", "object",
            "required", List.of("options", "max_selection"),
            "properties", Map.of(
                "options", Map.of(
                    "type", "array",
                    "minItems", 2,
                    "items", Map.of(
                        "type", "object",
                        "required", List.of("id", "text", "is_correct"),
                        "properties", Map.of(
                            "id", Map.of("type", "integer"),
                            "text", Map.of("type", "string", "minLength", 1),
                            "is_correct", Map.of("type", "boolean")
                        ),
                        "additionalProperties", false
                    )
                ),
                "max_selection", Map.of("type", "integer", "minimum", 1),
                "shuffle_options", Map.of("type", "boolean")
            ),
            "additionalProperties", false
        );

        QuestionType questionType = QuestionType.builder()
            .name(name)
            .config_schema(schema)
            .build();

        questionTypeRepository.save(questionType);
        System.out.println("QuestionType '" + name + "' creado exitosamente.");
    }

    private void createBlankSpacesType() {
        String name = "BLANK_SPACES";
        
        if (questionTypeRepository.findByName(name).isPresent()) {
            return;
        }

        Map<String, Object> schema = Map.of(
            "type", "object",
            "required", List.of("text", "blanks"),
            "properties", Map.of(
                "text", Map.of("type", "string"),
                "blanks", Map.of(
                    "type", "array",
                    "minItems", 1,
                    "items", Map.of(
                        "type", "object",
                        "required", List.of("placeholder", "correct_answers"),
                        "properties", Map.of(
                            "placeholder", Map.of("type", "string"),
                            "correct_answers", Map.of(
                                "type", "array",
                                "minItems", 1,
                                "items", Map.of("type", "string", "minLength", 1)
                            ),
                            "case_sensitive", Map.of("type", "boolean", "default", false)
                        ),
                        "additionalProperties", false
                    )
                )
            ),
            "additionalProperties", false
        );

        QuestionType questionType = QuestionType.builder()
            .name(name)
            .config_schema(schema)
            .build();

        questionTypeRepository.save(questionType);
        System.out.println("QuestionType '" + name + "' creado exitosamente.");
    }
}
