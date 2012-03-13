
import com.maverick.sshd.platform.*;
import java.util.*;
import java.io.*;

/**
 * A simple demonstration of an executable command that simply prints out
 * the currently logged on users.
 *
 * @author Lee David Painter
 */
public class ExampleCommand extends ExecutableCommand {

  public ExampleCommand() {
      super(32768);
  }

  public int getExitCode() {
    return 0;
  }

  public void kill() {
  }

  /**
   * Here you process the command and get ready to execute but DO NOT perform
   * any IO yet, wait for onStart()
   *
   * @param cmd
   * @param environment
   * @return
   */
  public boolean createProcess(String cmd, Map environment) {
    return true;
  }

  /**
   * The session is now open so you can perform all the IO you want but DONT
   * block this method; if you have lots of interaction you will need to
   * start a thread or override the processStdinData method to receive
   * notification of data.
   */
  public void onStart() {

    String[] output = new String[] { "fsdffsdff;sdflkdsfsdfsdfsdf", "sadfsdfsdfsdfsdafsdafsdfdsfsdf","fsdffsdff;sdflkdsfsdfsdfsdf", "sadfsdfsdfsdfsdafsdafsdfdsfsdf", "fsdffsdff;sdflkdsfsdfsdfsdf", "sadfsdfsdfsdfsdafsdafsdfdsfsdf", "fsdffsdff;sdflkdsfsdfsdfsdf", "sadfsdfsdfsdfsdafsdafsdfdsfsdf", "fsdffsdff;sdflkdsfsdfsdfsdf", "sadfsdfsdfsdfsdafsdafsdfdsfsdf", "fsdffsdff;sdflkdsfsdfsdfsdf", "sadfsdfsdfsdfsdafsdafsdfdsfsdf", "fsdffsdff;sdflkdsfsdfsdfsdf", "sadfsdfsdfsdfsdafsdafsdfdsfsdf", "fsdffsdff;sdflkdsfsdfsdfsdf", "sadfsdfsdfsdfsdafsdafsdfdsfsdf", "fsdffsdff;sdflkdsfsdfsdfsdf", "sadfsdfsdfsdfsdafsdafsdfdsfsdf", "fsdffsdff;sdflkdsfsdfsdfsdf", "sadfsdfsdfsdfsdafsdafsdfdsfsdf", "fsdffsdff;sdflkdsfsdfsdfsdf", "sadfsdfsdfsdfsdafsdafsdfdsfsdf", "fsdffsdff;sdflkdsfsdfsdfsdf", "sadfsdfsdfsdfsdafsdafsdfdsfsdf", "fsdffsdff;sdflkdsfsdfsdfsdf", "sadfsdfsdfsdfsdafsdafsdfdsfsdf", "fsdffsdff;sdflkdsfsdfsdfsdf", "sadfsdfsdfsdfsdafsdafsdfdsfsdf", "fsdffsdff;sdflkdsfsdfsdfsdf", "sadfsdfsdfsdfsdafsdafsdfdsfsdf"  };
    try {
      String[] users = session.getContext().getAuthenticationProvider().
          getLoggedOnUsers();
      for(int i=0;i<output.length;i++)
        stdout.write((output[i] + "\r\n").getBytes());
    }
    catch (IOException ex) {
      try {
        stderr.write(ex.getMessage().getBytes());
      }
      catch (IOException ex1) {
      }
    } finally {
      session.close();
    }
  }

}
