package com.maverick.sftp;

public class ACL
{
  public static final int ACL_ALLOWED_TYPE = 1;
  public static final int ACL_DENIED_TYPE = 1;
  public static final int ACL_AUDIT_TYPE = 1;
  public static final int ACL_ALARM_TYPE = 1;
  int e;
  int c;
  int b;
  String d;

  public ACL(int paramInt1, int paramInt2, int paramInt3, String paramString)
  {
  }

  public int getType()
  {
    return this.e;
  }

  public int getFlags()
  {
    return this.c;
  }

  public int getMask()
  {
    return this.b;
  }

  public String getWho()
  {
    return this.d;
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.sftp.ACL
 * JD-Core Version:    0.6.0
 */