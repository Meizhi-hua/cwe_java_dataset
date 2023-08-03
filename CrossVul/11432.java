
package com.facebook.thrift.protocol;
import com.facebook.thrift.TException;
import com.facebook.thrift.meta_data.FieldMetaData;
import java.util.Map;
public abstract class TProtocolDecorator extends TProtocol {
  private final TProtocol concreteProtocol;
  public TProtocolDecorator(TProtocol protocol) {
    super(protocol.getTransport());
    concreteProtocol = protocol;
  }
  public void writeMessageBegin(TMessage tMessage) throws TException {
    concreteProtocol.writeMessageBegin(tMessage);
  }
  public void writeMessageEnd() throws TException {
    concreteProtocol.writeMessageEnd();
  }
  public void writeStructBegin(TStruct tStruct) throws TException {
    concreteProtocol.writeStructBegin(tStruct);
  }
  public void writeStructEnd() throws TException {
    concreteProtocol.writeStructEnd();
  }
  public void writeFieldBegin(TField tField) throws TException {
    concreteProtocol.writeFieldBegin(tField);
  }
  public void writeFieldEnd() throws TException {
    concreteProtocol.writeFieldEnd();
  }
  public void writeFieldStop() throws TException {
    concreteProtocol.writeFieldStop();
  }
  public void writeMapBegin(TMap tMap) throws TException {
    concreteProtocol.writeMapBegin(tMap);
  }
  public void writeMapEnd() throws TException {
    concreteProtocol.writeMapEnd();
  }
  public void writeListBegin(TList tList) throws TException {
    concreteProtocol.writeListBegin(tList);
  }
  public void writeListEnd() throws TException {
    concreteProtocol.writeListEnd();
  }
  public void writeSetBegin(TSet tSet) throws TException {
    concreteProtocol.writeSetBegin(tSet);
  }
  public void writeSetEnd() throws TException {
    concreteProtocol.writeSetEnd();
  }
  public void writeBool(boolean b) throws TException {
    concreteProtocol.writeBool(b);
  }
  public void writeByte(byte b) throws TException {
    concreteProtocol.writeByte(b);
  }
  public void writeI16(short i) throws TException {
    concreteProtocol.writeI16(i);
  }
  public void writeI32(int i) throws TException {
    concreteProtocol.writeI32(i);
  }
  public void writeI64(long l) throws TException {
    concreteProtocol.writeI64(l);
  }
  public void writeFloat(float v) throws TException {
    concreteProtocol.writeFloat(v);
  }
  public void writeDouble(double v) throws TException {
    concreteProtocol.writeDouble(v);
  }
  public void writeString(String s) throws TException {
    concreteProtocol.writeString(s);
  }
  public void writeBinary(byte[] buf) throws TException {
    concreteProtocol.writeBinary(buf);
  }
  public TMessage readMessageBegin() throws TException {
    return concreteProtocol.readMessageBegin();
  }
  public void readMessageEnd() throws TException {
    concreteProtocol.readMessageEnd();
  }
  public TStruct readStructBegin(Map<Integer, FieldMetaData> m) throws TException {
    return concreteProtocol.readStructBegin(m);
  }
  public void readStructEnd() throws TException {
    concreteProtocol.readStructEnd();
  }
  public TField readFieldBegin() throws TException {
    return concreteProtocol.readFieldBegin();
  }
  public void readFieldEnd() throws TException {
    concreteProtocol.readFieldEnd();
  }
  public TMap readMapBegin() throws TException {
    return concreteProtocol.readMapBegin();
  }
  public void readMapEnd() throws TException {
    concreteProtocol.readMapEnd();
  }
  public TList readListBegin() throws TException {
    return concreteProtocol.readListBegin();
  }
  public void readListEnd() throws TException {
    concreteProtocol.readListEnd();
  }
  public TSet readSetBegin() throws TException {
    return concreteProtocol.readSetBegin();
  }
  public void readSetEnd() throws TException {
    concreteProtocol.readSetEnd();
  }
  public boolean readBool() throws TException {
    return concreteProtocol.readBool();
  }
  public byte readByte() throws TException {
    return concreteProtocol.readByte();
  }
  public short readI16() throws TException {
    return concreteProtocol.readI16();
  }
  public int readI32() throws TException {
    return concreteProtocol.readI32();
  }
  public long readI64() throws TException {
    return concreteProtocol.readI64();
  }
  public float readFloat() throws TException {
    return concreteProtocol.readFloat();
  }
  public double readDouble() throws TException {
    return concreteProtocol.readDouble();
  }
  public String readString() throws TException {
    return concreteProtocol.readString();
  }
  public byte[] readBinary() throws TException {
    return concreteProtocol.readBinary();
  }
  @Override
  protected int typeMinimumSize(byte type) {
    return concreteProtocol.typeMinimumSize(type);
  }
}
