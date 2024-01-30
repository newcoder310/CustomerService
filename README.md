parameters:
- name: selectedString
  type: string
  default: 'I want to use infra bedrock'
  values:
    - 'I want to use infra bedrock'
    - 'I want to use producer bedrock'

variables:
- name: gitCloneUrl
  ${{ if eq(parameters.selectedString, 'I want to use infra bedrock') }}:
    value: 'https://test.git/infra'
  ${{ if eq(parameters.selectedString, 'I want to use producer bedrock') }}:
    value: 'https://test.git/producer'

jobs:
- job: GitClone
  displayName: 'Git Clone Based on Selected String'
  steps:
  - checkout: self
    persistCredentials: true
    clean: true
    displayName: 'Git Clone Repository'
    inputs:
      repository: $(gitCloneUrl)
