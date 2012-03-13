package com.maverick.crypto.asn1;

import java.io.IOException;

public class DERUnknownTag extends DERObject
{
  int t;
  byte[] u;

  public DERUnknownTag(int paramInt, byte[] paramArrayOfByte)
  {
    this.t = paramInt;
    this.u = paramArrayOfByte;
  }

  public int getTag()
  {
    return this.t;
  }

  public byte[] getData()
  {
    return this.u;
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.A(this.t, this.u);
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof DERUnknownTag)))
      return false;
    DERUnknownTag localDERUnknownTag = (DERUnknownTag)paramObject;
    if (this.t != localDERUnknownTag.t)
      return false;
    if (this.u.length != localDERUnknownTag.u.length)
      return false;
    for (int i = 0; i < this.u.length; i++)
      if (this.u[i] != localDERUnknownTag.u[i])
        return false;
    return true;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERUnknownTag
 * JD-Core Version:    0.6.0
 */