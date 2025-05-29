pipeline {
    agent any

    environment {
        DEPLOY_DIR = '/opt/demo'
        JAR_NAME = 'demo-0.0.1-SNAPSHOT.jar'
        LOG_FILE = "${DEPLOY_DIR}/demo.log"
        PORT = '8090'
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    echo "Stopping old app if running..."
                    pkill -f $JAR_NAME || true

                    echo "Cleaning up logs..."
                    rm -f $LOG_FILE

                    echo "Starting new Spring Boot app..."
                    nohup java -jar $DEPLOY_DIR/$JAR_NAME --server.port=$PORT --server.address=0.0.0.0 > $LOG_FILE 2>&1 &

                    sleep 10
                    echo "Checking if app is listening on port $PORT..."
                    if ! lsof -i :$PORT > /dev/null; then
                        echo "App failed to start or listen on port $PORT"
                        echo "Last 50 lines of log:"
                        tail -n 50 $LOG_FILE
                        exit 1
                    fi
                    echo "App started successfully and listening on port $PORT"
                '''
            }
        }
    }
}
