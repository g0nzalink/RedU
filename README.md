[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/Jmnm3svF)


# 🧠 RedU

**Curso:** CS 2031 - Desarrollo Basado en Plataforma  
**Integrantes del equipo:**  
- Gonzalo Andrés Valladolid Jiménez
- Matías Sebastian Walde Verano
- Nicolas Valentino Diaz Flores
- Hector Emilio Huaman Puiquin 

---

## 📑 Índice

1. [Introducción](#introducción)
   - [Contexto](#contexto)
   - [Objetivos del Proyecto](#objetivos-del-proyecto)
2. [Identificación del Problema o Necesidad](#identificación-del-problema-o-necesidad)
   - [Descripción del Problema](#descripción-del-problema)
   - [Justificación](#justificación)
3. [Descripción de la Solución](#descripción-de-la-solución)
   - [Funcionalidades Implementadas](#funcionalidades-implementadas)
   - [Tecnologías Utilizadas](#tecnologías-utilizadas)
4. [Modelo de Entidades](#modelo-de-entidades)
   - [Diagrama de Entidades](#diagrama-de-entidades)
   - [Descripción de Entidades](#descripción-de-entidades)
5. [Testing y Manejo de Errores](#testing-y-manejo-de-errores)
   - [Niveles de Testing Realizados](#niveles-de-testing-realizados)
   - [Resultados](#resultados)
   - [Manejo de Errores](#manejo-de-errores)
6. [Medidas de Seguridad Implementadas](#medidas-de-seguridad-implementadas)
   - [Seguridad de Datos](#seguridad-de-datos)
   - [Prevención de Vulnerabilidades](#prevención-de-vulnerabilidades)
7. [Eventos y Asincronía](#eventos-y-asincronía)
8. [GitHub](#github)
   - [Uso de GitHub Projects](#uso-de-github-projects)
   - [Uso de GitHub Actions](#uso-de-github-actions)
9. [Conclusión](#conclusión)
   - [Logros del Proyecto](#logros-del-proyecto)
   - [Aprendizajes Clave](#aprendizajes-clave)
   - [Trabajo Futuro](#trabajo-futuro)
10. [Apéndices](#apéndices)
    - [Licencia](#licencia)
    - [Referencias](#referencias)

---

## 📘 Introducción

### Contexto
Este proyecto se desarrolla en el ámbito de la educación superior, respondiendo a necesidades en la comunicación y compromiso estudiantil. La industria educativa enfrenta desafíos en la retención de miembros en clubes universitarios y en la efectividad de los canales de difusión tradicionales, como correos electrónicos, que suelen ser ignorados. Además, existe una demanda no cubierta de colaboración interdisciplinaria, donde proyectos personales o grupales—como aquellos de estudiantes de administración que requieren apoyo de otras carreras—no logran alcanzar a su audiencia potencial. Al implementar soluciones innovadoras de comunicación, el proyecto no solo beneficia a los estudiantes y clubes, sino que también sienta las bases para un modelo escalable que podría aplicarse en otras instituciones, potenciando así la colaboración y la construcción de relaciones académicas y profesionales entre estudiantes y profesores.

### Objetivo del Proyecto
El objetivo principal de este proyecto a nivel provincial es mejorar la experiencia universitaria, enfocándonos especialmente en optimizar la comunicación de actividades, proyectos y oportunidades entre la universidad, los clubes estudiantiles y los alumnos. Actualmente, muchos estudiantes no reciben información oportuna sobre eventos o iniciativas, lo que lleva a la desintegración de clubes por falta de participación o al desconocimiento de proyectos relevantes. Para abordar este problema, nos centramos en objetivos específicos clave: mejorar la difusión de actividades tanto de clubes como de la universidad, fortalecer la comunicación institucional y promover la visibilidad de proyectos estudiantiles interdisciplinarios. De esta manera, buscamos crear un entorno más conectado y participativo, donde los estudiantes puedan aprovechar al máximo las oportunidades que ofrece la vida universitaria.

---

## 🧩 Identificación del Problema o Necesidad

### Descripción del Problema
Los estudiantes universitarios no siempre son conscientes de las actividades que se realizan en su propia universidad. No son conscientes de la gran cantidad de organizaciones estudiantiles que hay en cada universidad y, por lo tanto, se pierden de grandes experiencias de potencial aprendizaje. A su vez, no todos en la universidad se encuentran comunicados y no se conocen todos entre si. Es por ello que, cuando alguien inicia un proyecto, no todas las personas que podrían estar interesadas en él saben de la existencia de dicho proyecto. En vistas generarles, el problema es la dificil difusión que hay para las oportunidades que existen en la universidad para crecer de manera académica o profesional, provocando que un estudiante nunca sepa de lo que se ha perdido.  

### Justificación
Las organizacicones estudiantiles como tal presentan proyectos o actividades las cuales son sumamente interesantes para el público universitario. Es por ello que estas organizaciones y sus actividades merecen una mayor difusión para llegar a un público de manera más fácil y rápida. De esta manera, los estudiantes podrán tener a su disposición una gran cantidad de eventos en los cuales podrán desarrollar nuevas habilidades o hasta poder hacer networking con personas que comparten sus intereses. Por otro lado, los estudiantes o hasta los profesores podrán ser capaces de difundir proyectos o anuncios buscando grupos de estudios para fortalecer sus habilidades académicas o explorar nuevos objetivos. Es necesario que las ideas o hasta necesidades de los alumnos tengan una difusiuón rápida y accesible para que puedan desarrollar sus habilidades académicas al máximo. 

---

## 💡 Descripción de la Solución

### Funcionalidades Implementadas
- Registro y autenticación:
      - Verificación con correo institucional y código de alumno.
- Publicaciones de clubes y universidad:
      - Creación de publicaciones (solo por la directiva y la universidad).
      - Fechas de cierre automático (evita información desactualizada).
- Sistema de búsqueda:
      - En publicaciones con tags categóricos (#servicio, #competitivo, #informativo, #recreativo).
      - En proyectos con tags técnicos (#backend, #IA).
      - En ambos añadir una búsqueda en base a nombres.
- Sistema de "me gusta" y comentarios:
      - Interacción básica para medir el compromiso.
      - ¿Por qué? Fomenta la participación y retroalimentación.
- Perfiles de clubes:
      - Visualización de publicaciones y opción de seguirlos.
- Proyectos estudiantiles con aprobación manual:
      - Publicación tras revisión administrativa.
- Creación y eliminación de clubes: 
      - Únicamente posible de realizar por un usuario (universidad)

### Tecnologías Utilizadas
- Lenguaje(s): Java 21
- Frameworks: Spring Boot
- Base de datos: JPA, Postgres
- API externas: ...
- Otras herramientas: ...

---

## 🗃 Modelo de Entidades

### Diagrama de Entidades
![Diagrama ER o de clases](ruta/al/diagrama.png)

### Descripción de Entidades

### Descripción de Entidades

| Entidad      | Atributos Principales                                                                                       | Relaciones                                                                                                   |
|--------------|-------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------|
| **Club**       | email, nombre, descripcion, num_follow   | Tiene muchos `miembros` (entidad `Pertenece`) <br> Tiene muchos `seguidores` (`Usuario`)                    |
| **Usuario**    | email, nombre, apellidos, descripcion, tipo_usuario (Rol), publicaciones_likeadas| Puede pertenecer a muchos clubes (`Pertenece`) <br> Puede seguir clubes <br> Puede apoyar publicaciones    |
| **Alumno**     | Hereda de `Usuario`, tipo_usuario = Estudiante, carrera (Carrera), facultad (Facultad)| Un Alumno puede crear proyectos|
| **Profesor**   | Hereda de `Usuario`, tipo_usuario = Profesor, departamento (Departamento)| Un Profesor puede crear proyectos|
| **Pertenece**  | usuario_email, club_email, fecha_union (nullable), relacion (Relación)                                               | Representa la relación entre un `Usuario` y un `Club`                                                        |
| **Publicación**| id, fecha_publicacion, ultima_modificacion, titulo, descripcion, proyecto (bool), tag, num_apoyo, num_comentarios   | Tiene lista de apoyo (`Usuario`) <br> Tiene lista de comentarios <br> Puede estar asociada a un `Proyecto`  |
| **Proyecto**   | id (hereda de Publicación), proyecto = true, status (Status), fecha_cierre, personas_buscadas                        | Es una `Publicación` con campo `proyecto = true`                                                             |
| **Comentario** | id, publicacion_id, descripción, fecha_pub                                                                          | Pertenece a una `Publicación`                                                                                |

---

## 🧪 Testing y Manejo de Errores

### Niveles de Testing Realizados
- Pruebas unitarias
- Pruebas de integración
- Pruebas de sistema
- Pruebas de aceptación

### Resultados
- Total de pruebas realizadas
- Errores encontrados y cómo se resolvieron

### Manejo de Errores
- Uso de controladores de excepciones globales
- Ejemplo de excepción: `ResourceNotFoundException`
- Justificación de su uso

---

## 🔒 Medidas de Seguridad Implementadas

### Seguridad de Datos
- Cifrado de contraseñas con BCrypt
- Autenticación con JWT
- Roles y permisos

### Prevención de Vulnerabilidades
- Validación de entradas
- Protección contra SQL Injection
- CSRF y XSS

---

## 🔄 Eventos y Asincronía

- Eventos implementados: ...
- Uso de listeners asincrónicos con `@Async`
- Justificación de asincronía (mejorar rendimiento, desacoplar lógica)

---

## 🌐 GitHub

### Uso de GitHub Projects
- Organización por issues
- Asignación por miembros
- Uso de etiquetas y deadlines

### Uso de GitHub Actions
- Automatización de pruebas
- Despliegue continuo (CI/CD)
- Linting o validación de código

---

## ✅ Conclusión

### Logros del Proyecto
Breve resumen de lo que se logró.

### Aprendizajes Clave
- Lección 1
- Lección 2

### Trabajo Futuro
- Posibles mejoras
- Nuevas funcionalidades

---

## 📎 Apéndices

### Licencia
Este proyecto está licenciado bajo la [MIT License](LICENSE).

### Referencias
- Documentación oficial de Spring Boot
- Tutoriales y artículos utilizados
- Otros recursos relevantes
