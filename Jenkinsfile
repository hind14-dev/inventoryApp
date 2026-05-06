pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh '''stage(\'Build\') {
            steps {
                echo \'Building application...\'
            }
'''
        }
      }

      stage('Test') {
        steps {
          sh ''' stage(\'Test\') {
            steps {
                echo \'Running tests...\'
            }'''
          }
        }

        stage('docker build') {
          steps {
            sh '''stage(\'Docker Build\') {
            steps {
                script {
                    sh \'docker build -t myapp:latest .\'
                }
            }'''
            }
          }

        }
      }