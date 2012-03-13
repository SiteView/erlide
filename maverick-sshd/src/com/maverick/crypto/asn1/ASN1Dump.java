package com.maverick.crypto.asn1;

import com.maverick.crypto.encoders.Hex;
import java.util.Enumeration;

public class ASN1Dump
{
  private static String A = "    ";

  public static String _dumpAsString(String paramString, DERObject paramDERObject)
  {
    StringBuffer localStringBuffer;
    Object localObject1;
    Object localObject2;
    Object localObject3;
    if ((paramDERObject instanceof ASN1Sequence))
    {
      localStringBuffer = new StringBuffer();
      localObject1 = ((ASN1Sequence)paramDERObject).getObjects();
      localObject2 = paramString + A;
      localStringBuffer.append(paramString);
      if ((paramDERObject instanceof BERConstructedSequence))
        localStringBuffer.append("BER ConstructedSequence");
      else if ((paramDERObject instanceof DERConstructedSequence))
        localStringBuffer.append("DER ConstructedSequence");
      else if ((paramDERObject instanceof DERSequence))
        localStringBuffer.append("DER Sequence");
      else if ((paramDERObject instanceof BERSequence))
        localStringBuffer.append("BER Sequence");
      else
        localStringBuffer.append("Sequence");
      localStringBuffer.append(System.getProperty("line.separator"));
      while (((Enumeration)localObject1).hasMoreElements())
      {
        localObject3 = ((Enumeration)localObject1).nextElement();
        if ((localObject3 == null) || (localObject3.equals(new DERNull())))
        {
          localStringBuffer.append((String)localObject2);
          localStringBuffer.append("NULL");
          localStringBuffer.append(System.getProperty("line.separator"));
        }
        else if ((localObject3 instanceof DERObject))
        {
          localStringBuffer.append(_dumpAsString((String)localObject2, (DERObject)localObject3));
        }
        else
        {
          localStringBuffer.append(_dumpAsString((String)localObject2, ((DEREncodable)localObject3).getDERObject()));
        }
      }
      return localStringBuffer.toString();
    }
    if ((paramDERObject instanceof DERTaggedObject))
    {
      localStringBuffer = new StringBuffer();
      localObject1 = paramString + A;
      localStringBuffer.append(paramString);
      if ((paramDERObject instanceof BERTaggedObject))
        localStringBuffer.append("BER Tagged [");
      else
        localStringBuffer.append("Tagged [");
      localObject2 = (DERTaggedObject)paramDERObject;
      localStringBuffer.append(Integer.toString(((DERTaggedObject)localObject2).getTagNo()));
      localStringBuffer.append("]");
      if (!((DERTaggedObject)localObject2).isExplicit())
        localStringBuffer.append(" IMPLICIT ");
      localStringBuffer.append(System.getProperty("line.separator"));
      if (((DERTaggedObject)localObject2).isEmpty())
      {
        localStringBuffer.append((String)localObject1);
        localStringBuffer.append("EMPTY");
        localStringBuffer.append(System.getProperty("line.separator"));
      }
      else
      {
        localStringBuffer.append(_dumpAsString((String)localObject1, ((DERTaggedObject)localObject2).getObject()));
      }
      return localStringBuffer.toString();
    }
    if ((paramDERObject instanceof DERConstructedSet))
    {
      localStringBuffer = new StringBuffer();
      localObject1 = ((ASN1Set)paramDERObject).getObjects();
      localObject2 = paramString + A;
      localStringBuffer.append(paramString);
      localStringBuffer.append("ConstructedSet");
      localStringBuffer.append(System.getProperty("line.separator"));
      while (((Enumeration)localObject1).hasMoreElements())
      {
        localObject3 = ((Enumeration)localObject1).nextElement();
        if (localObject3 == null)
        {
          localStringBuffer.append((String)localObject2);
          localStringBuffer.append("NULL");
          localStringBuffer.append(System.getProperty("line.separator"));
        }
        else if ((localObject3 instanceof DERObject))
        {
          localStringBuffer.append(_dumpAsString((String)localObject2, (DERObject)localObject3));
        }
        else
        {
          localStringBuffer.append(_dumpAsString((String)localObject2, ((DEREncodable)localObject3).getDERObject()));
        }
      }
      return localStringBuffer.toString();
    }
    if ((paramDERObject instanceof BERSet))
    {
      localStringBuffer = new StringBuffer();
      localObject1 = ((ASN1Set)paramDERObject).getObjects();
      localObject2 = paramString + A;
      localStringBuffer.append(paramString);
      localStringBuffer.append("BER Set");
      localStringBuffer.append(System.getProperty("line.separator"));
      while (((Enumeration)localObject1).hasMoreElements())
      {
        localObject3 = ((Enumeration)localObject1).nextElement();
        if (localObject3 == null)
        {
          localStringBuffer.append((String)localObject2);
          localStringBuffer.append("NULL");
          localStringBuffer.append(System.getProperty("line.separator"));
        }
        else if ((localObject3 instanceof DERObject))
        {
          localStringBuffer.append(_dumpAsString((String)localObject2, (DERObject)localObject3));
        }
        else
        {
          localStringBuffer.append(_dumpAsString((String)localObject2, ((DEREncodable)localObject3).getDERObject()));
        }
      }
      return localStringBuffer.toString();
    }
    if ((paramDERObject instanceof DERSet))
    {
      localStringBuffer = new StringBuffer();
      localObject1 = ((ASN1Set)paramDERObject).getObjects();
      localObject2 = paramString + A;
      localStringBuffer.append(paramString);
      localStringBuffer.append("DER Set");
      localStringBuffer.append(System.getProperty("line.separator"));
      while (((Enumeration)localObject1).hasMoreElements())
      {
        localObject3 = ((Enumeration)localObject1).nextElement();
        if (localObject3 == null)
        {
          localStringBuffer.append((String)localObject2);
          localStringBuffer.append("NULL");
          localStringBuffer.append(System.getProperty("line.separator"));
        }
        else if ((localObject3 instanceof DERObject))
        {
          localStringBuffer.append(_dumpAsString((String)localObject2, (DERObject)localObject3));
        }
        else
        {
          localStringBuffer.append(_dumpAsString((String)localObject2, ((DEREncodable)localObject3).getDERObject()));
        }
      }
      return localStringBuffer.toString();
    }
    if ((paramDERObject instanceof DERObjectIdentifier))
      return paramString + "ObjectIdentifier(" + ((DERObjectIdentifier)paramDERObject).getId() + ")" + System.getProperty("line.separator");
    if ((paramDERObject instanceof DERBoolean))
      return paramString + "Boolean(" + ((DERBoolean)paramDERObject).isTrue() + ")" + System.getProperty("line.separator");
    if ((paramDERObject instanceof DERInteger))
      return paramString + "Integer(" + ((DERInteger)paramDERObject).getValue() + ")" + System.getProperty("line.separator");
    if ((paramDERObject instanceof DEROctetString))
      return paramString + paramDERObject.toString() + "[" + ((ASN1OctetString)paramDERObject).getOctets().length + "] " + System.getProperty("line.separator");
    if ((paramDERObject instanceof DERIA5String))
      return paramString + "IA5String(" + ((DERIA5String)paramDERObject).getString() + ") " + System.getProperty("line.separator");
    if ((paramDERObject instanceof DERPrintableString))
      return paramString + "PrintableString(" + ((DERPrintableString)paramDERObject).getString() + ") " + System.getProperty("line.separator");
    if ((paramDERObject instanceof DERVisibleString))
      return paramString + "VisibleString(" + ((DERVisibleString)paramDERObject).getString() + ") " + System.getProperty("line.separator");
    if ((paramDERObject instanceof DERBMPString))
      return paramString + "BMPString(" + ((DERBMPString)paramDERObject).getString() + ") " + System.getProperty("line.separator");
    if ((paramDERObject instanceof DERT61String))
      return paramString + "T61String(" + ((DERT61String)paramDERObject).getString() + ") " + System.getProperty("line.separator");
    if ((paramDERObject instanceof DERUTCTime))
      return paramString + "UTCTime(" + ((DERUTCTime)paramDERObject).getTime() + ") " + System.getProperty("line.separator");
    if ((paramDERObject instanceof DERUnknownTag))
      return paramString + "Unknown " + Integer.toString(((DERUnknownTag)paramDERObject).getTag(), 16) + " " + new String(Hex.encode(((DERUnknownTag)paramDERObject).getData())) + System.getProperty("line.separator");
    return (String)(String)(paramString + paramDERObject.toString() + System.getProperty("line.separator"));
  }

  public static String dumpAsString(Object paramObject)
  {
    if ((paramObject instanceof DERObject))
      return _dumpAsString("", (DERObject)paramObject);
    if ((paramObject instanceof DEREncodable))
      return _dumpAsString("", ((DEREncodable)paramObject).getDERObject());
    return "unknown object type " + paramObject.toString();
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.ASN1Dump
 * JD-Core Version:    0.6.0
 */