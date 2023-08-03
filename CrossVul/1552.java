
package io.vertx.test.core;
import io.vertx.core.MultiMap;
import io.vertx.core.http.impl.headers.VertxHttpHeaders;
public class VertxHttpHeadersTest extends CaseInsensitiveHeadersTest {
  @Override
  protected MultiMap newMultiMap() {
    return new VertxHttpHeaders();
  }
}
