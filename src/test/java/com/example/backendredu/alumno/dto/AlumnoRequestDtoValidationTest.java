package com.example.backendredu.alumno.dto;

import com.example.backendredu.alumno.domain.Carrera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AlumnoRequestDtoValidationTest {
    private Validator validator;

    private AlumnoRequestDto alumnoRequestDto;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    void shouldFailValidationWhenEmailIsNull(){
        alumnoRequestDto = new AlumnoRequestDto();
        alumnoRequestDto.setPassword("lS9vw76");
        alumnoRequestDto.setUsername("MickeyMouse");
        alumnoRequestDto.setDescription("Hola, soy una rata");
        alumnoRequestDto.setCarrera(Carrera.INGENIERIA_INDUSTRIAL);

        Set<ConstraintViolation<AlumnoRequestDto>> violations = validator.validate(alumnoRequestDto);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenPasswordIsNull(){
        alumnoRequestDto = new AlumnoRequestDto();
        alumnoRequestDto.setEmail("micky.mouse@utec.edu.pe");
        alumnoRequestDto.setUsername("MickyMouse");
        alumnoRequestDto.setDescription("Hola, soy una rata muy amigable");
        alumnoRequestDto.setCarrera(Carrera.INGENIERIA_INDUSTRIAL);

        Set<ConstraintViolation<AlumnoRequestDto>> violations = validator.validate(alumnoRequestDto);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenUsernameIsNull(){
        alumnoRequestDto = new AlumnoRequestDto();
        alumnoRequestDto.setEmail("micky.mouse@utec.edu.pe");
        alumnoRequestDto.setPassword("lS9vw76");
        alumnoRequestDto.setDescription("Hola, soy una rata muy amigable");
        alumnoRequestDto.setCarrera(Carrera.INGENIERIA_INDUSTRIAL);

        Set<ConstraintViolation<AlumnoRequestDto>> violations = validator.validate(alumnoRequestDto);
        assertEquals(1, violations.size());
    }

    // La descripción es opcional :0

    @Test
    void shouldFailValidationWhenCarreraIsNull(){
        alumnoRequestDto = new AlumnoRequestDto();
        alumnoRequestDto.setEmail("micky.mouse@utec.edu.pe");
        alumnoRequestDto.setPassword("lS9vw76");
        alumnoRequestDto.setUsername("MickyMouse");

        Set<ConstraintViolation<AlumnoRequestDto>> violations = validator.validate(alumnoRequestDto);
        assertEquals(1, violations.size());
    }
}
