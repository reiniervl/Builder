
mvn install:install-file -Dfile=target/Builder-0.9.8.jar -DpomFile=pom.xml
mvn install:install-file -Dfile=target/Builder-0.9.8-sources.jar -DgroupId=com.rvlstudio -DartifactId=Builder -Dversion=0.9.8 -Dpackaging=jar -Dclassifier=sources
mvn install:install-file -Dfile=target/Builder-0.9.8-javadoc.jar -DgroupId=com.rvlstudio -DartifactId=Builder -Dversion=0.9.8 -Dpackaging=jar -Dclassifier=javadoc