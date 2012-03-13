package com.sshtools.publickey;

import B;
import com.maverick.util.Base64;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Hashtable;

public abstract class Base64EncodedFileFormat
{
  protected String begin;
  protected String end;
  private Hashtable C = new Hashtable();
  private int B = 70;

  protected Base64EncodedFileFormat(String paramString1, String paramString2)
  {
    this.begin = paramString1;
    this.end = paramString2;
  }

  public static boolean isFormatted(byte[] paramArrayOfByte, String paramString1, String paramString2)
  {
    String str = new String(paramArrayOfByte);
    return (str.indexOf(paramString1) >= 0) && (str.indexOf(paramString2) > 0);
  }

  public void setHeaderValue(String paramString1, String paramString2)
  {
    this.C.put(paramString1, paramString2);
  }

  public String getHeaderValue(String paramString)
  {
    return (String)this.C.get(paramString);
  }

  protected byte[] getKeyBlob(byte[] paramArrayOfByte)
    throws IOException
  {
    BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(paramArrayOfByte)));
    StringBuffer localStringBuffer = new StringBuffer("");
    String str1;
    do
    {
      str1 = localBufferedReader.readLine();
      if (str1 != null)
        continue;
      throw new IOException("Incorrect file format!");
    }
    while (!str1.trim().endsWith(this.begin));
    while (true)
    {
      str1 = localBufferedReader.readLine();
      if (str1 == null)
        throw new IOException("Incorrect file format!");
      str1 = str1.trim();
      int i = str1.indexOf(": ");
      if (i <= 0)
        break;
      while (str1.endsWith("\\"))
      {
        str1 = str1.substring(0, str1.length() - 1);
        String str4 = localBufferedReader.readLine();
        if (str4 == null)
          throw new IOException("Incorrect file format!");
        str1 = str1 + str4.trim();
      }
      String str2 = str1.substring(0, i);
      String str3 = str1.substring(i + 2);
      this.C.put(str2, str3);
    }
    while (true)
    {
      localStringBuffer.append(str1);
      str1 = localBufferedReader.readLine();
      if (str1 == null)
        throw new IOException("Invalid file format!");
      str1 = str1.trim();
      if (str1.endsWith(this.end))
        break;
    }
    return Base64.decode(localStringBuffer.toString());
  }

  protected byte[] formatKey(byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    localByteArrayOutputStream.write(this.begin.getBytes());
    localByteArrayOutputStream.write(10);
    Object localObject = this.C.keys();
    while (((Enumeration)localObject).hasMoreElements())
    {
      String str1 = (String)((Enumeration)localObject).nextElement();
      String str2 = (String)this.C.get(str1);
      String str4 = str1 + ": " + str2;
      int i = 0;
      while (i < str4.length())
      {
        String str3 = str4.substring(i, i + this.B < str4.length() ? i + this.B : str4.length()) + (i + this.B < str4.length() ? "\\" : "");
        localByteArrayOutputStream.write(str3.getBytes());
        localByteArrayOutputStream.write(10);
        i += this.B;
      }
    }
    localObject = Base64.encodeBytes(paramArrayOfByte, false);
    localByteArrayOutputStream.write(((String)localObject).getBytes());
    localByteArrayOutputStream.write(10);
    localByteArrayOutputStream.write(this.end.getBytes());
    localByteArrayOutputStream.write(10);
    return (B)localByteArrayOutputStream.toByteArray();
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.sshtools.publickey.Base64EncodedFileFormat
 * JD-Core Version:    0.6.0
 */