package com.maverick.crypto.asn1;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public abstract class ASN1Set extends DERObject
{
  protected Vector set = new Vector();

  public static ASN1Set getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof ASN1Set)))
      return (ASN1Set)paramObject;
    throw new IllegalArgumentException("unknown object in getInstance");
  }

  public static ASN1Set getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      if (!paramASN1TaggedObject.isExplicit())
        throw new IllegalArgumentException("object implicit - explicit expected.");
      return (ASN1Set)paramASN1TaggedObject.getObject();
    }
    if (paramASN1TaggedObject.isExplicit())
    {
      localObject = new DERSet(paramASN1TaggedObject.getObject());
      return localObject;
    }
    if ((paramASN1TaggedObject.getObject() instanceof ASN1Set))
      return (ASN1Set)paramASN1TaggedObject.getObject();
    Object localObject = new ASN1EncodableVector();
    if ((paramASN1TaggedObject.getObject() instanceof ASN1Sequence))
    {
      ASN1Sequence localASN1Sequence = (ASN1Sequence)paramASN1TaggedObject.getObject();
      Enumeration localEnumeration = localASN1Sequence.getObjects();
      while (localEnumeration.hasMoreElements())
        ((ASN1EncodableVector)localObject).add((DEREncodable)localEnumeration.nextElement());
      return new DERSet((DEREncodableVector)localObject);
    }
    throw new IllegalArgumentException("unknown object in getInstanceFromTagged");
  }

  public Enumeration getObjects()
  {
    return this.set.elements();
  }

  public DEREncodable getObjectAt(int paramInt)
  {
    return (DEREncodable)this.set.elementAt(paramInt);
  }

  public int size()
  {
    return this.set.size();
  }

  public int hashCode()
  {
    Enumeration localEnumeration = getObjects();
    int i = 0;
    while (localEnumeration.hasMoreElements())
      i ^= localEnumeration.nextElement().hashCode();
    return i;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof ASN1Set)))
      return false;
    ASN1Set localASN1Set = (ASN1Set)paramObject;
    if (size() != localASN1Set.size())
      return false;
    Enumeration localEnumeration1 = getObjects();
    Enumeration localEnumeration2 = localASN1Set.getObjects();
    while (localEnumeration1.hasMoreElements())
      if (!localEnumeration1.nextElement().equals(localEnumeration2.nextElement()))
        return false;
    return true;
  }

  protected void addObject(DEREncodable paramDEREncodable)
  {
    this.set.addElement(paramDEREncodable);
  }

  abstract void encode(DEROutputStream paramDEROutputStream)
    throws IOException;
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.ASN1Set
 * JD-Core Version:    0.6.0
 */