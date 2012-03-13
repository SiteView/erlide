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
import com.sshtools.net.ForwardingClient;
import com.sshtools.net.SocketTransport;
/**
 * This example demonstrates the connection process using the standard SSH interfaces
 * that enable connections to both SSH1 and SSH2 servers with X Forwarding
 *
 * @author Lee David Painter
 */
public class XForwarding {

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


			/**
			 * Connect to the host
			 */
			SshClient ssh = con.connect(new SocketTransport(hostname, port), username);


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

               // Configure X Forwarding - this must be done before any sessions are opened
               ForwardingClient fwd = new ForwardingClient(ssh);
               fwd.allowX11Forwarding("localhost:0");

			/**
			 * Start a session and do basic IO
			 */
			if(ssh.isAuthenticated()) {

				final SshSession session = ssh.openSessionChannel();
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
						//	System.exit(0);
                              session.close();
						}
					}

				});
				t.start();
				int read;
				while((read = System.in.read()) > -1) {
					session.getOutputStream().write(read);
				}

                    session.close();
				//System.exit(0);
			}


			} catch(Throwable th) {
			th.printStackTrace();
		}
	}

}



