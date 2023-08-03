
package io.netty.channel.unix.tests;
import io.netty.channel.unix.DomainSocketAddress;
import io.netty.channel.unix.Socket;
import io.netty.util.internal.PlatformDependent;
import java.io.File;
import java.io.IOException;
public final class UnixTestUtils {
    public static DomainSocketAddress newSocketAddress() {
        try {
            File file;
            do {
                file = PlatformDependent.createTempFile("NETTY", "UDS", null);
                if (!file.delete()) {
                    throw new IOException("failed to delete: " + file);
                }
            } while (file.getAbsolutePath().length() > 128);
            return new DomainSocketAddress(file);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
    private UnixTestUtils() { }
}
