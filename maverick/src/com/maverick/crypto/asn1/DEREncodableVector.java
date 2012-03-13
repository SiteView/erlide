package com.maverick.crypto.asn1;

import java.util.Vector;

public class DEREncodableVector
{
  private Vector b = new Vector();

  public void add(DEREncodable paramDEREncodable)
  {
    this.b.addElement(paramDEREncodable);
  }

  public DEREncodable get(int paramInt)
  {
    return (DEREncodable)this.b.elementAt(paramInt);
  }

  public int size()
  {
    return this.b.size();
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.DEREncodableVector
 * JD-Core Version:    0.6.0
 */