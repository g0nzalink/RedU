package com.example.backendredu.profesor.dto;


import com.example.backendredu.profesor.domain.Departamento;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProfesorRequestDtoValidationTest {
    private Validator validator;

    private ProfesorRequestDto profesorRequestDto;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    void shouldFailValidationWhenEmailIsNull(){
        profesorRequestDto = new ProfesorRequestDto();
        profesorRequestDto.setPassword("lS9vw76");
        profesorRequestDto.setUsername("Goofy");
        profesorRequestDto.setDescription("Hola, soy un perro");
        profesorRequestDto.setDepartamento(Departamento.CIENCIAS);

        Set<ConstraintViolation<ProfesorRequestDto>> violations = validator.validate(profesorRequestDto);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenPasswordIsNull(){
        profesorRequestDto = new ProfesorRequestDto();
        profesorRequestDto.setEmail("goofy@utec.edu.pe");
        profesorRequestDto.setUsername("Goofy");
        profesorRequestDto.setDescription("Hola, soy un perro");
        profesorRequestDto.setDepartamento(Departamento.CIENCIAS);

        Set<ConstraintViolation<ProfesorRequestDto>> violations = validator.validate(profesorRequestDto);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenUsernameIsNull(){
        profesorRequestDto = new ProfesorRequestDto();
        profesorRequestDto.setEmail("goofy@utec.edu.pe");
        profesorRequestDto.setPassword("lS9vw76");
        profesorRequestDto.setDescription("Hola, soy un perro");
        profesorRequestDto.setDepartamento(Departamento.CIENCIAS);

        Set<ConstraintViolation<ProfesorRequestDto>> violations = validator.validate(profesorRequestDto);
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenDepartamentoIsNull(){
        profesorRequestDto = new ProfesorRequestDto();
        profesorRequestDto.setEmail("goofy@utec.edu.pe");
        profesorRequestDto.setPassword("lS9vw76");
        profesorRequestDto.setUsername("Goofy");
        profesorRequestDto.setDescription("Hola, soy un perro");

        Set<ConstraintViolation<ProfesorRequestDto>> violations = validator.validate(profesorRequestDto);
        assertEquals(1, violations.size());
    }
}
