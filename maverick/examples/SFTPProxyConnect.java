/* HEADER */
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.maverick.sftp.SftpFile;
import com.maverick.ssh.HostKeyVerification;
import com.maverick.ssh.SshAuthentication;
import com.maverick.ssh.SshClient;
import com.maverick.ssh.SshConnector;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshTunnel;
import com.maverick.ssh.components.SshPublicKey;
import com.sshtools.net.SocketTransport;
import com.sshtools.sftp.SftpClient;
/**
 * This example demonstrates how to proxy an SFTP session over a port forwarding
 * tunnel; this is useful for accessing servers behind a firewall.
 *
 * @author Lee David Painter
 */
public class SFTPProxyConnect {

	public static void main(String[] args) {


		final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		try {

               String proxyServer = "proxy.foo.com";
               String targetServer = "target.foo.com";
               String username = "lee";

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


			/**
			 * Connect to the host
			 */
			final SshClient ssh = con.connect(new SocketTransport(proxyServer, 22), username);

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
			 * Start a tunnel and proxy another connection over it
			 */
			if(ssh.isAuthenticated()) {


                 SshTunnel tunnel = ssh.openForwardingChannel(targetServer, 22, "127.0.0.1", 22,
                    "127.0.0.1", 22, null, null);


                 SshClient forwardedConnection = con.connect(tunnel, username);

                 forwardedConnection.authenticate(pwd);

                 SftpClient sftp = new SftpClient(forwardedConnection);

                 SftpFile[] children = sftp.ls();

                 for(int i=0;i<children.length;i++)
                   System.out.println(SftpClient.formatLongname(children[i]));

                   sftp.quit();

                   forwardedConnection.disconnect();

			}

               ssh.disconnect();


			} catch(Throwable th) {
			th.printStackTrace();
		}
	}

}

