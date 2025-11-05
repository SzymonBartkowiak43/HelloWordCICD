[cite_start]def registryHost = "registry.digitalocean.com" [cite: 1]
def registryNamespace = "hellooo"
def appName = "hellowordcicd"
def dockerImageName = "${registryHost}/${registryNamespace}/${appName}"
def releaseName = "hello-prod"
def chartPath = "helm-chart/"

pipeline {
    agent any

    stages {

        // ETAP 1 (Bez zmian)
        stage('Build, Test & Package') {
            agent {
                docker { image 'maven:3.9-eclipse-temurin-21' }
            }
            steps {
                [cite_start]sh 'mvn clean package' [cite: 2]
                stash(name: 'jar', includes: 'target/HelloWordCiCd-0.0.1-SNAPSHOT.jar')
            }
        }

        // ETAP 2 (Dodane logowanie)
        stage('Build Docker Image') {
            agent any
            steps {
                script {
                    unstash 'jar'

                    [cite_start]def imageWithTag = "${dockerImageName}:${env.BUILD_NUMBER}" [cite: 3]

                    // --- DODANE LOGOWANIE ---
                    echo "========================================="
                    echo "BUDOWANIE OBRAZU: ${imageWithTag}"
                    echo "========================================="

                    [cite_start]sh "docker build -t ${imageWithTag} ." [cite: 3]

                    echo "--- DATA UTWORZENIA OBRAZU ---"
                    sh "docker inspect ${imageWithTag} | grep Created"
                    echo "========================================="
                    // --- KONIEC LOGOWANIA ---

                    [cite_start]sh "docker tag ${imageWithTag} ${dockerImageName}:latest" [cite: 4]
                }
            }
        }

        // ETAP 3 (Bez zmian)
        stage('Push Image to DigitalOcean Registry') {
            [cite_start]agent any [cite: 6]
            steps {
                withCredentials([usernamePassword(credentialsId: 'digitalocean-registry-creds', usernameVariable: 'DO_TOKEN_USER', passwordVariable: 'DO_TOKEN_PASS')]) {
                    [cite_start]sh "docker login ${registryHost} -u ${DO_TOKEN_USER} -p ${DO_TOKEN_PASS}" [cite: 7]
                    sh "docker push ${dockerImageName}:${env.BUILD_NUMBER}"
                    sh "docker push ${dockerImageName}:latest"
                    [cite_start]sh "docker logout ${registryHost}" [cite: 8]
                }
            }
        }

        // ETAP 4 (Dodane logowanie)
        stage('Deploy to Kubernetes') {
            [cite_start]agent any [cite: 9]
            steps {
                [cite_start]withKubeConfig(credentialsId: 'doks-kubeconfig') { [cite: 10]

                    // --- DODANE LOGOWANIE ---
                    echo "========================================="
                    echo "WDRAŻANIE NA KUBERNETES"
                    echo "Aplikacja (Release): ${releaseName}"
                    echo "Nowy Tag Obrazu: ${env.BUILD_NUMBER}"
                    echo "========================================="

                    sh """
                        helm upgrade --install ${releaseName} ${chartPath} \
                             --set image.tag=${env.BUILD_NUMBER} \
                             --wait
                    [cite_start]""" [cite: 11]
                [cite_start]} [cite: 12]
            }
        }
    }
}