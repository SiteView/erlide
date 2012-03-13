package com.maverick.crypto.asn1.x509;

import com.maverick.crypto.asn1.ASN1Dump;
import com.maverick.crypto.asn1.ASN1OctetString;
import com.maverick.crypto.asn1.ASN1Sequence;
import com.maverick.crypto.asn1.DERBitString;
import com.maverick.crypto.asn1.DERBoolean;
import com.maverick.crypto.asn1.DEREncodable;
import com.maverick.crypto.asn1.DERIA5String;
import com.maverick.crypto.asn1.DERInputStream;
import com.maverick.crypto.asn1.DERInteger;
import com.maverick.crypto.asn1.DERObjectIdentifier;
import com.maverick.crypto.asn1.DEROutputStream;
import com.maverick.crypto.asn1.misc.MiscObjectIdentifiers;
import com.maverick.crypto.asn1.misc.NetscapeCertType;
import com.maverick.crypto.asn1.misc.NetscapeRevocationURL;
import com.maverick.crypto.asn1.misc.VerisignCzagExtension;
import com.maverick.crypto.asn1.pkcs.PKCSObjectIdentifiers;
import com.maverick.crypto.encoders.Hex;
import com.maverick.crypto.publickey.PublicKey;
import com.maverick.crypto.publickey.RsaPublicKey;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class X509Certificate
{
  private X509CertificateStructure C;
  private Hashtable B = new Hashtable();
  private Vector A = new Vector();

  public X509Certificate(X509CertificateStructure paramX509CertificateStructure)
  {
    this.C = paramX509CertificateStructure;
  }

  public void checkValidity()
    throws CertificateException
  {
    checkValidity(new Date());
  }

  public void checkValidity(Date paramDate)
    throws CertificateException
  {
    if (paramDate.after(getNotAfter()))
      throw new CertificateException(1, "Certificate expired on " + this.C.getEndDate().getTime());
    if (paramDate.before(getNotBefore()))
      throw new CertificateException(2, "certificate not valid till " + this.C.getStartDate().getTime());
  }

  public int getVersion()
  {
    return this.C.getVersion();
  }

  public BigInteger getSerialNumber()
  {
    return this.C.getSerialNumber().getValue();
  }

  public X509Name getIssuerDN()
  {
    return this.C.getIssuer();
  }

  public X509Name getSubjectDN()
  {
    return this.C.getSubject();
  }

  public Date getNotBefore()
  {
    return this.C.getStartDate().getDate();
  }

  public Date getNotAfter()
  {
    return this.C.getEndDate().getDate();
  }

  public byte[] getTBSCertificate()
    throws CertificateException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    DEROutputStream localDEROutputStream = new DEROutputStream(localByteArrayOutputStream);
    try
    {
      localDEROutputStream.writeObject(this.C.getTBSCertificate());
      return localByteArrayOutputStream.toByteArray();
    }
    catch (IOException localIOException)
    {
    }
    throw new CertificateException(3, localIOException.toString());
  }

  public byte[] getSignature()
  {
    return this.C.getSignature().getBytes();
  }

  public String getSigAlgOID()
  {
    return this.C.getSignatureAlgorithm().getObjectId().getId();
  }

  public String getSigAlgName()
    throws CertificateException
  {
    if (getSigAlgOID().equals("1.2.840.113549.1.1.4"))
      return "MD5WithRSAEncryption";
    if (getSigAlgOID().equals("1.2.840.113549.1.1.5"))
      return "SHA1WithRSAEncryption";
    throw new CertificateException(5, "Unsupported signature algorithm id " + getSigAlgOID());
  }

  public byte[] getSigAlgParams()
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    if (this.C.getSignatureAlgorithm().getParameters() != null)
    {
      try
      {
        DEROutputStream localDEROutputStream = new DEROutputStream(localByteArrayOutputStream);
        localDEROutputStream.writeObject(this.C.getSignatureAlgorithm().getParameters());
      }
      catch (Exception localException)
      {
        throw new RuntimeException("exception getting sig parameters " + localException);
      }
      return localByteArrayOutputStream.toByteArray();
    }
    return null;
  }

  public boolean[] getIssuerUniqueID()
  {
    DERBitString localDERBitString = this.C.getTBSCertificate().getIssuerUniqueId();
    if (localDERBitString != null)
    {
      byte[] arrayOfByte = localDERBitString.getBytes();
      boolean[] arrayOfBoolean = new boolean[arrayOfByte.length * 8 - localDERBitString.getPadBits()];
      for (int i = 0; i != arrayOfBoolean.length; i++)
        arrayOfBoolean[i] = ((arrayOfByte[(i / 8)] & 128 >>> i % 8) != 0 ? 1 : false);
      return arrayOfBoolean;
    }
    return null;
  }

  public boolean[] getSubjectUniqueID()
  {
    DERBitString localDERBitString = this.C.getTBSCertificate().getSubjectUniqueId();
    if (localDERBitString != null)
    {
      byte[] arrayOfByte = localDERBitString.getBytes();
      boolean[] arrayOfBoolean = new boolean[arrayOfByte.length * 8 - localDERBitString.getPadBits()];
      for (int i = 0; i != arrayOfBoolean.length; i++)
        arrayOfBoolean[i] = ((arrayOfByte[(i / 8)] & 128 >>> i % 8) != 0 ? 1 : false);
      return arrayOfBoolean;
    }
    return null;
  }

  public boolean[] getKeyUsage()
  {
    byte[] arrayOfByte = A("2.5.29.15");
    int i = 0;
    if (arrayOfByte != null)
    {
      try
      {
        DERInputStream localDERInputStream = new DERInputStream(new ByteArrayInputStream(arrayOfByte));
        DERBitString localDERBitString = (DERBitString)localDERInputStream.readObject();
        arrayOfByte = localDERBitString.getBytes();
        i = arrayOfByte.length * 8 - localDERBitString.getPadBits();
      }
      catch (Exception localException)
      {
        throw new RuntimeException("error processing key usage extension");
      }
      boolean[] arrayOfBoolean = new boolean[i < 9 ? 9 : i];
      for (int j = 0; j != i; j++)
        arrayOfBoolean[j] = ((arrayOfByte[(j / 8)] & 128 >>> j % 8) != 0 ? 1 : false);
      return arrayOfBoolean;
    }
    return null;
  }

  public int getBasicConstraints()
  {
    byte[] arrayOfByte = A("2.5.29.19");
    if (arrayOfByte != null)
      try
      {
        DERInputStream localDERInputStream = new DERInputStream(new ByteArrayInputStream(arrayOfByte));
        ASN1Sequence localASN1Sequence = (ASN1Sequence)localDERInputStream.readObject();
        if (localASN1Sequence.size() == 2)
        {
          if (((DERBoolean)localASN1Sequence.getObjectAt(0)).isTrue())
            return ((DERInteger)localASN1Sequence.getObjectAt(1)).getValue().intValue();
          return -1;
        }
        if (localASN1Sequence.size() == 1)
        {
          if ((localASN1Sequence.getObjectAt(0) instanceof DERBoolean))
          {
            if (((DERBoolean)localASN1Sequence.getObjectAt(0)).isTrue())
              return 2147483647;
            return -1;
          }
          return -1;
        }
      }
      catch (Exception localException)
      {
        throw new RuntimeException("error processing key usage extension");
      }
    return -1;
  }

  public X509Extension[] getCriticalExtensionOIDs()
  {
    if (getVersion() == 3)
    {
      Vector localVector = new Vector();
      X509Extensions localX509Extensions = this.C.getTBSCertificate().getExtensions();
      if (localX509Extensions != null)
      {
        Enumeration localEnumeration = localX509Extensions.oids();
        while (localEnumeration.hasMoreElements())
        {
          localObject = (DERObjectIdentifier)localEnumeration.nextElement();
          X509Extension localX509Extension = localX509Extensions.getExtension((DERObjectIdentifier)localObject);
          if (localX509Extension.isCritical())
            localVector.addElement(((DERObjectIdentifier)localObject).getId());
        }
        Object localObject = new X509Extension[localVector.size()];
        localVector.copyInto(localObject);
        return localObject;
      }
    }
    return (X509Extension)null;
  }

  private byte[] A(String paramString)
  {
    X509Extensions localX509Extensions = this.C.getTBSCertificate().getExtensions();
    if (localX509Extensions != null)
    {
      X509Extension localX509Extension = localX509Extensions.getExtension(new DERObjectIdentifier(paramString));
      if (localX509Extension != null)
        return localX509Extension.getValue().getOctets();
    }
    return null;
  }

  public byte[] getExtensionValue(String paramString)
  {
    X509Extensions localX509Extensions = this.C.getTBSCertificate().getExtensions();
    if (localX509Extensions != null)
    {
      X509Extension localX509Extension = localX509Extensions.getExtension(new DERObjectIdentifier(paramString));
      if (localX509Extension != null)
      {
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        DEROutputStream localDEROutputStream = new DEROutputStream(localByteArrayOutputStream);
        try
        {
          localDEROutputStream.writeObject(localX509Extension.getValue());
          return localByteArrayOutputStream.toByteArray();
        }
        catch (Exception localException)
        {
          throw new RuntimeException("error encoding " + localException.toString());
        }
      }
    }
    return null;
  }

  public X509Extension[] getNonCriticalExtensionOIDs()
  {
    if (getVersion() == 3)
    {
      Vector localVector = new Vector();
      X509Extensions localX509Extensions = this.C.getTBSCertificate().getExtensions();
      if (localX509Extensions != null)
      {
        Enumeration localEnumeration = localX509Extensions.oids();
        while (localEnumeration.hasMoreElements())
        {
          localObject = (DERObjectIdentifier)localEnumeration.nextElement();
          X509Extension localX509Extension = localX509Extensions.getExtension((DERObjectIdentifier)localObject);
          if (!localX509Extension.isCritical())
            localVector.addElement(((DERObjectIdentifier)localObject).getId());
        }
        Object localObject = new X509Extension[localVector.size()];
        localVector.copyInto(localObject);
        return localObject;
      }
    }
    return (X509Extension)null;
  }

  public boolean hasUnsupportedCriticalExtension()
  {
    if (getVersion() == 3)
    {
      X509Extensions localX509Extensions = this.C.getTBSCertificate().getExtensions();
      if (localX509Extensions != null)
      {
        Enumeration localEnumeration = localX509Extensions.oids();
        while (localEnumeration.hasMoreElements())
        {
          DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)localEnumeration.nextElement();
          if ((localDERObjectIdentifier.getId().equals("2.5.29.15")) || (localDERObjectIdentifier.getId().equals("2.5.29.19")))
            continue;
          X509Extension localX509Extension = localX509Extensions.getExtension(localDERObjectIdentifier);
          if (localX509Extension.isCritical())
            return true;
        }
      }
    }
    return false;
  }

  public PublicKey getPublicKey()
    throws CertificateException
  {
    try
    {
      AlgorithmIdentifier localAlgorithmIdentifier = this.C.getSubjectPublicKeyInfo().getAlgorithmId();
      if ((localAlgorithmIdentifier.getObjectId().equals(PKCSObjectIdentifiers.rsaEncryption)) || (localAlgorithmIdentifier.getObjectId().equals(X509ObjectIdentifiers.id_ea_rsa)))
      {
        RSAPublicKeyStructure localRSAPublicKeyStructure = RSAPublicKeyStructure.getInstance(this.C.getSubjectPublicKeyInfo().getPublicKey());
        return new RsaPublicKey(localRSAPublicKeyStructure.getModulus(), localRSAPublicKeyStructure.getPublicExponent());
      }
      throw new CertificateException(5, "Public key algorithm id " + localAlgorithmIdentifier.getObjectId().getId() + " is not supported");
    }
    catch (IOException localIOException)
    {
    }
    throw new CertificateException(4, localIOException.getMessage());
  }

  public byte[] getEncoded()
    throws CertificateException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    DEROutputStream localDEROutputStream = new DEROutputStream(localByteArrayOutputStream);
    try
    {
      localDEROutputStream.writeObject(this.C);
      return localByteArrayOutputStream.toByteArray();
    }
    catch (IOException localIOException)
    {
    }
    throw new CertificateException(3, localIOException.toString());
  }

  public void setBagAttribute(DERObjectIdentifier paramDERObjectIdentifier, DEREncodable paramDEREncodable)
  {
    this.B.put(paramDERObjectIdentifier, paramDEREncodable);
    this.A.addElement(paramDERObjectIdentifier);
  }

  public DEREncodable getBagAttribute(DERObjectIdentifier paramDERObjectIdentifier)
  {
    return (DEREncodable)this.B.get(paramDERObjectIdentifier);
  }

  public Enumeration getBagAttributeKeys()
  {
    return this.A.elements();
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String str = System.getProperty("line.separator");
    localStringBuffer.append("  [0]         Version: " + getVersion() + str);
    localStringBuffer.append("         SerialNumber: " + getSerialNumber() + str);
    localStringBuffer.append("             IssuerDN: " + getIssuerDN() + str);
    localStringBuffer.append("           Start Date: " + getNotBefore() + str);
    localStringBuffer.append("           Final Date: " + getNotAfter() + str);
    localStringBuffer.append("            SubjectDN: " + getSubjectDN() + str);
    try
    {
      localStringBuffer.append("           Public Key: " + getPublicKey() + str);
    }
    catch (CertificateException localCertificateException1)
    {
      localStringBuffer.append("           Public Key:  " + localCertificateException1.getMessage());
    }
    try
    {
      localStringBuffer.append("  Signature Algorithm: " + getSigAlgName() + str);
    }
    catch (CertificateException localCertificateException2)
    {
      localStringBuffer.append("  Signature Algorithm: " + localCertificateException2.getMessage());
    }
    byte[] arrayOfByte1 = getSignature();
    localStringBuffer.append("            Signature: " + new String(Hex.encode(arrayOfByte1, 0, 20)) + str);
    for (int i = 20; i < arrayOfByte1.length; i += 20)
      if (i < arrayOfByte1.length - 20)
        localStringBuffer.append("                       " + new String(Hex.encode(arrayOfByte1, i, 20)) + str);
      else
        localStringBuffer.append("                       " + new String(Hex.encode(arrayOfByte1, i, arrayOfByte1.length - i)) + str);
    X509Extensions localX509Extensions = this.C.getTBSCertificate().getExtensions();
    if (localX509Extensions != null)
    {
      Enumeration localEnumeration = localX509Extensions.oids();
      if (localEnumeration.hasMoreElements())
        localStringBuffer.append("       Extensions: \n");
      while (localEnumeration.hasMoreElements())
      {
        DERObjectIdentifier localDERObjectIdentifier = (DERObjectIdentifier)localEnumeration.nextElement();
        X509Extension localX509Extension = localX509Extensions.getExtension(localDERObjectIdentifier);
        if (localX509Extension.getValue() != null)
        {
          byte[] arrayOfByte2 = localX509Extension.getValue().getOctets();
          ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte2);
          DERInputStream localDERInputStream = new DERInputStream(localByteArrayInputStream);
          localStringBuffer.append("                       critical(" + localX509Extension.isCritical() + ") ");
          try
          {
            if (localDERObjectIdentifier.equals(X509Extensions.BasicConstraints))
            {
              localStringBuffer.append(new BasicConstraints((ASN1Sequence)localDERInputStream.readObject()) + str);
            }
            else if (localDERObjectIdentifier.equals(X509Extensions.KeyUsage))
            {
              localStringBuffer.append(new KeyUsage((DERBitString)localDERInputStream.readObject()) + str);
            }
            else if (localDERObjectIdentifier.equals(MiscObjectIdentifiers.netscapeCertType))
            {
              localStringBuffer.append(new NetscapeCertType((DERBitString)localDERInputStream.readObject()) + str);
            }
            else if (localDERObjectIdentifier.equals(MiscObjectIdentifiers.netscapeRevocationURL))
            {
              localStringBuffer.append(new NetscapeRevocationURL((DERIA5String)localDERInputStream.readObject()) + str);
            }
            else if (localDERObjectIdentifier.equals(MiscObjectIdentifiers.verisignCzagExtension))
            {
              localStringBuffer.append(new VerisignCzagExtension((DERIA5String)localDERInputStream.readObject()) + str);
            }
            else
            {
              localStringBuffer.append(localDERObjectIdentifier.getId());
              localStringBuffer.append(" value = " + ASN1Dump.dumpAsString(localDERInputStream.readObject()) + str);
            }
          }
          catch (Exception localException)
          {
            localStringBuffer.append(localDERObjectIdentifier.getId());
            localStringBuffer.append(" value = *****" + str);
          }
        }
        else
        {
          localStringBuffer.append(str);
        }
      }
    }
    return localStringBuffer.toString();
  }

  public final void verify(PublicKey paramPublicKey)
  {
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.crypto.asn1.x509.X509Certificate
 * JD-Core Version:    0.6.0
 */