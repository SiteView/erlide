package socks;

import java.io.IOException;

public abstract interface UDPEncapsulation
{
  public abstract byte[] udpEncapsulate(byte[] paramArrayOfByte, boolean paramBoolean)
    throws IOException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     socks.UDPEncapsulation
 * JD-Core Version:    0.6.0
 */