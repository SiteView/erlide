package com.maverick.ssh2;

public class GlobalRequest
{
  String b;
  byte[] c;

  public GlobalRequest(String paramString, byte[] paramArrayOfByte)
  {
    this.b = paramString;
    this.c = paramArrayOfByte;
  }

  public String getName()
  {
    return this.b;
  }

  public byte[] getData()
  {
    return this.c;
  }

  public void setData(byte[] paramArrayOfByte)
  {
    this.c = paramArrayOfByte;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.ssh2.GlobalRequest
 * JD-Core Version:    0.6.0
 */