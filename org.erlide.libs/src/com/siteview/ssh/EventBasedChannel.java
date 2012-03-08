package com.siteview.ssh;
/* HEADER */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.maverick.ssh.ChannelAdapter;
import com.maverick.ssh.HostKeyVerification;
import com.maverick.ssh.PseudoTerminalModes;
import com.maverick.ssh.SshAuthentication;
import com.maverick.ssh.SshChannel;
import com.maverick.ssh.SshClient;
import com.maverick.ssh.SshConnector;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SshSession;
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.ssh1.Ssh1Client;
import com.maverick.ssh2.Ssh2Context;
import com.sshtools.net.SocketTransport;
/**
 * This example demonstrates the connection process using the standard SSH interfaces
 * that enable connections to both SSH1 and SSH2 servers.
 *
 * @author Lee David Painter
 */
public class EventBasedChannel {

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

                    // Lets do some host key verification
                    HostKeyVerification hkv = new HostKeyVerification() {
                        public boolean verifyHost(String hostname, SshPublicKey key) {
                            try {
                                System.out.println("The connected host's key (" +
                                                   key.getAlgorithm() + ") is");
                                System.out.println(key.getFingerprint());
                            } catch (SshException e) {
                                e.printStackTrace();
                            }
                            return true;
                        }
                    };

                    con.getContext(SshConnector.SSH1).setHostKeyVerification(hkv);
                    con.getContext(SshConnector.SSH2).setHostKeyVerification(hkv);

                    ((Ssh2Context) con.getContext(SshConnector.SSH2)).setPreferredPublicKey(
                            Ssh2Context.PUBLIC_KEY_SSHDSS);



                    /**
                     * Connect to the host
                     *
                     * IMPORTANT: We must use buffered mode so that we have
                     * a background thread to fire data events back to us.
                     */
                    final SshClient ssh = con.connect(new SocketTransport(hostname, port),
                                                      username,
                                                      true);

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
                         * The following session demonstrates listenening
                         * for data events on a channel
                         */
                        ChannelAdapter eventListener = new ChannelAdapter() {

                            public void dataReceived(SshChannel channel,
                                                     byte[] buf,
                                                     int offset,
                                                     int len) {
                                    System.out.write(buf, offset, len);
                            }

                            public synchronized void channelClosed(SshChannel channel) {
                                notifyAll();
                            }

                        };

                        final SshSession session = ssh.openSessionChannel(eventListener);

                        // Use the newly added PseudoTerminalModes class to
                        // turn off echo on the remote shell
                        PseudoTerminalModes pty = new PseudoTerminalModes(ssh);
                        pty.setTerminalMode(PseudoTerminalModes.ECHO, false);

                        session.requestPseudoTerminal("vt100", 80, 24, 0, 0, pty);

                        // Because were using an event based model we dont
                        // want the InputStream filling up and deadlocking
                        // the session
                        session.setAutoConsumeInput(true);

                        session.executeCommand("echo Hello World");

                        // Now wait for it to complete
                        synchronized(eventListener) {
                            eventListener.wait();
                        }

                        /**
                         * The following session demonstrates using the
                         * InputStreams available method to tell when
                         * data is ready on a channel
                         */
                        final SshSession session2 = ssh.openSessionChannel(eventListener);

                        // We can reuse the old pty but we can't change it
                        session2.requestPseudoTerminal("vt100", 80, 24, 0, 0, pty);
                        session2.setAutoConsumeInput(true);
                        session2.startShell();

                        Thread t = new Thread() {
                            public void run() {

                                try {
                                    int read;
                                    while ((read = System.in.read()) > -1 && !session2.isClosed()) {
                                        session2.getOutputStream().write(read);
                                    }
                                } catch (IOException ex) {
                                }
                            }
                        };

                        t.start();

                        synchronized(eventListener) {
                            eventListener.wait();
                        }

                        // Force our System.in reader thread to exit when the
                        // user presses a key
                        System.out.println("Press any key to exit.");
                    }

                    ssh.disconnect();
                } catch(Throwable t) {
                    t.printStackTrace();
                }
	}

}

