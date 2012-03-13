package com.maverick.sshd.vfs;

import java.io.File;
import java.io.IOException;

class A
{
  private String A;
  private String C;
  private boolean B = false;

  A(String paramString1, String paramString2)
    throws IOException
  {
    paramString2.replace('\\', '/');
    File localFile = new File(paramString2);
    if (!localFile.exists())
      localFile.mkdirs();
    if (!paramString1.trim().startsWith("/"))
      this.A = ("/" + paramString1.trim());
    else
      this.A = paramString1.trim();
    this.C = localFile.getCanonicalPath();
  }

  void A(boolean paramBoolean)
  {
    this.B = paramBoolean;
  }

  String A()
  {
    return this.C.replace('\\', '/');
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.vfs.A
 * JD-Core Version:    0.6.0
 */