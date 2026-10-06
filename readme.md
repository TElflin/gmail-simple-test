To run test use console with command:
mvn test -Dbrowser="browser" -Denvironment="environment" -Dsuite="test.suit.xml"
Available browser are: edge, chrome firefox
Available environment configs are: test and acc
Available test suits are: regression.test.xml, smoke.test.xml
For example to run smoke test in chrome browser with test configuration use below command line:
mvn test -Dbrowser="chrome" -Denvironment="acc" -Dsuite="smoke.test.xml"