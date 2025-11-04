pipeline {
    agent any

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