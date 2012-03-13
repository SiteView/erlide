/* HEADER */
import java.io.IOException;
import java.net.InetSocketAddress;

import com.maverick.sshd.SessionChannel;
import com.maverick.sshd.platform.AuthenticationProvider;
import com.maverick.sshd.scp.ScpCommand;

/**
 * If your server is to support an interactive shell or the execution of
 * commands you need to implement this abstract class and set your class
 * in the ConfigurationContext.
 *
 * <code>
 * context.setSessionProvider(ExampleSession.class);
 * </code>
 *
 * This example session will simply echo all input until the 'exit' command
 * is received.
 *
 * @author Lee David Painter
 */
public class ExampleSession extends SessionChannel {

  String prompt = "[foo@maverick] ";
  String input = "";
  ScpCommand scp;
  public ExampleSession() {
    super();
  }

  /**
   * Process stdin data. This is data sent from the client to the
   * server.
   * @param data
   */
  protected void processStdinData(byte[] data) {

    if(scp!=null) {
      try {
        scp.getOutputStream().write(data);
      }
      catch (IOException ex) {
        ex.printStackTrace();
      }
    } else {
      boolean newline = false;
      for (int pos = 0; pos < data.length; pos++) {

        if (data[pos] == '\n' || data[pos] == '\r' && !newline) {
          input += "\r\n";
          sendChannelData("\r\n".getBytes());
          sendChannelData(input.getBytes());
          if (input.trim().equalsIgnoreCase("exit")) {
            close();
            connection.disconnect();
          }
          else
            sendChannelData(prompt.getBytes());
          input = "";
          newline = true;
        }
        else {
          sendChannelData(data, pos, 1);
          input += (char) data[pos];
          newline = false;
        }
      }
    }

  }

  /**
   * Process stderr data. This is not something that happens as the stderr
   * stream goes from server to client; however since the extended data types
   * work in both directions we have to provide a method. This will never
   * be called in real life.
   *
   * @param data
   */
  protected void processStderrData(byte[] data) {

  }

  /**
   * The channel has been closed. Perform any cleanup operations in here however
   * you cannot send any data now that the channel has been closed.
   */
  protected void onChannelClosed() {

  }

  /**
   * The user has requested the execution of a command.
   *
   * @param cmd
   * @return
   */
  protected boolean executeCommand(String cmd) {
    return false;
  }

  /**
   * Change the dimensions of the terminal window.
   * @param cols
   * @param rows
   * @param width
   * @param height
   */
  protected void changeWindowDimensions(int cols, int rows, int width, int height) {

  }

  /**
   * The session has now been opened. This is called after the command or shell
   * has been executed. i.e. after {@link #executeCommand(java.lang.String)}
   * or {@link #startShell()}
   */
  protected void onSessionOpen() {

    if(scp==null) {
      prompt = "[" + connection.getUsername() + "@maverick] ";
      String str = "Maverick SSHD\r\n(c) 2003-2009 SSHTools Ltd. All Rights Reserved\r\n\r\nThis shell will echo all input.\r\n";
      sendChannelData(str.getBytes());

      try {
          str = "Your logged on from " 
        	  + ((InetSocketAddress) AuthenticationProvider.getRemoteAddress(getSessionIdentifier())).getAddress().getCanonicalHostName()
        	  + "\r\n";
//                ((InetSocketAddress) getContext().getAuthenticationProvider().
//                 getRemoteAddress(getSessionIdentifier())).getAddress().getCanonicalHostName() + "\r\n";
          sendChannelData(str.getBytes());

      } catch(IOException ex) {
      }
      sendStderrData("This is STDERR data\r\n".getBytes());
      sendChannelData(prompt.getBytes());
    }
  }

  /**
   * The local side has sent an EOF message. No more data can be sent to the
   * remote side.
   */
  protected void onLocalEOF() {
    // The local side is EOF no more data can be sent

  }

  /**
   * The user has requested that the session start an interactive shell.
   *
   * @return <tt>true</tt> if the shell has been started</tt>
   */
  protected boolean startShell() {
    return true;
  }

  /**
   * The client requested that a pty (pseudo terminal) is allocated for the
   * session
   * @param term  the terminal type
   * @param cols the number of columns in characters
   * @param rows the number of rows in characters
   * @param width the width in pixels (informational only)
   * @param height the heigh in pixels( informational only)
   * @param encoded encoded terminal modes
   * @return
   */
  protected boolean allocatePseudoTerminal(String term,
                                           int cols,
                                           int rows,
                                           int width,
                                           int height,
                                           byte[] encoded) {
    return true;
  }

  /**
   * Process a signal sent by the client.
   *
   * @param signal
   */
  protected void processSignal(String signal) {

  }

  /**
   * Set an environment variable
   *
   * @param name
   * @param value
   * @return
   */
  protected boolean setEnvironmentVariable(String name, String value) {
    return true;
  }
}
