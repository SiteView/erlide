/* HEADER */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

import com.maverick.ssh.SshClient;
import com.maverick.ssh.SshConnector;
import com.maverick.ssh2.Ssh2Context;
import com.sshtools.net.SocketTransport;
import com.sshtools.sftp.SftpClient;

/**
 * This example demonstrates the performance differences using
 * different cipher algorithms and socket options.
 *
 * @author Lee David Painter
 */
public class PerformanceTest {

    static String hostname;
    static int port;
    static String username;
    static String password;
    static File sourceFile;
    static long sourceFileChecksum = -1;
    static File retreivedFile;

    public static void main(String[] args) {

          final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

          try {

                  /**
                   * Perform all the tests against the same host
                   */
                  System.out.print("Hostname: ");
                  hostname = reader.readLine();

                  int idx = hostname.indexOf(':');
                  port = 22;
                  if (idx > -1) {
                      port = Integer.parseInt(hostname.substring(idx + 1));
                      hostname = hostname.substring(0, idx);
                  }

                  System.out.print("Username [Enter for " + System.getProperty("user.name") +
                                   "]: ");
                  username = reader.readLine();

                  if (username == null || username.trim().equals(""))
                      username = System.getProperty("user.name");

                  System.out.print("Password: ");
                  password = reader.readLine();

                  System.out.println("Creating test file");
                  createTestFile();

                  // Can only be enabled for servers that support no encryption
                  //System.out.println("****No cipher****");
                  //performTest(getDefaultConfiguration("none", true));

                  System.out.println("****Internal Provider with 3DES****");
                  performTest(getDefaultConfiguration(Ssh2Context.CIPHER_TRIPLEDES_CBC, false));

                  System.out.println("****Internal Provider with 3DES and TCP_NODELAY****");
                  performTest(getDefaultConfiguration(Ssh2Context.CIPHER_TRIPLEDES_CBC, true));

                  System.out.println("****Internal Provider with Blowfish****");
                  performTest(getDefaultConfiguration(Ssh2Context.CIPHER_BLOWFISH_CBC, false));

                  System.out.println("****Internal Provider with Blowfish and TCP_NODELAY****");
                  performTest(getDefaultConfiguration(Ssh2Context.CIPHER_BLOWFISH_CBC, true));

                  System.out.println("****Internal Provider with AES****");
                  performTest(getDefaultConfiguration(Ssh2Context.CIPHER_AES128_CBC, false));

                  System.out.println("****Internal Provider with AES and TCP_NODELAY****");
                  performTest(getDefaultConfiguration(Ssh2Context.CIPHER_AES128_CBC, true));

                  System.out.println("****JCE Provider with 3DES****");
                  performTest(getJCEConfiguration(Ssh2Context.CIPHER_TRIPLEDES_CBC, false));

                  System.out.println("****JCE Provider with 3DES and TCP_NODELAY****");
                  performTest(getJCEConfiguration(Ssh2Context.CIPHER_TRIPLEDES_CBC, true));

                  System.out.println("****JCE Provider with Blowfish****");
                  performTest(getJCEConfiguration(Ssh2Context.CIPHER_BLOWFISH_CBC, false));

                  System.out.println("****JCE Provider with Blowfish and TCP_NODELAY****");
                  performTest(getJCEConfiguration(Ssh2Context.CIPHER_BLOWFISH_CBC, true));

                  System.out.println("****JCE Provider with AES****");
                  performTest(getJCEConfiguration(Ssh2Context.CIPHER_AES128_CBC, false));

                  System.out.println("****JCE Provider with AES and TCP_NODELAY****");
                  performTest(getJCEConfiguration(Ssh2Context.CIPHER_AES128_CBC, true));

                  System.out.println("Completed Tests");

          } catch(Throwable t) {
              t.printStackTrace();
          }

    }

