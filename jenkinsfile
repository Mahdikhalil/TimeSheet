pipeline{
    agent { label "devops" }
    options {
        buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '5', daysToKeepStr:'', numToKeepStr: '5')
        disableConcurrentBuilds()
    }
    stages {
        stage("hello"){
            steps {
                echo "hello"
            }
        }
    }

}