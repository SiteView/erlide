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
  private InputStream j = null;
  private OutputStream c = null;
  private Thread i;
  private long k;
  private boolean d = true;
  private boolean b = true;
  boolean e = false;
  boolean g = false;
  IOException h;
  public static final int DEFAULT_BUFFER_SIZE = 32768;
  int f = 32768;
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
    if (this.i == null)
      this.g = true;
    this.e = false;
    if (this.i != null)
      this.i.interrupt();
  }

  public IOException getLastError()
  {
    return this.h;
  }

  public void setCloseInput(boolean paramBoolean)
  {
    this.d = paramBoolean;
  }

  public void setCloseOutput(boolean paramBoolean)
  {
    this.b = paramBoolean;
  }

  public void setBufferSize(int paramInt)
  {
    if (paramInt <= 0)
      throw new IllegalArgumentException("Buffer size must be greater than zero!");
    this.f = paramInt;
  }

  public void connect(InputStream paramInputStream, OutputStream paramOutputStream)
  {
    this.j = paramInputStream;
    this.c = paramOutputStream;
    this.i = new Thread(new _b());
    this.i.setDaemon(true);
    this.i.setName("IOStreamConnector " + paramInputStream.toString() + ">>" + paramOutputStream.toString());
    this.i.start();
  }

  public long getBytes()
  {
    return this.k;
  }

  public boolean isClosed()
  {
    return this.g;
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

  class _b
    implements Runnable
  {
    _b()
    {
    }

    public void run()
    {
      byte[] arrayOfByte = new byte[IOStreamConnector.this.f];
      int i = 0;
      IOStreamConnector.this.e = true;
      while (IOStreamConnector.this.e)
        try
        {
          i = IOStreamConnector.e(IOStreamConnector.this).read(arrayOfByte, 0, arrayOfByte.length);
          if (i > 0)
          {
            IOStreamConnector.checkVersion(IOStreamConnector.this).write(arrayOfByte, 0, i);
            IOStreamConnector.b(IOStreamConnector.this, i);
            IOStreamConnector.checkVersion(IOStreamConnector.this).flush();
            for (int j = 0; j < IOStreamConnector.this.listenerList.size(); j++)
              ((IOStreamConnector.IOStreamConnectorListener)IOStreamConnector.this.listenerList.elementAt(j)).dataTransfered(arrayOfByte, i);
          }
          else if (i < 0)
          {
            EventServiceImplementation.getInstance().fireEvent(new Event(this, 110, true).addAttribute("LOG_MESSAGE", "Received EOF from InputStream " + IOStreamConnector.e(IOStreamConnector.this).toString()));
            IOStreamConnector.this.e = false;
          }
        }
        catch (IOException localIOException1)
        {
          if (IOStreamConnector.this.e)
          {
            IOStreamConnector.this.h = localIOException1;
            IOStreamConnector.this.e = false;
            EventServiceImplementation.getInstance().fireEvent(new Event(this, 110, true).addAttribute("LOG_MESSAGE", "Error from InputStream").addAttribute("THROWABLE", localIOException1));
          }
        }
      if (IOStreamConnector.b(IOStreamConnector.this))
        try
        {
          IOStreamConnector.e(IOStreamConnector.this).close();
        }
        catch (IOException localIOException2)
        {
        }
      if (IOStreamConnector.d(IOStreamConnector.this))
        try
        {
          IOStreamConnector.checkVersion(IOStreamConnector.this).close();
        }
        catch (IOException localIOException3)
        {
        }
      IOStreamConnector.this.g = true;
      for (int k = 0; k < IOStreamConnector.this.listenerList.size(); k++)
        ((IOStreamConnector.IOStreamConnectorListener)IOStreamConnector.this.listenerList.elementAt(k)).connectorClosed(IOStreamConnector.this);
      IOStreamConnector.b(IOStreamConnector.this, null);
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.util.IOStreamConnector
 * JD-Core Version:    0.6.0
 */