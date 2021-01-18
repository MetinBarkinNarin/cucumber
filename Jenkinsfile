node() {

    def repoURL = 'https://github.com/MetinBarkinNarin/cucumber.git'

    stage("Prepare Workspace") {
        cleanWs()
        env.WORKSPACE_LOCAL = sh(returnStdout: true, script: 'pwd').trim()
        env.BUILD_TIME = sh(returnStdout: true, script: 'date +%F-%T').trim()
        echo "Workspace set to:" + env.WORKSPACE_LOCAL
        echo "Build time:" + env.BUILD_TIME
    }
    stage('Checkout Self') {
        git branch: 'xray_video', credentialsId: '', url: repoURL
    }
    stage('Cucumber Tests') {
        withMaven(maven: 'maven-3') {
            sh """
			cd ${env.WORKSPACE_LOCAL}
			mvn clean test
		"""
        }
    }
    stage('Expose report') {
        archive '**/cucumber.json'
        cucumber '**/cucumber.json'
    }
    stage('Import tasks to Jira for Features') {

	  
		def xrayConnectorId = "946c7738-281f-4ee9-bff5-5b78308fb9d7"
	step([$class: 'XrayImportFeatureBuilder', folderPath: 'target/test-classes/com/mycompany/app', projectKey: 'WOO', serverInstance: xrayConnectorId])
	    
	    def response = sh(script: "curl -u mnarin:mnarin -X GET -H 'Content-Type: application/json' 'http://10.150.17.73:8100/rest/api/2/issue/10123'", returnStdout: true)
	    
		echo response
	    echo $XRAY_TESTS
	
    }
    stage('Import results to Xray') {

		def description = "[BUILD_URL|${env.BUILD_URL}]"
		def labels = '["regression","automated_regression"]'
		def environment = "DEV"
		def testExecutionFieldId = 10007
		def testEnvironmentFieldName = "customfield_10131"
		def projectKey = "WOO"
		def xrayConnectorId = "946c7738-281f-4ee9-bff5-5b78308fb9d7"
		def info = '''{
				"fields": {
					"project": {
					"key": "''' + projectKey + '''"
				},
				"labels":''' + labels + ''',
				"description":"''' + description + '''",
				"summary": "Automated Regression Execution @ ''' + env.BUILD_TIME + ' ' + environment + ''' " ,
				"issuetype": {
				"id": "''' + testExecutionFieldId + '''"
				},
				"''' + testEnvironmentFieldName + '''" : [
				"''' + environment + '''"
				]
				}
				}'''
			echo info
	 step([$class: 'XrayImportBuilder', endpointName: 'CUCUMBER_MULTIPART', importFilePath: 'target/cucumber.json', importInfo: info, inputInfoSwitcher: 'fileContent', serverInstance: xrayConnectorId ])
    }
}
