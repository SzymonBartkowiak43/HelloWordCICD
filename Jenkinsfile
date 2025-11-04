
def appName = "hellowordcicd"
def appPort = 8090
def dockerImageName = "szymon/${appName}"

pipeline {
    agent any

    options {
        cleanWs()
    }

    stages {

        stage('Build, Test & Package') {
            agent {
                docker { image 'maven:3.9-eclipse-temurin-21' }
            }
            steps {

                sh 'mvn clean package'
            }
        }

        stage('Build Docker Image') {
            agent any
            steps {
                script {

                    def imageWithTag = "${dockerImageName}:${env.BUILD_NUMBER}"
                    sh "docker build -t ${imageWithTag} ."


                    sh "docker tag ${imageWithTag} ${dockerImageName}:latest"
                }
            }
        }

        stage('Deploy Application') {
            agent any
            steps {
                script {

                    sh "docker stop ${appName} || true"
                    sh "docker rm ${appName} || true"

                    sh "docker run -d --name ${appName} -p ${appPort}:${appPort} ${dockerImageName}:latest"
                }
            }
        }
    }
}