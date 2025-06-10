pipeline {
	agent any

    environment {
		APP_NAME = "demo"
        DEPLOY_DIR = "/opt/demo"
        SERVICE_NAME = "demo" // Name used in systemd
        HEALTH_CHECK_URL = "http://localhost:8090/demo/api/actuator/health" // Update port/path if different
    }

    stages {

		stage('Build') {
			steps {
				sh 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy') {
			steps {
				script {
					def jarName = sh(script: "ls target/*.jar | head -n 1", returnStdout: true).trim()

                    sh """
                        sudo mkdir -p ${DEPLOY_DIR}
                        sudo cp ${jarName} ${DEPLOY_DIR}/${APP_NAME}.jar
                        sudo chown -R $USER:$USER ${DEPLOY_DIR}
                    """
                }
            }
        }

        stage('Restart Service') {
			steps {
				sh "sudo systemctl restart ${SERVICE_NAME}"
            }
        }
        stage('Sanity Check') {
			steps {
				script {
					// Wait for service to start (adjust delay and retries if needed)
					sh 'sleep 10'

					def response = sh(
						script: "curl -s -o /dev/null -w '%{http_code}' ${HEALTH_CHECK_URL}",
						returnStdout: true
					).trim()

					if (response != '200') {
						error "Health check failed! Service did not return HTTP 200. Got ${response} instead."
					}
				}
			}
		}
    }
}
