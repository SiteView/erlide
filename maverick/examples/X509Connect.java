/* HEADER */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;

import com.maverick.ssh.PseudoTerminalModes;
import com.maverick.ssh.SshAuthentication;
import com.maverick.ssh.SshClient;
import com.maverick.ssh.SshConnector;
import com.maverick.ssh.SshSession;
import com.maverick.ssh.components.jce.Ssh2RsaPrivateKey;
import com.maverick.ssh.components.jce.SshX509RsaSha1PublicKey;
import com.maverick.ssh1.Ssh1Client;
import com.maverick.ssh2.Ssh2Client;
import com.sshtools.net.SocketTransport;
import com.sshtools.publickey.ConsoleKnownHostsKeyVerification;
/**
 * This example demonstrates the connection process using the standard SSH interfaces
 * that enable connections to both SSH1 and SSH2 servers.
 *
 * @author Lee David Painter
 */
public class X509Connect {

	public static void main(String[] args) {


		final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		try {

                    System.out.print("Hostname: ");
                    String hostname = reader.readLine();

                    int idx = hostname.indexOf(':');
                    int port = 22;
                    if (idx > -1) {
                        port = Integer.parseInt(hostname.substring(idx + 1));
                        hostname = hostname.substring(0, idx);

                    }

                    System.out.print("Username [Enter for " + System.getProperty("user.name") +
                                     "]: ");
                    String username = reader.readLine();

                    if (username == null || username.trim().equals(""))
                        username = System.getProperty("user.name");

                    System.out.println("Connecting to " + hostname);

                    /**
                     * Create an SshConnector instance
                     */
                    SshConnector con = SshConnector.getInstance();

                    // Verify server host keys using the users known_hosts file
                    con.setKnownHosts(new ConsoleKnownHostsKeyVerification());

                    System.out.print("Identity: ");
                    String ident = reader.readLine();

                    System.out.print("Password: ");
                    String passphrase = reader.readLine();

                    System.out.println("Alias: ");
                    String alias = reader.readLine();

                    KeyStore keystore = KeyStore.getInstance("PKCS12");

                    keystore.load(new FileInputStream(ident), passphrase.toCharArray());

                    RSAPrivateKey prv = (RSAPrivateKey) keystore.getKey(alias, passphrase.toCharArray());
                    X509Certificate x509 = (X509Certificate) keystore.getCertificate(alias);

                    /**
                     * Connect to the host
                     */
                    final SshClient ssh = con.connect(new SocketTransport(hostname, port),
                                                      username);
                    System.out.println(ssh.toString());

                    String[] methods = ((Ssh2Client)ssh).getAuthenticationMethods(username);

                    for(int i=0;i<methods.length;i++) {
                        System.out.println(methods[i]);
                    }
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
                    com.maverick.ssh.PublicKeyAuthentication pk = new com.maverick.ssh.
                                                                  PublicKeyAuthentication();

                    pk.setPublicKey(new SshX509RsaSha1PublicKey(x509));
                    pk.setPrivateKey(new Ssh2RsaPrivateKey(prv));

                    if(ssh.authenticate(pk) != SshAuthentication.COMPLETE) {
                        throw new Exception("X509 authentication failed");
                    }

                    /**
                     * Start a session and do basic IO
                     */
                    if (ssh.isAuthenticated()) {

                        // Some old SSH2 servers kill the connection after the first
                        // session has closed and there are no other sessions started;
                        // so to avoid this we create the first session and dont ever use it
                        final SshSession session = ssh.openSessionChannel();

                        // Use the newly added PseudoTerminalModes class to
                        // turn off echo on the remote shell
                        PseudoTerminalModes pty = new PseudoTerminalModes(ssh);
                        pty.setTerminalMode(PseudoTerminalModes.ECHO, false);

                        session.requestPseudoTerminal("vt100", 80, 24, 0, 0, pty);

                        session.startShell();

                        Thread t = new Thread() {
                            public void run() {
                                try {
                                    int read;
                                    while ((read = session.getInputStream().read()) > -1) {
                                        System.out.write(read);
                                        System.out.flush();
                                    }
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        };

                        t.start();
                        int read;
                        byte[] buf = new byte[4096];
                        while((read = System.in.read()) > -1) {
                            session.getOutputStream().write(read);

                        }

                        session.close();
                    }

                    ssh.disconnect();
                } catch(Throwable t) {
                    t.printStackTrace();
                }
	}

}

