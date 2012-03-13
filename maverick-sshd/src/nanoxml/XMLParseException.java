package nanoxml;

public class XMLParseException extends RuntimeException
{
  public static final int NO_LINE = -1;
  private int A;

  public XMLParseException(String paramString1, String paramString2)
  {
    super("XML Parse Exception during parsing of " + (paramString1 == null ? "the XML definition" : new StringBuffer().append("a ").append(paramString1).append(" element").toString()) + ": " + paramString2);
    this.A = -1;
  }

  public XMLParseException(String paramString1, int paramInt, String paramString2)
  {
    super("XML Parse Exception during parsing of " + (paramString1 == null ? "the XML definition" : new StringBuffer().append("a ").append(paramString1).append(" element").toString()) + " at line " + paramInt + ": " + paramString2);
    this.A = paramInt;
  }

  public int getLineNr()
  {
    return this.A;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     nanoxml.XMLParseException
 * JD-Core Version:    0.6.0
 */