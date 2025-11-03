# Guía de Tests de Integración

## ¿Qué hemos configurado?

Esta guía explica cómo están configurados los tests de integración en el proyecto y cómo funcionan.

## 📋 Configuración General

### 1. Dependencias (pom.xml)
Se agregaron las siguientes dependencias:
- **H2 Database**: Base de datos en memoria para tests
- **Mockito**: Para crear mocks de servicios externos

### 2. Perfil de Test (application-test.properties)
- **Base de datos**: H2 en memoria (modo MySQL)
- **DDL**: create-drop (limpia la BD después de cada test)
- **Valores dummy**: Para Firebase y Email (no se usan realmente)

### 3. Configuración de Mocks (TestConfig.java)
Proporciona beans mock para:
- **EmailService**: Evita envíos reales de email
- **FirebaseStorageService**: Evita conexiones reales a Firebase

### 4. Clase Base (BaseIntegrationTest.java)
Todas las clases de test extienden de esta:
- Configura el contexto de Spring Boot
- Activa el perfil "test"
- Crea un tenant de prueba antes de cada test
- Limpia la BD después de cada test (`@DirtiesContext`)

## ✅ ¿Los tests verifican la persistencia en BD?

**SÍ**, los tests verifican que los datos se guarden correctamente porque:

1. **Base de datos real (H2)**: No usamos mocks para los repositorios
2. **Flush y Clear**: Forzamos que se escriba en BD y limpiamos el cache
3. **Consultas reales**: Consultamos desde la BD para verificar

### Ejemplo de verificación de persistencia:

```java
@Test
void shouldSaveContainerAndPersist() {
    // 1. Guardamos
    SimpleContainerDTO savedContainer = containerService.save(containerDto);
    
    // 2. Forzamos escritura en BD
    entityManager.flush();
    entityManager.clear();
    
    // 3. Consultamos directamente desde BD
    Container persistedContainer = containerRepository.findById(savedContainer.getId())
        .orElseThrow();
    
    // 4. Verificamos
    assertThat(persistedContainer.getName()).isEqualTo("Test Container");
}
```

## 🚀 Cómo ejecutar los tests

### Desde línea de comandos:
```bash
# Todos los tests
./mvnw test

# Solo tests de un servicio específico
./mvnw test -Dtest=UserServiceIntegrationTest

# Con más información
./mvnw test -X
```

### Desde tu IDE:
- **IntelliJ IDEA**: Click derecho en la clase de test → Run
- **Eclipse**: Click derecho en la clase de test → Run As → JUnit Test

## 📝 Estructura de Tests

### Tests creados:

1. **UserServiceIntegrationTest**: 
   - Registro de usuarios
   - Actualización de perfil
   - Gestión de favoritos
   - Reset de contraseña

2. **ItemServiceIntegrationTest**:
   - Crear items
   - Buscar items
   - Actualizar items
   - Asignar tags RFID
   - Mover items entre contenedores

3. **ContainerServiceIntegrationTest**:
   - Crear contenedores
   - Actualizar contenedores
   - Eliminar contenedores
   - Verificación explícita de persistencia

4. **SectorServiceIntegrationTest**:
   - CRUD de sectores
   - Búsqueda por nombre

5. **MovementServiceIntegrationTest**:
   - Registro de movimientos
   - Historial de movimientos
   - Eliminación de movimientos

## 🔧 Solución al problema de @MockBean deprecated

En **Spring Boot 3.4+**, `@MockBean` fue deprecated.

### ❌ Enfoque INCORRECTO:
```java
@MockBean  // ← deprecated
private EmailService emailService;
```

### ✅ Enfoque CORRECTO (que usamos):
```java
// En TestConfig.java
@Bean
@Primary
public EmailService emailService() {
    return mock(EmailService.class);
}

// En las clases de test
@Autowired  // ← inyectamos el mock
private EmailService emailService;
```

Este approach es mejor porque:
- No usa APIs deprecated
- Es más limpio (un mock para todos los tests)
- Evita problemas de inicialización de Firebase/Email
- Más rápido (se crea una sola vez)

## 🎯 Ventajas de estos tests

1. **Tests de Integración Reales**: Prueban todo el stack (Service → Repository → BD)
2. **Aislamiento**: Cada test tiene su propia BD limpia
3. **Rapidez**: H2 es muy rápida
4. **Sin servicios externos**: Mock de Email y Firebase
5. **Verificación de persistencia**: Confirman que los datos se guardan correctamente

## 🔍 Debugging

Si un test falla:

1. **Ver logs SQL**: Están habilitados en `application-test.properties`
2. **Agregar breakpoints**: En tu IDE
3. **Ver estado de BD**: Los tests son transaccionales, puedes inspeccionar

```java
@Test
void myTest() {
    // Agregar para debugging
    System.out.println(userRepository.findAll());
    
    // Tu test...
}
```

## 📊 Cobertura de Tests

Los tests cubren:
- ✅ Operaciones CRUD básicas
- ✅ Búsquedas y filtros
- ✅ Relaciones entre entidades
- ✅ Validaciones y excepciones
- ✅ Transacciones
- ✅ Persistencia en BD

## 💡 Próximos Pasos

Puedes agregar más tests para:
- `ReaderService`
- `TenantService`
- Casos edge más específicos
- Tests de rendimiento
- Tests de concurrencia

---

**¿Dudas?** Los tests están documentados con `@DisplayName` en español para facilitar su comprensión.

