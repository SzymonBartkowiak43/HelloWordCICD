pipeline {
    agent {
        docker { image 'maven:3.9-eclipse-temurin-17' }
    }

    stages {
        stage('Build') {
            steps {
                checkout scm
                sh 'mvn clean package'
            }
        }
    }
}
