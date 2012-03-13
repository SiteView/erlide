package com.maverick.crypto.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DERObjectIdentifier extends DERObject
{
  String zb;

  public static DERObjectIdentifier getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERObjectIdentifier)))
      return (DERObjectIdentifier)paramObject;
    if ((paramObject instanceof ASN1OctetString))
      return new DERObjectIdentifier(((ASN1OctetString)paramObject).getOctets());
    if ((paramObject instanceof ASN1TaggedObject))
      return getInstance(((ASN1TaggedObject)paramObject).getObject());
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERObjectIdentifier getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  DERObjectIdentifier(byte[] paramArrayOfByte)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = 0;
    int j = 1;
    for (int k = 0; k != paramArrayOfByte.length; k++)
    {
      int m = paramArrayOfByte[k] & 0xFF;
      i = i * 128 + (m & 0x7F);
      if ((m & 0x80) != 0)
        continue;
      if (j != 0)
      {
        switch (i / 40)
        {
        case 0:
          localStringBuffer.append('0');
          break;
        case 1:
          localStringBuffer.append('1');
          i -= 40;
          break;
        default:
          localStringBuffer.append('2');
          i -= 80;
        }
        j = 0;
      }
      localStringBuffer.append('.');
      localStringBuffer.append(Integer.toString(i));
      i = 0;
    }
    this.zb = localStringBuffer.toString();
  }

  public DERObjectIdentifier(String paramString)
  {
    this.zb = paramString;
  }

  public String getId()
  {
    return this.zb;
  }

  private void b(OutputStream paramOutputStream, int paramInt)
    throws IOException
  {
    if (paramInt >= 128)
    {
      if (paramInt >= 16384)
      {
        if (paramInt >= 2097152)
        {
          if (paramInt >= 268435456)
            paramOutputStream.write(paramInt >> 28 | 0x80);
          paramOutputStream.write(paramInt >> 21 | 0x80);
        }
        paramOutputStream.write(paramInt >> 14 | 0x80);
      }
      paramOutputStream.write(paramInt >> 7 | 0x80);
    }
    paramOutputStream.write(paramInt & 0x7F);
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    OIDTokenizer localOIDTokenizer = new OIDTokenizer(this.zb);
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    DEROutputStream localDEROutputStream = new DEROutputStream(localByteArrayOutputStream);
    b(localByteArrayOutputStream, Integer.parseInt(localOIDTokenizer.nextToken()) * 40 + Integer.parseInt(localOIDTokenizer.nextToken()));
    while (localOIDTokenizer.hasMoreTokens())
      b(localByteArrayOutputStream, Integer.parseInt(localOIDTokenizer.nextToken()));
    localDEROutputStream.close();
    byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
    paramDEROutputStream.b(6, arrayOfByte);
  }

  public int hashCode()
  {
    return this.zb.hashCode();
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof DERObjectIdentifier)))
      return false;
    return this.zb.equals(((DERObjectIdentifier)paramObject).zb);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERObjectIdentifier
 * JD-Core Version:    0.6.0
 */