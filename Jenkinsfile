def registryHost = "registry.digitalocean.com"
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
                sh 'mvn clean package'
            }
        }

        // ETAP 2 (Bez zmian)
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

        // ETAP 3: WYSYŁANIE OBRAZU (ZMIENIONY)
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

        // ETAP 4: WDROŻENIE NA K8S (Plugin Kubeconfig jest potrzebny!)
        stage('Deploy to Kubernetes') {
            agent any
            steps {
                // Upewnij się, że dodałeś kubeconfig z ID: 'doks-kubeconfig'
                // ORAZ zainstalowałeś narzędzia (helm, kubectl) na serwerze Jenkinsa!
                withKubeConfig(credentialsId: 'doks-kubeconfig') {

                    sh """
                        helm upgrade --install ${releaseName} ${chartPath} \
                             --set image.tag=${env.BUILD_NUMBER} \
                             --wait
                    """
                    // Nie musimy już ustawiać image.repository, bo jest
                    // na stałe wpisane w values.yaml
                }
            }
        }
    }
}