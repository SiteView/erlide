package com.siteview.ssh;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.maverick.events.Event;
import com.maverick.events.EventListener;
import com.maverick.nio.Daemon;
import com.maverick.nio.DaemonContext;
import com.maverick.ssh.SshException;
import com.maverick.sshd.AuthorizedKeysStoreImpl;
import com.maverick.sshd.ConnectionManager;
import com.maverick.sshd.ConnectionProtocol;
import com.maverick.sshd.SshContext;
import com.maverick.sshd.TransportProtocol;
import com.maverick.sshd.events.EventLog;
import com.maverick.sshd.events.SSHDEventCodes;
import com.maverick.sshd.events.SSHDLoggingListener;
import com.maverick.sshd.platform.AuthenticationProvider;
import com.maverick.sshd.scp.ScpCommand;
import com.maverick.sshd.vfs.VirtualFileSystem;
import com.sshtools.publickey.InvalidPassphraseException;
import com.sshtools.publickey.SshKeyPairGenerator;

/**
 * <p>An example server that fulfils the basic requirements of the Maverick SSHD.
 * To create a server simply extend the {@link com.maverick.sshd.SshDaemon} class
 * and configure the server to your requirements inside the
 * {@link com.maverick.sshd.SshDaemon#configure(ConfigurationContext)} method.</p>
 *
 * <p>For further information on configuration see the
 * {@link com.maverick.sshd.ConfigurationContext} documentation.</p>
 * @author Lee David Painter
 */
public class ExampleSSHD extends Daemon {

  int port=-1;
  String authorizedKeysFile=null;
  String preferredKeyExchange=null;
  
  /**
   * Create a server with one accept thread, 10 transfer threads and one
   * connection thread.
   * @throws IOException
   */
  public ExampleSSHD() {
  }
  
  public ExampleSSHD(int port) {
	  this.port=port;
  }
  
  public void useThisAuthorizedKeysFile(String filepath) {
	  this.authorizedKeysFile=filepath;
  }
  public void setPreferredKeyExchange(String preferredKeyExchange){
	  this.preferredKeyExchange=preferredKeyExchange;
  }

  private int authmethods=SshContext.ANY;
	
  //Currently password authentication is required as the connection from the proxy to the server uses it
	public void setRequiredAuthenticationMethods(int methods) {
		this.authmethods=methods;
	}
  
