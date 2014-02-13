In order to build VocBench, you need:

- a copy of Semantic Turkey (you may check the version of Semantic Turkey exploited
  by VocBench by inspecting the vocbench-web/vocbench-web-sematicturkey/pom.xml).
  Check http://semanticturkey.uniroma2.it/downloads/ for Semantic Turkey downloads 
  and then select the appropriate version to download
  
- build the two VocBench projects (just run maven clean install on both of them):
  - vocbench-web (this will produce a war file)
  - vocbench-st-bundle (this will produce an OSGi bundle, with .jar extension)

- Follow install instructions on: 
  http://vocbench.uniroma2.it/documentation/#reqandinstall