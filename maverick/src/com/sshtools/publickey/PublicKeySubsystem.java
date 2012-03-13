package com.sshtools.publickey;

import com.maverick.ssh.Packet;
import com.maverick.ssh.SshException;
import com.maverick.ssh.SubsystemChannel;
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.ssh2.Ssh2Session;
import com.maverick.util.ByteArrayReader;
import java.io.IOException;
import java.util.Vector;

public class PublicKeySubsystem extends SubsystemChannel
{
  int q;

  public PublicKeySubsystem(Ssh2Session paramSsh2Session)
    throws SshException
  {
    super(paramSsh2Session);
    try
    {
      if (!paramSsh2Session.startSubsystem("publickey@vandyke.com"))
        throw new SshException("The remote side failed to start the publickey subsystem", 6);
      Packet localPacket = createPacket();
      localPacket.writeString("version");
      localPacket.writeInt(1);
      sendMessage(localPacket);
      ByteArrayReader localByteArrayReader = new ByteArrayReader(nextMessage());
      localByteArrayReader.readString();
      int i = (int)localByteArrayReader.readInt();
      this.q = Math.min(i, 1);
    }
    catch (IOException localIOException)
    {
      throw new SshException(5, localIOException);
    }
  }

  public void add(SshPublicKey paramSshPublicKey, String paramString)
    throws SshException, PublicKeySubsystemException
  {
    try
    {
      Packet localPacket = createPacket();
      localPacket.writeString("add");
      localPacket.writeString(paramString);
      localPacket.writeString(paramSshPublicKey.getAlgorithm());
      localPacket.writeBinaryString(paramSshPublicKey.getEncoded());
      sendMessage(localPacket);
      c();
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException);
    }
  }

  public void remove(SshPublicKey paramSshPublicKey)
    throws SshException, PublicKeySubsystemException
  {
    try
    {
      Packet localPacket = createPacket();
      localPacket.writeString("remove");
      localPacket.writeString(paramSshPublicKey.getAlgorithm());
      localPacket.writeBinaryString(paramSshPublicKey.getEncoded());
      sendMessage(localPacket);
      c();
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException);
    }
  }

  public SshPublicKey[] list()
    throws SshException, PublicKeySubsystemException
  {
    try
    {
      Packet localPacket = createPacket();
      localPacket.writeString("list");
      sendMessage(localPacket);
      Vector localVector = new Vector();
      while (true)
      {
        ByteArrayReader localByteArrayReader = new ByteArrayReader(nextMessage());
        String str1 = localByteArrayReader.readString();
        String str3;
        if (str1.equals("publickey"))
        {
          String str2 = localByteArrayReader.readString();
          str3 = localByteArrayReader.readString();
          localVector.addElement(SshPublicKeyFileFactory.decodeSSH2PublicKey(str3, localByteArrayReader.readBinaryString()));
        }
        else
        {
          if (str1.equals("status"))
          {
            int i = (int)localByteArrayReader.readInt();
            str3 = localByteArrayReader.readString();
            if (i != 0)
              throw new PublicKeySubsystemException(i, str3);
            SshPublicKey[] arrayOfSshPublicKey = new SshPublicKey[localVector.size()];
            localVector.copyInto(arrayOfSshPublicKey);
            return arrayOfSshPublicKey;
          }
          throw new SshException("The server sent an invalid response to a list command", 3);
        }
      }
    }
    catch (IOException localIOException)
    {
    }
    throw new SshException(localIOException);
  }

  public void associateCommand(SshPublicKey paramSshPublicKey, String paramString)
    throws SshException, PublicKeySubsystemException
  {
    try
    {
      Packet localPacket = createPacket();
      localPacket.writeString("command");
      localPacket.writeString(paramSshPublicKey.getAlgorithm());
      localPacket.writeBinaryString(paramSshPublicKey.getEncoded());
      localPacket.writeString(paramString);
      sendMessage(localPacket);
      c();
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException);
    }
  }

  void c()
    throws SshException, PublicKeySubsystemException
  {
    try
    {
      ByteArrayReader localByteArrayReader = new ByteArrayReader(nextMessage());
      localByteArrayReader.readString();
      int i = (int)localByteArrayReader.readInt();
      String str = localByteArrayReader.readString();
      if (i != 0)
        throw new PublicKeySubsystemException(i, str);
    }
    catch (IOException localIOException)
    {
      throw new SshException(localIOException);
    }
  }
}

/* Location:           C:\src\maverick\dist\maverick-all.jar
 * Qualified Name:     com.sshtools.publickey.PublicKeySubsystem
 * JD-Core Version:    0.6.0
 */