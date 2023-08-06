import org.gradle.api.tasks.Delete
plugins {
    id("java")
}

group = "com.sadapay"
version = "1.0-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("junit:junit:4.13.2")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

sourceSets {
    main {
        java {
            srcDir("solution/src/main/java")
        }
    }
    test {
        java {
            srcDir("solution/test/main/java")
        }
    }
}

val cleanBinaries = project.tasks.register("cleanBinaries", Delete::class.java) {
    delete("build/bin")
}

val cleanLibs = project.tasks.register("cleanLibs", Delete::class.java) {
    delete("build/libs")
}

tasks.named("clean").configure {
    dependsOn(cleanBinaries)
    dependsOn(cleanLibs)
}