package com.siteview.ssh;
/* HEADER */
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.maverick.ssh.Shell;
import com.maverick.ssh.ShellController;
import com.maverick.ssh.ShellProcess;
import com.maverick.ssh.ShellProcessController;
import com.maverick.ssh.ShellTimeoutException;
import com.maverick.ssh.SshAuthentication;
import com.maverick.ssh.SshClient;
import com.maverick.ssh.SshConnector;
import com.maverick.ssh1.Ssh1Client;
import com.sshtools.net.SocketTransport;

/**
 * This example demonstrates the connection process using the standard SSH
 * interfaces that enable connections to both SSH1 and SSH2 servers.
 * 
 * @author Lee David Painter
 */
public class ImprovedShellConnect {

	public static void main(String[] args) {


//		org.apache.log4j.PropertyConfigurator.configure("log4j.properties");

		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));

		try {
			System.out.print("Hostname: ");
			String hostname;
			hostname = reader.readLine();

			int idx = hostname.indexOf(':');
			int port = 22;
			if (idx > -1) {
				port = Integer.parseInt(hostname.substring(idx + 1));
				hostname = hostname.substring(0, idx);

			}

			System.out.print("Username [Enter for "
					+ System.getProperty("user.name") + "]: ");
			String username;
			username = reader.readLine();

			if (username == null || username.trim().equals(""))
				username = System.getProperty("user.name");

			System.out.println("Connecting to " + hostname);

			/**
			 * Create an SshConnector instance
			 */
			SshConnector con = SshConnector.getInstance();

			/**
			 * Connect to the host
			 */
			final SshClient ssh = con.connect(new SocketTransport(hostname,
					port), username);

			/**
			 * Determine the version
			 */
			if (ssh instanceof Ssh1Client)
				System.out.println(hostname + " is an SSH1 server");
			else
				System.out.println(hostname + " is an SSH2 server");

			/**
			 * Authenticate the user using password authentication
			 */
			com.maverick.ssh.PasswordAuthentication pwd = new com.maverick.ssh.PasswordAuthentication();

			do {
				System.out.print("Password: ");
				pwd.setPassword(reader.readLine());
			} while (ssh.authenticate(pwd) != SshAuthentication.COMPLETE
					&& ssh.isConnected());

			/**
			 * Start a session and do basic IO
			 */
			if (ssh.isAuthenticated()) {

				/**
				 * Create a Shell
				 */
				Shell shell = new Shell(ssh);
				
				
				/**
				 * Switch to another user, this creates a new Shell object which can be used to execute
				 * commands under that user
				 */
				Shell rootShell = shell.su("sudo su root", "xxxxxxxxx");
				
				
				/**
				 * Execute a command under the new shell
				 */
				ShellProcess process = rootShell.executeCommand("ls");
				
				
				int r;
				while((r = process.getInputStream().read()) > -1) {
					System.out.write((char)r);
					System.out.flush();
				}

				/**
				 * Leave the shell
				 */
				rootShell.exit();
				
				/**
				 * Demonstrate a timeout, we will sleep for 10 seconds but timeout after 5 
				 */
				process = shell.executeCommand("sleep 10");
				ShellController contr = new ShellProcessController(process);
				String line;
				try {
					while((line = contr.readLine(5000))!=null) {
						System.out.println(line);
					}
				} catch (ShellTimeoutException e) {
					System.out.println("Command timed out!");
				}
				
				/** 
				 * Exit
				 */
				shell.exit();
			}

			ssh.disconnect();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
