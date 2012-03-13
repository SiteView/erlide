package com.maverick.sshd;

import com.maverick.nio.Daemon;
import com.maverick.nio.ProtocolContext;
import com.maverick.nio.ProtocolEngine;
import com.maverick.ssh.SshException;
import com.maverick.ssh.components.ComponentFactory;
import com.maverick.ssh.components.SshKeyPair;
import com.maverick.ssh.components.SshPublicKey;
import com.maverick.sshd.components.ServerComponentManager;
import com.maverick.sshd.platform.AuthenticationProvider;
import com.sshtools.publickey.InvalidPassphraseException;
import com.sshtools.publickey.SshKeyPairGenerator;
import com.sshtools.publickey.SshPrivateKeyFile;
import com.sshtools.publickey.SshPrivateKeyFileFactory;
import com.sshtools.publickey.SshPublicKeyFile;
import com.sshtools.publickey.SshPublicKeyFileFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.security.auth.login.Configuration;

public class SshContext extends ProtocolContext
{
  public static final String CIPHER_TRIPLEDES_CBC = "3des-cbc";
  public static final String CIPHER_BLOWFISH_CBC = "blowfish-cbc";
  public static final String CIPHER_AES128_CBC = "aes128-cbc";
  public static final String CIPHER_AES192_CBC = "aes192-cbc";
  public static final String CIPHER_AES256_CBC = "aes256-cbc";
  public static final String HMAC_SHA1 = "hmac-sha1";
  public static final String HMAC_MD5 = "hmac-md5";
  public static final String COMPRESSION_NONE = "none";
  public static final String COMPRESSION_ZLIB = "zlib";
  public static final String KEX_DIFFIE_HELLMAN_GROUP1_SHA1 = "diffie-hellman-group1-sha1";
  public static final String KEX_DIFFIE_HELLMAN_GROUP_EXCHANGE_SHA1 = "diffie-hellman-group-exchange-sha1";
  public static final String KEX_DIFFIE_HELLMAN_GROUP14_SHA1 = "diffie-hellman-group14-sha1";
  public static final String PUBLIC_KEY_SSHDSS = "ssh-dss";
  public static final String PUBLIC_KEY_SSHRSA = "ssh-rsa";
  public static final String PASSWORD_AUTHENTICATION = "password";
  public static final String PUBLICKEY_AUTHENTICATION = "publickey";
  public static final String KEYBOARD_INTERACTIVE_AUTHENTICATION = "keyboard-interactive";
  int f = 50;
  ComponentFactory z;
  ComponentFactory W;
  ComponentFactory o = ServerComponentManager.getInstance().supportedSsh2CiphersCS();
  ComponentFactory K = ServerComponentManager.getInstance().supportedSsh2CiphersSC();
  ComponentFactory u = ServerComponentManager.getInstance().supportedKeyExchanges();
  ComponentFactory i = ServerComponentManager.getInstance().supportedHMacsCS();
  ComponentFactory E = ServerComponentManager.getInstance().supportedHMacsSC();
  ComponentFactory £ = ServerComponentManager.getInstance().supportedPublicKeys();
  ComponentFactory q;
  ComponentFactory j;
  ComponentFactory m;
  Map w = Collections.synchronizedMap(new HashMap());
  AccessManager x = null;
  String M = "blowfish-cbc";
  String l = "blowfish-cbc";
  String P = "hmac-sha1";
  String n = "hmac-sha1";
  String µ = "none";
  String a = "none";
  String X = "diffie-hellman-group1-sha1";
  String T = "ssh-dss";
  int U = 100;
  int v = 10;
  int F = 6;
  int À = 131072;
  int Y = 2147483647;
  int e = 1073741824;
  boolean L = true;
  String ¤ = "";
  Class G = PasswordKeyboardInteractiveProvider.class;
  Class B;
  Class N = null;
  AuthenticationProvider p;
  Map Q = Collections.synchronizedMap(new HashMap());
  String k;
  Daemon º;
  String r = "Maverick_SSHD";
  Map I = Collections.synchronizedMap(new HashMap());
  Class S = null;
  boolean ¢ = false;
  Class ¥ = RemoteForwardingFactoryImpl.class;
  Class D = RemoteForwardingManager.class;
  int d = 0;
  int C = 0;
  int h = 30;
  int b = 128;
  int J = 10;
  String g = "ISO-8859-1";
  PublicKeyStore V = new AuthorizedKeysStoreImpl();
  ArrayList s = new ArrayList();
  int t = -1;
  boolean _ = false;
  String ª;
  String y;
  char[] c;
  String O;
  Configuration Z;
  String H = "Too many connections";
  public static final int ANY = 0;
  public static final int PUBLIC_KEY = 1;
  public static final int PASSWORD = 2;
  public static final int PUBLIC_KEYandPASSWORD = 3;
  public static final int KEYBOARD_INTERACTIVE = 4;
  ForwardingCallback R;

