/* HEADER */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.maverick.ssh.ChannelAdapter;
import com.maverick.ssh.HostKeyVerification;
import com.maverick.ssh.SshAuthentication;
import com.maverick.ssh.SshChannel;
import com.maverick.ssh.SshClient;
import com.maverick.ssh.SshConnector;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshSession;
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.ssh1.Ssh1Client;
import com.maverick.ssh2.Ssh2Channel;
import com.maverick.ssh2.Ssh2Client;
import com.maverick.ssh2.Ssh2Context;
import com.maverick.ssh2.Ssh2Session;
import com.sshtools.net.ForwardingClient;
import com.sshtools.net.SocketTransport;
/**
 * This example demonstrates the connection process using the standard SSH interfaces
 * that enable connections to both SSH1 and SSH2 servers and configuration of both
 * local and remote port forwarding.
 *
 * @author Lee David Painter
 */
public class PortFowardingConnect {

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
					// TODO Auto-generated catch block
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
			SshClient ssh = con.connect(new SocketTransport(hostname, port), username, true);

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

				final ForwardingClient fwd = new ForwardingClient(ssh);

				/**
				 * Request remote forwardings before starting the users shell, this
				 * enables us to write code that will work with both SSH1 and SSH2
				 * servers
				 */
				/*if(!fwd.requestRemoteForwarding("127.0.0.1", 8080,
						"127.0.0.1", 80)) {
					System.out.println("Forwarding request failed!");
				}*/

				/**
				 * Start the users session, this is required by SSH1 but not SSH2
				 * so in the interest of compatibility we start it everytime. It
				 * also acts as a thread to service incoming channel requests
				 * for the port forwarding for both versions. Since we have a single
				 * threaded API we have to do this to send a timely response
				 */
				final SshSession session = ssh.openSessionChannel();
				session.requestPseudoTerminal("vt100",80,24,0,0);
				session.startShell();

				/**
				 * Start local forwardings after starting the users session, again
				 * this is so that our code is compatible for both protocols. Starting
				 * before the users session would break SSH1.
				 */
				fwd.startLocalForwarding("127.0.0.1", 8080, "cnn.com", 80);

				final InputStream in = session.getInputStream();
				Thread t = new Thread() {
					public void run() {
						try {
							int read;
							while((read = in.read()) > -1) {
								if(read > 0)
									System.out.print((char)read);
							}
						} catch(Throwable t4) {
							t4.printStackTrace();
						} finally {
							System.exit(0);
						}
					}

				};
				t.start();

				/**
				 * Were also going to have a monitor thread that displays the active forwardings
				 * and tunnels every 10 seconds
				 */
				Thread m = new Thread() {
					public void run() {
						while(!session.isClosed()) {
							try {
								Thread.sleep(10000);
							} catch(Throwable t3) { }

							try {
							String[] listeners = fwd.getLocalForwardings();
							for(int i=0;i<listeners.length;i++) {
								System.out.print("Local forwarding " + listeners[i]);
								try {
									ForwardingClient.ActiveTunnel[] tun1 = fwd.getLocalForwardingTunnels(listeners[i]);
									System.out.println(" has " + String.valueOf(tun1.length) + " active tunnels");
								} catch(Throwable t2) {
									t2.printStackTrace();
								}
							}

							listeners = fwd.getRemoteForwardings();
							for(int i=0;i<listeners.length;i++) {
								System.out.print("Remote forwarding " + listeners[i]);
								try {
									ForwardingClient.ActiveTunnel[] tun2 = fwd.getRemoteForwardingTunnels(listeners[i]);
									System.out.println(" has " + String.valueOf(tun2.length) + " active tunnels");
								} catch(Throwable t5) {
								t5.printStackTrace();
								}
							}

							} catch(Throwable t1) {
								t1.printStackTrace();
							}

						}
					}
				};
				m.start();
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