    static void createTestFile() throws Throwable {

        /**
         * Generate a temporary file for uploading/downloading
         */
        sourceFile = new File(System.getProperty("user.home"), "sftp-file");
        java.util.Random rnd = new java.util.Random();

        FileOutputStream out = new FileOutputStream(sourceFile);
        byte[] buf = new byte[1024000];
        for (int i = 0; i < 100; i++) {
            rnd.nextBytes(buf);
            out.write(buf);
        }
        out.close();

        try {
            // Compute Adler-32 checksum
            CheckedInputStream cis = new CheckedInputStream(
                    new FileInputStream(sourceFile), new Adler32());
            byte[] tempBuf = new byte[16384];
            while (cis.read(tempBuf) >= 0) {
            }
            sourceFileChecksum = cis.getChecksum().getValue();
        } catch (IOException e) {
        }
    }

    static SshClient getDefaultConfiguration(String cipher, boolean noDelay) throws Throwable {
        SshConnector con = getConnector();
        ((Ssh2Context)con.getContext(2)).setPreferredCipherCS(cipher);
        ((Ssh2Context)con.getContext(2)).setPreferredCipherSC(cipher);
        SocketTransport transport = new SocketTransport(hostname, port);
        transport.setTcpNoDelay(noDelay);
        transport.setSendBufferSize(65535);
        transport.setReceiveBufferSize(65535);
        return con.connect(transport, username);
    }

    static SshClient getJCEConfiguration(String cipher, boolean noDelay) throws Throwable {
        SshConnector con = SshConnector.getInstance();
        ((Ssh2Context)con.getContext(2)).setPreferredCipherCS(cipher);
        ((Ssh2Context)con.getContext(2)).setPreferredCipherSC(cipher);
        SocketTransport transport = new SocketTransport(hostname, port);
        transport.setTcpNoDelay(noDelay);
        transport.setSendBufferSize(65535);
        transport.setReceiveBufferSize(65535);
        return con.connect(transport, username);
    }

    static SshConnector getConnector() throws Throwable {
        SshConnector con = SshConnector.getInstance();
        return con;
    }

    static void performTest(SshClient ssh) throws Throwable {

               /**
                * Authenticate the user using password authentication
                */
               com.maverick.ssh.PasswordAuthentication pwd = new com.maverick.ssh.PasswordAuthentication();
               pwd.setPassword(password);

               ssh.authenticate(pwd);

               /**
                * Start a session and do basic IO
                */
               if(ssh.isAuthenticated()) {

                   System.out.println("Client info: " + ssh.toString());
                   SftpClient sftp = new SftpClient(ssh);

                   sftp.setTransferMode(SftpClient.MODE_BINARY);

                     /**
                     * Create a directory for the test files
                     */
                    sftp.mkdirs("sftp/test-files");

                    /**
                     * Change directory
                     */
                    sftp.cd("sftp/test-files");

                    /**
                     * Put a file into our new directory
                     */
                    long length = sourceFile.length();
                    long t1 = System.currentTimeMillis();
                    sftp.put(sourceFile.getAbsolutePath());
                    long t2 = System.currentTimeMillis();
                    long e = t2 - t1;
                    float secs, kbs;
                    if (e >= 1000) {
                        kbs = (((float) length / 1024) / ((float) e / 1000) / 1000);
                        System.out.println("Upload Transfered at " + String.valueOf(kbs) +
                                           " MB/s");
                    }

                    /**
                     * Download the file inot a new location
                     */
                    File f2 = new File(System.getProperty("user.home"), "downloaded");
                    f2.mkdir();

                    sftp.lcd(f2.getAbsolutePath());
                    File retrievedFile = new File(f2, sourceFile.getName());
                    t1 = System.currentTimeMillis();
                    sftp.get(sourceFile.getName());
                    t2 = System.currentTimeMillis();
                    e = t2 - t1;
                    if (e >= 1000) {
                        kbs = (((float) length / 1024) / ((float) e / 1000) / 1000);
                        System.out.println("Download Transfered at " + String.valueOf(kbs) +
                                           " MB/s");
                    }

                    long checksum2 = 0;
                    try {
                        // Compute Adler-32 checksum
                        CheckedInputStream cis = new CheckedInputStream(
                                new FileInputStream(retrievedFile), new Adler32());
                        byte[] tempBuf = new byte[16384];
                        while (cis.read(tempBuf) >= 0) {
                        }
                        checksum2 = cis.getChecksum().getValue();
                    } catch (IOException ex) {
                    }

                    if (checksum2 != sourceFileChecksum) {
                        System.out.println("FILES DO NOT MATCH");
                    }
                }
     }
}
