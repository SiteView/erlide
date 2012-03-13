package com.maverick.crypto.asn1;

import java.util.Vector;

public class DEREncodableVector
{
  private Vector A = new Vector();

  public void add(DEREncodable paramDEREncodable)
  {
    this.A.addElement(paramDEREncodable);
  }

  public DEREncodable get(int paramInt)
  {
    return (DEREncodable)this.A.elementAt(paramInt);
  }

  public int size()
  {
    return this.A.size();
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.DEREncodableVector
 * JD-Core Version:    0.6.0
 */