  public SshContext()
    throws IOException
  {
    if (this.K.contains("aes128-cbc"))
      this.l = "aes128-cbc";
    if (this.o.contains("aes128-cbc"))
      this.M = "aes128-cbc";
    try
    {
      this.z = new ComponentFactory(Class.forName("com.maverick.ssh.compression.SshCompression"));
      this.z.add("none", Class.forName("java.lang.Object"));
      try
      {
        this.z.add("zlib", Class.forName("com.sshtools.zlib.ZLibCompression"));
      }
      catch (Throwable localThrowable1)
      {
      }
      this.W = new ComponentFactory(Class.forName("com.maverick.ssh.compression.SshCompression"));
      this.W.add("none", Class.forName("java.lang.Object"));
      try
      {
        this.W.add("zlib", Class.forName("com.sshtools.zlib.ZLibCompression"));
      }
      catch (Throwable localThrowable2)
      {
      }
      this.q = new ComponentFactory(Class.forName("com.maverick.sshd.AuthenticationMechanism"));
      this.q.add("password", Class.forName("com.maverick.sshd.PasswordAuthentication"));
      this.q.add("publickey", Class.forName("com.maverick.sshd.PublicKeyAuthentication"));
      this.q.add("keyboard-interactive", Class.forName("com.maverick.sshd.KeyboardInteractiveAuthentication"));
      this.j = new ComponentFactory(Class.forName("com.maverick.sshd.Channel"));
      this.j.add("direct-tcpip", LocalForwardingChannel.class);
      this.j.add("forwarded-tcpip", RemoteForwardingChannel.class);
      setSessionProvider(UnsupportedSession.class);
      this.m = new ComponentFactory(Class.forName("com.maverick.sshd.Subsystem"));
      this.m.add("sftp", SftpSubsystem.class);
    }
    catch (Throwable localThrowable3)
    {
      throw new IOException(localThrowable3.getMessage() != null ? localThrowable3.getMessage() : localThrowable3.getClass().getName());
    }
    this.keepAlive = true;
    this.tcpNoDelay = true;
    this.receiveBufferSize = 65535;
    this.sendBufferSize = 65535;
  }

  public void init(Daemon paramDaemon)
  {
    this.º = paramDaemon;
  }

  public SshContext(Daemon paramDaemon)
    throws IOException
  {
    this();
    init(paramDaemon);
  }

  public ProtocolEngine createEngine()
    throws IOException
  {
    return new TransportProtocol(this);
  }

  public void loadOrGenerateHostKey(File paramFile, String paramString, int paramInt)
    throws IOException, InvalidPassphraseException, SshException
  {
    loadOrGenerateHostKey(paramFile, paramString, paramInt, 0, 1, "");
  }

  public void loadOrGenerateHostKey(File paramFile, String paramString1, int paramInt, String paramString2)
    throws IOException, InvalidPassphraseException, SshException
  {
    loadOrGenerateHostKey(paramFile, paramString1, paramInt, 0, 1, paramString2);
  }

  public void loadHostKey(InputStream paramInputStream, String paramString, int paramInt)
    throws IOException, InvalidPassphraseException, SshException
  {
    loadHostKey(paramInputStream, paramString, paramInt, 0, 1, "");
  }

