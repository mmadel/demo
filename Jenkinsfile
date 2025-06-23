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
                if (!jarName) {
                    error "❌ No JAR file found in target directory!"
                }
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
