package socks;

import java.io.IOException;

public class SocksException extends IOException
{
  static final String[] d = { "Succeeded", "General SOCKS server failure", "Connection not allowed by ruleset", "Network unreachable", "Host unreachable", "Connection refused", "TTL expired", "Command not supported", "Address type not supported" };
  static final String[] e = { "SOCKS server not specified", "Unable to contact SOCKS server", "IO error", "None of Authentication methods are supported", "Authentication failed", "General SOCKS fault" };
  String b;
  int c;

  public SocksException(int paramInt)
  {
    this.c = paramInt;
    if (paramInt >> 16 == 0)
    {
      this.b = (paramInt <= d.length ? d[paramInt] : "Unknown error message");
    }
    else
    {
      paramInt = (paramInt >> 16) - 1;
      this.b = (paramInt <= e.length ? e[paramInt] : "Unknown error message");
    }
  }

  public SocksException(int paramInt, String paramString)
  {
    this.c = paramInt;
    this.b = paramString;
  }

  public int getErrorCode()
  {
    return this.c;
  }

  public String toString()
  {
    return this.b;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.SocksException
 * JD-Core Version:    0.6.0
 */