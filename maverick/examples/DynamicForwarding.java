import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import socks.InetRange;
import socks.ProxyServer;
import socks.server.IdentAuthenticator;
import socks.server.ServerAuthenticatorNone;
import socks.server.UserPasswordAuthenticator;
import socks.server.UserValidation;

import com.maverick.ssh.SshAuthentication;
import com.maverick.ssh.SshClient;
import com.maverick.ssh.SshConnector;
import com.maverick.ssh1.Ssh1Client;
import com.sshtools.net.SocketTransport;


public class DynamicForwarding {

	public static void main(String[] args) {

		org.apache.log4j.BasicConfigurator.configure();

		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));

		try {
			System.out.print("Hostname: ");
			String hostname;
			hostname = "localhost"; //reader.readLine();
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
			username = "lee"; // reader.readLine();

			if (username == null || username.trim().equals(""))
				username = System.getProperty("user.name");

			System.out.println("Connecting to " + hostname);

			/**
			 * Create an SshConnector instance
			 */
			SshConnector con = SshConnector.getInstance();

			// Verify server host keys using the users known_hosts file
			
			/**
			 * Connect to the host
			 */
			final SshClient ssh = con.connect(new SocketTransport(hostname,
					port), username, true);


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

				ProxyServer socksProxy = new ProxyServer(new ServerAuthenticatorNone(), ssh);
				
				socksProxy.start(5001);
			}
			
			Thread.sleep(600000);

			ssh.disconnect();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
