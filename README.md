# Room404 Frontend (Android)

## Descripción del proyecto

Este repositorio contiene el cliente frontend Android para Room404: una aplicación de puzzles con temática de ciberseguridad. Está desarrollada en Kotlin con Jetpack Compose y se comunica con el backend (Room404 BE) mediante Retrofit/OkHttp.

El objetivo de este README es proporcionar una guía técnica completa: arquitectura, instalación, configuración, endpoints consumidos, flujo de recuperación de contraseña por token, debugging y mapeo de archivos clave del proyecto.

---

## Tabla de contenidos

- **Descripción**
- **Tecnologías**
- **Estructura del proyecto**
- **Instalación y prerequisitos**
- **Configuración del entorno y variables**
- **Compilar y ejecutar**
- **Endpoints consumidos**
- **Flujo: recuperación de contraseña por token**
- **Deep links**
- **Depuración y verificación de requests**
- **Mapa de archivos clave**
- **Contribución**
- **Contacto**

---

## Tecnologías

- Kotlin
- Jetpack Compose
- Navigation Compose
- Retrofit2 + Gson + Scalars
- OkHttp + HttpLoggingInterceptor
- Coroutines (viewModelScope)
- Android Studio (Gradle)

---

## Estructura del proyecto

```
app/
 ├─ src/main/java/com/example/cocky_camel_room404_fe/
 │   ├─ ui/theme/        # UI composables y screens
 │   ├─ LoginViewModel.kt
 │   ├─ RegisterViewModel.kt
 │   ├─ SessionManager.kt
 │   ├─ RetrofitClient.kt
 │   └─ MainActivity.kt
 └─ src/main/AndroidManifest.xml
```

Detalles y enlaces a archivos clave en la sección "Mapa de archivos clave".

---

## Prerrequisitos

- Android Studio 2022.3+ con SDK Android apropiado
- JDK 17 (o la versión que use tu entorno de desarrollo)
- Emulador Android o dispositivo físico (conexión ADB)
- Para comunicarse con el backend local desde el emulador: el backend debe estar accesible (ver `BASE_URL` abajo). Por defecto el proyecto usa `http://10.0.2.2:8080/` para emulador.

---

## Configuración del entorno

1. Clona el repositorio:

```bash
git clone https://github.com/EricRodriguezRojo/Cocky_Camel_Room404_FE.git
cd Cocky_Camel_Room404_FE
```

2. Abrir en Android Studio: `File -> Open` y seleccionar el directorio del proyecto.

3. Variables a revisar/ajustar:

- BASE_URL: En `app/src/main/java/com/example/cocky_camel_room404_fe/ui/theme/RetrofitClient.kt` se define la URL base del backend. Para desarrollo local en emulador Android usar:

```
http://10.0.2.2:8080/
```

Si usas dispositivo físico o backend remoto, actualiza la URL adecuadamente.

---

## Dependencias importantes

- `com.squareup.retrofit2:retrofit`
- `com.squareup.retrofit2:converter-gson`
- `com.squareup.okhttp3:logging-interceptor`
- `org.jetbrains.kotlinx:kotlinx-coroutines-android`

Se declaran en `app/build.gradle.kts`.

---

## Compilar y ejecutar

Desde Android Studio: selección de módulo `app` -> Run.

Desde terminal (Windows PowerShell):

```powershell
.\gradlew assembleDebug
.\gradlew installDebug  # instala en dispositivo/emulador conectado
```

Para limpiar y reconstruir:

```powershell
.\gradlew clean
.\gradlew assembleDebug
```

---

## Endpoints consumidos (por el frontend)

Estos son los endpoints del backend que usa el frontend (ver `RetrofitClient`):

- POST `/api/user/login/{email}/{password}` — Login por email/contraseña
- POST `/api/user/google-login` — Login con Google
- POST `/api/user/forgot-password` — Solicitar correo de recuperación (body: `{ "email": "..." }`)
- POST `/api/user/verify-reset-token` — Verificar token (body: `{ "email":"...","token":"..." }`)
- POST `/api/user/reset-password` — Resetear contraseña (body: `{ "email":"...","token":"...","password":"..." }`)
- GET `/api/puzzles`, `/api/puzzle/{id}`, `/api/progress`, `/api/ranking`, etc.

Ejemplo curl usado por debugging:

```bash
curl -X POST http://localhost:8080/api/user/forgot-password -H 'Content-Type: application/json' -d '{"email":"user@example.com"}'
```

---

## Flujo: recuperación de contraseña por token (implementación FE)

