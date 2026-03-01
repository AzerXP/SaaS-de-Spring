package com.saas.spring.lesson;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saas.spring.lesson.dto.LessonInDto;
import com.saas.spring.lesson.dto.LessonOutDto;
import com.saas.spring.lesson.dto.LessonUpdateDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;

    @Transactional(readOnly = true)
    public List<LessonOutDto> getAllLessons() {
        log.debug("Obteniendo todas las lecciones");
        return lessonRepository.findAll().stream()
                .map(this::convertToOutDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public LessonOutDto getById(Long id) {
        log.debug("Obteniendo lección con id: {}", id);
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lección no encontrada con id: " + id));
        return convertToOutDto(lesson);
    }

    @Transactional
    public LessonOutDto createLesson(LessonInDto dto) {
        log.debug("Creando lección: {}", dto.title());
        Lesson lesson = Lesson.builder()
                .title(dto.title())
                .description(dto.description())
                .build();
        Lesson saved = lessonRepository.save(lesson);
        log.info("Lección creada con id: {}", saved.getId());
        return convertToOutDto(saved);
    }

    @Transactional
    public LessonOutDto updateLesson(LessonUpdateDto dto, Long id) {
        log.debug("Actualizando lección con id: {}", id);
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lección no encontrada con id: " + id));

        if (dto.title() != null) {
            lesson.setTitle(dto.title());
        }
        if (dto.description() != null) {
            lesson.setDescription(dto.description());
        }

        Lesson updated = lessonRepository.save(lesson);
        log.info("Lección actualizada con id: {}", updated.getId());
        return convertToOutDto(updated);
    }

    @Transactional
    public void deleteLesson(Long id) {
        log.debug("Eliminando lección con id: {}", id);
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lección no encontrada con id: " + id));
        lessonRepository.delete(lesson);
        log.info("Lección eliminada con id: {}", id);
    }

    private LessonOutDto convertToOutDto(Lesson lesson) {
        return new LessonOutDto(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getDescription()
        );
    }
}
