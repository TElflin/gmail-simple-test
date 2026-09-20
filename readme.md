To run test use console with command:
mvn test -Dbrowser="browser" -Denvironment="environment"
Available browser are: edge, chrome firefox
Available environment configs are: test and acc
For example to run test in chrome browser with test configuration use below command line:
mvn test -Dbrowser="chrome" -Denvironment="acc"