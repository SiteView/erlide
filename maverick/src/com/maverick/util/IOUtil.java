package com.maverick.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtil
{
  public static int BUFFER_SIZE = 8192;

  public static void copy(InputStream paramInputStream, OutputStream paramOutputStream)
    throws IOException
  {
    copy(paramInputStream, paramOutputStream, -1L);
  }

  public static void copy(InputStream paramInputStream, OutputStream paramOutputStream, long paramLong)
    throws IOException
  {
    copy(paramInputStream, paramOutputStream, paramLong, BUFFER_SIZE);
  }

  public static void copy(InputStream paramInputStream, OutputStream paramOutputStream, long paramLong, int paramInt)
    throws IOException
  {
    byte[] arrayOfByte = new byte[paramInt];
    int i = paramInt;
    if (paramLong >= 0L)
      while (paramLong > 0L)
      {
        if (paramLong < paramInt)
          i = paramInputStream.read(arrayOfByte, 0, (int)paramLong);
        else
          i = paramInputStream.read(arrayOfByte, 0, paramInt);
        if (i == -1)
          break;
        paramLong -= i;
        paramOutputStream.write(arrayOfByte, 0, i);
      }
    while (true)
    {
      i = paramInputStream.read(arrayOfByte, 0, paramInt);
      if (i < 0)
        break;
      paramOutputStream.write(arrayOfByte, 0, i);
    }
  }

  public static boolean closeStream(InputStream paramInputStream)
  {
    try
    {
      if (paramInputStream != null)
        paramInputStream.close();
      return true;
    }
    catch (IOException localIOException)
    {
    }
    return false;
  }

  public static boolean closeStream(OutputStream paramOutputStream)
  {
    try
    {
      if (paramOutputStream != null)
        paramOutputStream.close();
      return true;
    }
    catch (IOException localIOException)
    {
    }
    return false;
  }

  public static boolean delTree(File paramFile)
  {
    if (paramFile.isFile())
      return paramFile.delete();
    String[] arrayOfString = paramFile.list();
    for (int i = 0; i < arrayOfString.length; i++)
      if (!delTree(new File(paramFile, arrayOfString[i])))
        return false;
    return true;
  }

  public static void recurseDeleteDirectory(File paramFile)
  {
    String[] arrayOfString = paramFile.list();
    if (arrayOfString == null)
      return;
    for (int i = 0; i < arrayOfString.length; i++)
    {
      File localFile = new File(paramFile, arrayOfString[i]);
      if (localFile.isDirectory())
        recurseDeleteDirectory(localFile);
      localFile.delete();
    }
    paramFile.delete();
  }

  public static void copyFile(File paramFile1, File paramFile2)
    throws IOException
  {
    Object localObject1;
    Object localObject2;
    if (paramFile1.isDirectory())
    {
      if (!paramFile2.exists())
        paramFile2.mkdir();
      localObject1 = paramFile1.list();
      for (int i = 0; i < localObject1.length; i++)
      {
        localObject2 = new File(paramFile1, localObject1[i]);
        if ((((File)localObject2).getName().equals(".")) || (((File)localObject2).getName().equals("..")))
          continue;
        if (((File)localObject2).isDirectory())
        {
          File localFile = new File(paramFile2, ((File)localObject2).getName());
          copyFile((File)localObject2, localFile);
        }
        else
        {
          copyFile((File)localObject2, paramFile2);
        }
      }
    }
    else if ((paramFile1.isFile()) && ((paramFile2.isDirectory()) || (paramFile2.isFile())))
    {
      if (paramFile2.isDirectory())
        paramFile2 = new File(paramFile2, paramFile1.getName());
      localObject1 = new FileInputStream(paramFile1);
      FileOutputStream localFileOutputStream = new FileOutputStream(paramFile2);
      localObject2 = new byte[32678];
      int j;
      while ((j = ((FileInputStream)localObject1).read(localObject2)) > -1)
        localFileOutputStream.write(localObject2, 0, j);
      closeStream((InputStream)localObject1);
      closeStream(localFileOutputStream);
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.maverick.util.IOUtil
 * JD-Core Version:    0.6.0
 */