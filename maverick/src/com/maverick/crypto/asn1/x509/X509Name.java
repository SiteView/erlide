package com.maverick.crypto.asn1.x509;

import com.maverick.crypto.asn1.ASN1EncodableVector;
import com.maverick.crypto.asn1.ASN1InputStream;
import com.maverick.crypto.asn1.ASN1Sequence;
import com.maverick.crypto.asn1.ASN1Set;
import com.maverick.crypto.asn1.ASN1TaggedObject;
import com.maverick.crypto.asn1.DEREncodable;
import com.maverick.crypto.asn1.DERIA5String;
import com.maverick.crypto.asn1.DERObject;
import com.maverick.crypto.asn1.DERObjectIdentifier;
import com.maverick.crypto.asn1.DERPrintableString;
import com.maverick.crypto.asn1.DERSequence;
import com.maverick.crypto.asn1.DERSet;
import com.maverick.crypto.asn1.DERString;
import com.maverick.crypto.asn1.DERUTF8String;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class X509Name
  implements DEREncodable
{
  public static final DERObjectIdentifier C = new DERObjectIdentifier("2.5.4.6");
  public static final DERObjectIdentifier O = new DERObjectIdentifier("2.5.4.10");
  public static final DERObjectIdentifier OU = new DERObjectIdentifier("2.5.4.11");
  public static final DERObjectIdentifier T = new DERObjectIdentifier("2.5.4.12");
  public static final DERObjectIdentifier CN = new DERObjectIdentifier("2.5.4.3");
  public static final DERObjectIdentifier SN = new DERObjectIdentifier("2.5.4.5");
  public static final DERObjectIdentifier L = new DERObjectIdentifier("2.5.4.7");
  public static final DERObjectIdentifier ST = new DERObjectIdentifier("2.5.4.8");
  public static final DERObjectIdentifier SURNAME = new DERObjectIdentifier("2.5.4.4");
  public static final DERObjectIdentifier GIVENNAME = new DERObjectIdentifier("2.5.4.42");
  public static final DERObjectIdentifier INITIALS = new DERObjectIdentifier("2.5.4.43");
  public static final DERObjectIdentifier GENERATION = new DERObjectIdentifier("2.5.4.44");
  public static final DERObjectIdentifier UNIQUE_IDENTIFIER = new DERObjectIdentifier("2.5.4.45");
  public static final DERObjectIdentifier EmailAddress = new DERObjectIdentifier("1.2.840.113549.1.9.1");
  public static final DERObjectIdentifier E = EmailAddress;
  public static final DERObjectIdentifier DC = new DERObjectIdentifier("0.9.2342.19200300.100.1.25");
  public static final DERObjectIdentifier UID = new DERObjectIdentifier("0.9.2342.19200300.100.1.1");
  public static Hashtable OIDLookUp = new Hashtable();
  public static boolean DefaultReverse = false;
  public static Hashtable DefaultSymbols = OIDLookUp;
  public static Hashtable RFC2253Symbols = new Hashtable();
  public static Hashtable SymbolLookUp = new Hashtable();
  public static Hashtable DefaultLookUp = SymbolLookUp;
  private Vector x = new Vector();
  private Vector u = new Vector();
  private Vector v = new Vector();
  private ASN1Sequence w;

  public static X509Name getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public static X509Name getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof X509Name)))
      return (X509Name)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new X509Name((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory");
  }

  public X509Name(ASN1Sequence paramASN1Sequence)
  {
    this.w = paramASN1Sequence;
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    while (localEnumeration.hasMoreElements())
    {
      ASN1Set localASN1Set = (ASN1Set)localEnumeration.nextElement();
      for (int i = 0; i < localASN1Set.size(); i++)
      {
        ASN1Sequence localASN1Sequence = (ASN1Sequence)localASN1Set.getObjectAt(i);
        this.x.addElement(localASN1Sequence.getObjectAt(0));
        this.u.addElement(((DERString)localASN1Sequence.getObjectAt(1)).getString());
        this.v.addElement(i != 0 ? new Boolean(true) : new Boolean(false));
      }
    }
  }

  public X509Name(Hashtable paramHashtable)
  {
    this(null, paramHashtable);
  }

  public X509Name(Vector paramVector, Hashtable paramHashtable)
  {
    if (paramVector != null)
    {
      for (int i = 0; i != paramVector.size(); i++)
      {
        this.x.addElement(paramVector.elementAt(i));
        this.v.addElement(new Boolean(false));
      }
    }
    else
    {
      Enumeration localEnumeration = paramHashtable.keys();
      while (localEnumeration.hasMoreElements())
      {
        this.x.addElement(localEnumeration.nextElement());
        this.v.addElement(new Boolean(false));
      }
    }
    for (int j = 0; j != this.x.size(); j++)
    {
      DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)this.x.elementAt(j);
      if (paramHashtable.get(localDERObjectIdentifier) == null)
        throw new IllegalArgumentException("No attribute for object id - " + localDERObjectIdentifier.getId() + " - passed to distinguished name");
      this.u.addElement(paramHashtable.get(localDERObjectIdentifier));
    }
  }

  public X509Name(Vector paramVector1, Vector paramVector2)
  {
    if (paramVector1.size() != paramVector2.size())
      throw new IllegalArgumentException("oids vector must be same length as values.");
    for (int i = 0; i < paramVector1.size(); i++)
    {
      this.x.addElement(paramVector1.elementAt(i));
      this.u.addElement(paramVector2.elementAt(i));
      this.v.addElement(new Boolean(false));
    }
  }

  public X509Name(String paramString)
  {
    this(DefaultReverse, DefaultLookUp, paramString);
  }

  public X509Name(boolean paramBoolean, String paramString)
  {
    this(paramBoolean, DefaultLookUp, paramString);
  }

  public X509Name(boolean paramBoolean, Hashtable paramHashtable, String paramString)
  {
    X509NameTokenizer localX509NameTokenizer = new X509NameTokenizer(paramString);
    Object localObject;
    while (localX509NameTokenizer.hasMoreTokens())
    {
      localObject = localX509NameTokenizer.nextToken();
      int i = ((String)localObject).indexOf('=');
      if (i == -1)
        throw new IllegalArgumentException("badly formated directory string");
      String str1 = ((String)localObject).substring(0, i);
      String str2 = ((String)localObject).substring(i + 1);
      DERObjectIdentifier localDERObjectIdentifier = null;
      if (str1.toUpperCase().startsWith("OID."))
      {
        localDERObjectIdentifier = new DERObjectIdentifier(str1.substring(4));
      }
      else if ((str1.charAt(0) >= '0') && (str1.charAt(0) <= '9'))
      {
        localDERObjectIdentifier = new DERObjectIdentifier(str1);
      }
      else
      {
        localDERObjectIdentifier = (DERObjectIdentifier)paramHashtable.get(str1.toLowerCase());
        if (localDERObjectIdentifier == null)
          throw new IllegalArgumentException("Unknown object id - " + str1 + " - passed to distinguished name");
      }
      this.x.addElement(localDERObjectIdentifier);
      this.u.addElement(str2);
      this.v.addElement(new Boolean(false));
    }
    if (paramBoolean)
    {
      localObject = new Vector();
      Vector localVector = new Vector();
      for (int j = this.x.size() - 1; j >= 0; j--)
      {
        ((Vector)localObject).addElement(this.x.elementAt(j));
        localVector.addElement(this.u.elementAt(j));
        this.v.addElement(new Boolean(false));
      }
      this.x = ((Vector)localObject);
      this.u = localVector;
    }
  }

  public Vector getOIDs()
  {
    Vector localVector = new Vector();
    for (int i = 0; i != this.x.size(); i++)
      localVector.addElement(this.x.elementAt(i));
    return localVector;
  }

  public Vector getValues()
  {
    Vector localVector = new Vector();
    for (int i = 0; i != this.u.size(); i++)
      localVector.addElement(this.u.elementAt(i));
    return localVector;
  }

  private boolean b(String paramString)
  {
    for (int i = paramString.length() - 1; i >= 0; i--)
      if (paramString.charAt(i) > '')
        return false;
    return true;
  }

  public DERObject getDERObject()
  {
    if (this.w == null)
    {
      ASN1EncodableVector localASN1EncodableVector1 = new ASN1EncodableVector();
      for (int i = 0; i != this.x.size(); i++)
      {
        ASN1EncodableVector localASN1EncodableVector2 = new ASN1EncodableVector();
        DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)this.x.elementAt(i);
        localASN1EncodableVector2.add(localDERObjectIdentifier);
        String str = (String)this.u.elementAt(i);
        if (str.charAt(0) == '#')
        {
          str = str.toLowerCase();
          byte[] arrayOfByte = new byte[str.length() / 2];
          for (int j = 0; j != arrayOfByte.length; j++)
          {
            int k = str.charAt(j * 2 + 1);
            int m = str.charAt(j * 2 + 2);
            if (k < 97)
              arrayOfByte[j] = (byte)(k - 48 << 4);
            else
              arrayOfByte[j] = (byte)(k - 97 + 10 << 4);
            if (m < 97)
            {
              int tmp184_182 = j;
              byte[] tmp184_180 = arrayOfByte;
              tmp184_180[tmp184_182] = (byte)(tmp184_180[tmp184_182] | (byte)(m - 48));
            }
            else
            {
              int tmp202_200 = j;
              byte[] tmp202_198 = arrayOfByte;
              tmp202_198[tmp202_200] = (byte)(tmp202_198[tmp202_200] | (byte)(m - 97 + 10));
            }
          }
          ASN1InputStream localASN1InputStream = new ASN1InputStream(new ByteArrayInputStream(arrayOfByte));
          try
          {
            localASN1EncodableVector2.add(localASN1InputStream.readObject());
          }
          catch (IOException localIOException)
          {
            throw new RuntimeException("bad object in '#' string");
          }
        }
        else if (localDERObjectIdentifier.equals(EmailAddress))
        {
          localASN1EncodableVector2.add(new DERIA5String(str));
        }
        else if (b(str))
        {
          localASN1EncodableVector2.add(new DERPrintableString(str));
        }
        else
        {
          localASN1EncodableVector2.add(new DERUTF8String(str));
        }
        localASN1EncodableVector1.add(new DERSet(new DERSequence(localASN1EncodableVector2)));
      }
      this.w = new DERSequence(localASN1EncodableVector1);
    }
    return this.w;
  }

  public boolean equals(Object paramObject, boolean paramBoolean)
  {
    if (paramObject == this)
      return true;
    if (!paramBoolean)
      return equals(paramObject);
    if ((paramObject == null) || (!(paramObject instanceof X509Name)))
      return false;
    X509Name localX509Name = (X509Name)paramObject;
    int i = this.x.size();
    if (i != localX509Name.x.size())
      return false;
    for (int j = 0; j < i; j++)
    {
      String str1 = ((DERObjectIdentifier)this.x.elementAt(j)).getId();
      String str2 = (String)this.u.elementAt(j);
      String str3 = ((DERObjectIdentifier)localX509Name.x.elementAt(j)).getId();
      String str4 = (String)localX509Name.u.elementAt(j);
      if (!str1.equals(str3))
        continue;
      str2 = str2.trim().toLowerCase();
      str4 = str4.trim().toLowerCase();
      if (str2.equals(str4))
        continue;
      StringBuffer localStringBuffer1 = new StringBuffer();
      StringBuffer localStringBuffer2 = new StringBuffer();
      char c1;
      int k;
      char c2;
      if (str2.length() != 0)
      {
        c1 = str2.charAt(0);
        localStringBuffer1.append(c1);
        for (k = 1; k < str2.length(); k++)
        {
          c2 = str2.charAt(k);
          if ((c1 != ' ') || (c2 != ' '))
            localStringBuffer1.append(c2);
          c1 = c2;
        }
      }
      if (str4.length() != 0)
      {
        c1 = str4.charAt(0);
        localStringBuffer2.append(c1);
        for (k = 1; k < str4.length(); k++)
        {
          c2 = str4.charAt(k);
          if ((c1 != ' ') || (c2 != ' '))
            localStringBuffer2.append(c2);
          c1 = c2;
        }
      }
      if (!localStringBuffer1.toString().equals(localStringBuffer2.toString()))
        return false;
    }
    return true;
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject == this)
      return true;
    if ((paramObject == null) || (!(paramObject instanceof X509Name)))
      return false;
    X509Name localX509Name = (X509Name)paramObject;
    if (getDERObject().equals(localX509Name.getDERObject()))
      return true;
    int i = this.x.size();
    if (i != localX509Name.x.size())
      return false;
    boolean[] arrayOfBoolean = new boolean[i];
    for (int j = 0; j < i; j++)
    {
      int k = 0;
      String str1 = ((DERObjectIdentifier)this.x.elementAt(j)).getId();
      String str2 = (String)this.u.elementAt(j);
      for (int m = 0; m < i; m++)
      {
        if (arrayOfBoolean[m] == 1)
          continue;
        String str3 = ((DERObjectIdentifier)localX509Name.x.elementAt(m)).getId();
        String str4 = (String)localX509Name.u.elementAt(m);
        if (!str1.equals(str3))
          continue;
        str2 = str2.trim().toLowerCase();
        str4 = str4.trim().toLowerCase();
        if (str2.equals(str4))
        {
          arrayOfBoolean[m] = true;
          k = 1;
          break;
        }
        StringBuffer localStringBuffer1 = new StringBuffer();
        StringBuffer localStringBuffer2 = new StringBuffer();
        char c1;
        int n;
        char c2;
        if (str2.length() != 0)
        {
          c1 = str2.charAt(0);
          localStringBuffer1.append(c1);
          for (n = 1; n < str2.length(); n++)
          {
            c2 = str2.charAt(n);
            if ((c1 != ' ') || (c2 != ' '))
              localStringBuffer1.append(c2);
            c1 = c2;
          }
        }
        if (str4.length() != 0)
        {
          c1 = str4.charAt(0);
          localStringBuffer2.append(c1);
          for (n = 1; n < str4.length(); n++)
          {
            c2 = str4.charAt(n);
            if ((c1 != ' ') || (c2 != ' '))
              localStringBuffer2.append(c2);
            c1 = c2;
          }
        }
        if (!localStringBuffer1.toString().equals(localStringBuffer2.toString()))
          continue;
        arrayOfBoolean[m] = true;
        k = 1;
        break;
      }
      if (k == 0)
        return false;
    }
    return true;
  }

  public int hashCode()
  {
    ASN1Sequence localASN1Sequence = (ASN1Sequence)getDERObject();
    Enumeration localEnumeration = localASN1Sequence.getObjects();
    int i = 0;
    while (localEnumeration.hasMoreElements())
      i ^= localEnumeration.nextElement().hashCode();
    return i;
  }

  private void b(StringBuffer paramStringBuffer, Hashtable paramHashtable, DERObjectIdentifier paramDERObjectIdentifier, String paramString)
  {
    String str = (String)paramHashtable.get(paramDERObjectIdentifier);
    if (str != null)
      paramStringBuffer.append(str);
    else
      paramStringBuffer.append(paramDERObjectIdentifier.getId());
    paramStringBuffer.append("=");
    int i = paramStringBuffer.length();
    paramStringBuffer.append(paramString);
    int j = paramStringBuffer.length();
    while (i != j)
    {
      if ((paramStringBuffer.charAt(i) == ',') || (paramStringBuffer.charAt(i) == '"') || (paramStringBuffer.charAt(i) == '\\') || (paramStringBuffer.charAt(i) == '+') || (paramStringBuffer.charAt(i) == '<') || (paramStringBuffer.charAt(i) == '>') || (paramStringBuffer.charAt(i) == ';'))
      {
        paramStringBuffer.insert(i, "\\");
        i++;
        j++;
      }
      i++;
    }
  }

  public String toString(boolean paramBoolean, Hashtable paramHashtable)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = 1;
    int j;
    if (paramBoolean)
      for (j = this.x.size() - 1; j >= 0; j--)
      {
        if (i != 0)
          i = 0;
        else if (((Boolean)this.v.elementAt(j + 1)).booleanValue())
          localStringBuffer.append(" + ");
        else
          localStringBuffer.append(",");
        b(localStringBuffer, paramHashtable, (DERObjectIdentifier)this.x.elementAt(j), (String)this.u.elementAt(j));
      }
    else
      for (j = 0; j < this.x.size(); j++)
      {
        if (i != 0)
          i = 0;
        else if (((Boolean)this.v.elementAt(j)).booleanValue())
          localStringBuffer.append(" + ");
        else
          localStringBuffer.append(",");
        b(localStringBuffer, paramHashtable, (DERObjectIdentifier)this.x.elementAt(j), (String)this.u.elementAt(j));
      }
    return localStringBuffer.toString();
  }

  public String toString()
  {
    return toString(DefaultReverse, DefaultSymbols);
  }

  static
  {
    DefaultSymbols.put(C, "C");
    DefaultSymbols.put(O, "O");
    DefaultSymbols.put(T, "T");
    DefaultSymbols.put(OU, "OU");
    DefaultSymbols.put(CN, "CN");
    DefaultSymbols.put(L, "L");
    DefaultSymbols.put(ST, "ST");
    DefaultSymbols.put(SN, "SN");
    DefaultSymbols.put(EmailAddress, "E");
    DefaultSymbols.put(DC, "DC");
    DefaultSymbols.put(UID, "UID");
    DefaultSymbols.put(SURNAME, "SURNAME");
    DefaultSymbols.put(GIVENNAME, "GIVENNAME");
    DefaultSymbols.put(INITIALS, "INITIALS");
    DefaultSymbols.put(GENERATION, "GENERATION");
    RFC2253Symbols.put(C, "C");
    RFC2253Symbols.put(O, "O");
    RFC2253Symbols.put(T, "T");
    RFC2253Symbols.put(OU, "OU");
    RFC2253Symbols.put(CN, "CN");
    RFC2253Symbols.put(L, "L");
    RFC2253Symbols.put(ST, "ST");
    RFC2253Symbols.put(SN, "SN");
    RFC2253Symbols.put(EmailAddress, "EMAILADDRESS");
    RFC2253Symbols.put(DC, "DC");
    RFC2253Symbols.put(UID, "UID");
    RFC2253Symbols.put(SURNAME, "SURNAME");
    RFC2253Symbols.put(GIVENNAME, "GIVENNAME");
    RFC2253Symbols.put(INITIALS, "INITIALS");
    RFC2253Symbols.put(GENERATION, "GENERATION");
    DefaultLookUp.put("c", C);
    DefaultLookUp.put("o", O);
    DefaultLookUp.put("t", T);
    DefaultLookUp.put("ou", OU);
    DefaultLookUp.put("cn", CN);
    DefaultLookUp.put("l", L);
    DefaultLookUp.put("st", ST);
    DefaultLookUp.put("sn", SN);
    DefaultLookUp.put("emailaddress", E);
    DefaultLookUp.put("dc", DC);
    DefaultLookUp.put("e", E);
    DefaultLookUp.put("uid", UID);
    DefaultLookUp.put("surname", SURNAME);
    DefaultLookUp.put("givenname", GIVENNAME);
    DefaultLookUp.put("initials", INITIALS);
    DefaultLookUp.put("generation", GENERATION);
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.crypto.asn1.x509.X509Name
 * JD-Core Version:    0.6.0
 */