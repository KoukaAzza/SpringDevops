pipeline {
    agent any

    environment {
        SONAR_CREDENTIALS = credentials('c96c8d35-a68e-4fdd-a1dc-04ed95ee56b0')
        GIT_CREDENTIALS = credentials('6e1b6d0e-b9e5-4868-a709-7c1c5a16d124')
        GIT_REPO_URL = "github.com/mehdiaroua/DevopsBack.git"
        GIT_BRANCH = "main"
        REPO_DIR = 'DevopsBack'
    }

    stages {
       stage('Clone my Branch from Git') {
            steps {
                script {
                    // Check if the repository directory exists
                    if (fileExists(REPO_DIR)) {
                        // If the directory exists, update the existing repository
                        dir(REPO_DIR) {
                            sh 'git pull --rebase'
                        }
                    } else {
                        // If the directory does not exist, perform the initial clone
                        sh "git clone --branch ${env.GIT_BRANCH} https://mehdiaroua23:${env.GIT_CREDENTIALS}@${env.GIT_REPO_URL} ${REPO_DIR}"
                    }
                }
            }
        }
        stage('Clean and compile with Maven') {
            steps {
               dir(REPO_DIR) {
                    sh 'mvn clean compile'
                }
            }
        }
        stage('Test with Maven') {
            steps {
               dir(REPO_DIR) {
                    sh 'mvn test'
                }
            }
        }
        stage('Sonar test') {
            steps {
                dir(REPO_DIR) {
                    withCredentials([usernamePassword(credentialsId: 'c96c8d35-a68e-4fdd-a1dc-04ed95ee56b0', usernameVariable: 'SONAR_USER', passwordVariable: 'SONAR_PASSWORD')]) {
                        sh "mvn clean verify sonar:sonar -Dsonar.login=\$SONAR_USER -Dsonar.password=\$SONAR_PASSWORD"
                    }
                }
            }
        }
    }
}