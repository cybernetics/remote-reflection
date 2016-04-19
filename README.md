# Remote Reflection API
Remote Reflection is a Java API which make it easier to call remote methods on remote JVM in very simple way without the need for any special configurations or common contract between client and server.

##Usage
1- Create new maven project.  
2- Add JK-DB dependency to your `pom.xml` inside the dependencies sections 

		<dependency>
			<groupId>com.jalalkiswani</groupId>
			<artifactId>jk-remote-reflection</artifactId>
			<version>0.0.1</version>
		</dependency>
    
3- Be sure to set the minimum JDK level in your pom file to 1.7 by adding the following sections inside `build-->plugins` section :

	<plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.3</version>
        <configuration>
          <!-- http://maven.apache.org/plugins/maven-compiler-plugin/ -->
          <source>1.7</source>
          <target>1.7</target>
        </configuration>
      </plugin>   
      
###Server 
In the server application(the application which you want to expose its method remotely) , add the following after at the end of you main method:
	
	public class YourMainClass{
		...
	    public static void main(String args){
	    ...	    
	    ReflectionServer server = new ReflectionServer(PORT_NUMBER);
	    server.start();
		}
	}
 
Thats it , now you can expose any method inside this application VM to your application client.
 
###Client
In the client application(the application which should consume the remote method):

       ReflectionClient client = new ReflectionClient(remoteHostIp, remoteServerPort);
		MethodCallInfo info = new MethodCallInfo(className, methodName, param);
		client.callMethod(info);		

**For example:**		

		String className = "com.jk.reflection.test.TestRemoteObject";
		String methodName = "sayHello";
		MethodCallInfo info = new MethodCallInfo(className, methodName, "Jalal Kiswani");
		client.callMethod(info);
		System.out.println(info.getResult());
		
`Output : Hello Jalal Kiswani`
		
##API Design (UML)
![alt tag](https://github.com/kiswanij/remote-reflection/blob/master/design/RemoteReflection-API-UML.PNG)

Enjoy!  
Jalal  
http://www.jalalkiswani.com

 
