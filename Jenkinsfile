// Zdefiniujmy zmienne na górze dla łatwiejszego zarządzania
// Użyj nazwy z <artifactId> w pom.xml, małymi literami
def appName = "hellowordcicd"
def appPort = 8090
// Twoja nazwa na Docker Hub lub po prostu lokalna nazwa
def dockerImageName = "szymon/${appName}"

pipeline {
    agent any

    options {
        cleanWs() // Czyści workspace przed każdym buildem
    }

    stages {

        // ETAP 1: Budowanie i Testowanie
        stage('Build, Test & Package') {
            agent {
                // KRYTYCZNA ZMIANA: Musimy użyć Mavena z JDK 21, tak jak w pom.xml
                docker { image 'maven:3.9-eclipse-temurin-21' }
            }
            steps {
                // 'clean package' automatycznie odpala też testy
                sh 'mvn clean package'
            }
        }

        // ETAP 2: Budowanie Obrazu Dockera
        stage('Build Docker Image') {
            agent any // Używamy głównego agenta, bo ma dostęp do Dockera
            steps {
                script {
                    // Używamy unikalnego tagu (numer builda Jenkinsa)
                    def imageWithTag = "${dockerImageName}:${env.BUILD_NUMBER}"

                    // Budujemy obraz na podstawie Dockerfile w naszym repo
                    sh "docker build -t ${imageWithTag} ."

                    // Opcjonalnie: stwórzmy też tag 'latest' dla łatwego dostępu
                    sh "docker tag ${imageWithTag} ${dockerImageName}:latest"
                }
            }
        }

        // ETAP 3: Wdrożenie Aplikacji
        stage('Deploy Application') {
            agent any // Używamy głównego agenta
            steps {
                script {
                    // Zatrzymujemy i usuwamy stary kontener (jeśli istnieje)
                    // '|| true' sprawia, że build nie zakończy się błędem, jeśli kontenera nie ma
                    sh "docker stop ${appName} || true"
                    sh "docker rm ${appName} || true"

                    // Uruchamiamy nowy kontener z obrazu, który właśnie zbudowaliśmy
                    // -d: uruchom w tle
                    // --name: nadaj mu stałą nazwę (tę samą co w 'docker stop')
                    // -p ${appPort}:${appPort}: mapuj port 8090 serwera na port 8090 kontenera
                    sh "docker run -d --name ${appName} -p ${appPort}:${appPort} ${dockerImageName}:latest"
                }
            }
        }
    }
}