package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;
    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }
    @GetMapping("{id}") //GET http://localhost:8080/faculty/23
    public ResponseEntity<Faculty> getFacultyInfo(@PathVariable Long id) {
        Faculty faculty = facultyService.findFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PostMapping    //POST http://localhost:8080/faculty
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @PutMapping //PUT http://localhost:8080/faculty
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty foundFaculty = facultyService.editFaculty(faculty);
        if (foundFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("{id}")  //DELETE http://localhost:8080/faculty/23
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> findFaculties(@RequestParam(required = false) String name,
                                                             @RequestParam(required = false) String color) {
        if ((name != null && !name.isBlank()) || (color != null && !color.isBlank()) ) {
            return ResponseEntity.ok(facultyService.findFacultiesByNameOrColor(name, color));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("{id}/students")
    public ResponseEntity<Collection<Student>> findStudentByFaculty(@PathVariable Integer id) {
        if (id > 0) {
            return ResponseEntity.ok(facultyService.findFaculty(id).getStudents());
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/most_long_name_faculty")
    public ResponseEntity<String> getMostLongNameFaculty() {
        return ResponseEntity.ok(facultyService.getMostLongNameFaculty());
    }
}
