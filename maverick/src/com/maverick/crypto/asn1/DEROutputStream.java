package com.maverick.crypto.asn1;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DEROutputStream extends FilterOutputStream
  implements DERTags
{
  public DEROutputStream(OutputStream paramOutputStream)
  {
    super(paramOutputStream);
  }

  private void b(int paramInt)
    throws IOException
  {
    if (paramInt > 127)
    {
      int i = 1;
      int j = paramInt;
      while (j >>>= 8 != 0)
        i++;
      write((byte)(i | 0x80));
      for (int k = (i - 1) * 8; k >= 0; k -= 8)
        write((byte)(paramInt >> k));
    }
    else
    {
      write((byte)paramInt);
    }
  }

  void b(int paramInt, byte[] paramArrayOfByte)
    throws IOException
  {
    write(paramInt);
    b(paramArrayOfByte.length);
    write(paramArrayOfByte);
  }

  protected void writeNull()
    throws IOException
  {
    write(5);
    write(0);
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
      throw new IOException("object not DEREncodable");
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.DEROutputStream
 * JD-Core Version:    0.6.0
 */