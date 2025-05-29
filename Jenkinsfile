pipeline {
    agent any
     tools {
        maven 'maven-3.9.9'  // Name defined in Jenkins Global Tools Configuration
    }
    environment {
        JAR_NAME = 'app.jar'
        JAR_PATH = 'target\\app.jar'
        DEPLOY_DIR = 'C:\\apps\\springboot\\test'
        LOG_FILE = 'C:\\apps\\springboot\\test\\app.log'
    }
    stages {
        stage('Build') {
            steps {
                bat 'mvn clean package -DskipTests=false'
            }
        }
        stage('Stop Old App') {
            steps {
                bat '''
                for /f "tokens=2" %%i in ('netstat -ano ^| findstr :8080') do (
                    taskkill /PID %%i /F
                )
                '''
            }
        }
        stage('Run New App') {
            steps {
                bat '''
                if not exist %DEPLOY_DIR% mkdir %DEPLOY_DIR%
                copy %JAR_PATH% %DEPLOY_DIR%\\%JAR_NAME%
                start /b java -jar %DEPLOY_DIR%\\%JAR_NAME% > %LOG_FILE% 2>&1
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