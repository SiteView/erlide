
import com.maverick.sshd.AccessManager;
import java.net.SocketAddress;
import com.maverick.sshd.Channel;
import com.maverick.sshd.ForwardingChannel;

/**
 * An example {@link com.maverick.sshd.AccessManager} that provides the
 * ability to restrict access to SSH features based upon the username
 * of the connected user.
 *
 * @author Lee David Painter
 */
public class ExampleAccessManager implements AccessManager {

  /**
   * Can the user connect to the server?
   * @param username
   * @return
   */
  public boolean canConnect(String username) {
    return true;
  }


  public boolean canConnect(SocketAddress remoteclient) {
      return true;
  }

  public boolean canOpenChannel(byte[] sessionid,
                                String username,
                                Channel channel) {
      return true;
  }
  /**
   * Can the user start an interactive shell?
   * @param username
   * @return
   */
  public boolean canStartShell(byte[] sessionid,
                               String username) {
    return true;
  }

  /**
   * Can the user execute a command?
   * @param username
   * @param cmd
   * @return
   */
  public boolean canExecuteCommand(byte[] sessionid,
                                  String username,
                                  String cmd) {
    return true;
  }

  /**
   * Can the user start a subsystem
   * @param username
   * @param subsystem
   * @return
   */
  public boolean canStartSubsystem(byte[] sessionid,
                                   String username,
                                   String subsystem) {
    return true;
  }

  /**
   * Can the user open a forwarding channel?
   * @param username
   * @param hostname
   * @param port
   * @return
   */
  public boolean canForward(byte[] sessionid,
                            String username,
                            ForwardingChannel channel,
                            boolean isLocal) {
    return true;
  }

  /**
   * Can the user open a port on the server to listen for requests and
   * have those requests forwarded to the client machine?
   * @param username
   * @param bindAddress
   * @param bindPort
   * @return
   */
  public boolean canListen(byte[] sessionid,
                           String username,
                           String bindAddress,
                           int bindPort) {
    return true;
  }

  public String[] getRequiredAuthentications(
            byte[] sessionid,
            String username) {
        return null;
  }
}
