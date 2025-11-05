// Źródło: Jenkinsfile (plik 2), ale oczyszczony z cytatów

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

                // Schowaj plik .jar do "magazynu" Jenkinsa
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
                    sh "docker build -t ${imageWithTag} ."
                    sh "docker tag ${imageWithTag} ${dockerImageName}:latest"
                }
            }
        }

        // ETAP 3: WYSYŁANIE OBRAZU
        stage('Push Image to DigitalOcean Registry') {
            agent any
            steps {
                // Użyj danych logowania (ID: 'digitalocean-registry-creds')
                withCredentials([usernamePassword(credentialsId: 'digitalocean-registry-creds', usernameVariable: 'DO_TOKEN_USER', passwordVariable: 'DO_TOKEN_PASS')]) {

                    // Zaloguj się do rejestru DigitalOcean używając tokena
                    sh "docker login ${registryHost} -u ${DO_TOKEN_USER} -p ${DO_TOKEN_PASS}"

                    // Wypchnij obraz (ten z numerem builda)
                    sh "docker push ${dockerImageName}:${env.BUILD_NUMBER}"
                    sh "docker push ${dockerImageName}:latest"

                    // Wyloguj się (dobra praktyka)
                    sh "docker logout ${registryHost}"
                }
            }
        }

        // ETAP 4: WDROŻENIE NA K8S
        stage('Deploy to Kubernetes') {
            agent any
            steps {
                // Upewnij się, że dodałeś kubeconfig z ID: 'doks-kubeconfig'
                withKubeConfig(credentialsId: 'doks-kubeconfig') {

                    sh """
                        helm upgrade --install ${releaseName} ${chartPath} \
                             --set image.tag=${env.BUILD_NUMBER} \
                             --wait
                    """
                }
            }
        }
    }
}