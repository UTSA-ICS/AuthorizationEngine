# Authorization Engine (AE) for Policy Machine (PM) and OpenStack Integration #

This is an authorization component for enforcing a User-Attribute Enhanced OpenStack Access Control model in OpenStack utilizing the Policy Machine.

## Configuration Details 
1. This component has been developed using eclipse IDE with JRE 1.6 and it has dependencies on PM classes.
2. A working PM project ahould be configured and build properly for AE to work correctly.
3. In the build path of AE project, PM project should be added under "Projects" for a successful build. 
4. The Main.java is the main class for AE and requires following program and VM arguments.
  1. Program argument:
  
    `-enginehost $host_ip$ -engineport 8080 -debug`
  2. VM arguments: 
  
    `-Djavax.net.ssl.keyStore=$keystore_path$ -Djavax.net.ssl.keyStorePassword=aaaaaa -Djavax.net.ssl.trustStore=$truststore_path$`
