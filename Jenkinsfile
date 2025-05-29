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
        APP_PORT = 8090
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
        echo "🧹 Cleaning up old logs..."
        rm -f $LOG_FILE

        echo "🚀 Starting Spring Boot app..."
        nohup java -jar $DEPLOY_DIR/$JAR_NAME > $LOG_FILE 2>&1 &

        sleep 2

        echo "🔍 Checking if app started..."
        if ! ps aux | grep "$JAR_NAME" | grep -v grep > /dev/null; then
            echo "❌ App did not start correctly. Check the log at $LOG_FILE"
            cat $LOG_FILE
            exit 1
        else
            echo "✅ App started successfully."
        fi
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