package com.maverick.crypto.asn1;

import java.io.IOException;
import java.io.OutputStream;

public class ASN1OutputStream extends DEROutputStream
{
  public ASN1OutputStream(OutputStream paramOutputStream)
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
      throw new IOException("object not ASN1Encodable");
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.ASN1OutputStream
 * JD-Core Version:    0.6.0
 */