  public void loadHostKey(InputStream paramInputStream, String paramString1, int paramInt, String paramString2)
    throws IOException, InvalidPassphraseException, SshException
  {
    loadHostKey(paramInputStream, paramString1, paramInt, 0, 1, paramString2);
  }

  public void loadOrGenerateHostKey(File paramFile, String paramString1, int paramInt1, int paramInt2, int paramInt3, String paramString2)
    throws IOException, InvalidPassphraseException, SshException
  {
    if (!paramFile.exists())
      addHostKey(GenerateKeyFiles(paramFile, paramString1, paramInt1, paramInt2, paramInt3));
    else
      addHostKey(loadKey(paramFile, paramString2));
  }

  public void loadHostKey(InputStream paramInputStream, String paramString1, int paramInt1, int paramInt2, int paramInt3, String paramString2)
    throws IOException, InvalidPassphraseException, SshException
  {
    addHostKey(loadKey(paramInputStream, paramString2));
  }

  public SshKeyPair loadKey(File paramFile, String paramString)
    throws IOException, InvalidPassphraseException
  {
    return loadKey(new FileInputStream(paramFile), paramString);
  }

  public SshKeyPair loadKey(InputStream paramInputStream, String paramString)
    throws IOException, InvalidPassphraseException
  {
    SshKeyPair localSshKeyPair = SshPrivateKeyFileFactory.parse(paramInputStream).toKeyPair(paramString);
    paramInputStream.close();
    return localSshKeyPair;
  }

