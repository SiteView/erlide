package com.maverick.util;

import com.maverick.events.Event;
import com.maverick.events.EventService;
import com.maverick.events.EventServiceImplementation;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

public class IOStreamConnector
{
  private InputStream I = null;
  private OutputStream B = null;
  private Thread H;
  private long J;
  private boolean C = true;
  private boolean A = true;
  boolean D = false;
  boolean F = false;
  IOException G;
  public static final int DEFAULT_BUFFER_SIZE = 32768;
  int E = 32768;
  protected Vector listenerList = new Vector();

  public IOStreamConnector()
  {
  }

  public IOStreamConnector(InputStream paramInputStream, OutputStream paramOutputStream)
  {
    connect(paramInputStream, paramOutputStream);
  }

  public void close()
  {
    if (this.H == null)
      this.F = true;
    this.D = false;
    if (this.H != null)
      this.H.interrupt();
  }

  public IOException getLastError()
  {
    return this.G;
  }

  public void setCloseInput(boolean paramBoolean)
  {
    this.C = paramBoolean;
  }

  public void setCloseOutput(boolean paramBoolean)
  {
    this.A = paramBoolean;
  }

  public void setBufferSize(int paramInt)
  {
    if (paramInt <= 0)
      throw new IllegalArgumentException("Buffer size must be greater than zero!");
    this.E = paramInt;
  }

  public void connect(InputStream paramInputStream, OutputStream paramOutputStream)
  {
    this.I = paramInputStream;
    this.B = paramOutputStream;
    this.H = new Thread(new _A());
    this.H.setDaemon(true);
    this.H.setName("IOStreamConnector " + paramInputStream.toString() + ">>" + paramOutputStream.toString());
    this.H.start();
  }

  public long getBytes()
  {
    return this.J;
  }

  public boolean isClosed()
  {
    return this.F;
  }

  public void addListener(IOStreamConnectorListener paramIOStreamConnectorListener)
  {
    this.listenerList.addElement(paramIOStreamConnectorListener);
  }

  public void removeListener(IOStreamConnectorListener paramIOStreamConnectorListener)
  {
    this.listenerList.removeElement(paramIOStreamConnectorListener);
  }

  public static abstract interface IOStreamConnectorListener
  {
    public abstract void connectorClosed(IOStreamConnector paramIOStreamConnector);

    public abstract void dataTransfered(byte[] paramArrayOfByte, int paramInt);
  }

  class _A
    implements Runnable
  {
    _A()
    {
    }

    public void run()
    {
      byte[] arrayOfByte = new byte[IOStreamConnector.this.E];
      int i = 0;
      IOStreamConnector.this.D = true;
      while (IOStreamConnector.this.D)
        try
        {
          i = IOStreamConnector.this.I.read(arrayOfByte, 0, arrayOfByte.length);
          if (i > 0)
          {
            IOStreamConnector.this.B.write(arrayOfByte, 0, i);
            IOStreamConnector.access$214(IOStreamConnector.this, i);
            IOStreamConnector.this.B.flush();
            for (int j = 0; j < IOStreamConnector.this.listenerList.size(); j++)
              ((IOStreamConnector.IOStreamConnectorListener)IOStreamConnector.this.listenerList.elementAt(j)).dataTransfered(arrayOfByte, i);
          }
          else if (i < 0)
          {
            EventServiceImplementation.getInstance().fireEvent(new Event(this, 110, true).addAttribute("LOG_MESSAGE", "Received EOF from InputStream " + IOStreamConnector.this.I.toString()));
            IOStreamConnector.this.D = false;
          }
        }
        catch (IOException localIOException1)
        {
          if (IOStreamConnector.this.D)
          {
            IOStreamConnector.this.G = localIOException1;
            IOStreamConnector.this.D = false;
            EventServiceImplementation.getInstance().fireEvent(new Event(this, 110, true).addAttribute("LOG_MESSAGE", "Error from InputStream").addAttribute("THROWABLE", localIOException1));
          }
        }
      if (IOStreamConnector.this.C)
        try
        {
          IOStreamConnector.this.I.close();
        }
        catch (IOException localIOException2)
        {
        }
      if (IOStreamConnector.this.A)
        try
        {
          IOStreamConnector.this.B.close();
        }
        catch (IOException localIOException3)
        {
        }
      IOStreamConnector.this.F = true;
      for (int k = 0; k < IOStreamConnector.this.listenerList.size(); k++)
        ((IOStreamConnector.IOStreamConnectorListener)IOStreamConnector.this.listenerList.elementAt(k)).connectorClosed(IOStreamConnector.this);
      IOStreamConnector.access$502(IOStreamConnector.this, null);
    }
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.util.IOStreamConnector
 * JD-Core Version:    0.6.0
 */