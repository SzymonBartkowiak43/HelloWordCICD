def registryHost = "registry.digitalocean.com"
def registryNamespace = "hellooo"
def appName = "hellowordcicd"
def dockerImageName = "${registryHost}/${registryNamespace}/${appName}"
def releaseName = "hello-prod"
def chartPath = "helm-chart/"

pipeline {
    agent any

    stages {
        stage('Build & Package') {
            agent {
                docker { image 'maven:3.9-eclipse-temurin-21' }
            }
            steps {
                sh 'mvn clean package'
                stash(name: 'jar', includes: 'target/*.jar')
            }
        }

        stage('Build Docker Image') {
            agent any
            steps {
                script {
                    unstash 'jar'
                    sh "docker build -t ${dockerImageName}:latest ."
                }
            }
        }

        stage('Push to Registry') {
            agent any
            steps {
                withCredentials([usernamePassword(credentialsId: 'digitalocean-registry-creds', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                    sh """
                        echo \$PASS | docker login ${registryHost} -u \$USER --password-stdin
                        docker push ${dockerImageName}:latest
                        docker logout ${registryHost}
                    """
                }
            }
        }

        stage('Deploy to Kubernetes') {
            agent any
            steps {
                withKubeConfig(credentialsId: 'doks-kubeconfig') {
                    sh """
                        helm upgrade --install ${releaseName} ${chartPath} --set image.tag=latest --wait
                        kubectl rollout status deployment ${releaseName}-deployment --timeout=120s
                    """
                }
            }
        }
    }
}