  public static SshKeyPair GenerateKeyFiles(File paramFile, String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws IOException, SshException
  {
    SshKeyPair localSshKeyPair = SshKeyPairGenerator.generateKeyPair(paramString, paramInt1);
    SshPrivateKeyFile localSshPrivateKeyFile = SshPrivateKeyFileFactory.create(localSshKeyPair, "", paramString + " host key", paramInt2);
    FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
    localFileOutputStream.write(localSshPrivateKeyFile.getFormattedKey());
    localFileOutputStream.close();
    SshPublicKeyFile localSshPublicKeyFile = SshPublicKeyFileFactory.create(localSshKeyPair.getPublicKey(), paramString + " host key", paramInt3);
    localFileOutputStream = new FileOutputStream(paramFile.getAbsolutePath() + ".pub");
    localFileOutputStream.write(localSshPublicKeyFile.getFormattedKey());
    localFileOutputStream.close();
    return localSshKeyPair;
  }

  public void loadKeystore(File paramFile, String paramString1, String paramString2, String paramString3)
    throws IOException
  {
    ServerComponentManager.getInstance().loadKeystore(paramFile, paramString1, paramString2, paramString3, this);
  }

  public void loadKeystore(InputStream paramInputStream, String paramString1, String paramString2, String paramString3)
    throws IOException
  {
    ServerComponentManager.getInstance().loadKeystore(paramInputStream, paramString1, paramString2, paramString3, this);
  }

  public void loadKeystore(File paramFile, String paramString1, String paramString2, String paramString3, String paramString4)
    throws IOException
  {
    ServerComponentManager.getInstance().loadKeystore(paramFile, paramString1, paramString2, paramString3, paramString4, this);
  }

  public void loadKeystore(InputStream paramInputStream, String paramString1, String paramString2, String paramString3, String paramString4)
    throws IOException
  {
    ServerComponentManager.getInstance().loadKeystore(paramInputStream, paramString1, paramString2, paramString3, paramString4, this);
  }

  public void addGlobalRequestHandler(GlobalRequestHandler paramGlobalRequestHandler)
  {
    for (int i1 = 0; i1 < paramGlobalRequestHandler.supportedRequests().length; i1++)
      this.w.put(paramGlobalRequestHandler.supportedRequests()[i1], paramGlobalRequestHandler);
  }

  public GlobalRequestHandler getGlobalRequestHandler(String paramString)
  {
    return (GlobalRequestHandler)this.w.get(paramString);
  }

  public Class getRemoteForwardingFactoryImpl()
  {
    return this.¥;
  }

  public Class getRemoteForwardingManagerImpl()
  {
    return this.D;
  }

  public void setRemoteForwardingFactoryImpl(Class paramClass)
    throws IOException
  {
    if ((paramClass == null) || (!RemoteForwardingFactory.class.isAssignableFrom(paramClass)))
      throw new IOException("Remote forwarding channel factory impl does not implement com.maverick.sshd.RemoteForwardingChannelFactory!");
    this.¥ = paramClass;
  }

  public void setRemoteForwardingManagerImpl(Class paramClass)
    throws IOException
  {
    if ((paramClass == null) || (!RemoteForwardingManager.class.isAssignableFrom(paramClass)))
      throw new IOException("Remote forwarding manager impl does not extend com.maverick.sshd.RemoteForwardingManager!");
    this.D = paramClass;
  }

  public void setPublicKeyStore(PublicKeyStore paramPublicKeyStore)
  {
    this.V = paramPublicKeyStore;
  }

  public PublicKeyStore getPublicKeyStore()
  {
    return this.V;
  }

  public String[] getRequiredAuthentications()
  {
    String[] arrayOfString = new String[this.s.size()];
    this.s.toArray(arrayOfString);
    return arrayOfString;
  }

  public void addRequiredAuthentication(String paramString)
  {
    if (!this.s.contains(paramString))
      this.s.add(paramString);
  }

  public String getBannerMessage()
  {
    return this.¤;
  }

  public Daemon getServer()
  {
    return this.º;
  }

  public String getSFTPCharsetEncoding()
  {
    return this.g;
  }

  public void setSFTPCharsetEncoding(String paramString)
  {
    this.g = paramString;
  }

  public int getSessionTimeout()
  {
    return this.d;
  }

  public void setSessionTimeout(int paramInt)
  {
    this.d = paramInt;
  }

  public void setChannelLimit(int paramInt)
  {
    this.U = paramInt;
  }

  public int getChannelLimit()
  {
    return this.U;
  }

  public int getMaxAuthentications()
  {
    return this.v;
  }

  public void setMaxAuthentications(int paramInt)
  {
    this.v = paramInt;
  }

  public void setMaximumConnections(int paramInt)
  {
    this.t = paramInt;
  }

  public int getMaximumConnections()
  {
    return this.t;
  }

  public ComponentFactory supportedCiphersCS()
  {
    return this.o;
  }

  public ComponentFactory supportedCiphersSC()
  {
    return this.K;
  }

  public void setAsynchronousFileOperations(boolean paramBoolean)
  {
    this.L = paramBoolean;
  }

  public boolean isFileSystemAsynchronous()
  {
    return this.L;
  }

  public String getPreferredCipherCS()
  {
    return this.M;
  }

  public void setPreferredCipherCS(String paramString)
    throws IOException, SshException
  {
    if (this.o.contains(paramString))
    {
      this.M = paramString;
      setCipherPreferredPositionCS(paramString, 0);
    }
    else
    {
      throw new IOException(paramString + " is not supported");
    }
  }

  public void setAccessManager(AccessManager paramAccessManager)
  {
    this.x = paramAccessManager;
  }

  public AccessManager getAccessManager()
  {
    return this.x;
  }

  public void setBannerMessage(String paramString)
  {
    this.¤ = paramString;
  }

  public String getPreferredCipherSC()
  {
    return this.l;
  }

  public String getSoftwareVersionComments()
  {
    return this.r;
  }

  public void setSoftwareVersionComments(String paramString)
  {
    this.r = paramString;
  }

  public void setPreferredCipherSC(String paramString)
    throws IOException, SshException
  {
    if (this.K.contains(paramString))
    {
      this.l = paramString;
      setCipherPreferredPositionSC(paramString, 0);
    }
    else
    {
      throw new IOException(paramString + " is not supported");
    }
  }

  public ComponentFactory supportedMacsCS()
  {
    return this.i;
  }

  public ComponentFactory supportedMacsSC()
  {
    return this.E;
  }

  public String getPreferredMacCS()
  {
    return this.P;
  }

  public void setPreferredMacCS(String paramString)
    throws IOException, SshException
  {
    if (this.i.contains(paramString))
    {
      this.P = paramString;
      setMacPreferredPositionCS(paramString, 0);
    }
    else
    {
      throw new IOException(paramString + " is not supported");
    }
  }

  public String getPreferredMacSC()
  {
    return this.n;
  }

  public void setRemoteForwardingCancelKillsTunnels(boolean paramBoolean)
  {
    this.¢ = paramBoolean;
  }

  public boolean getRemoteForwardingCancelKillsTunnels()
  {
    return this.¢;
  }

  public int getMaximumPublicKeyVerificationAttempts()
  {
    return this.J;
  }

  public void setMaximumPublicKeyVerificationAttempts(int paramInt)
  {
    this.J = paramInt;
  }

  public void setPreferredMacSC(String paramString)
    throws IOException, SshException
  {
    if (this.E.contains(paramString))
    {
      this.n = paramString;
      setMacPreferredPositionSC(paramString, 0);
    }
    else
    {
      throw new IOException(paramString + " is not supported");
    }
  }

  public ComponentFactory supportedCompressionsCS()
  {
    return this.z;
  }

  public ComponentFactory supportedCompressionsSC()
  {
    return this.W;
  }

  public String getPreferredCompressionCS()
  {
    return this.µ;
  }

  public void setPreferredCompressionCS(String paramString)
    throws IOException
  {
    if (this.z.contains(paramString))
      this.µ = paramString;
    else
      throw new IOException(paramString + " is not supported");
  }

  public String getPreferredCompressionSC()
  {
    return this.a;
  }

  public void setPreferredCompressionSC(String paramString)
    throws IOException
  {
    if (this.W.contains(paramString))
      this.a = paramString;
    else
      throw new IOException(paramString + " is not supported");
  }

  public ComponentFactory supportedKeyExchanges()
  {
    return this.u;
  }

  public String getPreferredKeyExchange()
  {
    return this.X;
  }

  public void setPreferredKeyExchange(String paramString)
    throws IOException, SshException
  {
    if (this.u.contains(paramString))
    {
      this.X = paramString;
      setKeyExchangePreferredPosition(paramString, 0);
    }
    else
    {
      throw new IOException(paramString + " is not supported");
    }
  }

  public String getPreferredPublicKey()
  {
    if (this.Q.containsKey(this.T))
      return this.T;
    if (this.Q.entrySet().isEmpty())
      throw new RuntimeException("No host keys loaded!!");
    Map.Entry localEntry = (Map.Entry)this.Q.entrySet().iterator().next();
    return (String)localEntry.getKey();
  }

  public String getSupportedPublicKeys()
  {
    String str1 = "";
    if (this.Q.keySet().contains(this.T))
      str1 = str1 + this.T;
    Iterator localIterator = this.Q.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str2 = (String)localIterator.next();
      if (str2.equals(this.T))
        continue;
      str1 = str1 + (str1.length() == 0 ? "" : ",") + str2;
    }
    return str1;
  }

