pipeline {

    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '5', artifactNumToKeepStr: '5'))
    }

    tools {
        maven 'mvn_3.9.12'
    }

    stages {

        stage('Code Compilation') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Code QA Execution') {
            steps {
                sh 'mvn test'
            }
        }
        stage('SonarQube Code Quality') {
            environment {
                scannerHome = tool 'qube'
            }
            steps {
                echo 'Starting SonarQube Code Quality Scan...'
                withSonarQubeEnv('sonar-server') {
                    sh 'mvn sonar:sonar'
                }
                echo 'SonarQube Scan Completed. Checking Quality Gate...'
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
                echo 'Quality Gate Check Completed!'
            }
        }

        stage('Code Package') {
            steps {
                sh 'mvn package'
                sh 'cp target/*.jar target/bookmyplan-${BUILD_NUMBER}.jar'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t bookmyplan:latest .'
            }
        }

        stage('Tag Docker Image') {
            steps {
                sh '''
                docker tag bookmyplan:latest pankaj2204/demodockerrepo:latest
                '''
            }
        }
// ---------------- DOCKER Image Scan ----------------
        stage('Docker Image Scanning') {
            steps {
                echo 'Scanning Docker Image with Trivy...'
                sh 'trivy image bookmyplan:latest || echo "Scan Failed - Proceeding with Caution"'
                echo 'Docker Image Scanning Completed!'
            }
        }

        // ---------------- DOCKER HUB ----------------

        stage('Push to Docker Hub') {
            steps {
                withCredentials([string(credentialsId: 'dockerhubCred', variable: 'TOKEN')]) {
                    sh '''
                    echo "$TOKEN" | docker login -u pankaj2204 --password-stdin
                    docker push pankaj2204/demodockerrepo:latest
                    '''
                }
            }
        }

        // ---------------- AWS ECR ----------------

        stage('Push to Amazon ECR') {
            steps {
                script {
                    withDockerRegistry(
                            [credentialsId: 'ecr:ap-south-1:ecr-credentials',
                             url          : 'https://352731040067.dkr.ecr.ap-south-1.amazonaws.com']) {

                        sh '''
                        docker tag bookmyplan:latest 352731040067.dkr.ecr.ap-south-1.amazonaws.com/bookmyplan:latest
                        docker push 352731040067.dkr.ecr.ap-south-1.amazonaws.com/bookmyplan:latest
                        '''
                    }
                }
            }
        }

        // ---------------- NEXUS ----------------

        stage('Upload Docker Image to Nexus') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'nexus-credentials',
                            usernameVariable: 'USERNAME',
                            passwordVariable: 'PASSWORD')]) {

                        sh 'docker login http://65.0.69.2:8085/repository/bookmyplan/ -u admin -p ${PASSWORD}'
                        echo "Push Docker Image to Nexus : In Progress"
                        sh 'docker tag bookmyplan 65.0.69.2:8085/bookmyplan:latest'
                        sh 'docker push 65.0.69.2:8085/bookmyplan'
                        echo "Push Docker Image to Nexus : Completed"
                    }
                }
            }
        }

            // ---------------- CLEANUP ----------------

            stage('Cleanup Docker Images') {
                steps {
                    sh '''
                docker rmi pankaj2204/demodockerrepo:latest || true
                docker rmi bookmyplan:latest || true
                docker rmi 352731040067.dkr.ecr.ap-south-1.amazonaws.com/bookmyplan:latest || true
                docker rmi 13.203.30.87:8085/bookmyplan/bookmyplan:latest || true
                docker image prune -f
                '''
                }
            }
        }
    }
