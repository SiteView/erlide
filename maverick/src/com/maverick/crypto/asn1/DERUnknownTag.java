package com.maverick.crypto.asn1;

import java.io.IOException;

public class DERUnknownTag extends DERObject
{
  int qb;
  byte[] rb;

  public DERUnknownTag(int paramInt, byte[] paramArrayOfByte)
  {
    this.qb = paramInt;
    this.rb = paramArrayOfByte;
  }

  public int getTag()
  {
    return this.qb;
  }

  public byte[] getData()
  {
    return this.rb;
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.b(this.qb, this.rb);
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof DERUnknownTag)))
      return false;
    DERUnknownTag localDERUnknownTag = (DERUnknownTag)paramObject;
    if (this.qb != localDERUnknownTag.qb)
      return false;
    if (this.rb.length != localDERUnknownTag.rb.length)
      return false;
    for (int i = 0; i < this.rb.length; i++)
      if (this.rb[i] != localDERUnknownTag.rb[i])
        return false;
    return true;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERUnknownTag
 * JD-Core Version:    0.6.0
 */