  public void setPreferredPublicKey(String paramString)
    throws IOException, SshException
  {
    if (this.£.contains(paramString))
    {
      this.T = paramString;
      setPublicKeyPreferredPosition(paramString, 0);
    }
    else
    {
      throw new IOException(paramString + " is not supported");
    }
  }

  public SshKeyPair[] getHostKeys()
  {
    SshKeyPair[] arrayOfSshKeyPair = new SshKeyPair[this.Q.size()];
    this.Q.values().toArray(arrayOfSshKeyPair);
    return arrayOfSshKeyPair;
  }

  public SshKeyPair getHostKey(String paramString)
    throws IOException
  {
    if (!this.Q.containsKey(paramString))
      throw new IOException("The server does not have a " + paramString + " key configured");
    return (SshKeyPair)this.Q.get(paramString);
  }

  public void addHostKey(SshKeyPair paramSshKeyPair)
    throws IOException
  {
    if (this.Q.containsKey(paramSshKeyPair.getPublicKey().getAlgorithm()))
      throw new IOException("The server already has a " + paramSshKeyPair.getPublicKey().getAlgorithm() + " key configured");
    this.Q.put(paramSshKeyPair.getPublicKey().getAlgorithm(), paramSshKeyPair);
  }

  public void addCommand(String paramString, Class paramClass)
  {
    this.I.put(paramString.toLowerCase(), paramClass);
  }

