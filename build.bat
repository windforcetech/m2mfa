call mvn clean package release:release -Dmaven.test.skip=true -P product
start target\production