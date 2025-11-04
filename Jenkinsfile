pipeline {
    // Uruchom na głównym agencie (naszym 'moj-jenkins')
    agent any

    // Opcja czyszczenia workspace'u - zostawmy ją, jest bezpieczna.
    options {
        cleanWs()
    }

    stages {
        // Etap budowania, testowania i pakowania
        stage('Build, Test & Package') {
            // Ten etap uruchomi się w osobnym kontenerze
            agent {
                docker { image 'maven:3.9-eclipse-temurin-17' }
            }
            steps {
                // Uruchomienie budowania Mavena
                // 'clean package' automatycznie odpala też testy
                sh 'mvn clean package'
            }
        }
    }
}