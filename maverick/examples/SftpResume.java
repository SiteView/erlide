/* HEADER */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import com.maverick.ssh.SshAuthentication;
import com.maverick.ssh.SshClient;
import com.maverick.ssh.SshConnector;
import com.sshtools.net.SocketTransport;
import com.sshtools.sftp.SftpClient;

/**
 * This example demonstrates the connection process connecting to an SSH2 server and
 * usage of the SFTP client.
 *
 * @author Lee David Painter
 */
public class SftpResume {

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

               /**
                * Connect to the host
                */
               SshClient ssh = con.connect(new SocketTransport(hostname, port), username);

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

                 /**
                  * IMPORTANT: for this demonstration the file must be of
                  * sufficent size to still be in progress after 3 seconds.
                  *
                  * If the file is not big enough increase the size by amending
                  * the loop that creates the file
                  */
                 final File f = new File(System.getProperty("user.home"), "sftp-resume");
                 java.util.Random rnd = new java.util.Random();

                 FileOutputStream out = new FileOutputStream(f);
                 byte[] buf = new byte[4096];
                 for (int i = 0; i < 5000; i++) {
                   rnd.nextBytes(buf);
                   out.write(buf);
                 }
                 out.close();

                 final SftpClient sftp = new SftpClient(ssh);

                 Thread t = new Thread() {
                   public void run() {
                    try {
                      sftp.put(f.getAbsolutePath());
                    }
                    catch (Throwable ex) {
                      System.out.println("The upload has been interrupted");
                    }
                   }
                 };

                 // Start the upload thread, wait and then interrupt
                 t.start();
                 Thread.sleep(3000);

                 // Force the SFTP client to quit leaving a file
                 // that is not fully uploaded
                 sftp.quit();

                 // Open up an SFTP client again
                 final SftpClient sftp2 = new SftpClient(ssh);

                 // Put the file again instructing the client to resume
                 sftp2.put(f.getAbsolutePath(), true);

                 System.out.println("The upload has been completed");

                 // Now start a download
                 Thread t2 = new Thread() {
                   public void run() {
                     try {
                       sftp2.get(f.getName(), System.getProperty("user.home") + "/sftp-resume-downloaded");
                     }
                     catch (Throwable ex) {
                       System.out.println("The download has been interrupted");
                     }
                   }
                 };

                 // Start the upload thread, wait and then interrupt
                 t2.start();
                 Thread.sleep(3000);

                 sftp2.quit();

                 SftpClient sftp3 = new SftpClient(ssh);

                 sftp3.get(f.getName(), System.getProperty("user.home") + "/sftp-resume-downloaded", true);

                 System.out.println("The download has completed");
              }


               } catch(Throwable th) {
               th.printStackTrace();
          }
     }

}
