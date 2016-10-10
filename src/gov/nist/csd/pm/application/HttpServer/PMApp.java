package gov.nist.csd.pm.application.HttpServer;

import gov.nist.csd.pm.common.application.SSLSocketClient;
import gov.nist.csd.pm.common.info.PMCommand;
import gov.nist.csd.pm.common.net.Item;
import gov.nist.csd.pm.common.net.ItemType;
import gov.nist.csd.pm.common.net.Packet;
import gov.nist.csd.pm.common.util.CommandUtil;
import gov.nist.csd.pm.common.util.RandomGUID;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.swing.*;

import static gov.nist.csd.pm.common.info.PMCommand.GET_PERMITTED_OPS;
import static gov.nist.csd.pm.common.net.LocalHostInfo.getLocalHost;
import static gov.nist.csd.pm.common.net.Packet.failurePacket;

import java.util.HashMap;

public class PMApp {
	private static final String HOST = "localhost";
	private static final int PORT = 8080;
	/**
	 * @uml.property name="serverHostName"
	 */
	private String serverHostName;
	/**
	 * @uml.property name="serverPortNumber"
	 */
	private int serverPortNumber;
	/**
	 * @uml.property name="sessionId"
	 */
	private String sessionId;
	/**
	 * @uml.property name="sslClient"
	 * @uml.associationEnd
	 */
	private SSLSocketClient sslClient = null;
	/**
	 * @uml.property name="fakeSession"
	 */
	private boolean fakeSession = false;
	/**
	 * @uml.property name="sOpsetContainerDN"
	 */
	private String sOpsetContainerDN;
	/**
	 * @uml.property name="sUserAttrContainerDN"
	 */
	private String sUserAttrContainerDN;
	/**
	 * @uml.property name="sThisDomain"
	 */
	private String sThisDomain = null;
	/**
	 * @uml.property name="ctx"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	private DirContext ctx;

	private String ErrorMessage = null;

	Main obj1 = new Main(); // instance of Main class

	public void initialize(String host, int port) {

		if (host == null || host.length() == 0) {
			this.serverHostName = HOST;
		} else {
			this.serverHostName = host;
		}

		if (port < 1024) {
			this.serverPortNumber = PORT;
		} else {
			this.serverPortNumber = port;
		}

		char[] ksPass = null;
		ksPass = "aaaaaa".toCharArray();

		// Set up the SSL client part of the PM and check the connection
		try {
			System.setProperty("javax.net.ssl.keyStorePassword", new String(ksPass));
			sslClient = new SSLSocketClient(this.serverHostName, serverPortNumber, true, "PMApp");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Please check whether the PM engine has been started!\n"
					+ "(Unable to create SSL socket for " + serverHostName + ":" + serverPortNumber + ")");
			e.printStackTrace();
		}

		Packet res = (Packet) checkPmConnection();
		if (res.hasError()) {
			JOptionPane.showMessageDialog(null, "Unable to connect to server: " + res.getErrorMessage());
		}
	}

	private String getSessionUser(String sSessId) {
		try {
			Packet cmd = makeCmd("getSessionUser", sSessId);
			Packet res = sslClient.sendReceive(cmd, null);
			if (res == null || res.isEmpty()) {
				return null;
			}
			return res.getStringValue(0);
		} catch (Exception e) {
			return null;
		}
	}

	private String getSessionName(String sSessId) {
		try {
			Packet cmd = makeCmd("getSessionName", sSessId);
			Packet res = sslClient.sendReceive(cmd, null);
			if (res == null | res.isEmpty()) {
				return null;
			}
			return res.getStringValue(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Object checkPmConnection() {
		try {
			Packet cmd = makeCmd("connect");
			Packet res = sslClient.sendReceive(cmd, null);
			if (res == null || res.isEmpty()) {
				return failurePacket("Connect: undetermined error");
			}
			return res;
		} catch (Exception e) {
			return failurePacket("Exception in connect: " + e.getMessage());
		}
	}

	public boolean checkPermissionForRoleAndAttr(String user, String role, String objectName) {

		// we have user 1, and query PM for the attributes of user1, this name
		// is coming OS,
		// query the object and make sure user1 is there for attribute value is
		// being checked

		// given a user and Attr name, need to get hashmap of attribute for that
		// user and check if the attribute and attribute value is present
		// if the user attribute exists then do a PM policy check

		// how to get attribute for a user sent by OpenStack???
		
	

		Packet res1 = null;
		Packet res2 = null;
		Packet res3 = null;
		Packet res4 = null;
		Packet res5 = null;
		boolean rolePermission, attrPermission, isRoleCentricABAC;
		

		try {

			Packet cmd3 = CommandUtil.makeCmd(PMCommand.GET_USER_ATTRIBUTES, null,  "super");
			res3 = sslClient.sendReceive(cmd3, null); 
			isRoleCentricABAC = checkPolicyType(res3);
			
			if (isRoleCentricABAC){
//				String[] rolePerm = new String[(role.length)];				
//				for(int i =0;i<role.length;i++)
//				{							
				
				
				Packet cmd1 = CommandUtil.makeCmd(PMCommand.GET_OPSETS_BETWEEN, null, role, objectName, "o");
				res1 = sslClient.sendReceive(cmd1, null);
				rolePermission = checkRole(res1);
				
//				rolePerm[i]= String.valueOf(checkRole(res1));
//				if (rolePerm[i].equals("true"))
//					break;
//				}
				
//				for(String s: rolePerm){
//						if(s.equals("true"))
//							rolePermission =  true;
//					}
//					rolePermission =  false;
//				}
			
				//if any one value of this rolePerm is true
				
			//Packet cmd3 = CommandUtil.makeCmd(PMCommand.GET_USER_ATTRIBUTES,null,  "OpenStack");
			//res3 = sslClient.sendReceive(cmd3, null);
			//printResContent(res3);
			
			//Packet cmd5 = CommandUtil.makeCmd(PMCommand.GET_USERS_OF,  null, "IT");
			//res5 = sslClient.sendReceive(cmd5, null);
			
			
			//openSession(user, user);
			//Packet cmd4 = CommandUtil.makeCmd(PMCommand.GET_USERS_AND_ATTRS,sessionId);
			//res4 = sslClient.sendReceive(cmd4, null);

				if (rolePermission){
				// open a session for a user only if user is not in the opened
				// session map
				// if user is in map then use the old session ID.
					if (obj1.checkMap(user)) {
					//System.out.println("The user already has an  entry of session id in the user-sessId map.");
					sessionId = obj1.getFromMap(user);
					} else {
					//System.out.println("The user does not has a session open.");
						boolean sesOpen = openSession(user, user); // open session and store the session
												// ID in a {user,sessionID} map.
				
						if (sesOpen){
						//System.out.println("Session successfully open for "+user);
						obj1.addInMap(user, sessionId);
						//String temp = obj1.getFromMap(user);
						//System.out.println("The sessionId for "+user+" is "+temp);
						//System.out.println("Session Id added in the hashmap");
						} else{
							//System.out.println("There is an error while opening session - "+ ErrorMessage);
							return false;
						}
											
				}
				
				// closeSession();
				RandomGUID myGUID = new RandomGUID();
				String sProcessId = myGUID.toStringNoDashes();
				String sProcId = sProcessId;
				sProcId = "5D86D8B4B574F82D079E08655D256AE4"; // a fake process id
	
				Packet cmd2 = CommandUtil.makeCmd(GET_PERMITTED_OPS, sessionId, null, objectName);
				res2 = sslClient.sendReceive(cmd2, null);
				
				attrPermission = checkAttribute(res2);
				//System.out.println("The attribute permission is "+attrPermission);
				
				if (attrPermission) {
					//System.out.println(
							//"Access Allowed to [" + objectName + "] for user  [" + user + "] with role [" + role + "]");
					return true;
				} else {
					//System.out.println(
							//"Access Denied to [" + objectName + "] for user  [" + user + "] with role [" + role + "]");
					return false;
				}
				
				//System.out.println("Access Allowed to [" + objectName + "] for user  [" + user + "] with role [" + role + "]");
				//return true;
			} else {
				//System.out.println("Access Denied to role [" + role + "]");
				return false;
			}
			} else {
				Packet cmd1 = CommandUtil.makeCmd(PMCommand.GET_OPSETS_BETWEEN, null, role, objectName, "o");
				res1 = sslClient.sendReceive(cmd1, null);
				rolePermission = checkRole(res1);
				
				if (rolePermission){
					//System.out.println("Access Allowed to role [" + role + "]");
					return true;
				}else{
					//System.out.println("Access Denied to role [" + role + "]");
					return false;
				}
			}
				
			// Packet cmd3 = CommandUtil.makeCmd(PMCommand.GET_PROPERTY_VALUE,
			// sSessId, "Attributes", "a");
			// res3 = sslClient.sendReceive(cmd3, null);

			// Packet cmd5 = CommandUtil.makeCmd(PMCommand.GET_OPSETS_BETWEEN,
			// null, "member", "nova_cmd1", "b");
			// res5 = sslClient.sendReceive(cmd5, null);

			// Packet cmd4 = CommandUtil.makeCmd(PMCommand.GET_ENTITY_NAME,
			// sSessId1, sProcId, objectName);
			// res4 = sslClient.sendReceive(cmd4, null);
			
			} catch (Exception e) {
			e.printStackTrace();
			failurePacket(String.format("Exception in %s: %s", PMCommand.GET_OPSETS_BETWEEN, e.getMessage()));
			failurePacket(String.format("Exception in %s: %s", PMCommand.GET_PERMITTED_OPS, e.getMessage()));
		}
		return false;
	}

	// to check the content of each item in the packet, if File read present
	public boolean checkRole(Packet res) {
		if (res == null) {
			return false;
		} else {
			for (int i = 0; i < res.getItems().size(); i++) {
				Item item = res.getItems().get(i);
				ItemType t = item.getType();
				String v = item.getValueString();
				if (t.toPrefix().equals("ERR")) {
					//System.out.println("The error is " + v);
					return false;
				} else if (t.toPrefix().equals("TXT")) {
					return true;
				}
			}
		}
		return false;
	}

	// to check if an operation exists between user attribute and object
	// attribute/object
	public boolean checkAttribute(Packet res) {
		if (res == null) {
			return false;
		} else {
			for (int i = 0; i < res.getItems().size(); i++) {
				Item item = res.getItems().get(i);
				ItemType t = item.getType();
				String v = item.getValueString();
				if (t.toPrefix().equals("ERR")) {
					//System.out.println("The error is " + v);
					return false;
				} else if (t.toPrefix().equals("TXT") && v.contains("File read")) {
					return true;
				}
			}
		}
		return false;
	}

	// check policy type

	public boolean checkPolicyType(Packet res) {
		if (res == null) {
			//System.out.println("No user Attributes found");
			return false;
		} else {
			for (int i = 0; i < res.getItems().size(); i++) {
				Item item = res.getItems().get(i);
				ItemType t = item.getType();
				String v = item.getValueString();
				if (t.toPrefix().equals("ERR")) {
					//System.out.println("The error is " + v);
					return false;
				} else if (t.toPrefix().equals("TXT") && v.contains("Attributes")) {
					return true;
				}
			}
		}
		return false;
	}

	// print response packet content
	public void printResContent(Packet res) {
		if (res == null) {
			//System.out.println("Packet is empty");
			return;
		} else {
			//System.out.println("-----Packet start-------");
			for (int i = 0; i < res.getItems().size(); i++) {
				Item item = res.getItems().get(i);
				ItemType t = item.getType();
				String v = item.getValueString();
				if (v == null) {
					//System.out.println(t.toPrefix() + "value is null" + "at i = " + i);
				} else {
					//System.out.println("This is the value " + v + "at i = " + i);
				}
			}
		}
		//System.out.println("-----Packet end-------");
	}

	private boolean openSession(String sSessUser, String sPass) {
		if (sSessUser.length() == 0) {
			ErrorMessage = "Incorrect user name or password";
			// JOptionPane.showMessageDialog(null, "Incorrect user name or
			// password");
			return false;
		}

		// Find the local host name.
		String sSessHost = getLocalHost();

		// Send the host name, user name, and password to the server.
		// The engine will return the session name and id.
		try {
			Packet cmd = makeCmd("createSession", "My session", sSessHost, sSessUser, sPass);
			Packet res = sslClient.sendReceive(cmd, null);
			if (res.hasError()) {
				ErrorMessage = res.getErrorMessage();
				// JOptionPane.showMessageDialog(null, res.getErrorMessage());
				return false;
			}
			// The result should contain in the first three items:
			// session name
			// session id
			// user id.
			sessionId = res.getStringValue(1);
			//System.out.println("This is the session Id in PMApp: " + sessionId);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			// JOptionPane.showMessageDialog(null, "Error while opening session:
			// " + e.getMessage());
			return false;
		}
	}

	public void closeSession() {
		if (sessionId != null) { // && fakeSession - have to check what fake
									// session is doing
			deleteSession(sessionId);
			sessionId = null;
		}
	}

	private boolean deleteSession(String sId) {
		try {
			Packet cmd = makeCmd("deleteSession", sId);
			Packet res = sslClient.sendReceive(cmd, null);
			if (res.hasError()) {
				JOptionPane.showMessageDialog(null, res.getErrorMessage());
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

	public Packet makeCmd(String sCode, String... sArgs) throws Exception {
		return CommandUtil.makeCmd(sCode, sessionId == null ? "" : sessionId, sArgs);
	}
}
