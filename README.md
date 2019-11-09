# BikesAndroidApp

  Android APP that calls bysykkel REST services to fetch bikes and docs availability and displays them in a Google Map.
  
  - Build: 
    - Clone the git repo to your local folder `GIT_REPO`
    - cd `GIT_REPO`
    - Launch the following to build the app: `./gradlew build`
    - The built debug apk is available in `./app/build/outputs/apk/debug/app-debug.apk` 
  - JUnit Tests:
    - The tests are located in `app/src/test/java/org/gpo/bikesmap/service/BikeServiceTest.kt`
    - Launch the following to run the tests: `./gradlew test`
    - Test results are available in `app/build/reports/tests/testDebugUnitTest/index.html`
