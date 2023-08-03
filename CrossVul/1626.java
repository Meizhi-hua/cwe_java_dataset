
package org.postgresql.ds.common;
import org.postgresql.PGProperty;
import org.postgresql.jdbc.AutoSave;
import org.postgresql.jdbc.PreferQueryMode;
import org.postgresql.util.ExpressionProperties;
import org.postgresql.util.GT;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.URLCoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.sql.CommonDataSource;
public abstract class BaseDataSource implements CommonDataSource, Referenceable {
  private static final Logger LOGGER = Logger.getLogger(BaseDataSource.class.getName());
  private String[] serverNames = new String[] {"localhost"};
  private String databaseName = "";
  private String user;
  private String password;
  private int[] portNumbers = new int[] {0};
  private Properties properties = new Properties();
  static {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException(
        "BaseDataSource is unable to load org.postgresql.Driver. Please check if you have proper PostgreSQL JDBC Driver jar on the classpath",
        e);
    }
  }
  public Connection getConnection() throws SQLException {
    return getConnection(user, password);
  }
  public Connection getConnection(String user, String password) throws SQLException {
    try {
      Connection con = DriverManager.getConnection(getUrl(), user, password);
      if (LOGGER.isLoggable(Level.FINE)) {
        LOGGER.log(Level.FINE, "Created a {0} for {1} at {2}",
            new Object[] {getDescription(), user, getUrl()});
      }
      return con;
    } catch (SQLException e) {
      LOGGER.log(Level.FINE, "Failed to create a {0} for {1} at {2}: {3}",
          new Object[] {getDescription(), user, getUrl(), e});
      throw e;
    }
  }
  @Override
  public PrintWriter getLogWriter() {
    return null;
  }
  @Override
  public void setLogWriter(PrintWriter printWriter) {
  }
  @Deprecated
  public String getServerName() {
    return serverNames[0];
  }
  public String[] getServerNames() {
    return serverNames;
  }
  @Deprecated
  public void setServerName(String serverName) {
    this.setServerNames(new String[] { serverName });
  }
  public void setServerNames(String[] serverNames) {
    if (serverNames == null || serverNames.length == 0) {
      this.serverNames = new String[] {"localhost"};
    } else {
      serverNames = Arrays.copyOf(serverNames, serverNames.length);
      for (int i = 0; i < serverNames.length; i++) {
        if (serverNames[i] == null || serverNames[i].equals("")) {
          serverNames[i] = "localhost";
        }
      }
      this.serverNames = serverNames;
    }
  }
  public String getDatabaseName() {
    return databaseName;
  }
  public void setDatabaseName(String databaseName) {
    this.databaseName = databaseName;
  }
  public abstract String getDescription();
  public String getUser() {
    return user;
  }
  public void setUser(String user) {
    this.user = user;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  @Deprecated
  public int getPortNumber() {
    if (portNumbers == null || portNumbers.length == 0) {
      return 0;
    }
    return portNumbers[0];
  }
  public int[] getPortNumbers() {
    return portNumbers;
  }
  @Deprecated
  public void setPortNumber(int portNumber) {
    setPortNumbers(new int[] { portNumber });
  }
  public void setPortNumbers(int[] portNumbers) {
    if (portNumbers == null || portNumbers.length == 0) {
      portNumbers = new int[] { 0 };
    }
    this.portNumbers = Arrays.copyOf(portNumbers, portNumbers.length);
  }
  public String getOptions() {
    return PGProperty.OPTIONS.get(properties);
  }
  public void setOptions(String options) {
    PGProperty.OPTIONS.set(properties, options);
  }
  @Override
  public int getLoginTimeout() {
    return PGProperty.LOGIN_TIMEOUT.getIntNoCheck(properties);
  }
  @Override
  public void setLoginTimeout(int loginTimeout) {
    PGProperty.LOGIN_TIMEOUT.set(properties, loginTimeout);
  }
  public int getConnectTimeout() {
    return PGProperty.CONNECT_TIMEOUT.getIntNoCheck(properties);
  }
  public void setConnectTimeout(int connectTimeout) {
    PGProperty.CONNECT_TIMEOUT.set(properties, connectTimeout);
  }
  public int getProtocolVersion() {
    if (!PGProperty.PROTOCOL_VERSION.isPresent(properties)) {
      return 0;
    } else {
      return PGProperty.PROTOCOL_VERSION.getIntNoCheck(properties);
    }
  }
  public void setProtocolVersion(int protocolVersion) {
    if (protocolVersion == 0) {
      PGProperty.PROTOCOL_VERSION.set(properties, null);
    } else {
      PGProperty.PROTOCOL_VERSION.set(properties, protocolVersion);
    }
  }
  public int getReceiveBufferSize() {
    return PGProperty.RECEIVE_BUFFER_SIZE.getIntNoCheck(properties);
  }
  public void setReceiveBufferSize(int nbytes) {
    PGProperty.RECEIVE_BUFFER_SIZE.set(properties, nbytes);
  }
  public int getSendBufferSize() {
    return PGProperty.SEND_BUFFER_SIZE.getIntNoCheck(properties);
  }
  public void setSendBufferSize(int nbytes) {
    PGProperty.SEND_BUFFER_SIZE.set(properties, nbytes);
  }
  public void setPrepareThreshold(int count) {
    PGProperty.PREPARE_THRESHOLD.set(properties, count);
  }
  public int getPrepareThreshold() {
    return PGProperty.PREPARE_THRESHOLD.getIntNoCheck(properties);
  }
  public int getPreparedStatementCacheQueries() {
    return PGProperty.PREPARED_STATEMENT_CACHE_QUERIES.getIntNoCheck(properties);
  }
  public void setPreparedStatementCacheQueries(int cacheSize) {
    PGProperty.PREPARED_STATEMENT_CACHE_QUERIES.set(properties, cacheSize);
  }
  public int getPreparedStatementCacheSizeMiB() {
    return PGProperty.PREPARED_STATEMENT_CACHE_SIZE_MIB.getIntNoCheck(properties);
  }
  public void setPreparedStatementCacheSizeMiB(int cacheSize) {
    PGProperty.PREPARED_STATEMENT_CACHE_SIZE_MIB.set(properties, cacheSize);
  }
  public int getDatabaseMetadataCacheFields() {
    return PGProperty.DATABASE_METADATA_CACHE_FIELDS.getIntNoCheck(properties);
  }
  public void setDatabaseMetadataCacheFields(int cacheSize) {
    PGProperty.DATABASE_METADATA_CACHE_FIELDS.set(properties, cacheSize);
  }
  public int getDatabaseMetadataCacheFieldsMiB() {
    return PGProperty.DATABASE_METADATA_CACHE_FIELDS_MIB.getIntNoCheck(properties);
  }
  public void setDatabaseMetadataCacheFieldsMiB(int cacheSize) {
    PGProperty.DATABASE_METADATA_CACHE_FIELDS_MIB.set(properties, cacheSize);
  }
  public void setDefaultRowFetchSize(int fetchSize) {
    PGProperty.DEFAULT_ROW_FETCH_SIZE.set(properties, fetchSize);
  }
  public int getDefaultRowFetchSize() {
    return PGProperty.DEFAULT_ROW_FETCH_SIZE.getIntNoCheck(properties);
  }
  public void setUnknownLength(int unknownLength) {
    PGProperty.UNKNOWN_LENGTH.set(properties, unknownLength);
  }
  public int getUnknownLength() {
    return PGProperty.UNKNOWN_LENGTH.getIntNoCheck(properties);
  }
  public void setSocketTimeout(int seconds) {
    PGProperty.SOCKET_TIMEOUT.set(properties, seconds);
  }
  public int getSocketTimeout() {
    return PGProperty.SOCKET_TIMEOUT.getIntNoCheck(properties);
  }
  public void setCancelSignalTimeout(int seconds) {
    PGProperty.CANCEL_SIGNAL_TIMEOUT.set(properties, seconds);
  }
  public int getCancelSignalTimeout() {
    return PGProperty.CANCEL_SIGNAL_TIMEOUT.getIntNoCheck(properties);
  }
  public void setSsl(boolean enabled) {
    if (enabled) {
      PGProperty.SSL.set(properties, true);
    } else {
      PGProperty.SSL.set(properties, false);
    }
  }
  public boolean getSsl() {
    return PGProperty.SSL.getBoolean(properties) || "".equals(PGProperty.SSL.get(properties));
  }
  public void setSslfactory(String classname) {
    PGProperty.SSL_FACTORY.set(properties, classname);
  }
  public String getSslfactory() {
    return PGProperty.SSL_FACTORY.get(properties);
  }
  public String getSslMode() {
    return PGProperty.SSL_MODE.get(properties);
  }
  public void setSslMode(String mode) {
    PGProperty.SSL_MODE.set(properties, mode);
  }
  public String getSslFactoryArg() {
    return PGProperty.SSL_FACTORY_ARG.get(properties);
  }
  public void setSslFactoryArg(String arg) {
    PGProperty.SSL_FACTORY_ARG.set(properties, arg);
  }
  public String getSslHostnameVerifier() {
    return PGProperty.SSL_HOSTNAME_VERIFIER.get(properties);
  }
  public void setSslHostnameVerifier(String className) {
    PGProperty.SSL_HOSTNAME_VERIFIER.set(properties, className);
  }
  public String getSslCert() {
    return PGProperty.SSL_CERT.get(properties);
  }
  public void setSslCert(String file) {
    PGProperty.SSL_CERT.set(properties, file);
  }
  public String getSslKey() {
    return PGProperty.SSL_KEY.get(properties);
  }
  public void setSslKey(String file) {
    PGProperty.SSL_KEY.set(properties, file);
  }
  public String getSslRootCert() {
    return PGProperty.SSL_ROOT_CERT.get(properties);
  }
  public void setSslRootCert(String file) {
    PGProperty.SSL_ROOT_CERT.set(properties, file);
  }
  public String getSslPassword() {
    return PGProperty.SSL_PASSWORD.get(properties);
  }
  public void setSslPassword(String password) {
    PGProperty.SSL_PASSWORD.set(properties, password);
  }
  public String getSslPasswordCallback() {
    return PGProperty.SSL_PASSWORD_CALLBACK.get(properties);
  }
  public void setSslPasswordCallback(String className) {
    PGProperty.SSL_PASSWORD_CALLBACK.set(properties, className);
  }
  public void setApplicationName(String applicationName) {
    PGProperty.APPLICATION_NAME.set(properties, applicationName);
  }
  public String getApplicationName() {
    return PGProperty.APPLICATION_NAME.get(properties);
  }
  public void setTargetServerType(String targetServerType) {
    PGProperty.TARGET_SERVER_TYPE.set(properties, targetServerType);
  }
  public String getTargetServerType() {
    return PGProperty.TARGET_SERVER_TYPE.get(properties);
  }
  public void setLoadBalanceHosts(boolean loadBalanceHosts) {
    PGProperty.LOAD_BALANCE_HOSTS.set(properties, loadBalanceHosts);
  }
  public boolean getLoadBalanceHosts() {
    return PGProperty.LOAD_BALANCE_HOSTS.isPresent(properties);
  }
  public void setHostRecheckSeconds(int hostRecheckSeconds) {
    PGProperty.HOST_RECHECK_SECONDS.set(properties, hostRecheckSeconds);
  }
  public int getHostRecheckSeconds() {
    return PGProperty.HOST_RECHECK_SECONDS.getIntNoCheck(properties);
  }
  public void setTcpKeepAlive(boolean enabled) {
    PGProperty.TCP_KEEP_ALIVE.set(properties, enabled);
  }
  public boolean getTcpKeepAlive() {
    return PGProperty.TCP_KEEP_ALIVE.getBoolean(properties);
  }
  public void setBinaryTransfer(boolean enabled) {
    PGProperty.BINARY_TRANSFER.set(properties, enabled);
  }
  public boolean getBinaryTransfer() {
    return PGProperty.BINARY_TRANSFER.getBoolean(properties);
  }
  public void setBinaryTransferEnable(String oidList) {
    PGProperty.BINARY_TRANSFER_ENABLE.set(properties, oidList);
  }
  public String getBinaryTransferEnable() {
    return PGProperty.BINARY_TRANSFER_ENABLE.get(properties);
  }
  public void setBinaryTransferDisable(String oidList) {
    PGProperty.BINARY_TRANSFER_DISABLE.set(properties, oidList);
  }
  public String getBinaryTransferDisable() {
    return PGProperty.BINARY_TRANSFER_DISABLE.get(properties);
  }
  public String getStringType() {
    return PGProperty.STRING_TYPE.get(properties);
  }
  public void setStringType(String stringType) {
    PGProperty.STRING_TYPE.set(properties, stringType);
  }
  public boolean isColumnSanitiserDisabled() {
    return PGProperty.DISABLE_COLUMN_SANITISER.getBoolean(properties);
  }
  public boolean getDisableColumnSanitiser() {
    return PGProperty.DISABLE_COLUMN_SANITISER.getBoolean(properties);
  }
  public void setDisableColumnSanitiser(boolean disableColumnSanitiser) {
    PGProperty.DISABLE_COLUMN_SANITISER.set(properties, disableColumnSanitiser);
  }
  public String getCurrentSchema() {
    return PGProperty.CURRENT_SCHEMA.get(properties);
  }
  public void setCurrentSchema(String currentSchema) {
    PGProperty.CURRENT_SCHEMA.set(properties, currentSchema);
  }
  public boolean getReadOnly() {
    return PGProperty.READ_ONLY.getBoolean(properties);
  }
  public void setReadOnly(boolean readOnly) {
    PGProperty.READ_ONLY.set(properties, readOnly);
  }
  public String getReadOnlyMode() {
    return PGProperty.READ_ONLY_MODE.get(properties);
  }
  public void setReadOnlyMode(String mode) {
    PGProperty.READ_ONLY_MODE.set(properties, mode);
  }
  public boolean getLogUnclosedConnections() {
    return PGProperty.LOG_UNCLOSED_CONNECTIONS.getBoolean(properties);
  }
  public void setLogUnclosedConnections(boolean enabled) {
    PGProperty.LOG_UNCLOSED_CONNECTIONS.set(properties, enabled);
  }
  public boolean getLogServerErrorDetail() {
    return PGProperty.LOG_SERVER_ERROR_DETAIL.getBoolean(properties);
  }
  public void setLogServerErrorDetail(boolean enabled) {
    PGProperty.LOG_SERVER_ERROR_DETAIL.set(properties, enabled);
  }
  public String getAssumeMinServerVersion() {
    return PGProperty.ASSUME_MIN_SERVER_VERSION.get(properties);
  }
  public void setAssumeMinServerVersion(String minVersion) {
    PGProperty.ASSUME_MIN_SERVER_VERSION.set(properties, minVersion);
  }
  public String getJaasApplicationName() {
    return PGProperty.JAAS_APPLICATION_NAME.get(properties);
  }
  public void setJaasApplicationName(String name) {
    PGProperty.JAAS_APPLICATION_NAME.set(properties, name);
  }
  public boolean getJaasLogin() {
    return PGProperty.JAAS_LOGIN.getBoolean(properties);
  }
  public void setJaasLogin(boolean doLogin) {
    PGProperty.JAAS_LOGIN.set(properties, doLogin);
  }
  public String getKerberosServerName() {
    return PGProperty.KERBEROS_SERVER_NAME.get(properties);
  }
  public void setKerberosServerName(String serverName) {
    PGProperty.KERBEROS_SERVER_NAME.set(properties, serverName);
  }
  public boolean getUseSpNego() {
    return PGProperty.USE_SPNEGO.getBoolean(properties);
  }
  public void setUseSpNego(boolean use) {
    PGProperty.USE_SPNEGO.set(properties, use);
  }
  public String getGssLib() {
    return PGProperty.GSS_LIB.get(properties);
  }
  public void setGssLib(String lib) {
    PGProperty.GSS_LIB.set(properties, lib);
  }
  public String getSspiServiceClass() {
    return PGProperty.SSPI_SERVICE_CLASS.get(properties);
  }
  public void setSspiServiceClass(String serviceClass) {
    PGProperty.SSPI_SERVICE_CLASS.set(properties, serviceClass);
  }
  public boolean getAllowEncodingChanges() {
    return PGProperty.ALLOW_ENCODING_CHANGES.getBoolean(properties);
  }
  public void setAllowEncodingChanges(boolean allow) {
    PGProperty.ALLOW_ENCODING_CHANGES.set(properties, allow);
  }
  public String getSocketFactory() {
    return PGProperty.SOCKET_FACTORY.get(properties);
  }
  public void setSocketFactory(String socketFactoryClassName) {
    PGProperty.SOCKET_FACTORY.set(properties, socketFactoryClassName);
  }
  public String getSocketFactoryArg() {
    return PGProperty.SOCKET_FACTORY_ARG.get(properties);
  }
  public void setSocketFactoryArg(String socketFactoryArg) {
    PGProperty.SOCKET_FACTORY_ARG.set(properties, socketFactoryArg);
  }
  public void setReplication(String replication) {
    PGProperty.REPLICATION.set(properties, replication);
  }
  public String getEscapeSyntaxCallMode() {
    return PGProperty.ESCAPE_SYNTAX_CALL_MODE.get(properties);
  }
  public void setEscapeSyntaxCallMode(String callMode) {
    PGProperty.ESCAPE_SYNTAX_CALL_MODE.set(properties, callMode);
  }
  public String getReplication() {
    return PGProperty.REPLICATION.get(properties);
  }
  public String getLoggerLevel() {
    return PGProperty.LOGGER_LEVEL.get(properties);
  }
  public void setLoggerLevel(String loggerLevel) {
    PGProperty.LOGGER_LEVEL.set(properties, loggerLevel);
  }
  public String getLoggerFile() {
    ExpressionProperties exprProps = new ExpressionProperties(properties, System.getProperties());
    return PGProperty.LOGGER_FILE.get(exprProps);
  }
  public void setLoggerFile(String loggerFile) {
    PGProperty.LOGGER_FILE.set(properties, loggerFile);
  }
  public String getUrl() {
    StringBuilder url = new StringBuilder(100);
    url.append("jdbc:postgresql:
    for (int i = 0; i < serverNames.length; i++) {
      if (i > 0) {
        url.append(",");
      }
      url.append(serverNames[i]);
      if (portNumbers != null && portNumbers.length >= i && portNumbers[i] != 0) {
        url.append(":").append(portNumbers[i]);
      }
    }
    url.append("/").append(URLCoder.encode(databaseName));
    StringBuilder query = new StringBuilder(100);
    for (PGProperty property : PGProperty.values()) {
      if (property.isPresent(properties)) {
        if (query.length() != 0) {
          query.append("&");
        }
        query.append(property.getName());
        query.append("=");
        query.append(URLCoder.encode(property.get(properties)));
      }
    }
    if (query.length() > 0) {
      url.append("?");
      url.append(query);
    }
    return url.toString();
  }
  public String getURL() {
    return getUrl();
  }
  public void setUrl(String url) {
    Properties p = org.postgresql.Driver.parseURL(url, null);
    if (p == null) {
      throw new IllegalArgumentException("URL invalid " + url);
    }
    for (PGProperty property : PGProperty.values()) {
      if (!this.properties.containsKey(property.getName())) {
        setProperty(property, property.get(p));
      }
    }
  }
  public void setURL(String url) {
    setUrl(url);
  }
  public String getProperty(String name) throws SQLException {
    PGProperty pgProperty = PGProperty.forName(name);
    if (pgProperty != null) {
      return getProperty(pgProperty);
    } else {
      throw new PSQLException(GT.tr("Unsupported property name: {0}", name),
        PSQLState.INVALID_PARAMETER_VALUE);
    }
  }
  public void setProperty(String name, String value) throws SQLException {
    PGProperty pgProperty = PGProperty.forName(name);
    if (pgProperty != null) {
      setProperty(pgProperty, value);
    } else {
      throw new PSQLException(GT.tr("Unsupported property name: {0}", name),
        PSQLState.INVALID_PARAMETER_VALUE);
    }
  }
  public String getProperty(PGProperty property) {
    return property.get(properties);
  }
  public void setProperty(PGProperty property, String value) {
    if (value == null) {
      return;
    }
    switch (property) {
      case PG_HOST:
        setServerNames(value.split(","));
        break;
      case PG_PORT:
        String[] ps = value.split(",");
        int[] ports = new int[ps.length];
        for (int i = 0 ; i < ps.length; i++) {
          try {
            ports[i] = Integer.parseInt(ps[i]);
          } catch (NumberFormatException e) {
            ports[i] = 0;
          }
        }
        setPortNumbers(ports);
        break;
      case PG_DBNAME:
        setDatabaseName(value);
        break;
      case USER:
        setUser(value);
        break;
      case PASSWORD:
        setPassword(value);
        break;
      default:
        properties.setProperty(property.getName(), value);
    }
  }
  protected Reference createReference() {
    return new Reference(getClass().getName(), PGObjectFactory.class.getName(), null);
  }
  public Reference getReference() throws NamingException {
    Reference ref = createReference();
    StringBuilder serverString = new StringBuilder();
    for (int i = 0; i < serverNames.length; i++) {
      if (i > 0) {
        serverString.append(",");
      }
      String serverName = serverNames[i];
      serverString.append(serverName);
    }
    ref.add(new StringRefAddr("serverName", serverString.toString()));
    StringBuilder portString = new StringBuilder();
    for (int i = 0; i < portNumbers.length; i++) {
      if (i > 0) {
        portString.append(",");
      }
      int p = portNumbers[i];
      portString.append(Integer.toString(p));
    }
    ref.add(new StringRefAddr("portNumber", portString.toString()));
    ref.add(new StringRefAddr("databaseName", databaseName));
    if (user != null) {
      ref.add(new StringRefAddr("user", user));
    }
    if (password != null) {
      ref.add(new StringRefAddr("password", password));
    }
    for (PGProperty property : PGProperty.values()) {
      if (property.isPresent(properties)) {
        ref.add(new StringRefAddr(property.getName(), property.get(properties)));
      }
    }
    return ref;
  }
  public void setFromReference(Reference ref) {
    databaseName = getReferenceProperty(ref, "databaseName");
    String portNumberString = getReferenceProperty(ref, "portNumber");
    if (portNumberString != null) {
      String[] ps = portNumberString.split(",");
      int[] ports = new int[ps.length];
      for (int i = 0; i < ps.length; i++) {
        try {
          ports[i] = Integer.parseInt(ps[i]);
        } catch (NumberFormatException e) {
          ports[i] = 0;
        }
      }
      setPortNumbers(ports);
    } else {
      setPortNumbers(null);
    }
    setServerNames(getReferenceProperty(ref, "serverName").split(","));
    for (PGProperty property : PGProperty.values()) {
      setProperty(property, getReferenceProperty(ref, property.getName()));
    }
  }
  private static String getReferenceProperty(Reference ref, String propertyName) {
    RefAddr addr = ref.get(propertyName);
    if (addr == null) {
      return null;
    }
    return (String) addr.getContent();
  }
  protected void writeBaseObject(ObjectOutputStream out) throws IOException {
    out.writeObject(serverNames);
    out.writeObject(databaseName);
    out.writeObject(user);
    out.writeObject(password);
    out.writeObject(portNumbers);
    out.writeObject(properties);
  }
  protected void readBaseObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    serverNames = (String[]) in.readObject();
    databaseName = (String) in.readObject();
    user = (String) in.readObject();
    password = (String) in.readObject();
    portNumbers = (int[]) in.readObject();
    properties = (Properties) in.readObject();
  }
  public void initializeFrom(BaseDataSource source) throws IOException, ClassNotFoundException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    source.writeBaseObject(oos);
    oos.close();
    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(bais);
    readBaseObject(ois);
  }
  public PreferQueryMode getPreferQueryMode() {
    return PreferQueryMode.of(PGProperty.PREFER_QUERY_MODE.get(properties));
  }
  public void setPreferQueryMode(PreferQueryMode preferQueryMode) {
    PGProperty.PREFER_QUERY_MODE.set(properties, preferQueryMode.value());
  }
  public AutoSave getAutosave() {
    return AutoSave.of(PGProperty.AUTOSAVE.get(properties));
  }
  public void setAutosave(AutoSave autoSave) {
    PGProperty.AUTOSAVE.set(properties, autoSave.value());
  }
  public boolean getCleanupSavepoints() {
    return PGProperty.CLEANUP_SAVEPOINTS.getBoolean(properties);
  }
  public void setCleanupSavepoints(boolean cleanupSavepoints) {
    PGProperty.CLEANUP_SAVEPOINTS.set(properties, cleanupSavepoints);
  }
  public boolean getReWriteBatchedInserts() {
    return PGProperty.REWRITE_BATCHED_INSERTS.getBoolean(properties);
  }
  public void setReWriteBatchedInserts(boolean reWrite) {
    PGProperty.REWRITE_BATCHED_INSERTS.set(properties, reWrite);
  }
  public boolean getHideUnprivilegedObjects() {
    return PGProperty.HIDE_UNPRIVILEGED_OBJECTS.getBoolean(properties);
  }
  public void setHideUnprivilegedObjects(boolean hideUnprivileged) {
    PGProperty.HIDE_UNPRIVILEGED_OBJECTS.set(properties, hideUnprivileged);
  }
  public String getMaxResultBuffer() {
    return PGProperty.MAX_RESULT_BUFFER.get(properties);
  }
  public void setMaxResultBuffer(String maxResultBuffer) {
    PGProperty.MAX_RESULT_BUFFER.set(properties, maxResultBuffer);
  }
  @Override
  public java.util.logging.Logger getParentLogger() {
    return Logger.getLogger("org.postgresql");
  }
  public boolean isSsl() {
    return getSsl();
  }
  public String getSslfactoryarg() {
    return getSslFactoryArg();
  }
  public void setSslfactoryarg(final String arg) {
    setSslFactoryArg(arg);
  }
  public String getSslcert() {
    return getSslCert();
  }
  public void setSslcert(final String file) {
    setSslCert(file);
  }
  public String getSslmode() {
    return getSslMode();
  }
  public void setSslmode(final String mode) {
    setSslMode(mode);
  }
  public String getSslhostnameverifier() {
    return getSslHostnameVerifier();
  }
  public void setSslhostnameverifier(final String className) {
    setSslHostnameVerifier(className);
  }
  public String getSslkey() {
    return getSslKey();
  }
  public void setSslkey(final String file) {
    setSslKey(file);
  }
  public String getSslrootcert() {
    return getSslRootCert();
  }
  public void setSslrootcert(final String file) {
    setSslRootCert(file);
  }
  public String getSslpasswordcallback() {
    return getSslPasswordCallback();
  }
  public void setSslpasswordcallback(final String className) {
    setSslPasswordCallback(className);
  }
  public String getSslpassword() {
    return getSslPassword();
  }
  public void setSslpassword(final String sslpassword) {
    setSslPassword(sslpassword);
  }
  public int getRecvBufferSize() {
    return getReceiveBufferSize();
  }
  public void setRecvBufferSize(final int nbytes) {
    setReceiveBufferSize(nbytes);
  }
  public boolean isAllowEncodingChanges() {
    return getAllowEncodingChanges();
  }
  public boolean isLogUnclosedConnections() {
    return getLogUnclosedConnections();
  }
  public boolean isTcpKeepAlive() {
    return getTcpKeepAlive();
  }
  public boolean isReadOnly() {
    return getReadOnly();
  }
  public boolean isDisableColumnSanitiser() {
    return getDisableColumnSanitiser();
  }
  public boolean isLoadBalanceHosts() {
    return getLoadBalanceHosts();
  }
  public boolean isCleanupSavePoints() {
    return getCleanupSavepoints();
  }
  public void setCleanupSavePoints(final boolean cleanupSavepoints) {
    setCleanupSavepoints(cleanupSavepoints);
  }
  public boolean isReWriteBatchedInserts() {
    return getReWriteBatchedInserts();
  }
}
