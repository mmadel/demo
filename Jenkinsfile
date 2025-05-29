pipeline {
    agent any
     tools {
        maven 'maven-3.9.9'  // Name defined in Jenkins Global Tools Configuration
    }
    environment {
        JAR_NAME = 'app.jar'
        JAR_PATH = 'target/app.jar'
        DEPLOY_DIR = '/home/jenkins/app'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests=false'
            }
        }
         stage('Stop Old App') {
            steps {
                sh '''
                PID=$(lsof -t -i:8080)
                if [ -n "$PID" ]; then
                    kill -9 $PID
                fi
                '''
            }
        }
        stage('Run New App') {
            steps {
                sh '''
                mkdir -p $DEPLOY_DIR
                cp $JAR_PATH $DEPLOY_DIR/$JAR_NAME
                nohup java -jar $DEPLOY_DIR/$JAR_NAME > $DEPLOY_DIR/app.log 2>&1 &
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