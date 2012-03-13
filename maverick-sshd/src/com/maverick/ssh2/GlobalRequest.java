package com.maverick.ssh2;

public class GlobalRequest
{
  String A;
  byte[] B;

  public GlobalRequest(String paramString, byte[] paramArrayOfByte)
  {
    this.A = paramString;
    this.B = paramArrayOfByte;
  }

  public String getName()
  {
    return this.A;
  }

  public byte[] getData()
  {
    return this.B;
  }

  public void setData(byte[] paramArrayOfByte)
  {
    this.B = paramArrayOfByte;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.ssh2.GlobalRequest
 * JD-Core Version:    0.6.0
 */