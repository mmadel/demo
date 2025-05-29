pipeline {
    agent any
     tools {
        maven 'maven-3.9.9'  // Name defined in Jenkins Global Tools Configuration
    }
    stages {
        stage('Build') { 
            steps {
                sh 'mvn clean compile'
            }
        }
        stage('Package') {
            steps {
               sh 'mvn package'
            }
        }
        stage('Archive') {
            steps {
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }
        stage('Run New JAR') {
            steps {
                sh '''
                    echo "Running new JAR..."
                    nohup java -jar $JAR_PATH > app.log 2>&1 &
                '''
            }
        }
    }
    post{
        failure {
            echo 'Build failed!'
        }
        success {
            echo 'Build successful!'
        }
    }
}