  public boolean containsCommand(String paramString)
  {
    return this.I.containsKey(paramString.toLowerCase());
  }

  public Class getCommand(String paramString)
  {
    return (Class)this.I.get(paramString.toLowerCase());
  }

  public Class getShellCommand()
  {
    return this.S;
  }

  public void setShellCommand(Class paramClass)
  {
    this.S = paramClass;
  }

  public boolean hasPublicKey(String paramString)
  {
    return this.Q.containsKey(paramString);
  }

  public ComponentFactory supportedAuthenticationMechanisms()
  {
    return this.q;
  }

  public void setAuthenticationProvider(AuthenticationProvider paramAuthenticationProvider)
  {
    this.p = paramAuthenticationProvider;
  }

  public AuthenticationProvider getAuthenticationProvider()
  {
    return this.p;
  }

  public void setFileSystemProvider(Class paramClass)
  {
    this.B = paramClass;
    if (this.N == null)
      this.N = paramClass;
  }

  public void setFileSystemProvider_KeyStore(Class paramClass)
  {
    this.N = paramClass;
  }

  public Class getFileSystemProvider()
  {
    return this.B;
  }

  public Class getFileSystemProvider_KeyStore()
  {
    return this.N;
  }

  public void setSessionProvider(Class paramClass)
  {
    if (paramClass != null)
      this.j.add("session", paramClass);
  }

  public String getDefaultTerminal()
  {
    return this.k;
  }

  public void setDefaultTerminal(String paramString)
  {
    this.k = paramString;
  }

  public ComponentFactory supportedChannels()
  {
    return this.j;
  }

  public ComponentFactory supportedSubsystems()
  {
    return this.m;
  }

  public void setCompressionLevel(int paramInt)
  {
    this.F = paramInt;
  }

  public int getCompressionLevel()
  {
    return this.F;
  }

  public void setAllowDeniedKEX(boolean paramBoolean)
  {
    this._ = paramBoolean;
  }

  public void setRequiredAuthenticationMethods(int paramInt)
  {
    switch (paramInt)
    {
    case 0:
      this.s.clear();
      break;
    case 1:
      this.s.clear();
      addRequiredAuthentication("publickey");
      break;
    case 2:
      this.s.clear();
      addRequiredAuthentication("password");
      break;
    case 3:
      this.s.clear();
      addRequiredAuthentication("password");
      addRequiredAuthentication("publickey");
      break;
    case 4:
      this.s.clear();
      addRequiredAuthentication("keyboard-interactive");
    }
  }

  public boolean getAllowDeniedKEX()
  {
    return this._;
  }

  public int getMaximumSocketsBacklogPerRemotelyForwardedConnection()
  {
    return this.f;
  }

  public void setMaximumSocketsBacklogPerRemotelyForwardedConnection(int paramInt)
  {
    this.f = paramInt;
  }

  public String getTooManyConnectionsText()
  {
    return this.H;
  }

  public void setTooManyConnectionsText(String paramString)
  {
    this.H = paramString;
  }

  public String getCiphersSC()
  {
    return this.K.list(this.l);
  }

  public String getCiphersCS()
  {
    return this.o.list(this.M);
  }

  public String getMacsCS()
  {
    return this.i.list(this.P);
  }

  public String getMacsSC()
  {
    return this.E.list(this.n);
  }

  public String getPublicKeys()
  {
    return this.£.list(this.T);
  }

  public String getKeyExchanges()
  {
    return this.u.list(this.X);
  }

  public void setPreferredCipherSC(int[] paramArrayOfInt)
    throws SshException
  {
    this.l = this.K.createNewOrdering(paramArrayOfInt);
  }

  public void setPreferredCipherCS(int[] paramArrayOfInt)
    throws SshException
  {
    this.M = this.o.createNewOrdering(paramArrayOfInt);
  }

  public void setPreferredMacSC(int[] paramArrayOfInt)
    throws SshException
  {
    this.l = this.E.createNewOrdering(paramArrayOfInt);
  }

