pipeline {
    agent any
     tools {
        maven 'maven-3.9.9'  // Name defined in Jenkins Global Tools Configuration
    }
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