pipeline {
    agent {
        docker { image 'maven:3.9-eclipse-temurin-17' }
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/SzymonBartkowiak43/HelloWordCICD.git'
            }
        }

        stage('Test') {
            steps {

                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn clean package'
            }
        }
    }
}