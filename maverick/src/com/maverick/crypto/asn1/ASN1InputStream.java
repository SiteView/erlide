package com.maverick.crypto.asn1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class ASN1InputStream extends DERInputStream
{
  private DERObject db = new DERObject()
  {
    void encode(DEROutputStream paramDEROutputStream)
      throws IOException
    {
      throw new IOException("Eeek!");
    }
  };
  boolean cb = false;

  public ASN1InputStream(InputStream paramInputStream)
  {
    super(paramInputStream);
  }

  protected int readLength()
    throws IOException
  {
    int i = read();
    if (i < 0)
      throw new IOException("EOF found when length expected");
    if (i == 128)
      return -1;
    if (i > 127)
    {
      int j = i & 0x7F;
      i = 0;
      for (int k = 0; k < j; k++)
      {
        int m = read();
        if (m < 0)
          throw new IOException("EOF found reading length");
        i = (i << 8) + m;
      }
    }
    return i;
  }

  protected void readFully(byte[] paramArrayOfByte)
    throws IOException
  {
    int i = paramArrayOfByte.length;
    if (i == 0)
      return;
    int j;
    while ((j = read(paramArrayOfByte, paramArrayOfByte.length - i, i)) > 0)
      if (i -= j == 0)
        return;
    if (i != 0)
      throw new EOFException("EOF encountered in middle of object");
  }

  protected DERObject buildObject(int paramInt, byte[] paramArrayOfByte)
    throws IOException
  {
    if ((paramInt & 0x40) != 0)
      return new DERApplicationSpecific(paramInt, paramArrayOfByte);
    ByteArrayInputStream localByteArrayInputStream;
    ASN1InputStream localASN1InputStream;
    ASN1EncodableVector localASN1EncodableVector;
    DERObject localDERObject1;
    switch (paramInt)
    {
    case 5:
      return new DERNull();
    case 48:
      localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
      localASN1InputStream = new ASN1InputStream(localByteArrayInputStream);
      localASN1EncodableVector = new ASN1EncodableVector();
      for (localDERObject1 = localASN1InputStream.readObject(); localDERObject1 != null; localDERObject1 = localASN1InputStream.readObject())
        localASN1EncodableVector.add(localDERObject1);
      return new DERSequence(localASN1EncodableVector);
    case 49:
      localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
      localASN1InputStream = new ASN1InputStream(localByteArrayInputStream);
      localASN1EncodableVector = new ASN1EncodableVector();
      for (localDERObject1 = localASN1InputStream.readObject(); localDERObject1 != null; localDERObject1 = localASN1InputStream.readObject())
        localASN1EncodableVector.add(localDERObject1);
      return new DERSet(localASN1EncodableVector);
    case 1:
      return new DERBoolean(paramArrayOfByte);
    case 2:
      return new DERInteger(paramArrayOfByte);
    case 10:
      return new DEREnumerated(paramArrayOfByte);
    case 6:
      return new DERObjectIdentifier(paramArrayOfByte);
    case 3:
      int i = paramArrayOfByte[0];
      byte[] arrayOfByte1 = new byte[paramArrayOfByte.length - 1];
      System.arraycopy(paramArrayOfByte, 1, arrayOfByte1, 0, paramArrayOfByte.length - 1);
      return new DERBitString(arrayOfByte1, i);
    case 12:
      return new DERUTF8String(paramArrayOfByte);
    case 19:
      return new DERPrintableString(paramArrayOfByte);
    case 22:
      return new DERIA5String(paramArrayOfByte);
    case 20:
      return new DERT61String(paramArrayOfByte);
    case 26:
      return new DERVisibleString(paramArrayOfByte);
    case 27:
      return new DERGeneralString(paramArrayOfByte);
    case 28:
      return new DERUniversalString(paramArrayOfByte);
    case 30:
      return new DERBMPString(paramArrayOfByte);
    case 4:
      return new DEROctetString(paramArrayOfByte);
    case 23:
      return new DERUTCTime(paramArrayOfByte);
    case 24:
      return new DERGeneralizedTime(paramArrayOfByte);
    case 7:
    case 8:
    case 9:
    case 11:
    case 13:
    case 14:
    case 15:
    case 16:
    case 17:
    case 18:
    case 21:
    case 25:
    case 29:
    case 31:
    case 32:
    case 33:
    case 34:
    case 35:
    case 36:
    case 37:
    case 38:
    case 39:
    case 40:
    case 41:
    case 42:
    case 43:
    case 44:
    case 45:
    case 46:
    case 47:
    }
    if ((paramInt & 0x80) != 0)
    {
      int j = paramInt & 0x1F;
      if (j == 31)
      {
        int k = 0;
        j = 0;
        while ((paramArrayOfByte[k] & 0x80) != 0)
        {
          j |= paramArrayOfByte[(k++)] & 0x7F;
          j <<= 7;
        }
        j |= paramArrayOfByte[k] & 0x7F;
        byte[] arrayOfByte2 = paramArrayOfByte;
        paramArrayOfByte = new byte[arrayOfByte2.length - (k + 1)];
        System.arraycopy(arrayOfByte2, k + 1, paramArrayOfByte, 0, paramArrayOfByte.length);
      }
      if (paramArrayOfByte.length == 0)
      {
        if ((paramInt & 0x20) == 0)
          return new DERTaggedObject(false, j, new DERNull());
        return new DERTaggedObject(false, j, new DERSequence());
      }
      if ((paramInt & 0x20) == 0)
        return new DERTaggedObject(false, j, new DEROctetString(paramArrayOfByte));
      localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
      localASN1InputStream = new ASN1InputStream(localByteArrayInputStream);
      DERObject localDERObject2 = localASN1InputStream.readObject();
      if (localASN1InputStream.available() == 0)
        return new DERTaggedObject(j, localDERObject2);
      localASN1EncodableVector = new ASN1EncodableVector();
      while (localDERObject2 != null)
      {
        localASN1EncodableVector.add(localDERObject2);
        localDERObject2 = localASN1InputStream.readObject();
      }
      return new DERTaggedObject(false, j, new DERSequence(localASN1EncodableVector));
    }
    return new DERUnknownTag(paramInt, paramArrayOfByte);
  }

  private byte[] c()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    int i;
    for (int j = read(); ((i = read()) >= 0) && ((j != 0) || (i != 0)); j = i)
      localByteArrayOutputStream.write(j);
    return localByteArrayOutputStream.toByteArray();
  }

  private BERConstructedOctetString b()
    throws IOException
  {
    Vector localVector = new Vector();
    while (true)
    {
      DERObject localDERObject = readObject();
      if (localDERObject == this.db)
        break;
      localVector.addElement(localDERObject);
    }
    return new BERConstructedOctetString(localVector);
  }

  public DERObject readObject()
    throws IOException
  {
    int i = read();
    if (i == -1)
    {
      if (this.cb)
        throw new EOFException("attempt to read past end of file.");
      this.cb = true;
      return null;
    }
    int j = readLength();
    if (j < 0)
    {
      DERObject localDERObject1;
      switch (i)
      {
      case 5:
        return new BERNull();
      case 48:
        localObject1 = new ASN1EncodableVector();
        while (true)
        {
          localDERObject1 = readObject();
          if (localDERObject1 == this.db)
            break;
          ((ASN1EncodableVector)localObject1).add(localDERObject1);
        }
        return new BERSequence((DEREncodableVector)localObject1);
      case 49:
        localObject1 = new ASN1EncodableVector();
        while (true)
        {
          localDERObject1 = readObject();
          if (localDERObject1 == this.db)
            break;
          ((ASN1EncodableVector)localObject1).add(localDERObject1);
        }
        return new BERSet((DEREncodableVector)localObject1);
      case 36:
        return b();
      }
      if ((i & 0x80) != 0)
      {
        int k = i & 0x1F;
        if (k == 31)
        {
          int m = read();
          k = 0;
          while ((m >= 0) && ((m & 0x80) != 0))
          {
            k |= m & 0x7F;
            k <<= 7;
            m = read();
          }
          k |= m & 0x7F;
        }
        if ((i & 0x20) == 0)
        {
          localObject2 = c();
          return new BERTaggedObject(false, k, new DEROctetString(localObject2));
        }
        Object localObject2 = readObject();
        if (localObject2 == this.db)
          return new DERTaggedObject(k);
        DERObject localDERObject2 = readObject();
        if (localDERObject2 == this.db)
          return new BERTaggedObject(k, (DEREncodable)localObject2);
        localObject1 = new ASN1EncodableVector();
        ((ASN1EncodableVector)localObject1).add((DEREncodable)localObject2);
        do
        {
          ((ASN1EncodableVector)localObject1).add(localDERObject2);
          localDERObject2 = readObject();
        }
        while (localDERObject2 != this.db);
        return new BERTaggedObject(false, k, new BERSequence((DEREncodableVector)localObject1));
      }
      throw new IOException("unknown BER object encountered");
    }
    if ((i == 0) && (j == 0))
      return this.db;
    Object localObject1 = new byte[j];
    readFully(localObject1);
    return (DERObject)(DERObject)buildObject(i, localObject1);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.ASN1InputStream
 * JD-Core Version:    0.6.0
 */