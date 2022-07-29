# **NWeather**

### **Architectural Pattern:**

App's design is built with **Clean Architecture + MVVM with Modularization by Feature instead of Clean Architecture with Monolithic architecture**. App works like **offline-first app**.

App implemented the functionality to retrieve weather forecast as suppose as  this functionality will be shared between many features. This implementation try to illustrate modularization when some features have same some functionalities in the project.

- **Offline-first(cache support):**
  - App always firstly get data (weather forecast) from local database(Room database).
  - If there is no data in local cache -> get data from network,
    - if successfully -> save data to local cache (update cache) and show data to UI,
    - if unsuccessfully -> show error to UI.
  - If there is data in local cache -> check cache is outdated or not yet(app's cache is set 5 minutes for expiration time)
    - if cache is not outdated yet -> show cache to UI
    - if cache is outdated -> fetch data from network, update cache, show to UI like above.
- **Cache database schema:**
  - There is one table to persist weather forecast information by city id with updated datetime, city id will be primary key. In this table, there is one column that contains weather forecast data is serialized from a pure data object of "domain" layer (in Clean Archi: each layer have its separated pure data model and have some mappers in the middle of their pure data model), because this pure data object is in "domain" layer(business logic) so it only contains necessary information for cache, not persist all information return from network like previous version.

### **Code Folder Structure:**

- **Note:**
  - Clean Archi implementation is put in **core-data**, **core-database**, **core-network**, **core-ui**, **core-domain** **module** when it implement **a functionality that will be shared for many features**. If a functionality serve only for one feature so all Clean Arch implementation(domain, data, device, ui... layers) of this functionality should put in that feature module, should not put here.
  - **Dependency rule between modules:**
    - One **feature** module should not know another feature module. It know only **core-** module
    - **core-** module should not know any **feature** module. It know only another **core-** module
- App' **modules**:
  - **app**: connects components together required for the app to function correctly(Dependency injection initialization, MainActivity, Application class, app-level navigation...)
  - **core-domain**: contains classes of "**domain**" layer in Clean Archi, **abstract business logic(entity, usecase, repository interface).**
  - **core-data**:  contains classes of "**data**" layer in Clean Archi, **abstract fetching app data (from disk or network)** and **cache mechanism**.
  - **core-database**: contains classes of "**data**" layer in Clean Archi, contains local database storage using Room.
  - **core-network**: contains classes of "**data**" layer in Clean Archi, making network requests and handling responses from a remote data source.
  - **core-ui**: contains classes of "**ui**" layer in Clean Archi, contains UI components, composables and resources, such as icons used by different features.
  - **feature-city-weather-forecast**: implement weather forecast feature.
  - **core-common**: contains common classes shared between modules(util, helper...)

### **Library & Feature:**

- **Koin**: to inject dependency.
- **Retrofit + okhttp**: to call network api.
- **Room**: to persist cache.
- **Moshi**: to parse JSON
- **ViewBinding**: to do not need use findViewById()
- **LiveData**: to keep app's state (LiveData is a regular observable + lifecycle-aware)
- **Kotlin Coroutine**: to execute asynchronous tasks.
- **Kotlin Flow**: to receive and custom multiple values emitted from source(reactive programming) by using their operator set. This app can also be implemented by using "suspend function" but with Flow is helpful in some cases like real-time database observation and have a rich operator set to apply on data emitted from source
- **JUnit + mockk**: to write unit test
- **desugar_jdk_libs**: to support Java 8+ API, to use java.time.* instead of ThreeTen library

### **Compatibility:**

- minSdk 21, targetSdk 32, Kotlin AndroidX

###  **Improvement:**

- Setup a best practice for build system configuration that work
  efficiently for modularization by feature.

###  **Functionalities:**

- [x] The application is a simple Android application which is written by Java/Kotlin.
- [x] The application is able to retrieve the weather information from OpenWeatherMaps API.
- [x] The application is able to allow user to input the searching term.
- [x] The application is able to proceed searching with a condition of the search term length must be from 3 characters or above.
- [x] The application is able to render the searched results as a list of weather items.
- [x] The application is able to support caching mechanism so as to prevent the app from generating a bunch of API requests.
- [x] The application is able to manage caching mechanism & lifecycle.
- [x] The application is able to handle failures.
- [ ] The application is able to support the disability to scale large text for who can't see the text clearly.
- [ ] The application is able to support the disability to read out the text using VoiceOver controls.

### **Expected Outputs**

- [x] 1. Programming language: Kotlin is required, Java is optional.
- [x] 2. Design app's architecture (suggest MVVM)
- [x] 3. Apply LiveData mechanism
- [x] 4. UI should be looks like in attachment.
- [x] 5. Write UnitTests *(Unit Tests have been written, remaining two or three methods in Repository and ViewModel using Kotlin Coroutine and Flow need to be cover, I'm researching for best practices to test Coroutine and Flow)*
- [ ] 6. Acceptance Tests
- [x] 7. Exception handling
- [x] 8. Caching handling
- [ ] 9. Secure Android app from:
  - [ ] a. Decompile APK
  - [ ] b. Rooted device
  - [ ] c. Data transmission via network
  - [ ] d. Encryption for sensitive information
- [ ] 10. Accessibility for Disability Supports:
  - [ ] a. Talkback: Use a screen reader.
  - [ ] b. Scaling Text: Display size and font size: To change the size of items on your screen, adjust the display size or font size.
- [ ] 11. Entity relationship diagram for the database and solution diagrams for the components, infrastructure design if any
- [x] 12. Readme file includes:
  - [x] a. Brief explanation for the software development principles, patterns & practices being applied
  - [x] b. Brief explanation for the code folder structure and the key Java/Kotlin libraries and frameworks being used
  - [x] c. All the required steps in order to get the application run on local computer
  - [x] d. Checklist of items the candidate has done.