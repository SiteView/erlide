/* HEADER */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.maverick.ssh.HostKeyVerification;
import com.maverick.ssh.SshAuthentication;
import com.maverick.ssh.SshConnector;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshSession;
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.ssh2.KBIAuthentication;
import com.maverick.ssh2.KBIPrompt;
import com.maverick.ssh2.KBIRequestHandler;
import com.maverick.ssh2.Ssh2Client;
import com.maverick.ssh2.Ssh2Context;
import com.sshtools.net.SocketTransport;
/**
 * This example demonstrates the connection process using the standard SSH interfaces
 * that enable connections to both SSH1 and SSH2 servers with keyboard-interactive authentication.
 *
 * @author Lee David Painter
 */
public class KBIConnect {

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
				}
				return true;
			}
			};

			/**
			 * Keyboard interactive is only available in SSH2 so make sure we only support that version
			 */
			con.setSupportedVersions(SshConnector.SSH2);

			/**
			 * Set our preferred public key type
			 */
			con.getContext(SshConnector.SSH2).setHostKeyVerification(hkv);
			((Ssh2Context)con.getContext(SshConnector.SSH2)).setPreferredPublicKey(Ssh2Context.PUBLIC_KEY_SSHDSS);

			/**
			 * Connect to the host
			 */
			Ssh2Client ssh = (Ssh2Client)con.connect(new SocketTransport(hostname, port), username);

			/**
			 * Display the available authentication methods
			 */
			String[] methods = ssh.getAuthenticationMethods(username);
			for(int i=0;i<methods.length;i++)
				System.out.println(methods[i]);


			/**
			 * Authenticate the user using password authentication
			 */
			KBIAuthentication kbi = new KBIAuthentication();

			kbi.setKBIRequestHandler(new KBIRequestHandler() {
				public boolean showPrompts(String name, String instruction, KBIPrompt[] prompts) {
					try {
						System.out.println(name);
						System.out.println(instruction);
						for(int i=0;i<prompts.length;i++) {
							System.out.print(prompts[i].getPrompt());
							prompts[i].setResponse(reader.readLine());
						}
						return true;
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}
				}
			});

			if(ssh.authenticate(kbi)!=SshAuthentication.COMPLETE) {
				System.out.println("Authentication failed!");
				System.exit(0);
			}

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
								System.out.print((char)read);
							}
						} catch(Throwable t1) {
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


