env.BUILD_IMAGE_CORE = "xiaozhi/backend-service"
env.REPO_SNAPSHOT = "192.168.200.56:5000"
env.SONAR_SERVER = "http://58.246.138.178:8095/"

node {
	stage ('check env') {
		sh 'mvn -v'
	}
	
    stage ('checkout code') {
         checkout(
  	[$class: 'SubversionSCM', 
  	filterChangelog: false, ignoreDirPropChanges: false, 
  	locations: [[credentialsId: 'svn-weifj', depthOption: 'infinity', ignoreExternalsOption: true, 
  	local: '.', remote: 'http://58.246.138.178:88/svn/SH2015GH057/branches/server/1.4.0-SNAPSHOT@HEAD']], 
  	workspaceUpdater: [$class: 'UpdateWithCleanUpdater']])
    }
  

    stage ('clean') {
    	sh 'mvn clean'
    }
    
    stage ('test') {
        //  
    	sh 'mvn test -Dmaven.test.failure.ignore'
    }
    stage('code analysis') {
        // requires SonarQube Scanner 2.8+
        env.sonarqubeScannerHome = tool name:'sonar-scanner-2.8', type: 'hudson.plugins.sonar.SonarRunnerInstallation'
        sh '${sonarqubeScannerHome}/bin/sonar-scanner'
        emailext body: '''Project: $PROJECT_NAME 
        Build Number: # $BUILD_NUMBER
        Build Status: $BUILD_STATUS
        sonarqube code static analysis http://58.246.138.178:8095/ to view the results.''', 
        recipientProviders: [[$class: 'DevelopersRecipientProvider']], 
        subject: 'Code static analysis - $PROJECT_NAME - Build # $BUILD_NUMBER', 
        to: 'weifj@dist.com.cn'
    }
    stage ('package') {
    	sh 'mvn clean package -Dmaven.test.skip=true'
    	emailext body: '''Project: $PROJECT_NAME 
        Build Number: # $BUILD_NUMBER
        Build Status: $BUILD_STATUS
        Check console output at $BUILD_URL to view the results.''', 
        recipientProviders: [[$class: 'DevelopersRecipientProvider']], 
        subject: '$BUILD_STATUS - $PROJECT_NAME - Build # $BUILD_NUMBER', 
        to: 'weifj@dist.com.cn'
    }
    stage('create docker image') {
     docker.build '$BUILD_IMAGE_CORE:$BUILD_NUMBER'
    }
	stage('publish docker image'){
     sh 'docker tag $BUILD_IMAGE_CORE:$BUILD_NUMBER $REPO_SNAPSHOT/$BUILD_IMAGE_CORE:$BUILD_NUMBER'
     sh 'docker push $REPO_SNAPSHOT/$BUILD_IMAGE_CORE:$BUILD_NUMBER'
   }
    stage ('run docker image') {
    try {
     
      echo 'Run application using Docker image'
      sh "docker run -d -p 1234:8080 --name backend-service-${env.BUILD_NUMBER} xiaozhi/backend-service:${env.BUILD_NUMBER}"

    } catch (error) {
    } finally {
      // Stop and remove container here
      
    }
   }
   stage('manual test'){
       input message: '测试是否通过', ok: '通过'
   }
   stage ('clean all') {
    	sh 'mvn clean'
   }
}