Resumen del flujo implementado en la app:

1. El usuario pulsa "Olvidé mi contraseña" en la pantalla de login (`LoginScreen`).
2. `ForgotPasswordScreen` solicita el endpoint `/api/user/forgot-password` enviando `{ email }`.
   - Guarda temporalmente el `resetEmail` en `SessionManager` y navega a la pantalla de entrada del token.
3. El usuario introduce el token recibido por email en `TokenEntryScreen`.
   - Se llama a `/api/user/verify-reset-token` con `{ email, token }`.
4. Si el token es válido, se navega a `ResetPasswordScreen`.
   - El formulario envía `{ email, token, password }` a `/api/user/reset-password`.
5. En caso de éxito, se limpia `resetEmail` de `SessionManager` y se redirige al login.

Puntos importantes de implementación:

- El email se propaga entre pantallas usando `SessionManager` y el estado de `MainActivity` para evitar pérdida de dato en deep links.
- El payload del `reset-password` debe contener `email`, `token` y `password` (si falta alguno, el backend rechazará la petición).

---

## Deep links y manejo de token en enlaces

- El proyecto registra `intent-filter` en `AndroidManifest.xml` para manejar enlaces con esquema `room404://` y http/https.
- Al abrir la app con un enlace que contiene un token, `MainActivity` parsea `intent.data` y redirige al flujo de entrada de token.

Esto permite al email de recuperación contener un enlace directo que abre la app y pre-llena el token.

---

## Depuración y verificación de requests

1. Habilitar logging HTTP:
   - `RetrofitClient` incluye `HttpLoggingInterceptor` en OkHttp; revisa el nivel (BODY) para ver payloads JSON en Logcat.

2. Ver en Logcat:
   - Filtra por `OkHttp` o por el tag usado en la app para ver las peticiones y respuestas.

3. Validar payloads importantes:
   - Ver que la petición a `/api/user/reset-password` contenga las claves `email`, `token`, `password`.

4. Si usas emulador y el backend está local: asegúrate que el `BASE_URL` sea `http://10.0.2.2:8080/`.

5. Si necesitas inspeccionar tráfico HTTPS o usar un proxy (Charles/Fiddler), configura el emulador/dispositivo para redirigir tráfico.

---

## Mapa de archivos clave (qué hace cada uno)

- **`app/src/main/java/com/example/cocky_camel_room404_fe/ui/theme/RetrofitClient.kt`**: instancia singleton de Retrofit y definición de `Room404Api`. Ajustar `BASE_URL` aquí.
- **`app/src/main/java/com/example/cocky_camel_room404_fe/SessionManager.kt`**: helper de `SharedPreferences` para token, role, nickname y `resetEmail` temporal.
- **`app/src/main/java/com/example/cocky_camel_room404_fe/MainActivity.kt`**: navegación central, manejo de deep links y estado `recoveryEmail` compartido entre pantallas.
- **`app/src/main/java/com/example/cocky_camel_room404_fe/ui/theme/ForgotPasswordScreen.kt`**: UI y llamada a `/forgot-password`.
- **`app/src/main/java/com/example/cocky_camel_room404_fe/ui/theme/TokenEntryScreen.kt`**: UI para introducir token y llamada a `/verify-reset-token`.
- **`app/src/main/java/com/example/cocky_camel_room404_fe/ui/theme/ResetPasswordScreen.kt`**: UI de nueva contraseña y llamada a `/reset-password`.
- **`app/src/main/java/com/example/cocky_camel_room404_fe/LoginViewModel.kt`** y **`LoginScreen.kt`**: flujo de autenticación (login + navegación a "olvidé mi contraseña").

En el árbol del repositorio puedes abrir cualquiera de estos archivos para ver la implementación exacta.

---

## Buenas prácticas y notas importantes

- Evitar almacenar datos sensibles en texto plano; `SessionManager` sólo almacena tokens en SharedPreferences para sesión.
- Para producción: configurar `BASE_URL` y credenciales fuera del código, usar HTTPS y validar certificados.
- Externalizar claves y secretos (no mantener en el repo).

---

## Contribuir

1. Fork del repositorio
2. Crear rama feature: `git checkout -b feature/mi-cambios`
3. Hacer commits claros y atómicos
4. Abrir Pull Request explicando los cambios

### Guía de estilo rápida

- Mantener código Kotlin idiomático
- Usar corutinas y `viewModelScope` para operaciones asíncronas
- Evitar llamadas de red en la UI thread

---

## Arquitectura y decisiones técnicas (detallado)

