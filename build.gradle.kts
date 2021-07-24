import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    `maven-publish`
    signing
    jacoco

    kotlin("jvm") version "1.5.21"
    id("com.adarshr.test-logger") version "3.0.0"
    id("org.jetbrains.dokka") version "1.4.32"
}

repositories {
    mavenCentral()
}

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.4.32")
    }
}

group = "com.github.eendroroy"
version = "1.0.1"
val isReleaseVersion = !"$version".endsWith("SNAPSHOT")

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("commons-codec:commons-codec:1.15")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    withSourcesJar()
    withJavadocJar()
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

tasks {
    dokkaJavadoc {
        outputDirectory.set(file("$buildDir/docs"))
    }

    javadoc {
        dependsOn(dokkaJavadoc)
    }
}

testlogger {
    slowThreshold = 10
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name.set(project.name)
                description.set("OTP Generator")
                url.set("https://github.com/eendroroy/kotp.git")

                licenses {
                    license {
                        name.set("MIT")
                        url.set("http://opensource.org/licenses/MIT")
                        distribution.set("repo")
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/eendroroy/kotp.git")
                    developerConnection.set("scm:git:https://github.com/eendroroy/kotp.git")
                    url.set("https://github.com/eendroroy/kotp.git")
                }

                developers {
                    developer {
                        id.set("eendroroy")
                        name.set("indrajit")
                        email.set("eendroroy@gmail.com")
                    }
                }
            }
        }
    }

    repositories {
        maven {
            val releaseRepo = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotRepo = "https://oss.sonatype.org/content/repositories/snapshots/"
            url = uri(if (isReleaseVersion) releaseRepo else snapshotRepo)
            credentials {
                username = project.findProperty("oss.user")?.toString() ?: System.getenv("OSS_USERNAME")
                password = project.findProperty("oss.key")?.toString() ?: System.getenv("OSS_TOKEN")
            }
        }
    }
}

signing {
    gradle.taskGraph.whenReady {
        isRequired = isReleaseVersion
    }
    sign(publishing.publications["mavenJava"])
}