  public void setPreferredMacCS(int[] paramArrayOfInt)
    throws SshException
  {
    this.l = this.i.createNewOrdering(paramArrayOfInt);
  }

  public void setCipherPreferredPositionCS(String paramString, int paramInt)
    throws SshException
  {
    this.M = this.o.changePositionofAlgorithm(paramString, paramInt);
  }

  public void setCipherPreferredPositionSC(String paramString, int paramInt)
    throws SshException
  {
    this.l = this.K.changePositionofAlgorithm(paramString, paramInt);
  }

  public void setMacPreferredPositionSC(String paramString, int paramInt)
    throws SshException
  {
    this.n = this.E.changePositionofAlgorithm(paramString, paramInt);
  }

  public void setMacPreferredPositionCS(String paramString, int paramInt)
    throws SshException
  {
    this.P = this.i.changePositionofAlgorithm(paramString, paramInt);
  }

  public void setPublicKeyPreferredPosition(String paramString, int paramInt)
    throws SshException
  {
    this.P = this.£.changePositionofAlgorithm(paramString, paramInt);
  }

  public void setKeyExchangePreferredPosition(String paramString, int paramInt)
    throws SshException
  {
    this.P = this.u.changePositionofAlgorithm(paramString, paramInt);
  }

  public void setForwardingCallback(ForwardingCallback paramForwardingCallback)
  {
    this.R = paramForwardingCallback;
  }

  public ForwardingCallback getForwardingCallback()
  {
    return this.R;
  }

  public void setMaximumPacketLength(int paramInt)
  {
    this.À = paramInt;
  }

  public int getMaximumPacketLength()
  {
    return this.À;
  }

  public void setKeyExchangeTransferLimit(int paramInt)
  {
    if (paramInt < 1024000)
      throw new IllegalArgumentException("The minimum number of bytes allowed between key exchange is 1MB (1024000 bytes)");
    this.e = paramInt;
  }

  public void setKeyExchangePacketLimit(int paramInt)
  {
    if (paramInt < 100)
      throw new IllegalArgumentException("The minimum number of packets allowed between key exchanges is 100");
    this.Y = paramInt;
  }

  public int getKeyExchangeTransferLimit()
  {
    return this.e;
  }

  public int getKeyExchangePacketLimit()
  {
    return this.Y;
  }

  public int getIdleConnectionTimeoutSeconds()
  {
    return this.C;
  }

  public void setIdleConnectionTimeoutSeconds(int paramInt)
  {
    this.C = paramInt;
  }

  public Class getKeyboardInteractiveProvider()
  {
    return this.G;
  }

  public void setKeyboardInteractiveProvider(Class paramClass)
  {
    this.G = paramClass;
  }

  public ComponentFactory supportedPublicKeys()
  {
    return this.£;
  }

  public int getKeepAliveInterval()
  {
    return this.h;
  }

  public void setKeepAliveInterval(int paramInt)
  {
    this.h = paramInt;
  }

  public int getKeepAliveDataMaxLength()
  {
    return this.b;
  }

  public void setKeepAliveDataMaxLength(int paramInt)
  {
    this.b = paramInt;
  }

  public String getKerberosRealm()
  {
    return this.ª;
  }

  public void setKerberosRealm(String paramString)
  {
    this.ª = paramString;
  }

  public String getKerberosDC()
  {
    return this.y;
  }

  public void setKerberosDC(String paramString)
  {
    this.y = paramString;
  }

  public char[] getKerberosServicePassword()
  {
    return this.c;
  }

  public void setKerberosServicePassword(char[] paramArrayOfChar)
  {
    this.c = paramArrayOfChar;
  }

  public String getKerberosServicePrincipal()
  {
    return this.O;
  }

  public void setKerberosServicePrincipal(String paramString)
  {
    this.O = paramString;
  }

  public Configuration getKerberosConfiguration()
  {
    return this.Z;
  }

  public void setKerberosConfiguration(Configuration paramConfiguration)
  {
    this.Z = paramConfiguration;
  }
}

/* Location:           C:\src\maverick-sshd\dist\maverick-sshd.jar
 * Qualified Name:     com.maverick.sshd.SshContext
 * JD-Core Version:    0.6.0
 */