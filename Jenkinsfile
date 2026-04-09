pipeline {
    agent any
    environment {
        DOCKER_HUB_USER = "moumba"
        IMAGE_NAME = "frontgc-backend"
        REGISTRY_CREDENTIALS = 'docker-hub-credentials'
    }
    stages {
        stage(' Build JAR') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }
        stage(' Docker Build & Push') {
            steps {
                script {
                    sh "docker build -t ${DOCKER_HUB_USER}/${IMAGE_NAME}:latest ."
                    withCredentials([usernamePassword(credentialsId: "${REGISTRY_CREDENTIALS}", passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        sh "docker login -u ${USER} -p ${PASS}"
                        sh "docker push ${DOCKER_HUB_USER}/${IMAGE_NAME}:latest"
                    }
                }
            }
        }
        stage(' Deploy to AWS') {
            steps {
                sshagent(['ssh-server-key']) {
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@15.188.37.53 'sudo docker-compose pull backend && sudo docker-compose up -d backend'"
                }
            }
        }
    }
}