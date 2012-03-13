package com.maverick.crypto.asn1;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DERInputStream extends FilterInputStream
  implements DERTags
{
  public DERInputStream(InputStream paramInputStream)
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
    while (i > 0)
    {
      int j = read(paramArrayOfByte, paramArrayOfByte.length - i, i);
      if (j < 0)
        throw new EOFException("unexpected end of stream");
      i -= j;
    }
  }

  protected DERObject buildObject(int paramInt, byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayInputStream localByteArrayInputStream;
    BERInputStream localBERInputStream;
    DERConstructedSequence localDERConstructedSequence;
    switch (paramInt)
    {
    case 5:
      return null;
    case 48:
      localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
      localBERInputStream = new BERInputStream(localByteArrayInputStream);
      localDERConstructedSequence = new DERConstructedSequence();
      try
      {
        while (true)
        {
          DERObject localDERObject1 = localBERInputStream.readObject();
          localDERConstructedSequence.addObject(localDERObject1);
        }
      }
      catch (EOFException localEOFException1)
      {
        return localDERConstructedSequence;
      }
    case 49:
      localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
      localBERInputStream = new BERInputStream(localByteArrayInputStream);
      ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
      try
      {
        while (true)
        {
          DERObject localDERObject2 = localBERInputStream.readObject();
          localASN1EncodableVector.add(localDERObject2);
        }
      }
      catch (EOFException localEOFException2)
      {
        return new DERConstructedSet(localASN1EncodableVector);
      }
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
      byte[] arrayOfByte = new byte[paramArrayOfByte.length - 1];
      System.arraycopy(paramArrayOfByte, 1, arrayOfByte, 0, paramArrayOfByte.length - 1);
      return new DERBitString(arrayOfByte, i);
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
    case 28:
      return new DERUniversalString(paramArrayOfByte);
    case 27:
      return new DERGeneralString(paramArrayOfByte);
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
      if ((paramInt & 0x1F) == 31)
        throw new IOException("unsupported high tag encountered");
      if (paramArrayOfByte.length == 0)
      {
        if ((paramInt & 0x20) == 0)
          return new DERTaggedObject(false, paramInt & 0x1F, new DERNull());
        return new DERTaggedObject(false, paramInt & 0x1F, new DERConstructedSequence());
      }
      if ((paramInt & 0x20) == 0)
        return new DERTaggedObject(false, paramInt & 0x1F, new DEROctetString(paramArrayOfByte));
      localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
      localBERInputStream = new BERInputStream(localByteArrayInputStream);
      DERObject localDERObject3 = localBERInputStream.readObject();
      if (localBERInputStream.available() == 0)
        return new DERTaggedObject(paramInt & 0x1F, localDERObject3);
      localDERConstructedSequence = new DERConstructedSequence();
      localDERConstructedSequence.addObject(localDERObject3);
      try
      {
        while (true)
        {
          localDERObject3 = localBERInputStream.readObject();
          localDERConstructedSequence.addObject(localDERObject3);
        }
      }
      catch (EOFException localEOFException3)
      {
        return new DERTaggedObject(false, paramInt & 0x1F, localDERConstructedSequence);
      }
    }
    return new DERUnknownTag(paramInt, paramArrayOfByte);
  }

  public DERObject readObject()
    throws IOException
  {
    int i = read();
    if (i == -1)
      throw new EOFException();
    int j = readLength();
    byte[] arrayOfByte = new byte[j];
    readFully(arrayOfByte);
    return buildObject(i, arrayOfByte);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERInputStream
 * JD-Core Version:    0.6.0
 */