  /**
   * This is where the server is configured; add the servers host keys and
   * configure the authentication, file system and session providers. You
   * can optionally define an access manager that will be consulted before
   * giving a user access to individual SSH features.
   *
   * @param context
   * @throws IOException
   */
  protected void configure(DaemonContext context) throws IOException {

    /* DEBUG */ EventLog.LogEvent(this,"Configuring Example SSHD");
    
    SshContext sshContext = new SshContext(this);
	
    // Use one of the following to set a preferred key exchange
	try {
		//sshContext.setPreferredKeyExchange(SshContext.KEX_DIFFIE_HELLMAN_GROUP14_SHA1);
		sshContext.setPreferredKeyExchange(SshContext.KEX_DIFFIE_HELLMAN_GROUP1_SHA1);
		//sshContext.setPreferredKeyExchange(SshContext.KEX_DIFFIE_HELLMAN_GROUP_EXCHANGE_SHA1);
	} catch (SshException e1) {
		throw new IOException("Could not set preferred key exchange");
	}
	
	// Use the following to set the preferred ciphers for both client->server and
	// server->client streams
	try {
		sshContext.setPreferredCipherCS(SshContext.CIPHER_AES128_CBC);
		sshContext.setPreferredCipherSC(SshContext.CIPHER_AES128_CBC);
		
		//sshContext.setPreferredCipherCS(SshContext.CIPHER_AES192_CBC);
		//sshContext.setPreferredCipherSC(SshContext.CIPHER_AES192_CBC);
		
		//sshContext.setPreferredCipherCS(SshContext.CIPHER_AES256_CBC);
		//sshContext.setPreferredCipherSC(SshContext.CIPHER_AES256_CBC);
		
		//sshContext.setPreferredCipherCS(SshContext.CIPHER_BLOWFISH_CBC);
		//sshContext.setPreferredCipherSC(SshContext.CIPHER_BLOWFISH_CBC);
		
		//sshContext.setPreferredCipherCS(SshContext.CIPHER_TRIPLEDES_CBC);
		//sshContext.setPreferredCipherSC(SshContext.CIPHER_TRIPLEDES_CBC);
		
	} catch (SshException e1) {
		throw new IOException("Could not set preferred cipher");
	}
	
	// Use the following to set the preferred hmacs for both client->server and
	// server->client streams
	try {
		sshContext.setPreferredMacCS(SshContext.HMAC_MD5);
		sshContext.setPreferredMacSC(SshContext.HMAC_MD5);
		
		//sshContext.setPreferredMacCS(SshContext.HMAC_SHA1);
		//sshContext.setPreferredMacSC(SshContext.HMAC_SHA1);
	} catch (SshException e1) {
		throw new IOException("Could not set preferred hmac");
	}
	
	
	if(authorizedKeysFile!=null) {
		sshContext.setPublicKeyStore(new AuthorizedKeysStoreImpl(authorizedKeysFile));
		sshContext.addRequiredAuthentication(SshContext.PUBLICKEY_AUTHENTICATION);
	}
	
	sshContext.setChannelLimit(1000);
	
    // Add an RSA key and preferably a DSA key as well to avoid connection
    // errors with clients that only support one key type (F-Secure 4.3 for example)
    try {
    	try {
    		
    		sshContext.loadOrGenerateHostKey(new File("ssh_host_rsa_key"),
			                      SshKeyPairGenerator.SSH2_RSA,
			                      1024);

	    	sshContext.loadOrGenerateHostKey(new File("ssh_host_dsa_key"),
	                              SshKeyPairGenerator.SSH2_DSA,
	                              1024);
	    	
    	} catch (SshException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}

    	
    } catch(InvalidPassphraseException ex) { }

    // Listen on multiple ports (on all interfaces)
    context.addListeningInterface("0.0.0.0", 22, sshContext);
    if(port!=-1){
    	context.addListeningInterface("0.0.0.0", port, sshContext);
    }
    
    //    context.addListeningInterface("::", 10022,sshContext);

    // Setup an authentication banner
    sshContext.setBannerMessage("Use the username 'lee' to login with any password.");

    // Setup the authentication provider
    /* DEBUG */ EventLog.LogDebugEvent(this,"setting authentication provider");
    AuthenticationProvider authProv;
    try {
    	authProv = new AuthenticationProvider(new ExampleAuthenticationProvider(), sshContext);
    } catch (Exception ex) {
        throw new IOException(ex.getMessage());
    }
    sshContext.setAuthenticationProvider(authProv);
    /* DEBUG */ EventLog.LogDebugEvent(this,"authentication provider set");

    // Setup the process provider (you can alternativley use setShellCommand
    // if you dont want to extend SessionChannel
    sshContext.setSessionProvider(ExampleSession.class);
    
    // Tell the SSHD which file system were using
    sshContext.setFileSystemProvider(VirtualFileSystem.class);

    // Using asynchronous file operations will create a thread for each sftp
    // connection but will ensure the smooth running the server by not
    // allowing networked file system operations to block the connections
    // read/write threads
    sshContext.setAsynchronousFileOperations(false);

    // Should the cancellation of a remote forwarding also kill any
    // active tunnels over the forwarding rule?
    sshContext.setRemoteForwardingCancelKillsTunnels(true);

    // Configure some VirtualFileSystem mounts and the VFS root
    System.setProperty("com.maverick.sshd.vfs.VFSRoot", "C:\\");
    //System.setProperty("com.maverick.sshd.vfs.VFSMount.1", "/foo=C:\\");
    //System.setProperty("com.maverick.sshd.vfs.VFSMount.2", "/management=M:\\");

    // You can add further mounts by using com.maverick.sshd.vfs.VFSMount.2
    // com.maverick.sshd.vfs.VFSMount.3 etc

    // Configure access permissions on a user by user basis
    sshContext.setAccessManager(new ExampleAccessManager());

    // Add SCP command support
    sshContext.addCommand("scp", ScpCommand.class);

    // Add a custom command
    sshContext.addCommand("users", ExampleCommand.class);

    context.setPermanentTransferThreads(1);

    sshContext.setSoftwareVersionComments("ExampleSSHD_1.2.3_Comments");

    //context.supportedAuthenticationMechanisms().add("none", NoneAuthentication.class);

    // Set available socket options
    sshContext.setSocketOptionKeepAlive(true);
    sshContext.setSocketOptionTcpNoDelay(true);
    sshContext.setSocketOptionReuseAddress(true);

    sshContext.setAllowDeniedKEX(true);
	
	sshContext.setIdleConnectionTimeoutSeconds(60);
	//sshContext.setSessionTimeout(30);
	
    /* DEBUG */ EventLog.LogEvent(this,"Configuration complete.");
  }

  public static void main(String[] args) throws Exception {
//	use standalone cryptography
//	System.setProperty("com.maverick.ssh.components.ComponentManager.tryStandaloneCryptographyBeforeJCE","true");


	System.out.println(Daemon.getVersion());
	System.out.println(Daemon.getReleaseDate());
	
	//org.apache.log4j.PropertyConfigurator.configure("log4j.properties");
//    org.apache.log4j.BasicConfigurator.configure();
    DaemonContext.addEventListener(new SSHDLoggingListener());
	
    Thread.currentThread().setName("Main");
    
    // ADD YOUR LICENSE CODE HERE....

    //     END OF LICENSE CODE

    final ExampleSSHD sshd = new ExampleSSHD(4000);
//    sshd.useThisAuthorizedKeysFile("authorized_keys_folder/authorized_keys");
    sshd.startup();

  }
}
