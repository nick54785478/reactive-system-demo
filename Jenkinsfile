pipeline{
  agent any
  tools {
      jdk 'jdk' // 這邊的jdk17是在tools自行設定的名稱，要一致才可對應的到
      maven 'maven'
  }
  
  environment {
      harborUser = 'admin'
      harborPwd = 'Harbor12345'
      harborUrl = 'http://172.31.74.219:80'
      harborRepo = 'auth-service'
  }
  
  stages {
    stage('拉取git-hub 程式') {
        steps {
			checkout scmGit(branches: [[name:'${tag}']], extensions: [], userRemoteConfigs: [[credentialsId: '70a1f37d-a4bd-433f-8253-e8f475c21174', url: 'https://github.com/nick54785478/reactive-system-demo.git']])
            echo '拉取 Github 程式 -SUCCESS'
        }
    }
    stage('通過 maven 構建項目') {
        steps {
            sh '/var/jenkins_home/maven/bin/mvn clean install -DskipTests'
            echo '通過 maven 構建項目 -SUCCESS'
	    }
    }
    stage('通過sonarqube做代碼檢測') {
        steps {
            echo '通過sonarqube做代碼檢測'
			withSonarQubeEnv('sonar-qube') {
			    sh '/var/jenkins_home/tools/hudson.plugins.sonar.SonarRunnerInstallation/sonar-scanner/bin/sonar-scanner -Dsoner.source=./ -Dsonar.projectname=${JOB_NAME} -Dsonar.projectKey=${JOB_NAME} -Dsonar.java.binaries=./impl/target/'
			}
			echo '通過sonarqube做代碼檢測 -SUCCESS'
        }
    }
    stage('通過Docker構建image') {
        steps {
			steps {
		        sh 'docker build -t ${JOB_NAME}:${tag} .'
                echo '通過Docker構建image - SUCCESS'
            }
        }
    }
    stage('將image推送到harbor') {
        steps {
            sh '''docker login -u ${harborUser} -p  ${harborPwd} ${harborUrl}
                docker tag ${JOB_NAME}:${tag} ${harborUrl}/${harborRepo}/${JOB_NAME}:${tag}
                docker push ${harborUrl}/${harborRepo}/${JOB_NAME}:${tag}'''
            echo '將image推送到harbor - SUCCESS'
        }
    }
  }
}