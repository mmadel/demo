pipeline {
    agent any 
    stages {
        stage('Build') { 
            steps {
                mvn 'clean install'
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