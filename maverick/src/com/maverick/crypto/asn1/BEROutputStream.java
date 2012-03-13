package com.maverick.crypto.asn1;

import java.io.IOException;
import java.io.OutputStream;

public class BEROutputStream extends DEROutputStream
{
  public BEROutputStream(OutputStream paramOutputStream)
  {
    super(paramOutputStream);
  }

  public void writeObject(Object paramObject)
    throws IOException
  {
    if (paramObject == null)
      writeNull();
    else if ((paramObject instanceof DERObject))
      ((DERObject)paramObject).encode(this);
    else if ((paramObject instanceof DEREncodable))
      ((DEREncodable)paramObject).getDERObject().encode(this);
    else
      throw new IOException("object not BEREncodable");
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.BEROutputStream
 * JD-Core Version:    0.6.0
 */