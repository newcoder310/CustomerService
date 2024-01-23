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

  # Clone the source repository
  - script: |
      git config --global user.email "you@example.com"
      git config --global user.name "Your Name"
      git clone https://<your-source-repo-url> sourceRepo
    displayName: 'Clone Source Repository'

  # Initialize a new Git repository in the artifacts directory
  - script: |
      cd $(Build.ArtifactStagingDirectory)
      git init
      git config --global user.email "you@example.com"
      git config --global user.name "Your Name"
    displayName: 'Initialize New Repository'

  # Copy all files from the source repository to the artifacts directory
  - script: |
      cp -r sourceRepo/* $(Build.ArtifactStagingDirectory)
    displayName: 'Copy All Files'

  # Commit and push all files to the target repository
  - script: |
      cd $(Build.ArtifactStagingDirectory)
      git add .
      git commit -m "Copy all files from source repository"

      # Replace with the URL of your target Azure Repos repository
      targetRepoUrl="https://<your-organization>.visualstudio.com/DefaultCollection/_git/<your-target-repo>"
      git remote add targetRepo $targetRepoUrl
      git push -u targetRepo master
    displayName: 'Commit and Push'