1. Arquitectura general: la app sigue un patrón MVVM ligero:
   - **View (Compose)**: pantallas en `ui/theme/*` (composables para cada pantalla)
   - **ViewModel**: `*ViewModel.kt` para manejar estado y llamadas de red con corutinas
   - **Model / API**: `RetrofitClient` + DTOs usados por `Room404Api`

2. Networking:
   - `RetrofitClient` expone un singleton `instance` de `Room404Api`.
   - `OkHttpClient` incorpora `HttpLoggingInterceptor` (nivel BODY) para depuración de payloads.
   - Se usan `GsonConverterFactory` y `ScalarsConverterFactory` según endpoints.

3. Navegación:
   - Navigation Compose centralizada en `MainActivity.kt` con rutas nombradas para cada pantalla.
   - `MainActivity` mantiene un estado `recoveryEmail` que se pasa a los composables cuando es necesario (evita pérdida de datos en deep links).

4. Almacenamiento local y sesión:
   - `SessionManager.kt` usa `SharedPreferences` para token JWT, rol, nickname y un `resetEmail` temporal.
   - No almacenar contraseñas en texto plano.

5. Seguridad y producción:
   - En desarrollo se usa HTTP local (`http://10.0.2.2:8080/`). Para producción: HTTPS, validación de certificados y externalizar `BASE_URL`.

---

## Rutas de navegación y pantallas clave (mapa más detallado)

- `login` → `LoginScreen.kt` / `LoginViewModel.kt`
- `register` → `RegisterScreen.kt` / `RegisterViewModel.kt`
- `forgot_password` → `ForgotPasswordScreen.kt`
- `enter_token` → `TokenEntryScreen.kt`
- `reset_password/{token}` → `ResetPasswordScreen.kt`
- `main_menu` → `MainMenuScreen.kt` (entradas al resto de apps falsas)

Cada pantalla delega llamadas de red a `RetrofitClient.instance` y notifica resultados al usuario mediante `Toast` o diálogos.

---

## Modelos de datos usados por el frontend

- `LoginResponse` — token, message, role
- `User` — email, nickname, role, isPremium
- `ForgotPasswordRequest` — { email }
- `VerifyTokenRequest` — { email, token }
- `reset-password` request — { email, token, password }

Estos DTOs se encuentran en `RetrofitClient.kt`.

---

## Recuperación de contraseña: checklist de verificación (útil para testing)

1. Backend accesible y `BASE_URL` apuntando al servidor correcto.
2. Desde `LoginScreen` navegar a `ForgotPasswordScreen` e introducir email válido.
3. Ver en Logcat la petición POST a `/api/user/forgot-password` con body `{"email":"..."}`.
4. Comprobar que el backend envía un email (simulado o real). Si es simulado, revisar endpoint `/api/emails` si aplica.
5. Abrir enlace de email en emulador/dispositivo o introducir token manualmente en `TokenEntryScreen`.
6. Verificar llamada POST a `/api/user/verify-reset-token` con `{ email, token }`.
7. En `ResetPasswordScreen` introducir nueva contraseña y ver POST a `/api/user/reset-password` con `{ email, token, password }`.
8. Confirmar respuesta de éxito y que `SessionManager.clearResetEmail()` se invoque.

---

## Debugging avanzado

- Para ver payloads exactos en tiempo de ejecución: mirar Logcat y filtrar por `OkHttp` o `Retrofit`.
- Si la app no se conecta al backend local desde dispositivo físico, usar `adb reverse tcp:8080 tcp:8080` o exponer el backend (ngrok) y actualizar `BASE_URL`.
- Tests rápidos con `curl` para simular los endpoints desde el mismo equipo donde corre el backend.

Ejemplo: verificación manual del reset-password

```bash
curl -X POST http://localhost:8080/api/user/reset-password \
  -H 'Content-Type: application/json' \
  -d '{"email":"user@example.com","token":"123456","password":"NuevaPass123"}'
```

---

## Pruebas y calidad

- No hay un suite de tests unitarios integrado en este repo en este momento; agregar tests para ViewModels y utilidades es bienvenido.
- Validar manualmente los flujos críticos: login, registro, recuperación de contraseña, navegación deep-link.

---

## Contacto

### Autores

- **Eric Rodriguez Rojo** — pl2024431@365.stucom.com
- **Marc Fernández González** — pl2025196@365.stucom.com
- **Abril Palau Pardillos** — pl2024243@365.stucom.com

Repositorio GitHub: https://github.com/EricRodriguezRojo/Cocky_Camel_Room404_FE

---