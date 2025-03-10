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
- Theme updates based user's device theme **(Material You)**

## 📸 Screenshots  
#### Landing Screen
<img src="https://github.com/user-attachments/assets/d4c758dc-8133-4c9a-9eb4-cb1917ccf8ad" width="20%" />
<img src="https://github.com/user-attachments/assets/5c869558-7925-4755-a236-048bcb359c9f" width="20%" />

#### Media [Grid View]
<img src="https://github.com/user-attachments/assets/5790a559-af07-47d5-a672-78bbf4971e04" width="20%" />
<img src="https://github.com/user-attachments/assets/d427688d-23ad-4765-a4a4-a14c0516cccd" width="20%" />
<img src="https://github.com/user-attachments/assets/2661b1e1-dc89-42c4-a9e5-16d863953192" width="20%" />
<img src="https://github.com/user-attachments/assets/0541b737-68fe-44c8-84be-1c6ebc0121fb" width="20%" />

#### Media [List View]
<img src="https://github.com/user-attachments/assets/f009b5a5-f6ec-430f-acde-c95446b58bd5" width="20%" />
<img src="https://github.com/user-attachments/assets/5f940a0e-9805-4d62-b0c9-648533940018" width="20%" />
<img src="https://github.com/user-attachments/assets/b160e74a-4294-41ba-9f86-d2a2c49a9c6f" width="20%" />
<img src="https://github.com/user-attachments/assets/75740ca8-d9d2-46de-96a3-5f528d203a47" width="20%" />

#### No Media Available 
<img src="https://github.com/user-attachments/assets/c13f541a-9c7c-4bdb-9da8-a7438e7dd6a7" width="20%" />
<img src="https://github.com/user-attachments/assets/4b3b6bd6-b405-4381-84c4-0aa5418188e0" width="20%" />

#### Permission Denied Rationale
<img src="https://github.com/user-attachments/assets/19fd5db2-a02f-459a-a3b6-e601dccb94e5" width="20%" />
<img src="https://github.com/user-attachments/assets/6d843528-1d9a-46d0-aeb5-258520706e6e" width="20%" />

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
- **data** → Handles data sources (LocalStorage, Database, etc.)  
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
