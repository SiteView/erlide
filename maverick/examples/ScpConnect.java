import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import com.maverick.sftp.FileTransferProgress;
import com.maverick.sftp.SftpFile;
import com.maverick.ssh.SshAuthentication;
import com.maverick.ssh.SshClient;
import com.maverick.ssh.SshConnector;
import com.maverick.ssh1.Ssh1Client;
import com.maverick.ssh2.Ssh2Client;
import com.maverick.ssh2.Ssh2Context;
import com.sshtools.net.SocketTransport;
import com.sshtools.publickey.ConsoleKnownHostsKeyVerification;
import com.sshtools.scp.ScpClient;
import com.sshtools.sftp.SftpClient;

/**
 * This example demonstrates the connection process connecting to an SSH2 server and
 * usage of the SCP client. 
 *
 */
public class ScpConnect {

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

            System.out.print("Username [Enter for " + System.getProperty("user.name") + "]: ");

            String username = reader.readLine();

            if (username == null || username.trim().equals(""))
                username = System.getProperty("user.name");
            System.out.println("Connecting to " + hostname);

            /**
             * Create an SshConnector instance
             */
            SshConnector con = SshConnector.getInstance();

            // Lets do some host key verification

            con.getContext(SshConnector.SSH2).setHostKeyVerification(new ConsoleKnownHostsKeyVerification());

            Ssh2Context ssh2Context = (Ssh2Context) con.getContext(SshConnector.SSH2);
            ssh2Context.setPreferredPublicKey(Ssh2Context.PUBLIC_KEY_SSHDSS);

            /**
             * Connect to the host
             */
            SocketTransport t = new SocketTransport(hostname, port);
            t.setTcpNoDelay(true);

            SshClient ssh = con.connect(t, username);

            /**
             * Determine the version
             */
            if (ssh instanceof Ssh1Client) {
                System.out.println(hostname + " is an SSH1 server!! SFTP is not supported");
                ssh.disconnect();
                System.exit(0);
            } else
                System.out.println(hostname + " is an SSH2 server");

            Ssh2Client ssh2 = (Ssh2Client) ssh;
            /**
             * Authenticate the user using password authentication
             */
            com.maverick.ssh.PasswordAuthentication pwd = new com.maverick.ssh.PasswordAuthentication();

            do {
                System.out.print("Password: ");
                pwd.setPassword(reader.readLine());
            } while (ssh2.authenticate(pwd) != SshAuthentication.COMPLETE && ssh.isConnected());

            /**
             * Start a session and do basic IO
             */
            if (ssh.isAuthenticated()) {

                ScpClient scp = new ScpClient(ssh2);

                /**
                 * create a test file 1
                 */
                File textFile = new File(System.getProperty("user.home"), "shining.txt");
                FileOutputStream tout = new FileOutputStream(textFile);

                // Create a file with \r\n as EOL
                for (int i = 0; i < 100; i++) {
                    tout.write("All work and no play makes Jack a dull boy\r\n".getBytes());
                }
                tout.close();

                /**
                 * create a test file 2
                 */
                textFile = new File(System.getProperty("user.home"), "shining1.txt");
                tout = new FileOutputStream(textFile);

                // Create a file with \r\n as EOL
                for (int i = 0; i < 100; i++) {
                    tout.write("All work and no play makes Jack a dull boy\r\n".getBytes());
                }
                tout.close();

                /**
                 * create a test file 3
                 */
                textFile = new File(System.getProperty("user.home"), "shining2.txt");
                tout = new FileOutputStream(textFile);

                // Create a file with \r\n as EOL
                for (int i = 0; i < 100; i++) {
                    tout.write("All work and no play makes Jack a dull boy\r\n".getBytes());
                }
                tout.close();

                //put file 1
                scp.put("shining.txt", "theshining.txt", false);

                //put files 2 and 3
                String[] testfiles = { "shining1.txt", "shining2.txt" };
                scp.put(testfiles, "", false);

                /**
                 * put all files in the remote directory using *
                 */
                scp.put("*ini*", "", false);
                System.out.println("\nput *ini*\n");

                System.out.println("Check that copied all local files to remote, press enter.");
                reader.readLine();
                /**
                 * put all files in the remote directory using *.*
                 */
                scp.put("*.txt*", "", false);
                System.out.println("\nPut *.txt*\n");

            }

        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

}
