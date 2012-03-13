
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.sshd.*;
import com.maverick.sshd.events.EventLog;
import com.maverick.sshd.platform.*;
import java.net.SocketAddress;
import java.net.InetSocketAddress;

/**
 * An authentication provider enables the server to authenticate users.
 *
 * @author Lee David Painter
 */
public class ExampleAuthenticationProvider implements NativeAuthenticationProvider {
	public int getAuthenticationStatus() {
		return -1;
	}
  public boolean logonUser(byte[] sessionid, String username, SocketAddress ipAddress, SshPublicKey key,boolean verifyOnly) {
		// TODO Auto-generated method stub
		return false;
	}

  public ExampleAuthenticationProvider() {
  }

  /**
   * Change the password of a user. This method is only called if the logonUser
   * method throws a PasswordChangeException.
   *
   * @param username
   * @param oldpassword
   * @param newpassword
   * @return
   */
  public boolean changePassword(byte[] sessionid,
                                String username,
                                String oldpassword,
                                String newpassword) {
    return true;
  }

  /**
   * Log the user into the system by authenticating the password provided.
   *
   * @param sessionid
   * @param username
   * @param password
   * @return
   * @throws com.maverick.sshd.platform.PasswordChangeException
   */
  public boolean logonUser(byte[] sessionid,
                           String username,
                           String password,
                           SocketAddress ipAddress)
          throws com.maverick.sshd.platform.PasswordChangeException {

	    TransportProtocol transport = ConnectionManager.getInstance().getTransport(sessionid);
        EventLog.LogEvent(this,"User " + username
	                 + " is attempting login from "
	                 + ((InetSocketAddress)transport.getRemoteAddress()).getAddress().getHostAddress() + "/" + transport.getRemotePort()
	                 + " on local address "
	                 + transport.getLocalAddress().getHostAddress() + "/" + transport.getLocalPort());

    if(username.equals("lee") || username.equals("david")|| username.equals("root"))
      return true;
    if(username.equals("richard"))
      throw new PasswordChangeException();

    return false;
  }

  /**
   * Log the user into the system. This method is used when the authentication
   * has already been accepted by the server, i.e. when the user has supplied
   * a public key that has been accepted by the server.
   *
   * @param sessionid
   * @param username
   * @return
   */
  public boolean logonUser(byte[] sessionid,
                           String username,
                           SocketAddress ipAddress) {
    return true;
  }

  public void logoffUser(byte[] sessionid,
                         String username,
                           SocketAddress ipAddress) {
    // When the connection closes this method is called to allow you to
    // log off the user from the session.
  }

  /**
   * Get the group that the user belongs to.
   *
   * @param username
   * @return
   */
  public String getUserGroup(byte[] sessionid,
                             String username) {
    return "users";
  }

  /**
   * Get the users home directory.
   *
   * @param username
   * @return
   */
  public String getHomeDirectory(byte[] sessionid,
                                 String username) {
    // For demonstration purposes only; this method simply returns the
    // home directory of the user account that the server is running in.
    return System.getProperty("user.home");
  }

}
