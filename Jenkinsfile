pipeline {
    agent any

    stages {

        stage('Build') {
            steps {
                dir('inventory-app') {
                    sh 'mvn clean package'
                }
            }
        }

        stage('Test') {
            steps {
                dir('inventory-app') {
                    sh 'mvn test'
                }
            }
        }

        stage('Docker Build') {
            steps {
                dir('inventory-app') {
                    sh 'docker build -t inventoryapp:latest .'
                }
            }
        }
    }

    post {
        success {
            echo 'sa marche bien 😎'
        }
    }
}