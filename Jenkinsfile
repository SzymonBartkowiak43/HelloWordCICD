// == CZYSTY JENKINSFILE ==

def registryHost = "registry.digitalocean.com"
def registryNamespace = "hellooo"
def appName = "hellowordcicd"
def dockerImageName = "${registryHost}/${registryNamespace}/${appName}"
def releaseName = "hello-prod"
def chartPath = "helm-chart/"

pipeline {
    agent any

    stages {

        // ETAP 1: Buduje I CHOWA plik .jar
        stage('Build, Test & Package') {
            agent {
                docker { image 'maven:3.9-eclipse-temurin-21' }
            }
            steps {
                sh 'mvn clean package'
                stash(name: 'jar', includes: 'target/HelloWordCiCd-0.0.1-SNAPSHOT.jar')
            }
        }

        // ETAP 2: Wyciąga plik .jar i buduje obraz
        stage('Build Docker Image') {
            agent any
            steps {
                script {
                    // Wyciągnij plik .jar z magazynu do obecnego folderu
                    unstash 'jar'

                    def imageWithTag = "${dockerImageName}:${env.BUILD_NUMBER}"

                    echo "========================================="
                    echo "BUDOWANIE OBRAZU: ${imageWithTag}"
                    echo "========================================="

                    sh "docker build -t ${imageWithTag} ."

                    echo "--- DATA UTWORZENIA OBRAZU ---"
                    sh "docker inspect ${imageWithTag} | grep Created"
                    echo "========================================="

                    sh "docker tag ${imageWithTag} ${dockerImageName}:latest"
                }
            }
        }

        // ETAP 3: WYSYŁANIE OBRAZU
        stage('Push Image to DigitalOcean Registry') {
            agent any
            steps {
                withCredentials([usernamePassword(credentialsId: 'digitalocean-registry-creds', usernameVariable: 'DO_TOKEN_USER', passwordVariable: 'DO_TOKEN_PASS')]) {
                    sh "docker login ${registryHost} -u ${DO_TOKEN_USER} -p ${DO_TOKEN_PASS}"
                    sh "docker push ${dockerImageName}:${env.BUILD_NUMBER}"
                    sh "docker push ${dockerImageName}:latest"
                    sh "docker logout ${registryHost}"
                }
            }
        }

        // ETAP 4: WDROŻENIE NA K8S
        stage('Deploy to Kubernetes') {
            agent any
            steps {
                withKubeConfig(credenti
