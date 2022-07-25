**Architectural Pattern:**
App's design is built with **MVVM pattern** and **offline-first app**.

- **MVVM pattern:**
  - **V**: UI (Activity, Fragment).
  - **VM**: ViewModel contains LiveData(app's state), V(UI) observes these states.
  - **M**: Model and business logic(Repository, Dao Room, API network call).

- **Offline-first(cache support):**
  - App always firstly get data (weather forecast) from local database(Room database).
  - If there is no data in local cache -> get data from network,
    - if successfully -> save data to local cache (update cache) and show data to UI,
    - if unsuccessfully -> show error to UI.
  - If there is data in local cache -> check cache is outdated or not yet(app's cache is set 5 minutes for expiration time)
    - if cache is not outdated yet -> show cache to UI
    - if cache is outdated -> fetch data from network, update cache, show to UI like above.
- **Cache database schema:**
  - There is one table to persist result return from network request with updated datetime, URL will be primary key in this table. With this schema for cache storage will may be avoid some migration tasks for database.

**Code Folder Structure:**
- **.data**: **.disk** contains implementation for reading/writing data to local database, **.network** contains implementation for network
  api call.
- **.di**: contains dependency injection.
- **.enum**: contains enums.
- **.model**: contains pure data model.
- **.repository**: contains implementation for data repository, offline-first mechanism.
- **.screen**: contains UI classes and files like Fragment with its ViewModel.
- **.util**: contains util classes and files for datetime formatter, default value
- **.viewmodel**: contains common viewmodel classes.

**Library & Feature:**
- **Koin**: to inject dependency.
- **Retrofit + okhttp**: to call network api.
- **Room**: to persist cache.
- **Moshi**: to parse JSON
- **ViewBinding**: to do not need use findViewById()
- **LiveData**: to keep app's state (LiveData is a regular observable + lifecycle-aware)
- **Kotlin Coroutine**: to execute asynchronous tasks.
- **Kotlin Flow**: to receive and custom multiple values emitted from source(reactive programming) by using their operator set. This app
  can also be implemented by using "suspend function" but with Flow is
  helpful in some cases like real-time database observation and have a
  rich operator set to apply on data emitted from source
- **JUnit + mockk**: to write unit test
- **desugar_jdk_libs**: to support Java 8+ API, to use java.time.* instead of ThreeTen library

**Compatibility:**
- minSdk 21, targetSdk 32, Kotlin AndroidX

**Functionalities:**
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

**Expected Outputs**
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
