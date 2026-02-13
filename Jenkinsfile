pipeline {
    agent any

    environment {
        IMAGE_NAME = "bookmyplan"
        DOCKERHUB_REPO = "pankaj2204/demodockerrepo"
        ECR_REPO = "352731040067.dkr.ecr.ap-south-1.amazonaws.com/bookmyplan"
        NEXUS_REPO = "65.0.76.100:8085/bookmyplan"
    }

    stages {

        stage('Checkout Code') {
            steps {
                echo "Checking out source code..."
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "Building Docker image..."
                sh "docker build -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('Push Docker Image to DockerHub') {
            steps {
                script {
                    withDockerRegistry([credentialsId: 'dockerhub-credentials']) {
                        sh """
                            docker tag ${IMAGE_NAME}:latest ${DOCKERHUB_REPO}:latest
                            docker push ${DOCKERHUB_REPO}:latest
                        """
                    }
                }
            }
        }

        stage('Push Docker Image to Amazon ECR') {
            steps {
                script {
                    withDockerRegistry([
                        credentialsId: 'ecr:ap-south-1:ecr-credentials',
                        url: "https://${ECR_REPO}"
                    ]) {
                        sh """
                            docker tag ${IMAGE_NAME}:latest ${ECR_REPO}:latest
                            docker push ${ECR_REPO}:latest
                        """
                    }
                }
            }
        }

        stage('Upload Docker Image to Nexus') {
            steps {
                script {
                    withCredentials([
                        usernamePassword(
                            credentialsId: 'nexus-credentials',
                            usernameVariable: 'USERNAME',
                            passwordVariable: 'PASSWORD'
                        )
                    ]) {
                        sh """
                            docker login http://65.0.76.100:8085 -u $USERNAME -p $PASSWORD
                            docker tag ${IMAGE_NAME}:latest ${NEXUS_REPO}:latest
                            docker push ${NEXUS_REPO}:latest
                        """
                    }
                }
            }
        }

        stage('Clean Up Local Docker Images') {
            steps {
                echo "Cleaning local images..."
                sh """
                    docker rmi ${IMAGE_NAME}:latest || true
                    docker rmi ${DOCKERHUB_REPO}:latest || true
                    docker rmi ${ECR_REPO}:latest || true
                    docker rmi ${NEXUS_REPO}:latest || true
                    docker image prune -f
                """
            }
        }
    }

    post {
        success {
            echo "✅ Pipeline completed successfully!"
        }
        failure {
            echo "❌ Pipeline failed. Check logs."
        }
    }
}