# common

[![CI](https://github.com/Pobiwan007/common/actions/workflows/ci.yml/badge.svg)](https://github.com/Pobiwan007/common/actions/workflows/ci.yml)
[![JitPack](https://jitpack.io/v/Pobiwan007/common.svg)](https://jitpack.io/#Pobiwan007/common)
![minSdk](https://img.shields.io/badge/minSdk-24-brightgreen)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?logo=kotlin&logoColor=white)

Small, dependency-light foundation shared by my Android libraries
([baseComposeView](https://github.com/Pobiwan007/baseComposeView),
[yandexTranslateModule](https://github.com/Pobiwan007/yandexTranslateModule)):
a typed `Result`, a domain error hierarchy and a few coroutine/Flow helpers.

## Why a custom `Result`?

`kotlin.Result` only carries a `Throwable`. Here the **error type is part of the signature**,
so a `when` over the result is checked by the compiler and every domain error has to be handled:

```kotlin
sealed interface Result<out D, out E : RootError> {
    data class Success<out D, out E : RootError>(val data: D) : Result<D, E>
    data class Error<out D, out E : RootError>(val error: E) : Result<D, E>
    data object Loading : Result<Nothing, Nothing>
}
```

Errors are sealed classes that carry a string resource, so the UI can render them without mapping code:

```kotlin
sealed class ApiError(val message: Int) : Error { // message is a string resource id
    data object NoInternetError : ApiError(R.string.no_internet_error)
    data object ServerError : ApiError(R.string.server_error)
    // ...
}
```

## Usage

```kotlin
// Repository: wrap a suspend call. IOException -> NoInternetError, anything else -> OtherError,
// CancellationException is rethrown so structured concurrency keeps working.
suspend fun loadUser(id: String): Result<User, ApiError> =
    safeResultApiCall { api.getUser(id) }.map { it.toDomain() }

// ViewModel
loadUser(id)
    .onSuccess { user -> _state.value = UiState.Content(user) }
    .onError { error -> _state.value = UiState.Error(error.message) }
```

`Flow.mutableStateIn` works like `stateIn`, but returns a `MutableStateFlow`, which is handy for
optimistic UI updates that are later overwritten by the source of truth:

```kotlin
val settings = repository.settingsFlow().mutableStateIn(viewModelScope, Settings.Default)
settings.value = settings.value.copy(darkTheme = true) // instant UI feedback
```

## Installation

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        maven("https://jitpack.io")
    }
}

// build.gradle.kts
dependencies {
    implementation("com.github.Pobiwan007:common:v1.0.0")
}
```

## Build & test

```bash
./gradlew testReleaseUnitTest assembleRelease
```

| Package | Contents |
|---|---|
| `com.common` | `Result`, `Error`, `ApiError`, `UsernameValidationError`, `map` / `onSuccess` / `onError` / `dataOrNull` |
| `com.common.coroutines` | `safeResultApiCall` |
| `com.common.flow` | `Flow.mutableStateIn` |
| `com.common.extension` | helpers for lists of key/value pairs |
