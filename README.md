# ABOUT PROJECT:

## Steps to run:

- you need postgres DB connection. You can use provided k8s deployment.
- DB needs to have `super-chat-app` DB created
- edit application.yaml datasource - jdbc url and credentials
    - if you use provided k8s yamls files, credentials are set, but you need to update port to generated node port
- run application

#### Additional info:
- you can look at the BusinessLogicTest.kt to see happy path scenarios of the business flow
- after you successfully run application, you can also check swagger ui on default `/q/swagger-ui/`

- placeholders are [$user=chatUserUsername] for users and [$btc] for bitcoin price
- placeholders are static and their values are evaluated in the moment of receiving message

#### Potential improvements
- wrap lists returned from API in object, lists are problematic
- dockerfile, k8s deployment
- do grouping of messages on db level, instead of application level using ContactKey
- add unit tests (for now, decided that happy path integration tests would be more beneficial)