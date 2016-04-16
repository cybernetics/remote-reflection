# Remote Reflection API
Remote Reflection is a Java API which make it easier to call remote methods on remote JVM in very simple way without the need for any special configurations or common contract between client and server.

##Usage
###Server 
In the server application(the application which you want to expose its method remotely) , add the following after at the end of you main method:
>	    public class YourMainClass{
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
>      ReflectionClient client = new ReflectionClient(remoteHostIp, remoteServerPort);
		MethodCallInfo info = new MethodCallInfo(className, methodName, param);
		client.callMethod(info);		

**For example:**		
>		String className = "com.jk.reflection.test.TestRemoteObject";
		String methodName = "sayHello";
		MethodCallInfo info = new MethodCallInfo(className, methodName, "Jalal Kiswani");
		client.callMethod(info);
		System.out.println(info.getResult());
		
It will print "Hello Jalal Kiswani";
		
##API Design (UML)
![alt tag](https://github.com/kiswanij/remote-reflection/blob/master/design/RemoteReflection-API-UML.PNG)

Enjoy!
**Jalal**

 
