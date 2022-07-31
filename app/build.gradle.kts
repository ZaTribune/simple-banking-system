plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")

    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")

}

application {
    mainClass.set("banking.App")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}