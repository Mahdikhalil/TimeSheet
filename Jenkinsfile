pipeline {
  agent any
  stages {
    stage('Checkout GIT') {
      steps {
        echo "pulling";
        git branch: "feature/Mahdi", url: "https://github.com/Mahdikhalil/TimeSheet";
      }
    }
    stage("Test, Build and sonar") {
      steps {
        bat "mvn package sonar:sonar"
      }

    }
    stage("Nexus stage") {
        steps {
            bat "mvn clean package deploy:deploy-file -DgroupId=tn.esprit.spring -DartifactId=spring-boot-data-jpa-entity -Dversion=3.0 -DgeneratePom=true -Dpackaging=jar -DrepositoryId=deploymentRepo -Durl=http://localhost:8081/repository/maven-releases/ -Dfile=target/Timesheet-spring-boot-core-data-jpa-mvc-REST-1-0.0.1-SNAPSHOT.jar"
        }}
  }
    post {
      always {


            emailext body: "${currentBuild.currentResult}: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}",
                mimeType: 'text/html',
                subject: "[Jenkins] ${env.JOB_NAME}",
                to: "mahdi.khalil@esprit.tn",

                recipientProviders: [[$class: 'CulpritsRecipientProvider']]

        echo 'Testss!'


      }
    }


}