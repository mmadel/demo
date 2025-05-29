pipeline {
    agent any

    environment {
        DEPLOY_DIR = "/opt/demo"
        JAR_NAME = "demo-0.0.1-SNAPSHOT.jar"
        LOG_FILE = "/opt/demo/demo.log"
        SSH_USER = "jenkins"             // Change this to your VPS user
        SSH_HOST = "localhost"
        SSH_CREDENTIALS = "jenkins-cred"  // Replace with your Jenkins SSH credential ID
        APP_PORT = "8090"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy & Run via SSH') {
            steps {
                sshagent([env.SSH_CREDENTIALS]) {
                    sh """
                    ssh -o StrictHostKeyChecking=no ${env.SSH_USER}@${env.SSH_HOST} '
                        echo "Stopping old app if running..."
                        pkill -f ${env.JAR_NAME} || true

                        echo "Cleaning up logs..."
                        rm -f ${env.LOG_FILE}

                        echo "Starting new Spring Boot app..."
                        setsid java -jar ${env.DEPLOY_DIR}/${env.JAR_NAME} --server.port=${env.APP_PORT} --server.address=0.0.0.0 > ${env.LOG_FILE} 2>&1 &

                        sleep 5

                        echo "Checking if app is listening on port ${env.APP_PORT}..."
                        if ! lsof -i :${env.APP_PORT} > /dev/null; then
                            echo "App failed to start or listen on port ${env.APP_PORT}"
                            echo "Last 50 lines of log:"
                            tail -n 50 ${env.LOG_FILE}
                            exit 1
                        fi

                        echo "App started successfully and listening on port ${env.APP_PORT}"
                    '
                    """
                }
            }
        }

        stage('Post-Deployment Health Check') {
            steps {
                // Simple curl check to localhost from Jenkins host
                sh """
                ssh -o StrictHostKeyChecking=no ${env.SSH_USER}@${env.SSH_HOST} '
                    curl --fail http://localhost:${env.APP_PORT}/demp/api/ping || exit 1
                '
                """
            }
        }
    }

    post {
        success {
            echo "Deployment successful and app is healthy!"
        }
        failure {
            echo "Deployment failed! Check logs and Jenkins console output."
        }
    }
}
