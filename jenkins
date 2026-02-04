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
                echo 'Starting Code Compilation...'
                sh 'mvn clean compile'
                echo 'Code Compilation Completed Successfully!'
            }
        }
        stage('Code QA Execution') {
            steps {
                echo 'Running JUnit Test Cases...'
                sh 'mvn clean test'
                echo 'JUnit Test Cases Completed Successfully!'
            }
        }

       stage('Code Package') {
            steps {
                echo 'Creating WAR Artifact...'
                sh 'mvn clean package'
                sh '''
                    cp target/*.jar target/bookmyplan-1.1.${BUILD_NUMBER}.jar
                '''
                echo 'WAR Artifact Created Successfully!'
            }
        }
    }
}