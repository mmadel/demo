pipeline {
	agent any

    environment {
		APP_NAME = "demo"
        DEPLOY_DIR = "/opt/demo"
        SERVICE_NAME = "demo" // Name used in systemd
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
    }
}
