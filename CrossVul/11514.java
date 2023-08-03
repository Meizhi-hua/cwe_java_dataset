
package org.opencastproject.mediapackage.identifier;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
@XmlJavaTypeAdapter(Id.Adapter.class)
public interface Id {
  @Deprecated
  String compact();
  class Adapter extends XmlAdapter<IdImpl, Id> {
    public IdImpl marshal(Id id) throws Exception {
      if (id instanceof IdImpl) {
        return (IdImpl) id;
      } else {
        throw new IllegalStateException("an unknown ID is un use: " + id);
      }
    }
    public Id unmarshal(IdImpl id) throws Exception {
      return id;
    }
  }
  String toString();
}
