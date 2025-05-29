pipeline {
    agent any
     tools {
        maven 'maven-3.9.9'  // Name defined in Jenkins Global Tools Configuration
    }
    environment {
        APP_NAME = 'demo'
        DEPLOY_DIR = "/opt/${APP_NAME}"
        JAR_NAME = "${APP_NAME}-0.0.1-SNAPSHOT.jar"  // ✅ use actual filename from target/
        JAR_PATH = "target/${JAR_NAME}"
        LOG_FILE = "${DEPLOY_DIR}/${APP_NAME}.log"
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
                echo "🔍 Checking if anything is running on port $APP_PORT..."
                PID=$(lsof -ti :$APP_PORT || true)
                if [ -n "$PID" ]; then
                    echo "⚠️ Stopping process on port $APP_PORT (PID: $PID)"
                    kill -9 $PID
                else
                    echo "✅ Nothing is running on port $APP_PORT"
                fi
                '''
            }
        }
        stage('Deploy') {
            steps {
                sh '''
                mkdir -p $DEPLOY_DIR
                echo "Copying built JAR: $JAR_PATH to $DEPLOY_DIR"
                cp $JAR_PATH $DEPLOY_DIR/$JAR_NAME
                '''
            }
        }
        stage('Run') {
            steps {
                sh '''
                nohup java -jar $DEPLOY_DIR/$JAR_NAME > $LOG_FILE
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