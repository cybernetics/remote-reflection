# Remote Reflection API
Remote Reflection is a Java API which make it easier to call remote methods on remote JVM in very simple way without the need for any special configurations or common contract between client and server.

![alt tag](https://github.com/kiswanij/remote-reflection/blob/master/design/RemoteReflection-API-UML.PNG)

#Usage
In the server(the application which you want to expose its method remotely) , add the following after at the end of you main method:

public class YourMainClass{
...
public static void main(String args){
..
..
server = new ReflectionServer(PORT_NUMBER);
server.start();
}
}
 Thats it , now you can expose any method inside this application VM to your client.
 
In the client application(the application which should consume the remote method):
ReflectionClient client = new ReflectionClient(remoteHostIp, remoteServerPort);
MethodCallInfo info = new MethodCallInfo(className, methodName, param);
client.callMethod(info);

For example:		
		String className = "com.jk.reflection.test.TestRemoteObject";
		String methodName = "sayHello";
		MethodCallInfo info = new MethodCallInfo(className, methodName, "Jalal Kiswani");
		client.callMethod(info);
		Systtem.out.println(info.getResult());
		
it will print "Hello Jalal Kiswani";
		
Enjoy!
Jalal

 
