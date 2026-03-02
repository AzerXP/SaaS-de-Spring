package com.saas.spring.course;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saas.spring.course.dto.CourseInDto;
import com.saas.spring.course.dto.CourseOutDto;
import com.saas.spring.course.dto.CourseUpdateDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public List<CourseOutDto> getAllCourses() {
        log.debug("Obteniendo todos los cursos");
        return courseRepository.findAll().stream()
                .map(this::convertToOutDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public CourseOutDto getById(Long id) {
        log.debug("Obteniendo curso con id: {}", id);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con id: " + id));
        return convertToOutDto(course);
    }

    @Transactional
    public CourseOutDto createCourse(CourseInDto dto) {
        log.debug("Creando curso: {}", dto.title());
        Course course = Course.builder()
                .title(dto.title())
                .description(dto.description())
                .build();
        Course saved = courseRepository.save(course);
        log.info("Curso creado con id: {}", saved.getId());
        return convertToOutDto(saved);
    }

    @Transactional
    public CourseOutDto updateCourse(CourseUpdateDto dto, Long id) {
        log.debug("Actualizando curso con id: {}", id);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con id: " + id));

        if (dto.title() != null) {
            course.setTitle(dto.title());
        }
        if (dto.description() != null) {
            course.setDescription(dto.description());
        }

        Course updated = courseRepository.save(course);
        log.info("Curso actualizado con id: {}", updated.getId());
        return convertToOutDto(updated);
    }

    @Transactional
    public void deleteCourse(Long id) {
        log.debug("Eliminando curso con id: {}", id);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con id: " + id));
        courseRepository.delete(course);
        log.info("Curso eliminado con id: {}", id);
    }

    private CourseOutDto convertToOutDto(Course course) {
        return new CourseOutDto(
                course.getId(),
                course.getTitle(),
                course.getDescription()
        );
    }
}
