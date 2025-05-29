pipeline {
    agent any 
    stages {
        stage('Build') { 
            steps {
                sh 'mvn clean install'
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