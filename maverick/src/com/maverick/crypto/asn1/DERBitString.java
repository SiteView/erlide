package com.maverick.crypto.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DERBitString extends DERObject
  implements DERString
{
  private static final char[] pb = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
  protected byte[] data;
  protected int padBits;

  protected static int getPadBits(int paramInt)
  {
    int i = 0;
    for (int j = 3; j >= 0; j--)
    {
      if (j != 0)
      {
        if (paramInt >> j * 8 == 0)
          continue;
        i = paramInt >> j * 8 & 0xFF;
        break;
      }
      if (paramInt == 0)
        continue;
      i = paramInt & 0xFF;
      break;
    }
    if (i == 0)
      return 7;
    for (j = 1; (i <<= 1 & 0xFF) != 0; j++);
    return 8 - j;
  }

  protected static byte[] getBytes(int paramInt)
  {
    int i = 4;
    for (int j = 3; (j >= 1) && ((paramInt & 255 << j * 8) == 0); j--)
      i--;
    byte[] arrayOfByte = new byte[i];
    for (int k = 0; k < i; k++)
      arrayOfByte[k] = (byte)(paramInt >> k * 8 & 0xFF);
    return arrayOfByte;
  }

  public static DERBitString getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERBitString)))
      return (DERBitString)paramObject;
    if ((paramObject instanceof ASN1OctetString))
    {
      byte[] arrayOfByte1 = ((ASN1OctetString)paramObject).getOctets();
      int i = arrayOfByte1[0];
      byte[] arrayOfByte2 = new byte[arrayOfByte1.length - 1];
      System.arraycopy(arrayOfByte1, 1, arrayOfByte2, 0, arrayOfByte1.length - 1);
      return new DERBitString(arrayOfByte2, i);
    }
    if ((paramObject instanceof ASN1TaggedObject))
      return getInstance(((ASN1TaggedObject)paramObject).getObject());
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERBitString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  protected DERBitString(byte paramByte, int paramInt)
  {
    this.data = new byte[1];
    this.data[0] = paramByte;
    this.padBits = paramInt;
  }

  public DERBitString(byte[] paramArrayOfByte, int paramInt)
  {
    this.data = paramArrayOfByte;
    this.padBits = paramInt;
  }

  public DERBitString(byte[] paramArrayOfByte)
  {
    this(paramArrayOfByte, 0);
  }

  public DERBitString(DEREncodable paramDEREncodable)
  {
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      DEROutputStream localDEROutputStream = new DEROutputStream(localByteArrayOutputStream);
      localDEROutputStream.writeObject(paramDEREncodable);
      localDEROutputStream.close();
      this.data = localByteArrayOutputStream.toByteArray();
      this.padBits = 0;
    }
    catch (IOException localIOException)
    {
      throw new IllegalArgumentException("Error processing object : " + localIOException.toString());
    }
  }

  public byte[] getBytes()
  {
    return this.data;
  }

  public int getPadBits()
  {
    return this.padBits;
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[getBytes().length + 1];
    arrayOfByte[0] = (byte)getPadBits();
    System.arraycopy(getBytes(), 0, arrayOfByte, 1, arrayOfByte.length - 1);
    paramDEROutputStream.b(3, arrayOfByte);
  }

  public int hashCode()
  {
    int i = 0;
    for (int j = 0; j != this.data.length; j++)
      i ^= (this.data[j] & 0xFF) << j % 4;
    return i;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof DERBitString)))
      return false;
    DERBitString localDERBitString = (DERBitString)paramObject;
    if (this.data.length != localDERBitString.data.length)
      return false;
    for (int i = 0; i != this.data.length; i++)
      if (this.data[i] != localDERBitString.data[i])
        return false;
    return this.padBits == localDERBitString.padBits;
  }

  public String getString()
  {
    StringBuffer localStringBuffer = new StringBuffer("#");
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    ASN1OutputStream localASN1OutputStream = new ASN1OutputStream(localByteArrayOutputStream);
    try
    {
      localASN1OutputStream.writeObject(this);
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException("internal error encoding BitString");
    }
    byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
    for (int i = 0; i != arrayOfByte.length; i++)
    {
      localStringBuffer.append(pb[((arrayOfByte[i] >>> 4) % 15)]);
      localStringBuffer.append(pb[(arrayOfByte[i] & 0xF)]);
    }
    return localStringBuffer.toString();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.DERBitString
 * JD-Core Version:    0.6.0
 */