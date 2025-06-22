pipeline {
    agent any

    environment {
        APP_NAME = "demo"
        DEPLOY_DIR = "/opt/demo"
        SERVICE_NAME = "demo"
        HEALTH_CHECK_URL = "http://161.97.126.192:8090/demo/api/actuator/health" // remote service URL
        REMOTE_USER = "root"
        REMOTE_HOST = "161.97.126.192" // use container name, IP, or hostname
        REMOTE_SSH_PORT = "22"   // change to 2222 if needed
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

stage('Deploy') {
    steps {
        sshagent (credentials: ['test-env-user']) {
            script {
                def jarName = sh(script: "ls target/*.jar | head -n 1", returnStdout: true).trim()

                // Create deploy dir and ensure service file exists
                sh """
                    ssh -p ${REMOTE_SSH_PORT} ${REMOTE_USER}@${REMOTE_HOST} '
                        sudo mkdir -p ${DEPLOY_DIR} && sudo chown ${REMOTE_USER}:${REMOTE_USER} ${DEPLOY_DIR}

                        SERVICE_FILE="/etc/systemd/system/${SERVICE_NAME}.service"
                        if [ ! -f "\$SERVICE_FILE" ]; then
                            echo "Creating missing service file at \$SERVICE_FILE"
                            sudo bash -c "cat > \$SERVICE_FILE" <<EOF
[Unit]
Description=${APP_NAME} Spring Boot Application
After=network.target

[Service]
User=${REMOTE_USER}
ExecStart=/usr/bin/java -jar ${DEPLOY_DIR}/${APP_NAME}.jar
StandardOutput=file:/var/log/${DEPLOY_DIR}/${APP_NAME}.log
StandardError=file:/var/log/${DEPLOY_DIR}/${APP_NAME}-error.log
SuccessExitStatus=143
Restart=always
RestartSec=5
SyslogIdentifier=${APP_NAME}

[Install]
WantedBy=multi-user.target
EOF
                            sudo systemctl daemon-reload
                            sudo systemctl enable ${SERVICE_NAME}
                        fi
                    '
                """

                // Copy JAR to remote server
                sh "scp -P ${REMOTE_SSH_PORT} ${jarName} ${REMOTE_USER}@${REMOTE_HOST}:${DEPLOY_DIR}/${APP_NAME}.jar"

                // Restart service
                sh "ssh -p ${REMOTE_SSH_PORT} ${REMOTE_USER}@${REMOTE_HOST} 'sudo systemctl restart ${SERVICE_NAME} && sleep 10'"
            }
        }
    }
}

        stage('Sanity Check') {
            steps {
                sshagent (credentials: ['test-env-user']) {
                    script {
                        def response = sh(
                            script: "ssh -p ${REMOTE_SSH_PORT} ${REMOTE_USER}@${REMOTE_HOST} 'curl -s -o /dev/null -w \"%{http_code}\" ${HEALTH_CHECK_URL}'",
                            returnStdout: true
                        ).trim()

                        if (response != '200') {
                            error "❌ Health check failed! Service did not return HTTP 200. Got ${response} instead."
                        } else {
                            echo "✅ Health check passed with status ${response}."
                        }
                    }
                }
            }
        }
    }
}
