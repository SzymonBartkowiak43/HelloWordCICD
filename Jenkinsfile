pipeline {
    agent any

    // DODAJ TĘ SEKCJĘ
    options {
        // To polecenie mówi Jenkinsowi: "Zawsze wyczyść workspace
        // przed pobraniem kodu".
        cleanWs()
    }

    stages {
        stage('Build, Test & Package') {
            agent {
                docker { image 'maven:3.9-eclipse-temurin-17' }
            }
            steps {
                sh 'mvn clean package'
            }
        }
    }
}