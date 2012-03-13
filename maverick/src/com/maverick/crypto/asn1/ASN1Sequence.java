package com.maverick.crypto.asn1;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public abstract class ASN1Sequence extends DERObject
{
  private Vector ac = new Vector();

  public static ASN1Sequence getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof ASN1Sequence)))
      return (ASN1Sequence)paramObject;
    throw new IllegalArgumentException("unknown object in getInstance");
  }

  public static ASN1Sequence getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      if (!paramASN1TaggedObject.isExplicit())
        throw new IllegalArgumentException("object implicit - explicit expected.");
      return (ASN1Sequence)paramASN1TaggedObject.getObject();
    }
    if (paramASN1TaggedObject.isExplicit())
    {
      if ((paramASN1TaggedObject instanceof BERTaggedObject))
        return new BERSequence(paramASN1TaggedObject.getObject());
      return new DERSequence(paramASN1TaggedObject.getObject());
    }
    if ((paramASN1TaggedObject.getObject() instanceof ASN1Sequence))
      return (ASN1Sequence)paramASN1TaggedObject.getObject();
    throw new IllegalArgumentException("unknown object in getInstanceFromTagged");
  }

  public Enumeration getObjects()
  {
    return this.ac.elements();
  }

  public DEREncodable getObjectAt(int paramInt)
  {
    return (DEREncodable)this.ac.elementAt(paramInt);
  }

  public int size()
  {
    return this.ac.size();
  }

  public int hashCode()
  {
    Enumeration localEnumeration = getObjects();
    int i = 0;
    while (localEnumeration.hasMoreElements())
    {
      Object localObject = localEnumeration.nextElement();
      if (localObject != null)
        i ^= localObject.hashCode();
    }
    return i;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof ASN1Sequence)))
      return false;
    ASN1Sequence localASN1Sequence = (ASN1Sequence)paramObject;
    if (size() != localASN1Sequence.size())
      return false;
    Enumeration localEnumeration1 = getObjects();
    Enumeration localEnumeration2 = localASN1Sequence.getObjects();
    while (localEnumeration1.hasMoreElements())
    {
      Object localObject1 = localEnumeration1.nextElement();
      Object localObject2 = localEnumeration2.nextElement();
      if ((localObject1 != null) && (localObject2 != null))
      {
        if (!localObject1.equals(localObject2))
          return false;
      }
      else if ((localObject1 != null) || (localObject2 != null))
        return false;
    }
    return true;
  }

  protected void addObject(DEREncodable paramDEREncodable)
  {
    this.ac.addElement(paramDEREncodable);
  }

  abstract void encode(DEROutputStream paramDEROutputStream)
    throws IOException;
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.ASN1Sequence
 * JD-Core Version:    0.6.0
 */