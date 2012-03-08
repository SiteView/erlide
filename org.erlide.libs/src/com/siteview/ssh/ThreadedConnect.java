package com.siteview.ssh;
/* HEADER */
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
import com.maverick.ssh2.Ssh2Context;
import com.sshtools.net.SocketTransport;
/**
    a ssh connection manager: one timedout connection for multiple data collections
 */
public class ThreadedConnect {

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

			con.getContext(SshConnector.SSH1).setHostKeyVerification(hkv);
			con.getContext(SshConnector.SSH2).setHostKeyVerification(hkv);

			((Ssh2Context)con.getContext(SshConnector.SSH2)).setPreferredPublicKey(Ssh2Context.PUBLIC_KEY_SSHDSS);

			/**
			 * Connect to the host
			 */
			final SshClient ssh = con.connect(new SocketTransport(hostname, port), username);


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

                 // Some old SSH2 servers (Solaris) kill the connection after the first
                 // session has closed and there are no other sessions started;
                 // so to avoid this we create the first session and dont ever use it
                 SshSession s = ssh.openSessionChannel();
                 s.startShell();

                 ThreadPool pool = new ThreadPool();

                 for(int i=0;i<100;i++) {

                   if(!ssh.isConnected())
                     break;

                   final int num = i;

                     pool.addOperation(new Runnable() {

                     public void run() {

                       System.out.println("Executing session " + num);
                       System.out.println(ssh.getChannelCount() + " channels currently open");
                       SshSession session = null;
                       try {

                       // We use a buffered session so we can spawn more threads
                       // to test multiple threads access to a single channel
                         session = ssh.openSessionChannel();

                         if(session.requestPseudoTerminal("vt100", 80, 24, 0, 0)) {

                             session.executeCommand("set");
                             InputStream in = session.getInputStream();

                             ByteArrayOutputStream out = new ByteArrayOutputStream();
                             int read;
                             while ((read = in.read()) > -1) {
                                 if (read > 0)
                                     out.write(read);
                             }

                             synchronized (System.out) {
                                 System.out.write(out.toByteArray());
                             }
                         } else
                             System.out.println("Failed to allocate pseudo terminal");
                       }
                       catch(Throwable t1) {
                         t1.printStackTrace();
                       }
                       finally {
                         if(session!=null)
                           session.close();
                           System.out.println("Completed session " + num);
                       }
                     }

                   });

                 }

			}


			} catch(Throwable th) {
			th.printStackTrace();
		}
	}

}

class ThreadPool {

   Thread t[] = new Thread[5];

   public synchronized void addOperation(Runnable r) {

     int nextThread;
     System.out.println("Adding new operation");
     while((nextThread = getNextThread()) == -1) {
      try {
        wait();
      }
      catch(InterruptedException ex) {
      }
     }

     start(r, nextThread);

   }

   public int getNextThread() {
     synchronized(t) {
       for(int i=0;i<t.length;i++) {
         if(t[i]==null) {
           return i;
         }
       }
     }
     return -1;

   }
   public synchronized void release() {
     notify();
   }

   public synchronized void start(final Runnable r, final int i) {
     t[i] = new Thread() {
       public void run() {

        try {
          r.run();
        }
        catch(Exception ex) {
        }

        synchronized(t) {
           t[i] = null;
        }

        release();
       }

     };

     t[i].start();
   }
}


