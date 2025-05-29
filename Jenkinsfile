pipeline {
    agent any 
    stages {
        stage('Build') { 
            steps {
                withMaven() {
                    sh 'mvn package'
                }
            }
        }
        stage('Test') { 
            steps {
                echo 'test application'
            }
        }
        stage('Deploy') { 
            steps {
                echo 'deploy application'
            }
        }
    }
}