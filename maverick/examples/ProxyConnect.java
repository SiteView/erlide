/* HEADER */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.maverick.ssh.HostKeyVerification;
import com.maverick.ssh.SshAuthentication;
import com.maverick.ssh.SshClient;
import com.maverick.ssh.SshConnector;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshSession;
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.ssh1.Ssh1Client;
import com.maverick.ssh2.Ssh2Context;
import com.sshtools.net.HttpProxyTransport;
/**
 * This example demonstrates the connection process using the standard SSH interfaces
 * that enable connections to both SSH1 and SSH2 servers.
 *
 * @author Lee David Painter
 */
public class ProxyConnect {

	public static void main(String[] args) {

		final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		try {

			System.out.print("Hostname: ");
			String hostname = reader.readLine();

			int idx = hostname.indexOf(':');
			int port = 22;
			if(idx > -1) {
				port = Integer.parseInt(hostname.substring(idx+1));
				hostname = hostname.substring(0, idx);

			}

			System.out.print("Username [Enter for " + System.getProperty("user.name") + "]: ");
			String username = reader.readLine();

			if(username==null || username.trim().equals(""))
				username = System.getProperty("user.name");

			System.out.println("Connecting to " + hostname);

			/**
			 * Create an SshConnector instance
			 */
			SshConnector con = SshConnector.getInstance();

			// Lets do some host key verification
			HostKeyVerification hkv = new HostKeyVerification() {
			public boolean verifyHost(String hostname, SshPublicKey key) {
				try {
					System.out.println("The connected host's key (" + key.getAlgorithm() + ") is");
					System.out.println(key.getFingerprint());
				} catch (SshException e) {
					e.printStackTrace();
				}
				return true;
			}
			};

			con.getContext(SshConnector.SSH1).setHostKeyVerification(hkv);
			con.getContext(SshConnector.SSH2).setHostKeyVerification(hkv);

			((Ssh2Context)con.getContext(SshConnector.SSH2)).setPreferredPublicKey(Ssh2Context.PUBLIC_KEY_SSHDSS);

			/**
			 * Connect to the host
			 */
			SshClient ssh = con.connect(HttpProxyTransport.connectViaProxy(hostname, 22,
                                "mars", 8080, "user", "password", "J2SSH Maverick"), username);


			/**
			 * Determine the version
			 */
			if(ssh instanceof Ssh1Client)
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
			}
			while(ssh.authenticate(pwd)!=SshAuthentication.COMPLETE
					&& ssh.isConnected());

			/**
			 * Start a session and do basic IO
			 */
			if(ssh.isAuthenticated()) {

				SshSession session = ssh.openSessionChannel();
				session.requestPseudoTerminal("vt100",80,24,0,0);
				session.startShell();


				final InputStream in = session.getInputStream();
				Thread t = new Thread(new Runnable() {
					public void run() {
						try {
							int read;
							while((read = in.read()) > -1) {
								if(read > 0)
									System.out.print((char)read);
							}
						} catch(Throwable t1) {
							t1.printStackTrace();
						} finally {
							System.exit(0);
						}
					}

				});
				t.start();
				int read;
				while((read = System.in.read()) > -1) {
					session.getOutputStream().write(read);
				}

				System.exit(0);
			}


			} catch(Throwable th) {
			th.printStackTrace();
		}
	}

}



