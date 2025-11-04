pipeline {
    agent any  // Zmienione z docker na any

    tools {
        maven 'Maven 3.9'  // Nazwa musi odpowiadać konfiguracji w Jenkins Global Tool Configuration
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