# Atomic Gallery  

Atomic Gallery is a modern gallery application built entirely using Jetpack Compose. It follows a modular architecture and leverages modern Android development best practices. The app supports dynamic UI customization with Material 3, allowing users to switch between different layouts and themes.  

## 🚀 Features  
- Fully built with Jetpack Compose  
- **Compose Previews for UI development**  
- Supports **List & Grid** view switching  
- **Dynamic theme switching** (Light, Dark, System)  
- Uses Flow for reactive data handling  
- Dependency injection with Hilt  
- **Unit test cases included**  
- Image loading with Coil  
- Modular architecture for scalability  

## 🏛️ Architecture  
The project follows a clean and modular architecture:  

```
app -> :feature:gallery, :common

:feature:gallery -> :domain, :common

:domain -> :data, :common

:data -> :common
```

### **Modules Overview**  
- **app** → Main application entry point  
- **feature:gallery** → Contains UI and feature-specific logic  
- **domain** → Contains business logic and use cases  
- **data** → Handles data sources (API, Database, etc.)  
- **common** → Contains shared utilities  

## 🛠️ Tech Stack  
- **Jetpack Compose** - UI Toolkit  
- **Material 3 (M3)** - Modern Android UI components  
- **Flow** - Reactive streams  
- **Hilt** - Dependency injection  
- **Coil** - Image loading  

## 📦 Setup & Installation  
1. Clone the repository  
   ```sh
   git clone https://github.com/moinkhan-tech-in/AtomicGallery
   cd AtomicGallery
   ```
2. Open in Android Studio  
3. Sync Gradle and Run the App  

## 🧪 Running Unit Tests  
To run unit tests for all modules, use the following Gradle command:  

```sh
./gradlew test
```

To run tests for a specific module, use:  

```sh
./gradlew :feature:gallery:testDebugUnitTest
or
./gradlew :feature:gallery:test
```
![image](https://github.com/user-attachments/assets/0b29c2a5-fd9d-4b99-8da0-45eabe8af9d5)
