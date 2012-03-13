package nanoxml;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class XMLElement
{
  public static final int NANOXML_MAJOR_VERSION = 2;
  public static final int NANOXML_MINOR_VERSION = 2;
  private Hashtable F;
  private Vector C;
  private String A;
  private String D;
  private Hashtable H;
  private int B;
  private boolean J;
  private boolean K;
  private char I;
  private Reader G;
  private int E;

  public XMLElement()
  {
    this(new Hashtable(), false, true, true);
  }

  public XMLElement(Hashtable paramHashtable)
  {
    this(paramHashtable, false, true, true);
  }

  public XMLElement(boolean paramBoolean)
  {
    this(new Hashtable(), paramBoolean, true, true);
  }

  public XMLElement(Hashtable paramHashtable, boolean paramBoolean)
  {
    this(paramHashtable, paramBoolean, true, true);
  }

  public XMLElement(Hashtable paramHashtable, boolean paramBoolean1, boolean paramBoolean2)
  {
    this(paramHashtable, paramBoolean1, true, paramBoolean2);
  }

  protected XMLElement(Hashtable paramHashtable, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    this.K = paramBoolean1;
    this.J = paramBoolean3;
    this.A = null;
    this.D = "";
    this.F = new Hashtable();
    this.C = new Vector();
    this.H = paramHashtable;
    this.B = 0;
    Enumeration localEnumeration = this.H.keys();
    while (localEnumeration.hasMoreElements())
    {
      Object localObject1 = localEnumeration.nextElement();
      Object localObject2 = this.H.get(localObject1);
      if ((localObject2 instanceof String))
      {
        localObject2 = ((String)localObject2).toCharArray();
        this.H.put(localObject1, localObject2);
      }
    }
    if (paramBoolean2)
    {
      this.H.put("amp", new char[] { '&' });
      this.H.put("quot", new char[] { '"' });
      this.H.put("apos", new char[] { '\'' });
      this.H.put("lt", new char[] { '<' });
      this.H.put("gt", new char[] { '>' });
    }
  }

  public void addChild(XMLElement paramXMLElement)
  {
    this.C.addElement(paramXMLElement);
  }

  public void setAttribute(String paramString, Object paramObject)
  {
    if (this.J)
      paramString = paramString.toUpperCase();
    this.F.put(paramString, paramObject.toString());
  }

  public void addProperty(String paramString, Object paramObject)
  {
    setAttribute(paramString, paramObject);
  }

  public void setIntAttribute(String paramString, int paramInt)
  {
    if (this.J)
      paramString = paramString.toUpperCase();
    this.F.put(paramString, Integer.toString(paramInt));
  }

  public void addProperty(String paramString, int paramInt)
  {
    setIntAttribute(paramString, paramInt);
  }

  public void setDoubleAttribute(String paramString, double paramDouble)
  {
    if (this.J)
      paramString = paramString.toUpperCase();
    this.F.put(paramString, Double.toString(paramDouble));
  }

  public void addProperty(String paramString, double paramDouble)
  {
    setDoubleAttribute(paramString, paramDouble);
  }

  public int countChildren()
  {
    return this.C.size();
  }

  public Enumeration enumerateAttributeNames()
  {
    return this.F.keys();
  }

  public Enumeration enumeratePropertyNames()
  {
    return enumerateAttributeNames();
  }

  public Enumeration enumerateChildren()
  {
    return this.C.elements();
  }

  public Vector getChildren()
  {
    try
    {
      return (Vector)this.C.clone();
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  public String getContents()
  {
    return getContent();
  }

  public String getContent()
  {
    return this.D;
  }

  public int getLineNr()
  {
    return this.B;
  }

  public Object getAttribute(String paramString)
  {
    return getAttribute(paramString, null);
  }

  public Object getAttribute(String paramString, Object paramObject)
  {
    if (this.J)
      paramString = paramString.toUpperCase();
    Object localObject = this.F.get(paramString);
    if (localObject == null)
      localObject = paramObject;
    return localObject;
  }

  public Object getAttribute(String paramString1, Hashtable paramHashtable, String paramString2, boolean paramBoolean)
  {
    if (this.J)
      paramString1 = paramString1.toUpperCase();
    Object localObject1 = this.F.get(paramString1);
    if (localObject1 == null)
      localObject1 = paramString2;
    Object localObject2 = paramHashtable.get(localObject1);
    if (localObject2 == null)
      if (paramBoolean)
        localObject2 = localObject1;
      else
        throw invalidValue(paramString1, (String)localObject1);
    return localObject2;
  }

  public String getStringAttribute(String paramString)
  {
    return getStringAttribute(paramString, null);
  }

  public String getStringAttribute(String paramString1, String paramString2)
  {
    return (String)getAttribute(paramString1, paramString2);
  }

  public String getStringAttribute(String paramString1, Hashtable paramHashtable, String paramString2, boolean paramBoolean)
  {
    return (String)getAttribute(paramString1, paramHashtable, paramString2, paramBoolean);
  }

  public int getIntAttribute(String paramString)
  {
    return getIntAttribute(paramString, 0);
  }

  public int getIntAttribute(String paramString, int paramInt)
  {
    if (this.J)
      paramString = paramString.toUpperCase();
    String str = (String)this.F.get(paramString);
    if (str == null)
      return paramInt;
    try
    {
      return Integer.parseInt(str);
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    throw invalidValue(paramString, str);
  }

  public int getIntAttribute(String paramString1, Hashtable paramHashtable, String paramString2, boolean paramBoolean)
  {
    if (this.J)
      paramString1 = paramString1.toUpperCase();
    Object localObject = this.F.get(paramString1);
    if (localObject == null)
      localObject = paramString2;
    Integer localInteger;
    try
    {
      localInteger = (Integer)paramHashtable.get(localObject);
    }
    catch (ClassCastException localClassCastException)
    {
      throw invalidValueSet(paramString1);
    }
    if (localInteger == null)
    {
      if (!paramBoolean)
        throw invalidValue(paramString1, (String)localObject);
      try
      {
        localInteger = Integer.valueOf((String)localObject);
      }
      catch (NumberFormatException localNumberFormatException)
      {
        throw invalidValue(paramString1, (String)localObject);
      }
    }
    return localInteger.intValue();
  }

  public double getDoubleAttribute(String paramString)
  {
    return getDoubleAttribute(paramString, 0.0D);
  }

  public double getDoubleAttribute(String paramString, double paramDouble)
  {
    if (this.J)
      paramString = paramString.toUpperCase();
    String str = (String)this.F.get(paramString);
    if (str == null)
      return paramDouble;
    try
    {
      return Double.valueOf(str).doubleValue();
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    throw invalidValue(paramString, str);
  }

  public double getDoubleAttribute(String paramString1, Hashtable paramHashtable, String paramString2, boolean paramBoolean)
  {
    if (this.J)
      paramString1 = paramString1.toUpperCase();
    Object localObject = this.F.get(paramString1);
    if (localObject == null)
      localObject = paramString2;
    Double localDouble;
    try
    {
      localDouble = (Double)paramHashtable.get(localObject);
    }
    catch (ClassCastException localClassCastException)
    {
      throw invalidValueSet(paramString1);
    }
    if (localDouble == null)
    {
      if (!paramBoolean)
        throw invalidValue(paramString1, (String)localObject);
      try
      {
        localDouble = Double.valueOf((String)localObject);
      }
      catch (NumberFormatException localNumberFormatException)
      {
        throw invalidValue(paramString1, (String)localObject);
      }
    }
    return localDouble.doubleValue();
  }

  public boolean getBooleanAttribute(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
  {
    if (this.J)
      paramString1 = paramString1.toUpperCase();
    Object localObject = this.F.get(paramString1);
    if (localObject == null)
      return paramBoolean;
    if (localObject.equals(paramString2))
      return true;
    if (localObject.equals(paramString3))
      return false;
    throw invalidValue(paramString1, (String)localObject);
  }

  public int getIntProperty(String paramString1, Hashtable paramHashtable, String paramString2)
  {
    return getIntAttribute(paramString1, paramHashtable, paramString2, false);
  }

  public String getProperty(String paramString)
  {
    return getStringAttribute(paramString);
  }

  public String getProperty(String paramString1, String paramString2)
  {
    return getStringAttribute(paramString1, paramString2);
  }

  public int getProperty(String paramString, int paramInt)
  {
    return getIntAttribute(paramString, paramInt);
  }

  public double getProperty(String paramString, double paramDouble)
  {
    return getDoubleAttribute(paramString, paramDouble);
  }

  public boolean getProperty(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
  {
    return getBooleanAttribute(paramString1, paramString2, paramString3, paramBoolean);
  }

  public Object getProperty(String paramString1, Hashtable paramHashtable, String paramString2)
  {
    return getAttribute(paramString1, paramHashtable, paramString2, false);
  }

  public String getStringProperty(String paramString1, Hashtable paramHashtable, String paramString2)
  {
    return getStringAttribute(paramString1, paramHashtable, paramString2, false);
  }

  public int getSpecialIntProperty(String paramString1, Hashtable paramHashtable, String paramString2)
  {
    return getIntAttribute(paramString1, paramHashtable, paramString2, true);
  }

  public double getSpecialDoubleProperty(String paramString1, Hashtable paramHashtable, String paramString2)
  {
    return getDoubleAttribute(paramString1, paramHashtable, paramString2, true);
  }

  public String getName()
  {
    return this.A;
  }

  public String getTagName()
  {
    return getName();
  }

  public void parseFromReader(Reader paramReader)
    throws IOException, XMLParseException
  {
    parseFromReader(paramReader, 1);
  }

  public void parseFromReader(Reader paramReader, int paramInt)
    throws IOException, XMLParseException
  {
    this.I = '\000';
    this.G = paramReader;
    this.E = paramInt;
    while (true)
    {
      char c = scanWhitespace();
      if (c != '<')
        throw expectedInput("<");
      c = readChar();
      if ((c == '!') || (c == '?'))
      {
        skipSpecialTag(0);
      }
      else
      {
        unreadChar(c);
        scanElement(this);
        return;
      }
    }
  }

  public void parseString(String paramString)
    throws XMLParseException
  {
    try
    {
      parseFromReader(new StringReader(paramString), 1);
    }
    catch (IOException localIOException)
    {
    }
  }

  public void parseString(String paramString, int paramInt)
    throws XMLParseException
  {
    parseString(paramString.substring(paramInt));
  }

  public void parseString(String paramString, int paramInt1, int paramInt2)
    throws XMLParseException
  {
    parseString(paramString.substring(paramInt1, paramInt2));
  }

  public void parseString(String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws XMLParseException
  {
    paramString = paramString.substring(paramInt1, paramInt2);
    try
    {
      parseFromReader(new StringReader(paramString), paramInt3);
    }
    catch (IOException localIOException)
    {
    }
  }

  public void parseCharArray(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws XMLParseException
  {
    parseCharArray(paramArrayOfChar, paramInt1, paramInt2, 1);
  }

  public void parseCharArray(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3)
    throws XMLParseException
  {
    try
    {
      CharArrayReader localCharArrayReader = new CharArrayReader(paramArrayOfChar, paramInt1, paramInt2);
      parseFromReader(localCharArrayReader, paramInt3);
    }
    catch (IOException localIOException)
    {
    }
  }

  public void removeChild(XMLElement paramXMLElement)
  {
    this.C.removeElement(paramXMLElement);
  }

  public void removeAttribute(String paramString)
  {
    if (this.J)
      paramString = paramString.toUpperCase();
    this.F.remove(paramString);
  }

  public void removeProperty(String paramString)
  {
    removeAttribute(paramString);
  }

  public void removeChild(String paramString)
  {
    removeAttribute(paramString);
  }

  protected XMLElement createAnotherElement()
  {
    return new XMLElement(this.H, this.K, false, this.J);
  }

  public void setContent(String paramString)
  {
    this.D = paramString;
  }

  public void setTagName(String paramString)
  {
    setName(paramString);
  }

  public void setName(String paramString)
  {
    this.A = paramString;
  }

  public String toString()
  {
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(localByteArrayOutputStream);
      write(localOutputStreamWriter);
      localOutputStreamWriter.flush();
      return new String(localByteArrayOutputStream.toByteArray());
    }
    catch (IOException localIOException)
    {
    }
    return super.toString();
  }

  public void write(Writer paramWriter)
    throws IOException
  {
    if (this.A == null)
    {
      writeEncoded(paramWriter, this.D);
      return;
    }
    paramWriter.write(60);
    paramWriter.write(this.A);
    Enumeration localEnumeration;
    Object localObject;
    if (!this.F.isEmpty())
    {
      localEnumeration = this.F.keys();
      while (localEnumeration.hasMoreElements())
      {
        paramWriter.write(32);
        localObject = (String)localEnumeration.nextElement();
        String str = (String)this.F.get(localObject);
        paramWriter.write((String)localObject);
        paramWriter.write(61);
        paramWriter.write(34);
        writeEncoded(paramWriter, str);
        paramWriter.write(34);
      }
    }
    if ((this.D != null) && (this.D.length() > 0))
    {
      paramWriter.write(62);
      writeEncoded(paramWriter, this.D);
      paramWriter.write(60);
      paramWriter.write(47);
      paramWriter.write(this.A);
      paramWriter.write(62);
    }
    else if (this.C.isEmpty())
    {
      paramWriter.write(47);
      paramWriter.write(62);
    }
    else
    {
      paramWriter.write(62);
      localEnumeration = enumerateChildren();
      while (localEnumeration.hasMoreElements())
      {
        localObject = (XMLElement)localEnumeration.nextElement();
        ((XMLElement)localObject).write(paramWriter);
      }
      paramWriter.write(60);
      paramWriter.write(47);
      paramWriter.write(this.A);
      paramWriter.write(62);
    }
  }

  protected void writeEncoded(Writer paramWriter, String paramString)
    throws IOException
  {
    for (int i = 0; i < paramString.length(); i++)
    {
      int j = paramString.charAt(i);
      switch (j)
      {
      case 60:
        paramWriter.write(38);
        paramWriter.write(108);
        paramWriter.write(116);
        paramWriter.write(59);
        break;
      case 62:
        paramWriter.write(38);
        paramWriter.write(103);
        paramWriter.write(116);
        paramWriter.write(59);
        break;
      case 38:
        paramWriter.write(38);
        paramWriter.write(97);
        paramWriter.write(109);
        paramWriter.write(112);
        paramWriter.write(59);
        break;
      case 34:
        paramWriter.write(38);
        paramWriter.write(113);
        paramWriter.write(117);
        paramWriter.write(111);
        paramWriter.write(116);
        paramWriter.write(59);
        break;
      case 39:
        paramWriter.write(38);
        paramWriter.write(97);
        paramWriter.write(112);
        paramWriter.write(111);
        paramWriter.write(115);
        paramWriter.write(59);
        break;
      default:
        int k = j;
        if ((k < 32) || (k > 126))
        {
          paramWriter.write(38);
          paramWriter.write(35);
          paramWriter.write(120);
          paramWriter.write(Integer.toString(k, 16));
          paramWriter.write(59);
        }
        else
        {
          paramWriter.write(j);
        }
      }
    }
  }

  protected void scanIdentifier(StringBuffer paramStringBuffer)
    throws IOException
  {
    while (true)
    {
      char c = readChar();
      if (((c < 'A') || (c > 'Z')) && ((c < 'a') || (c > 'z')) && ((c < '0') || (c > '9')) && (c != '_') && (c != '.') && (c != ':') && (c != '-') && (c <= '~'))
      {
        unreadChar(c);
        return;
      }
      paramStringBuffer.append(c);
    }
  }

  protected char scanWhitespace()
    throws IOException
  {
    while (true)
    {
      int i = readChar();
      switch (i)
      {
      case 9:
      case 10:
      case 13:
      case 32:
        break;
      default:
        return i;
      }
    }
  }

  protected char scanWhitespace(StringBuffer paramStringBuffer)
    throws IOException
  {
    while (true)
    {
      char c = readChar();
      switch (c)
      {
      case '\t':
      case '\n':
      case ' ':
        paramStringBuffer.append(c);
      case '\r':
        break;
      default:
        return c;
      }
    }
  }

  protected void scanString(StringBuffer paramStringBuffer)
    throws IOException
  {
    int i = readChar();
    if ((i != 39) && (i != 34))
      throw expectedInput("' or \"");
    while (true)
    {
      char c = readChar();
      if (c == i)
        return;
      if (c == '&')
        resolveEntity(paramStringBuffer);
      else
        paramStringBuffer.append(c);
    }
  }

  protected void scanPCData(StringBuffer paramStringBuffer)
    throws IOException
  {
    while (true)
    {
      char c = readChar();
      if (c == '<')
      {
        c = readChar();
        if (c == '!')
        {
          checkCDATA(paramStringBuffer);
        }
        else
        {
          unreadChar(c);
          return;
        }
      }
      else if (c == '&')
      {
        resolveEntity(paramStringBuffer);
      }
      else
      {
        paramStringBuffer.append(c);
      }
    }
  }

  protected boolean checkCDATA(StringBuffer paramStringBuffer)
    throws IOException
  {
    char c = readChar();
    if (c != '[')
    {
      unreadChar(c);
      skipSpecialTag(0);
      return false;
    }
    if (!checkLiteral("CDATA["))
    {
      skipSpecialTag(1);
      return false;
    }
    int i = 0;
    while (i < 3)
    {
      c = readChar();
      int j;
      switch (c)
      {
      case ']':
        if (i < 2)
        {
          i++;
          continue;
        }
        paramStringBuffer.append(']');
        paramStringBuffer.append(']');
        i = 0;
        break;
      case '>':
        if (i < 2)
        {
          for (j = 0; j < i; j++)
            paramStringBuffer.append(']');
          i = 0;
          paramStringBuffer.append('>');
          continue;
        }
        i = 3;
        break;
      default:
        for (j = 0; j < i; j++)
          paramStringBuffer.append(']');
        paramStringBuffer.append(c);
        i = 0;
      }
    }
    return true;
  }

  protected void skipComment()
    throws IOException
  {
    int i = 2;
    while (i > 0)
    {
      int j = readChar();
      if (j == 45)
        i--;
      else
        i = 2;
    }
    if (readChar() != '>')
      throw expectedInput(">");
  }

  protected void skipSpecialTag(int paramInt)
    throws IOException
  {
    int i = 1;
    int j = 0;
    int k;
    if (paramInt == 0)
    {
      k = readChar();
      if (k == 91)
      {
        paramInt++;
      }
      else if (k == 45)
      {
        k = readChar();
        if (k == 91)
        {
          paramInt++;
        }
        else if (k == 93)
        {
          paramInt--;
        }
        else if (k == 45)
        {
          skipComment();
          return;
        }
      }
    }
    while (i > 0)
    {
      k = readChar();
      if (j == 0)
      {
        if ((k == 34) || (k == 39))
          j = k;
        else if (paramInt <= 0)
          if (k == 60)
            i++;
          else if (k == 62)
            i--;
        if (k == 91)
          paramInt++;
        else if (k == 93)
          paramInt--;
      }
      else if (k == j)
      {
        j = 0;
      }
    }
  }

  protected boolean checkLiteral(String paramString)
    throws IOException
  {
    int i = paramString.length();
    for (int j = 0; j < i; j++)
      if (readChar() != paramString.charAt(j))
        return false;
    return true;
  }

  protected char readChar()
    throws IOException
  {
    if (this.I != 0)
    {
      i = this.I;
      this.I = '\000';
      return i;
    }
    int i = this.G.read();
    if (i < 0)
      throw unexpectedEndOfData();
    if (i == 10)
    {
      this.E += 1;
      return '\n';
    }
    return (char)i;
  }

  protected void scanElement(XMLElement paramXMLElement)
    throws IOException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    scanIdentifier(localStringBuffer);
    String str = localStringBuffer.toString();
    paramXMLElement.setName(str);
    Object localObject;
    for (char c = scanWhitespace(); (c != '>') && (c != '/'); c = scanWhitespace())
    {
      localStringBuffer.setLength(0);
      unreadChar(c);
      scanIdentifier(localStringBuffer);
      localObject = localStringBuffer.toString();
      c = scanWhitespace();
      if (c != '=')
        throw expectedInput("=");
      unreadChar(scanWhitespace());
      localStringBuffer.setLength(0);
      scanString(localStringBuffer);
      paramXMLElement.setAttribute((String)localObject, localStringBuffer);
    }
    if (c == '/')
    {
      c = readChar();
      if (c != '>')
        throw expectedInput(">");
      return;
    }
    localStringBuffer.setLength(0);
    c = scanWhitespace(localStringBuffer);
    if (c != '<')
    {
      unreadChar(c);
      scanPCData(localStringBuffer);
    }
    else
    {
      while (true)
      {
        c = readChar();
        if (c != '!')
          break;
        if (checkCDATA(localStringBuffer))
        {
          scanPCData(localStringBuffer);
          break label247;
        }
        c = scanWhitespace(localStringBuffer);
        if (c == '<')
          continue;
        unreadChar(c);
        scanPCData(localStringBuffer);
        break label247;
      }
      localStringBuffer.setLength(0);
    }
    label247: if (localStringBuffer.length() == 0)
    {
      while (c != '/')
      {
        if (c == '!')
        {
          c = readChar();
          if (c != '-')
            throw expectedInput("Comment or Element");
          c = readChar();
          if (c != '-')
            throw expectedInput("Comment or Element");
          skipComment();
        }
        else
        {
          unreadChar(c);
          localObject = createAnotherElement();
          scanElement((XMLElement)localObject);
          paramXMLElement.addChild((XMLElement)localObject);
        }
        c = scanWhitespace();
        if (c != '<')
          throw expectedInput("<");
        c = readChar();
      }
      unreadChar(c);
    }
    else if (this.K)
    {
      paramXMLElement.setContent(localStringBuffer.toString().trim());
    }
    else
    {
      paramXMLElement.setContent(localStringBuffer.toString());
    }
    c = readChar();
    if (c != '/')
      throw expectedInput("/");
    unreadChar(scanWhitespace());
    if (!checkLiteral(str))
      throw expectedInput(str);
    if (scanWhitespace() != '>')
      throw expectedInput(">");
  }

  protected void resolveEntity(StringBuffer paramStringBuffer)
    throws IOException
  {
    char c = '\000';
    StringBuffer localStringBuffer = new StringBuffer();
    while (true)
    {
      c = readChar();
      if (c == ';')
        break;
      localStringBuffer.append(c);
    }
    String str = localStringBuffer.toString();
    if (str.charAt(0) == '#')
    {
      try
      {
        if (str.charAt(1) == 'x')
          c = (char)Integer.parseInt(str.substring(2), 16);
        else
          c = (char)Integer.parseInt(str.substring(1), 10);
      }
      catch (NumberFormatException localNumberFormatException)
      {
        throw unknownEntity(str);
      }
      paramStringBuffer.append(c);
    }
    else
    {
      char[] arrayOfChar = (char[])(char[])this.H.get(str);
      if (arrayOfChar == null)
        throw unknownEntity(str);
      paramStringBuffer.append(arrayOfChar);
    }
  }

  protected void unreadChar(char paramChar)
  {
    this.I = paramChar;
  }

  protected XMLParseException invalidValueSet(String paramString)
  {
    String str = "Invalid value set (entity name = \"" + paramString + "\")";
    return new XMLParseException(getName(), this.E, str);
  }

  protected XMLParseException invalidValue(String paramString1, String paramString2)
  {
    String str = "Attribute \"" + paramString1 + "\" does not contain a valid " + "value (\"" + paramString2 + "\")";
    return new XMLParseException(getName(), this.E, str);
  }

  protected XMLParseException unexpectedEndOfData()
  {
    String str = "Unexpected end of data reached";
    return new XMLParseException(getName(), this.E, str);
  }

  protected XMLParseException syntaxError(String paramString)
  {
    String str = "Syntax error while parsing " + paramString;
    return new XMLParseException(getName(), this.E, str);
  }

  protected XMLParseException expectedInput(String paramString)
  {
    String str = "Expected: " + paramString;
    return new XMLParseException(getName(), this.E, str);
  }

  protected XMLParseException unknownEntity(String paramString)
  {
    String str = "Unknown or invalid entity: &" + paramString + ";";
    return new XMLParseException(getName(), this.E, str);
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     nanoxml.XMLElement
 * JD-Core Version:    0.6.0
 */