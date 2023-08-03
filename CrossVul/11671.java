
package org.postgresql.core;
import org.postgresql.PGConnection;
import org.postgresql.PGProperty;
import org.postgresql.jdbc.FieldMetadata;
import org.postgresql.jdbc.TimestampUtils;
import org.postgresql.util.LruCache;
import org.postgresql.xml.PGXmlFactoryFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimerTask;
public interface BaseConnection extends PGConnection, Connection {
  void cancelQuery() throws SQLException;
  ResultSet execSQLQuery(String s) throws SQLException;
  ResultSet execSQLQuery(String s, int resultSetType, int resultSetConcurrency)
      throws SQLException;
  void execSQLUpdate(String s) throws SQLException;
  QueryExecutor getQueryExecutor();
  ReplicationProtocol getReplicationProtocol();
  Object getObject(String type, String value, byte[] byteValue) throws SQLException;
  Encoding getEncoding() throws SQLException;
  TypeInfo getTypeInfo();
  boolean haveMinimumServerVersion(int ver);
  boolean haveMinimumServerVersion(Version ver);
  byte[] encodeString(String str) throws SQLException;
  String escapeString(String str) throws SQLException;
  boolean getStandardConformingStrings();
  TimestampUtils getTimestampUtils();
  java.util.logging.Logger getLogger();
  boolean getStringVarcharFlag();
  TransactionState getTransactionState();
  boolean binaryTransferSend(int oid);
  boolean isColumnSanitiserDisabled();
  void addTimerTask(TimerTask timerTask, long milliSeconds);
  void purgeTimerTasks();
  LruCache<FieldMetadata.Key, FieldMetadata> getFieldMetadataCache();
  CachedQuery createQuery(String sql, boolean escapeProcessing, boolean isParameterized,
      String... columnNames)
      throws SQLException;
  void setFlushCacheOnDeallocate(boolean flushCacheOnDeallocate);
  boolean hintReadOnly();
  PGXmlFactoryFactory getXmlFactoryFactory() throws SQLException;
}
