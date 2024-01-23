trigger:
- main

pool:
  vmImage: 'windows-latest'

jobs:
- job: CopyFiles
  displayName: 'Copy Files'
  steps:
  - checkout: self

  # Copy files from the source repository to the artifacts directory
  - task: CopyFiles@2
    inputs:
      SourceFolder: 'path/to/source/files'  # Replace with the path of your source files
      Contents: '**'  # Copy all files
      TargetFolder: '$(Build.ArtifactStagingDirectory)'

  # Publish the copied files as build artifacts
  - task: PublishBuildArtifacts@1
    inputs:
      PathtoPublish: '$(Build.ArtifactStagingDirectory)'
      ArtifactName: 'copiedFiles'
      publishLocation: 'Container'

- job: DownloadAndPush
  displayName: 'Download and Push'
  dependsOn: CopyFiles
  steps:
  - checkout: self

  # Download the build artifacts containing the copied files
  - task: DownloadBuildArtifacts@0
    inputs:
      buildType: 'current'
      downloadType: 'single'
      artifactName: 'copiedFiles'
      downloadPath: '$(Build.ArtifactStagingDirectory)'

  # Commit and push the copied files to the target repository
  - script: |
      git config --global user.email "you@example.com"
      git config --global user.name "Your Name"

      # Replace with the URL of your target Azure Repos repository
      targetRepoUrl="https://<your-organization>.visualstudio.com/DefaultCollection/_git/<your-target-repo>"
      
      # Move to the directory containing the copied files
      cd $(Build.ArtifactStagingDirectory)/copiedFiles

      # Initialize a new Git repository
      git init

      # Add and commit the files
      git add .
      git commit -m "Copy files from source repository"

      # Add the target repository as a remote
      git remote add targetRepo $targetRepoUrl

      # Push the changes to the target repository
      git push -u targetRepo master
    displayName: 'Commit and Push'
