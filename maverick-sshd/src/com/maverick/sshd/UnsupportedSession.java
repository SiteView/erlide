package com.maverick.sshd;

public class UnsupportedSession extends SessionChannel
{
  String Ñ = "This server does not support an interactive session.\r\nGoodbye.\r\n";

  protected void processStdinData(byte[] paramArrayOfByte)
  {
  }

  protected void processStderrData(byte[] paramArrayOfByte)
  {
  }

  protected void onChannelClosed()
  {
  }

  protected boolean executeCommand(String paramString)
  {
    return false;
  }

  protected void changeWindowDimensions(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
  }

  protected void onSessionOpen()
  {
  }

  protected void onLocalEOF()
  {
  }

  protected boolean startShell()
  {
    1 local1 = new Thread()
    {
      public void run()
      {
        try
        {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
        UnsupportedSession.this.sendChannelData(UnsupportedSession.this.Ñ.getBytes());
        UnsupportedSession.this.close();
      }
    };
    local1.start();
    return true;
  }

  protected boolean allocatePseudoTerminal(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
  {
    return true;
  }

  protected void processSignal(String paramString)
  {
  }

  protected void onRemoteEOF()
  {
  }

  protected boolean setEnvironmentVariable(String paramString1, String paramString2)
  {
    return true;
  }

  protected void onChannelOpenFailure()
  {
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.UnsupportedSession
 * JD-Core Version:    0.6.0
 */