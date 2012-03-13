/* HEADER */
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.maverick.ssh.CommandExecutor;
import com.maverick.ssh.PseudoTerminalModes;
import com.maverick.ssh.SshAuthentication;
import com.maverick.ssh.SshClient;
import com.maverick.ssh.SshConnector;
import com.maverick.ssh.SshSession;
import com.maverick.ssh1.Ssh1Client;
import com.sshtools.net.SocketTransport;
import com.sshtools.publickey.ConsoleKnownHostsKeyVerification;
/**
 * This example demonstrates the connection process using the standard SSH interfaces
 * that enable connections to both SSH1 and SSH2 servers.
 *
 * @author Lee David Painter
 */
public class MultipleCommands {

    static SshClient ssh = null;
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

                    /**
                     * Connect to the host
                     */
                    SocketTransport transport = new SocketTransport(hostname, port);
                    transport.setSoTimeout(30000);
                    ssh = con.connect(transport,
                                                      username);

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
                    com.maverick.ssh.PasswordAuthentication pwd = new com.maverick.ssh.
                                                                  PasswordAuthentication();

                    do {
                        System.out.print("Password: ");
                        pwd.setPassword(reader.readLine());
                    } while (ssh.authenticate(pwd) != SshAuthentication.COMPLETE
                             && ssh.isConnected());

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

                         CommandExecutor i = new CommandExecutor(session, "\r", "PS1=COMMAND#", "COMMAND#", "");

                         String output = i.executeCommand("ll");

                         System.out.println(output);

                         output = i.executeCommand("env");

                         System.out.println(output);

                         output = i.executeCommand("exit");

                         System.out.println(output);

                         session.close();
                    }

                    ssh.disconnect();
                } catch(Throwable t) {
                    t.printStackTrace();
                }
	}

}

