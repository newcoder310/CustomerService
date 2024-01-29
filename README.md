parameters:
- name: selectedString
  type: string
  default: 'this is a test string'
  values:
    - 'this is a test string'
    - 'another string'
    # Add more options as needed

jobs:
- job: AssignVariableBasedOnParameter
  displayName: 'Assign Variable Based on Parameter'
  steps:
  - script: |
      selectedString="$(selectedString)"

      if [ "$selectedString" == "this is a test string" ]; then
        echo "Selected string is 'this is a test string'."
        urlForSelectedString="https://example.com/test-url"
      elif [ "$selectedString" == "another string" ]; then
        echo "Selected string is 'another string'."
        urlForSelectedString="https://example.com/another-url"
      else
        echo "Selected string is not recognized."
      fi

      echo "URL for selected string: $urlForSelectedString"
    displayName: 'Bash Script'
parameters:
- name: selectedString
  type: string
  default: 'this is a test string'
  values:
    - 'this is a test string'
    - 'another string'
    # Add more options as needed

variables:
  urlForSelectedString: ''

jobs:
- job: AssignVariableBasedOnParameter
  displayName: 'Assign Variable Based on Parameter'
  steps:
  - powershell: |
      if ("$(selectedString)" -eq 'this is a test string') {
        Write-Host "Selected string is 'this is a test string'."
        Write-Host "##vso[task.setvariable variable=urlForSelectedString]https://example.com/test-url"
      } elseif ("$(selectedString)" -eq 'another string') {
        Write-Host "Selected string is 'another string'."
        Write-Host "##vso[task.setvariable variable=urlForSelectedString]https://example.com/another-url"
      } else {
        Write-Host "Selected string is not recognized."
      }
    env:
      selectedString: $(selectedString)
    displayName: 'PowerShell Script'


parameters:
- name: selectedString
  type: string
  default: 'this is a test string'
  values:
    - 'this is a test string'
    - 'another string'
    # Add more options as needed

variables:
  urlForSelectedString: ''

jobs:
- job: AssignVariableBasedOnParameter
  displayName: 'Assign Variable Based on Parameter'
  steps:
  - powershell: |
      if ("$(selectedString)" -eq 'this is a test string') {
        Write-Host "Selected string is 'this is a test string'."
        Write-Host "##vso[task.setvariable variable=urlForSelectedString]https://example.com/test-url"
      } elseif ("$(selectedString)" -eq 'another string') {
        Write-Host "Selected string is 'another string'."
        Write-Host "##vso[task.setvariable variable=urlForSelectedString]https://example.com/another-url"
      } else {
        Write-Host "Selected string is not recognized."
      }
    env:
      selectedString: $(selectedString)



trigger:
- main

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
      git config --global user.email "you@example.com"
      git config --global user.name "Your Name"
      git clone https://<your-source-repo-url> $(Build.SourcesDirectory)/sourceRepo
    displayName: 'Clone Source Repository'

  # Create a new directory and copy files to it
  - script: |
      mkdir "$(Build.SourcesDirectory)/sourceRepo/src/main/java/com/abn/test/demo"
      cp -r "$(Build.SourcesDirectory)/sourceRepo/src/main/java/com/source-repo-path/*" "$(Build.SourcesDirectory)/sourceRepo/src/main/java/com/abn/test/demo/"
    displayName: 'Create Directory and Copy Files'

  # Initialize a new Git repository in the artifacts directory
  - script: |
      cd "$(Build.SourcesDirectory)/sourceRepo"
      git init
      git config --global user.email "you@example.com"
      git config --global user.name "Your Name"
    displayName: 'Initialize New Repository'

  # Copy all files from the source repository to the artifacts directory
  - script: |
      cp -r src/main/java/com/abn/test/demo/* "$(Build.SourcesDirectory)/sourceRepo"
    displayName: 'Copy All Files'

  # Commit and push all files to the target repository using a PAT for authentication
  - script: |
      cd "$(Build.SourcesDirectory)/sourceRepo"
      git add .
      git commit -m "Copy all files from source repository"

      # Replace with the URL of your target Azure Repos repository
      targetRepoUrl="https://<your-PAT>@<your-organization>.visualstudio.com/DefaultCollection/_git/<your-target-repo>"
      git remote add targetRepo $targetRepoUrl
      git push -u targetRepo master
    displayName: 'Commit and Push'
