pipeline {
    agent any

    options {
        cleanWs()
    }

    stages {
        stage('Checkout') {
            steps {
                // Pobiera repozytorium skonfigurowane w jobie
                checkout scm
            }
        }

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
