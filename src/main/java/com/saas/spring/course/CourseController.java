package com.saas.spring.course;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saas.spring.course.dto.CourseInDto;
import com.saas.spring.course.dto.CourseOutDto;
import com.saas.spring.course.dto.CourseUpdateDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/courses")
@Slf4j
@Tag(name = "Courses", description = "Gestión de cursos del sistema")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    @Operation(summary = "Get courses paginated", description = "Obtiene todos los cursos paginados")
    public ResponseEntity<Page<CourseOutDto>> getCoursesPaginated(
            @RequestParam(defaultValue = "0") 
            @Parameter(description = "Número de página", example = "0")
            int page,
            
            @RequestParam(defaultValue = "10") 
            @Parameter(description = "Tamaño de página", example = "10")
            int size,
            
            @RequestParam(defaultValue = "id") 
            @Parameter(description = "Campo por el que ordenar", example = "id")
            String sortBy,
            
            @RequestParam(defaultValue = "asc") 
            @Parameter(description = "Dirección de ordenamiento (asc o desc)", example = "asc")
            String sortDirection) {
            
        log.info("Obteniendo cursos paginados - página: {}, tamaño: {}", page, size);
            
        Sort sort = sortDirection.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
            
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CourseOutDto> courses = courseService.getAllCoursesPaginated(pageable);
            
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all courses", description = "Obtiene todos los cursos disponibles en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cursos obtenidos exitosamente",
            content = @Content(schema = @Schema(implementation = CourseOutDto.class)))
    })
    public ResponseEntity<List<CourseOutDto>> getCourses() {
        log.info("Obteniendo todos los cursos");
        List<CourseOutDto> courses = this.courseService.getAllCourses();
        log.debug("Cantidad de cursos encontrados: {}", courses.size());
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID", description = "Obtiene un curso específico por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Curso encontrado",
            content = @Content(schema = @Schema(implementation = CourseOutDto.class),
                examples = {
                    @ExampleObject(name = "Course Example", summary = "Ejemplo de curso",
                        value = """
                        {
                          "id": 1,
                          "title": "Curso de Java",
                          "description": "Aprende Java desde cero hasta avanzado"
                        }
                        """)
                }
            )),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    public ResponseEntity<CourseOutDto> getCourse(@PathVariable Long id) {
        log.info("Obteniendo curso con id: {}", id);
        CourseOutDto course = this.courseService.getById(id);
        return ResponseEntity.ok(course);
    }

    @PostMapping
    @Operation(summary = "Create course", description = "Crea un nuevo curso en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Curso creado exitosamente",
            content = @Content(schema = @Schema(implementation = CourseOutDto.class),
                examples = {
                    @ExampleObject(name = "Course Created", summary = "Curso creado",
                        value = """
                        {
                          "id": 1,
                          "title": "Curso de Python",
                          "description": "Domina Python con este curso completo"
                        }
                        """)
                }
            )),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<CourseOutDto> createCourse(@RequestBody @Valid CourseInDto dto) {
        log.info("Creando curso: {}", dto.title());
        CourseOutDto created = this.courseService.createCourse(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update course", description = "Actualiza parcialmente un curso existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Curso actualizado",
            content = @Content(schema = @Schema(implementation = CourseOutDto.class))),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos")
    })
    public ResponseEntity<CourseOutDto> updateCourse(
            @PathVariable Long id,
            @RequestBody @Valid CourseUpdateDto dto
    ) {
        log.info("Actualizando curso con id: {}", id);
        CourseOutDto updated = this.courseService.updateCourse(dto, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete course", description = "Elimina permanentemente un curso y todas sus lecciones asociadas")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Curso y sus lecciones eliminadas exitosamente"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        log.info("Borrando curso con id: {}", id);
        this.courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
