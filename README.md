trigger: none

variables:
  - template: ./global.variables.yml
    parameters:
      feature: ${{ parameters.feature }}

parameters:
  - name: blockName
    type: string
    default: GRD0001031
  - name: repoName
    type: string
    default: jump-bedrock-test-repo
  - name: packageRoot
    type: string
    default: test
  - name: packageChild
    type: string 
    default: demo  
  - name: feature
    type: string
    values:
      - 'I want to be able use setup our infrastructure by updating very few properties'
      - 'I want to be able to seamlessly setup out deployment pipeline by updating very few properties'
      - 'I want to be able to expose apis our apis to the outside world but I don''t want to write any boiler plate code related to this'
      - 'I want to be able to consume apis but I don''t want to write any boiler plate code related to this'
      - 'I want to be able to validate my session using channel session manager'
  
pr:
  - '*'

pool:
  vmImage: 'windows-latest'

jobs:

- job: CopyRepo
  displayName: 'Copy Repository'
  steps:
    - checkout: self

    # Clone the source repository directly into $(Build.SourcesDirectory)
    - script: |
        echo $(featureValue)
        git -c http.extraHeader="AUTHORIZATION: bearer $(System.AccessToken)" clone $(featureValue) $(Build.SourcesDirectory)/sourceRepo
      displayName: 'Clone Source Repository'

    - ${{ if and(ne(parameters.feature, 'I want to be able use setup our infrastructure by updating very few properties'),ne(parameters.feature, 'I want to be able to seamlessly setup out deployment pipeline by updating very few properties')) }}: 
      # Create a new directory and copy files to it
      - script: |
          mkdir "$(Build.SourcesDirectory)/sourceRepo/src/main/java/com/abnamro/${{ parameters.packageRoot }}/${{ parameters.packageChild }}"
          cp -r "$(Build.SourcesDirectory)/sourceRepo/src/main/java/com/abnamro/jump/bedrocksb/"* "$(Build.SourcesDirectory)/sourceRepo/src/main/java/com/abnamro/${{ parameters.packageRoot }}/${{ parameters.packageChild }}/"
          rm -r "$(Build.SourcesDirectory)/sourceRepo/src/main/java/com/abnamro/jump/"
          mkdir "$(Build.SourcesDirectory)/sourceRepo/src/test/java/com/abnamro/${{ parameters.packageRoot }}/${{ parameters.packageChild }}"
          cp -r "$(Build.SourcesDirectory)/sourceRepo/src/test/java/com/abnamro/jump/bedrocksb/"* "$(Build.SourcesDirectory)/sourceRepo/src/test/java/com/abnamro/${{ parameters.packageRoot }}/${{ parameters.packageChild }}/"
          rm -r "$(Build.SourcesDirectory)/sourceRepo/src/test/java/com/abnamro/jump/"
        displayName: 'Create Directory and Copy Files'

      # Copy all files from the source repository to the artifacts directory
      - script: |
          cp -r "$(Build.SourcesDirectory)/sourceRepo/src/main/java/com/abnamro/${{ parameters.packageRoot }}/${{ parameters.packageChild }}/"* "$(Build.SourcesDirectory)/sourceRepo"
        displayName: 'Copy All Files'

    # Initialize a new Git repository in the artifacts directory
    - script: |
        cd "$(Build.SourcesDirectory)/sourceRepo"
        git init
        echo build user email: "$(Build.RequestedForEmail)"
        echo build user name: "$(Build.RequestedFor)"
        git config --global user.email "$(Build.RequestedForEmail)"
        git config --global user.name "$(Build.RequestedFor)"
      displayName: 'Initialize New Repository'      

    # Fetch changes from the target repository
    - script: |
        cd "$(Build.SourcesDirectory)/sourceRepo"
        git remote add targetRepo https://cbsp-abnamro@dev.azure.com/cbsp-abnamro/${{parameters.blockName}}/_git/${{parameters.repoName}}
        git -c http.extraHeader="AUTHORIZATION: bearer $(System.AccessToken)" fetch targetRepo
      displayName: 'Fetch changes from target repository'

    # Merge changes from the target repository
    - script: |
        cd "$(Build.SourcesDirectory)/sourceRepo"
        git merge targetRepo/master --no-edit
      displayName: 'Merge changes from target repository'

    # Commit merged changes
    - script: |
        cd "$(Build.SourcesDirectory)/sourceRepo"
        git add .
        git commit -m "Merge changes from source repository and target repository"
      displayName: 'Commit merged changes'

    # Push merged changes to the target repository
    - script: |
        cd "$(Build.SourcesDirectory)/sourceRepo"
        git push targetRepo master
      displayName: 'Push merged changes to target repository'
