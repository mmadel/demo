pipeline {
    agent any 
    stages {
        stage('Build') { 
            steps {
                sh 'mvn -B -DskipTests clean package'
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