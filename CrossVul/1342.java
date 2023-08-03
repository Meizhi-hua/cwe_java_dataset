
package org.zeroturnaround.zip;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.zip.commons.FileUtils;
import org.zeroturnaround.zip.commons.FilenameUtils;
import org.zeroturnaround.zip.commons.IOUtils;
import org.zeroturnaround.zip.transform.ZipEntryTransformer;
import org.zeroturnaround.zip.transform.ZipEntryTransformerEntry;
public final class ZipUtil {
  private static final String PATH_SEPARATOR = "/";
  public static final int DEFAULT_COMPRESSION_LEVEL = Deflater.DEFAULT_COMPRESSION;
  private static final Logger log = LoggerFactory.getLogger("org/zeroturnaround/zip/ZipUtil".replace('/', '.')); 
  private ZipUtil() {
  }
  public static boolean containsEntry(File zip, String name) {
    ZipFile zf = null;
    try {
      zf = new ZipFile(zip);
      return zf.getEntry(name) != null;
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
    finally {
      closeQuietly(zf);
    }
  }
  @Deprecated
  public static int getCompressionLevelOfEntry(File zip, String name) {
    return getCompressionMethodOfEntry(zip, name);
  }
  public static int getCompressionMethodOfEntry(File zip, String name) {
    ZipFile zf = null;
    try {
      zf = new ZipFile(zip);
      ZipEntry zipEntry = zf.getEntry(name);
      if (zipEntry == null) {
        return -1;
      }
      return zipEntry.getMethod();
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
    finally {
      closeQuietly(zf);
    }
  }
  public static boolean containsAnyEntry(File zip, String[] names) {
    ZipFile zf = null;
    try {
      zf = new ZipFile(zip);
      for (int i = 0; i < names.length; i++) {
        if (zf.getEntry(names[i]) != null) {
          return true;
        }
      }
      return false;
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
    finally {
      closeQuietly(zf);
    }
  }
  public static byte[] unpackEntry(File zip, String name) {
    ZipFile zf = null;
    try {
      zf = new ZipFile(zip);
      return doUnpackEntry(zf, name);
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
    finally {
      closeQuietly(zf);
    }
  }
  public static byte[] unpackEntry(File zip, String name, Charset charset) {
    ZipFile zf = null;
    try {
      if (charset != null) {
        zf = new ZipFile(zip, charset);
      }
      else {
        zf = new ZipFile(zip);
      }
      return doUnpackEntry(zf, name);
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
    finally {
      closeQuietly(zf);
    }
  }
  public static byte[] unpackEntry(ZipFile zf, String name) {
    try {
      return doUnpackEntry(zf, name);
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
  }
  private static byte[] doUnpackEntry(ZipFile zf, String name) throws IOException {
    ZipEntry ze = zf.getEntry(name);
    if (ze == null) {
      return null; 
    }
    InputStream is = zf.getInputStream(ze);
    try {
      return IOUtils.toByteArray(is);
    }
    finally {
      IOUtils.closeQuietly(is);
    }
  }
  public static byte[] unpackEntry(InputStream is, String name) {
    ByteArrayUnpacker action = new ByteArrayUnpacker();
    if (!handle(is, name, action))
      return null; 
    return action.getBytes();
  }
  private static class ByteArrayUnpacker implements ZipEntryCallback {
    private byte[] bytes;
    public void process(InputStream in, ZipEntry zipEntry) throws IOException {
      bytes = IOUtils.toByteArray(in);
    }
    public byte[] getBytes() {
      return bytes;
    }
  }
  public static boolean unpackEntry(File zip, String name, File file) {
    return unpackEntry(zip, name, file, null);
  }
  public static boolean unpackEntry(File zip, String name, File file, Charset charset) {
    ZipFile zf = null;
    try {
      if (charset != null) {
        zf = new ZipFile(zip, charset);
      }
      else {
        zf = new ZipFile(zip);
      }
      return doUnpackEntry(zf, name, file);
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
    finally {
      closeQuietly(zf);
    }
  }
  public static boolean unpackEntry(ZipFile zf, String name, File file) {
    try {
      return doUnpackEntry(zf, name, file);
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
  }
  private static boolean doUnpackEntry(ZipFile zf, String name, File file) throws IOException {
    if (log.isTraceEnabled()) {
      log.trace("Extracting '" + zf.getName() + "' entry '" + name + "' into '" + file + "'.");
    }
    ZipEntry ze = zf.getEntry(name);
    if (ze == null) {
      return false; 
    }
    if (ze.isDirectory() || zf.getInputStream(ze) == null) {
      if (file.isDirectory()) {
        return true;
      }
      if (file.exists()) {
        FileUtils.forceDelete(file);
      }
      return file.mkdirs();
    }
    InputStream in = new BufferedInputStream(zf.getInputStream(ze));
    try {
      FileUtils.copy(in, file);
    }
    finally {
      IOUtils.closeQuietly(in);
    }
    return true;
  }
  public static boolean unpackEntry(InputStream is, String name, File file) throws IOException {
    return handle(is, name, new FileUnpacker(file));
  }
  private static class FileUnpacker implements ZipEntryCallback {
    private final File file;
    public FileUnpacker(File file) {
      this.file = file;
    }
    public void process(InputStream in, ZipEntry zipEntry) throws IOException {
      FileUtils.copy(in, file);
    }
  }
  public static void iterate(File zip, ZipEntryCallback action) {
    iterate(zip, action, null);
  }
  public static void iterate(File zip, ZipEntryCallback action, Charset charset) {
    ZipFile zf = null;
    try {
      if (charset == null) {
        zf = new ZipFile(zip);
      }
      else {
        zf = new ZipFile(zip, charset);
      }
      Enumeration<? extends ZipEntry> en = zf.entries();
      while (en.hasMoreElements()) {
        ZipEntry e = (ZipEntry) en.nextElement();
        InputStream is = zf.getInputStream(e);
        try {
          action.process(is, e);
        }
        catch (IOException ze) {
          throw new ZipException("Failed to process zip entry '" + e.getName() + "' with action " + action, ze);
        }
        catch (ZipBreakException ex) {
          break;
        }
        finally {
          IOUtils.closeQuietly(is);
        }
      }
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
    finally {
      closeQuietly(zf);
    }
  }
  public static void iterate(File zip, String[] entryNames, ZipEntryCallback action) {
    iterate(zip, entryNames, action, null);
  }
  public static void iterate(File zip, String[] entryNames, ZipEntryCallback action, Charset charset) {
    ZipFile zf = null;
    try {
      if (charset == null) {
        zf = new ZipFile(zip);
      }
      else {
        zf = new ZipFile(zip, charset);
      }
      for (int i = 0; i < entryNames.length; i++) {
        ZipEntry e = zf.getEntry(entryNames[i]);
        if (e == null) {
          continue;
        }
        InputStream is = zf.getInputStream(e);
        try {
          action.process(is, e);
        }
        catch (IOException ze) {
          throw new ZipException("Failed to process zip entry '" + e.getName() + " with action " + action, ze);
        }
        catch (ZipBreakException ex) {
          break;
        }
        finally {
          IOUtils.closeQuietly(is);
        }
      }
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
    finally {
      closeQuietly(zf);
    }
  }
  public static void iterate(File zip, ZipInfoCallback action) {
    ZipFile zf = null;
    try {
      zf = new ZipFile(zip);
      Enumeration<? extends ZipEntry> en = zf.entries();
      while (en.hasMoreElements()) {
        ZipEntry e = (ZipEntry) en.nextElement();
        try {
          action.process(e);
        }
        catch (IOException ze) {
          throw new ZipException("Failed to process zip entry '" + e.getName() + " with action " + action, ze);
        }
        catch (ZipBreakException ex) {
          break;
        }
      }
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
    finally {
      closeQuietly(zf);
    }
  }
  public static void iterate(File zip, String[] entryNames, ZipInfoCallback action) {
    ZipFile zf = null;
    try {
      zf = new ZipFile(zip);
      for (int i = 0; i < entryNames.length; i++) {
        ZipEntry e = zf.getEntry(entryNames[i]);
        if (e == null) {
          continue;
        }
        try {
          action.process(e);
        }
        catch (IOException ze) {
          throw new ZipException("Failed to process zip entry '" + e.getName() + " with action " + action, ze);
        }
        catch (ZipBreakException ex) {
          break;
        }
      }
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
    finally {
      closeQuietly(zf);
    }
  }
  public static void iterate(InputStream is, ZipEntryCallback action, Charset charset) {
    try {
      ZipInputStream in = null;
      try {
        in = newCloseShieldZipInputStream(is, charset);
        ZipEntry entry;
        while ((entry = in.getNextEntry()) != null) {
          try {
            action.process(in, entry);
          }
          catch (IOException ze) {
            throw new ZipException("Failed to process zip entry '" + entry.getName() + " with action " + action, ze);
          }
          catch (ZipBreakException ex) {
            break;
          }
        }
      }
      finally {
        if (in != null) {
          in.close();
        }
      }
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
  }
  public static void iterate(InputStream is, ZipEntryCallback action) {
    iterate(is, action, null);
  }
  public static void iterate(InputStream is, String[] entryNames, ZipEntryCallback action, Charset charset) {
    Set<String> namesSet = new HashSet<String>();
    for (int i = 0; i < entryNames.length; i++) {
      namesSet.add(entryNames[i]);
    }
    try {
      ZipInputStream in = null;
      try {
        in = newCloseShieldZipInputStream(is, charset);
        ZipEntry entry;
        while ((entry = in.getNextEntry()) != null) {
          if (!namesSet.contains(entry.getName())) {
            continue;
          }
          try {
            action.process(in, entry);
          }
          catch (IOException ze) {
            throw new ZipException("Failed to process zip entry '" + entry.getName() + " with action " + action, ze);
          }
          catch (ZipBreakException ex) {
            break;
          }
        }
      }
      finally {
        if (in != null) {
          in.close();
        }
      }
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
  }
  public static void iterate(InputStream is, String[] entryNames, ZipEntryCallback action) {
    iterate(is, entryNames, action, null);
  }
  private static ZipInputStream newCloseShieldZipInputStream(final InputStream is, Charset charset) {
    InputStream in = new BufferedInputStream(new CloseShieldInputStream(is));
    if (charset == null) {
      return new ZipInputStream(in);
    }
    return ZipFileUtil.createZipInputStream(in, charset);
  }
  public static boolean handle(File zip, String name, ZipEntryCallback action) {
    ZipFile zf = null;
    try {
      zf = new ZipFile(zip);
      ZipEntry ze = zf.getEntry(name);
      if (ze == null) {
        return false; 
      }
      InputStream in = new BufferedInputStream(zf.getInputStream(ze));
      try {
        action.process(in, ze);
      }
      finally {
        IOUtils.closeQuietly(in);
      }
      return true;
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
    finally {
      closeQuietly(zf);
    }
  }
  public static boolean handle(InputStream is, String name, ZipEntryCallback action) {
    SingleZipEntryCallback helper = new SingleZipEntryCallback(name, action);
    iterate(is, helper);
    return helper.found();
  }
  private static class SingleZipEntryCallback implements ZipEntryCallback {
    private final String name;
    private final ZipEntryCallback action;
    private boolean found;
    public SingleZipEntryCallback(String name, ZipEntryCallback action) {
      this.name = name;
      this.action = action;
    }
    public void process(InputStream in, ZipEntry zipEntry) throws IOException {
      if (name.equals(zipEntry.getName())) {
        found = true;
        action.process(in, zipEntry);
      }
    }
    public boolean found() {
      return found;
    }
  }
  public static void unpack(File zip, final File outputDir) {
    unpack(zip, outputDir, IdentityNameMapper.INSTANCE);
  }
  public static void unpack(File zip, final File outputDir, Charset charset) {
    unpack(zip, outputDir, IdentityNameMapper.INSTANCE, charset);
  }
  public static void unpack(File zip, File outputDir, NameMapper mapper, Charset charset) {
    log.debug("Extracting '{}' into '{}'.", zip, outputDir);
    iterate(zip, new Unpacker(outputDir, mapper), charset);
  }
  public static void unpack(File zip, File outputDir, NameMapper mapper) {
    log.debug("Extracting '{}' into '{}'.", zip, outputDir);
    iterate(zip, new Unpacker(outputDir, mapper));
  }
  public static void unwrap(File zip, final File outputDir) {
    unwrap(zip, outputDir, IdentityNameMapper.INSTANCE);
  }
  public static void unwrap(File zip, File outputDir, NameMapper mapper) {
    log.debug("Unwrapping '{}' into '{}'.", zip, outputDir);
    iterate(zip, new Unwraper(outputDir, mapper));
  }
  public static void unpack(InputStream is, File outputDir) {
    unpack(is, outputDir, IdentityNameMapper.INSTANCE, null);
  }
  public static void unpack(InputStream is, File outputDir, Charset charset) {
    unpack(is, outputDir, IdentityNameMapper.INSTANCE, charset);
  }
  public static void unpack(InputStream is, File outputDir, NameMapper mapper) {
    unpack(is, outputDir, mapper, null);
  }
  public static void unpack(InputStream is, File outputDir, NameMapper mapper, Charset charset) {
    log.debug("Extracting {} into '{}'.", is, outputDir);
    iterate(is, new Unpacker(outputDir, mapper), charset);
  }
  public static void unwrap(InputStream is, File outputDir) {
    unwrap(is, outputDir, IdentityNameMapper.INSTANCE);
  }
  public static void unwrap(InputStream is, File outputDir, NameMapper mapper) {
    log.debug("Unwrapping {} into '{}'.", is, outputDir);
    iterate(is, new Unwraper(outputDir, mapper));
  }
  private static class Unpacker implements ZipEntryCallback {
    private final File outputDir;
    private final NameMapper mapper;
    public Unpacker(File outputDir, NameMapper mapper) {
      this.outputDir = outputDir;
      this.mapper = mapper;
    }
    public void process(InputStream in, ZipEntry zipEntry) throws IOException {
      String name = mapper.map(zipEntry.getName());
      if (name != null) {
        File file = new File(outputDir, name);
        if (zipEntry.isDirectory()) {
          FileUtils.forceMkdir(file);
        }
        else {
          FileUtils.forceMkdir(file.getParentFile());
          if (log.isDebugEnabled() && file.exists()) {
            log.debug("Overwriting file '{}'.", zipEntry.getName());
          }
          FileUtils.copy(in, file);
        }
        ZTFilePermissions permissions = ZipEntryUtil.getZTFilePermissions(zipEntry);
        if (permissions != null) {
          ZTFilePermissionsUtil.getDefaultStategy().setPermissions(file, permissions);
        }
      }
    }
  }
  public static class BackslashUnpacker implements ZipEntryCallback {
    private final File outputDir;
    private final NameMapper mapper;
    public BackslashUnpacker(File outputDir, NameMapper mapper) {
      this.outputDir = outputDir;
      this.mapper = mapper;
    }
    public BackslashUnpacker(File outputDir) {
      this(outputDir, IdentityNameMapper.INSTANCE);
    }
    public void process(InputStream in, ZipEntry zipEntry) throws IOException {
      String name = mapper.map(zipEntry.getName());
      if (name != null) {
        if (name.indexOf('\\') != -1) {
          File parentDirectory = outputDir;
          String[] dirs = name.split("\\\\");
          for (int i = 0; i < dirs.length - 1; i++) {
            File file = new File(parentDirectory, dirs[i]);
            if (!file.exists()) {
              FileUtils.forceMkdir(file);
            }
            parentDirectory = file;
          }
          File destFile = new File(parentDirectory, dirs[dirs.length - 1]);
          FileUtils.copy(in, destFile);
        }
        else {
          File destFile = new File(outputDir, name);
          FileUtils.copy(in, destFile);
        }
      }
    }
  }
  private static class Unwraper implements ZipEntryCallback {
    private final File outputDir;
    private final NameMapper mapper;
    private String rootDir;
    public Unwraper(File outputDir, NameMapper mapper) {
      this.outputDir = outputDir;
      this.mapper = mapper;
    }
    public void process(InputStream in, ZipEntry zipEntry) throws IOException {
      String root = getRootName(zipEntry.getName());
      if (rootDir == null) {
        rootDir = root;
      }
      else if (!rootDir.equals(root)) {
        throw new ZipException("Unwrapping with multiple roots is not supported, roots: " + rootDir + ", " + root);
      }
      String name = mapper.map(getUnrootedName(root, zipEntry.getName()));
      if (name != null) {
        File file = new File(outputDir, name);
        if (zipEntry.isDirectory()) {
          FileUtils.forceMkdir(file);
        }
        else {
          FileUtils.forceMkdir(file.getParentFile());
          if (log.isDebugEnabled() && file.exists()) {
            log.debug("Overwriting file '{}'.", zipEntry.getName());
          }
          FileUtils.copy(in, file);
        }
      }
    }
    private String getUnrootedName(String root, String name) {
      return name.substring(root.length());
    }
    private String getRootName(final String name) {
      String newName = name.substring(FilenameUtils.getPrefixLength(name));
      int idx = newName.indexOf(PATH_SEPARATOR);
      if (idx < 0) {
        throw new ZipException("Entry " + newName + " from the root of the zip is not supported");
      }
      return newName.substring(0, newName.indexOf(PATH_SEPARATOR));
    }
  }
  public static void explode(File zip) {
    try {
      File tempFile = FileUtils.getTempFileFor(zip);
      FileUtils.moveFile(zip, tempFile);
      unpack(tempFile, zip);
      if (!tempFile.delete()) {
        throw new IOException("Unable to delete file: " + tempFile);
      }
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
  }
  public static byte[] packEntry(File file) {
    log.trace("Compressing '{}' into a ZIP file with single entry.", file);
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    try {
      ZipOutputStream out = new ZipOutputStream(result);
      ZipEntry entry = ZipEntryUtil.fromFile(file.getName(), file);
      InputStream in = new BufferedInputStream(new FileInputStream(file));
      try {
        ZipEntryUtil.addEntry(entry, in, out);
      }
      finally {
        IOUtils.closeQuietly(in);
      }
      out.close();
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
    return result.toByteArray();
  }
  public static void pack(File rootDir, File zip) {
    pack(rootDir, zip, DEFAULT_COMPRESSION_LEVEL);
  }
  public static void pack(File rootDir, File zip, int compressionLevel) {
    pack(rootDir, zip, IdentityNameMapper.INSTANCE, compressionLevel);
  }
  public static void pack(final File sourceDir, final File targetZipFile, final boolean preserveRoot) {
    if (preserveRoot) {
      final String parentName = sourceDir.getName();
      pack(sourceDir, targetZipFile, new NameMapper() {
        public String map(String name) {
          return parentName + PATH_SEPARATOR + name;
        }
      });
    }
    else {
      pack(sourceDir, targetZipFile);
    }
  }
  public static void packEntry(File fileToPack, File destZipFile) {
    packEntry(fileToPack, destZipFile, IdentityNameMapper.INSTANCE);
  }
  public static void packEntry(File fileToPack, File destZipFile, final String fileName) {
    packEntry(fileToPack, destZipFile, new NameMapper() {
      public String map(String name) {
        return fileName;
      }
    });
  }
  public static void packEntry(File fileToPack, File destZipFile, NameMapper mapper) {
    packEntries(new File[] { fileToPack }, destZipFile, mapper);
  }
  public static void packEntries(File[] filesToPack, File destZipFile) {
    packEntries(filesToPack, destZipFile, IdentityNameMapper.INSTANCE);
  }
  public static void packEntries(File[] filesToPack, File destZipFile, NameMapper mapper) {
    packEntries(filesToPack, destZipFile, mapper, DEFAULT_COMPRESSION_LEVEL);
  }
  public static void packEntries(File[] filesToPack, File destZipFile, int compressionLevel) {
    packEntries(filesToPack, destZipFile, IdentityNameMapper.INSTANCE, compressionLevel);
  }
  public static void packEntries(File[] filesToPack, File destZipFile, NameMapper mapper, int compressionLevel) {
    log.debug("Compressing '{}' into '{}'.", filesToPack, destZipFile);
    ZipOutputStream out = null;
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(destZipFile);
      out = new ZipOutputStream(new BufferedOutputStream(fos));
      out.setLevel(compressionLevel);
      for (int i = 0; i < filesToPack.length; i++) {
        File fileToPack = filesToPack[i];
        ZipEntry zipEntry = ZipEntryUtil.fromFile(mapper.map(fileToPack.getName()), fileToPack);
        out.putNextEntry(zipEntry);
        FileUtils.copy(fileToPack, out);
        out.closeEntry();
      }
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
    finally {
      IOUtils.closeQuietly(out);
      IOUtils.closeQuietly(fos);
    }
  }
  public static void pack(File sourceDir, File targetZip, NameMapper mapper) {
    pack(sourceDir, targetZip, mapper, DEFAULT_COMPRESSION_LEVEL);
  }
  public static void pack(File sourceDir, File targetZip, NameMapper mapper, int compressionLevel) {
    log.debug("Compressing '{}' into '{}'.", sourceDir, targetZip);
    if (!sourceDir.exists()) {
      throw new ZipException("Given file '" + sourceDir + "' doesn't exist!");
    }
    ZipOutputStream out = null;
    try {
      out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(targetZip)));
      out.setLevel(compressionLevel);
      pack(sourceDir, out, mapper, "", true);
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
    finally {
      IOUtils.closeQuietly(out);
    }
  }
  public static void pack(File sourceDir, OutputStream os) {
    pack(sourceDir, os, IdentityNameMapper.INSTANCE, DEFAULT_COMPRESSION_LEVEL);
  }
  public static void pack(File sourceDir, OutputStream os, int compressionLevel) {
    pack(sourceDir, os, IdentityNameMapper.INSTANCE, compressionLevel);
  }
  public static void pack(File sourceDir, OutputStream os, NameMapper mapper) {
    pack(sourceDir, os, mapper, DEFAULT_COMPRESSION_LEVEL);
  }
  public static void pack(File sourceDir, OutputStream os, NameMapper mapper, int compressionLevel) {
    log.debug("Compressing '{}' into a stream.", sourceDir);
    if (!sourceDir.exists()) {
      throw new ZipException("Given file '" + sourceDir + "' doesn't exist!");
    }
    ZipOutputStream out = null;
    IOException error = null;
    try {
      out = new ZipOutputStream(new BufferedOutputStream(os));
      out.setLevel(compressionLevel);
      pack(sourceDir, out, mapper, "", true);
    }
    catch (IOException e) {
      error = e;
    }
    finally {
      if (out != null && error == null) {
        try {
          out.finish();
          out.flush();
        }
        catch (IOException e) {
          error = e;
        }
      }
    }
    if (error != null) {
      throw ZipExceptionUtil.rethrow(error);
    }
  }
  private static void pack(File dir, ZipOutputStream out, NameMapper mapper, String pathPrefix, boolean mustHaveChildren) throws IOException {
    String[] filenames = dir.list();
    if (filenames == null) {
      if (!dir.exists()) {
        throw new ZipException("Given file '" + dir + "' doesn't exist!");
      }
      throw new IOException("Given file is not a directory '" + dir + "'");
    }
    if (mustHaveChildren && filenames.length == 0) {
      throw new ZipException("Given directory '" + dir + "' doesn't contain any files!");
    }
    for (int i = 0; i < filenames.length; i++) {
      String filename = filenames[i];
      File file = new File(dir, filename);
      boolean isDir = file.isDirectory();
      String path = pathPrefix + file.getName(); 
      if (isDir) {
        path += PATH_SEPARATOR; 
      }
      String name = mapper.map(path);
      if (name != null) {
        ZipEntry zipEntry = ZipEntryUtil.fromFile(name, file);
        out.putNextEntry(zipEntry);
        if (!isDir) {
          FileUtils.copy(file, out);
        }
        out.closeEntry();
      }
      if (isDir) {
        pack(file, out, mapper, path, false);
      }
    }
  }
  public static void repack(File srcZip, File dstZip, int compressionLevel) {
    log.debug("Repacking '{}' into '{}'.", srcZip, dstZip);
    RepackZipEntryCallback callback = new RepackZipEntryCallback(dstZip, compressionLevel);
    try {
      iterate(srcZip, callback);
    }
    finally {
      callback.closeStream();
    }
  }
  public static void repack(InputStream is, File dstZip, int compressionLevel) {
    log.debug("Repacking from input stream into '{}'.", dstZip);
    RepackZipEntryCallback callback = new RepackZipEntryCallback(dstZip, compressionLevel);
    try {
      iterate(is, callback);
    }
    finally {
      callback.closeStream();
    }
  }
  public static void repack(File zip, int compressionLevel) {
    try {
      File tmpZip = FileUtils.getTempFileFor(zip);
      repack(zip, tmpZip, compressionLevel);
      if (!zip.delete()) {
        throw new IOException("Unable to delete the file: " + zip);
      }
      FileUtils.moveFile(tmpZip, zip);
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
  }
  private static final class RepackZipEntryCallback implements ZipEntryCallback {
    private ZipOutputStream out;
    private RepackZipEntryCallback(File dstZip, int compressionLevel) {
      try {
        this.out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(dstZip)));
        this.out.setLevel(compressionLevel);
      }
      catch (IOException e) {
        ZipExceptionUtil.rethrow(e);
      }
    }
    public void process(InputStream in, ZipEntry zipEntry) throws IOException {
      ZipEntryUtil.copyEntry(zipEntry, in, out);
    }
    private void closeStream() {
      IOUtils.closeQuietly(out);
    }
  }
  public static void unexplode(File dir) {
    unexplode(dir, DEFAULT_COMPRESSION_LEVEL);
  }
  public static void unexplode(File dir, int compressionLevel) {
    try {
      File zip = FileUtils.getTempFileFor(dir);
      pack(dir, zip, compressionLevel);
      FileUtils.deleteDirectory(dir);
      FileUtils.moveFile(zip, dir);
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
  }
  public static void pack(ZipEntrySource[] entries, OutputStream os) {
    if (log.isDebugEnabled()) {
      log.debug("Creating stream from {}.", Arrays.asList(entries));
    }
    pack(entries, os, false);
  }
  private static void pack(ZipEntrySource[] entries, OutputStream os, boolean closeStream) {
    try {
      ZipOutputStream out = new ZipOutputStream(os);
      for (int i = 0; i < entries.length; i++) {
        addEntry(entries[i], out);
      }
      out.flush();
      out.finish();
      if (closeStream) {
        out.close();
      }
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
  }
  public static void pack(ZipEntrySource[] entries, File zip) {
    if (log.isDebugEnabled()) {
      log.debug("Creating '{}' from {}.", zip, Arrays.asList(entries));
    }
    OutputStream out = null;
    try {
      out = new BufferedOutputStream(new FileOutputStream(zip));
      pack(entries, out, true);
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
    finally {
      IOUtils.closeQuietly(out);
    }
  }
  public static void addEntry(File zip, String path, File file, File destZip) {
    addEntry(zip, new FileSource(path, file), destZip);
  }
  public static void addEntry(final File zip, final String path, final File file) {
    operateInPlace(zip, new InPlaceAction() {
      public boolean act(File tmpFile) {
        addEntry(zip, path, file, tmpFile);
        return true;
      }
    });
  }
  public static void addEntry(File zip, String path, byte[] bytes, File destZip) {
    addEntry(zip, new ByteSource(path, bytes), destZip);
  }
  public static void addEntry(File zip, String path, byte[] bytes, File destZip, final int compressionMethod) {
    addEntry(zip, new ByteSource(path, bytes, compressionMethod), destZip);
  }
  public static void addEntry(final File zip, final String path, final byte[] bytes) {
    operateInPlace(zip, new InPlaceAction() {
      public boolean act(File tmpFile) {
        addEntry(zip, path, bytes, tmpFile);
        return true;
      }
    });
  }
  public static void addEntry(final File zip, final String path, final byte[] bytes, final int compressionMethod) {
    operateInPlace(zip, new InPlaceAction() {
      public boolean act(File tmpFile) {
        addEntry(zip, path, bytes, tmpFile, compressionMethod);
        return true;
      }
    });
  }
  public static void addEntry(File zip, ZipEntrySource entry, File destZip) {
    addEntries(zip, new ZipEntrySource[] { entry }, destZip);
  }
  public static void addEntry(final File zip, final ZipEntrySource entry) {
    operateInPlace(zip, new InPlaceAction() {
      public boolean act(File tmpFile) {
        addEntry(zip, entry, tmpFile);
        return true;
      }
    });
  }
  public static void addEntries(File zip, ZipEntrySource[] entries, File destZip) {
    if (log.isDebugEnabled()) {
      log.debug("Copying '" + zip + "' to '" + destZip + "' and adding " + Arrays.asList(entries) + ".");
    }
    OutputStream destOut = null;
    try {
      destOut = new BufferedOutputStream(new FileOutputStream(destZip));
      addEntries(zip, entries, destOut);
    }
    catch (IOException e) {
      ZipExceptionUtil.rethrow(e);
    }
    finally {
      IOUtils.closeQuietly(destOut);
    }
  }
  public static void addEntries(File zip, ZipEntrySource[] entries, OutputStream destOut) {
    if (log.isDebugEnabled()) {
      log.debug("Copying '" + zip + "' to a stream and adding " + Arrays.asList(entries) + ".");
    }
    ZipOutputStream out = null;
    try {
      out = new ZipOutputStream(destOut);
      copyEntries(zip, out);
      for (int i = 0; i < entries.length; i++) {
        addEntry(entries[i], out);
      }
      out.finish();
    }
    catch (IOException e) {
      ZipExceptionUtil.rethrow(e);
    }
  }
  public static void addEntries(InputStream is, ZipEntrySource[] entries, OutputStream destOut) {
    if (log.isDebugEnabled()) {
      log.debug("Copying input stream to an output stream and adding " + Arrays.asList(entries) + ".");
    }
    ZipOutputStream out = null;
    try {
      out = new ZipOutputStream(destOut);
      copyEntries(is, out);
      for (int i = 0; i < entries.length; i++) {
        addEntry(entries[i], out);
      }
      out.finish();
    }
    catch (IOException e) {
      ZipExceptionUtil.rethrow(e);
    }
  }
  public static void addEntries(final File zip, final ZipEntrySource[] entries) {
    operateInPlace(zip, new InPlaceAction() {
      public boolean act(File tmpFile) {
        addEntries(zip, entries, tmpFile);
        return true;
      }
    });
  }
  public static void removeEntry(File zip, String path, File destZip) {
    removeEntries(zip, new String[] { path }, destZip);
  }
  public static void removeEntry(final File zip, final String path) {
    operateInPlace(zip, new InPlaceAction() {
      public boolean act(File tmpFile) {
        removeEntry(zip, path, tmpFile);
        return true;
      }
    });
  }
  public static void removeEntries(File zip, String[] paths, File destZip) {
    if (log.isDebugEnabled()) {
      log.debug("Copying '" + zip + "' to '" + destZip + "' and removing paths " + Arrays.asList(paths) + ".");
    }
    ZipOutputStream out = null;
    try {
      out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destZip)));
      copyEntries(zip, out, new HashSet<String>(Arrays.asList(paths)));
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
    finally {
      IOUtils.closeQuietly(out);
    }
  }
  public static void removeEntries(final File zip, final String[] paths) {
    operateInPlace(zip, new InPlaceAction() {
      public boolean act(File tmpFile) {
        removeEntries(zip, paths, tmpFile);
        return true;
      }
    });
  }
  private static void copyEntries(File zip, final ZipOutputStream out) {
    final Set<String> names = new HashSet<String>();
    iterate(zip, new ZipEntryCallback() {
      public void process(InputStream in, ZipEntry zipEntry) throws IOException {
        String entryName = zipEntry.getName();
        if (names.add(entryName)) {
          ZipEntryUtil.copyEntry(zipEntry, in, out);
        }
        else if (log.isDebugEnabled()) {
          log.debug("Duplicate entry: {}", entryName);
        }
      }
    });
  }
  private static void copyEntries(InputStream is, final ZipOutputStream out) {
    final Set<String> names = new HashSet<String>();
    iterate(is, new ZipEntryCallback() {
      public void process(InputStream in, ZipEntry zipEntry) throws IOException {
        String entryName = zipEntry.getName();
        if (names.add(entryName)) {
          ZipEntryUtil.copyEntry(zipEntry, in, out);
        }
        else if (log.isDebugEnabled()) {
          log.debug("Duplicate entry: {}", entryName);
        }
      }
    });
  }
  private static void copyEntries(File zip, final ZipOutputStream out, final Set<String> ignoredEntries) {
    final Set<String> names = new HashSet<String>();
    final Set<String> dirNames = filterDirEntries(zip, ignoredEntries);
    iterate(zip, new ZipEntryCallback() {
      public void process(InputStream in, ZipEntry zipEntry) throws IOException {
        String entryName = zipEntry.getName();
        if (ignoredEntries.contains(entryName)) {
          return;
        }
        for (String dirName : dirNames) {
          if (entryName.startsWith(dirName)) {
            return;
          }
        }
        if (names.add(entryName)) {
          ZipEntryUtil.copyEntry(zipEntry, in, out);
        }
        else if (log.isDebugEnabled()) {
          log.debug("Duplicate entry: {}", entryName);
        }
      }
    });
  }
  static Set<String> filterDirEntries(File zip, Collection<String> names) {
    Set<String> dirs = new HashSet<String>();
    if (zip == null) {
      return dirs;
    }
    ZipFile zf = null;
    try {
      zf = new ZipFile(zip);
      for (String entryName : names) {
        ZipEntry entry = zf.getEntry(entryName);
        if (entry != null) {
          if (entry.isDirectory()) {
            dirs.add(entry.getName());
          }
          else if (zf.getInputStream(entry) == null) {
            dirs.add(entry.getName() + PATH_SEPARATOR);
          }
        }
      }
    }
    catch (IOException e) {
      ZipExceptionUtil.rethrow(e);
    }
    finally {
      closeQuietly(zf);
    }
    return dirs;
  }
  public static boolean replaceEntry(File zip, String path, File file, File destZip) {
    return replaceEntry(zip, new FileSource(path, file), destZip);
  }
  public static boolean replaceEntry(final File zip, final String path, final File file) {
    return operateInPlace(zip, new InPlaceAction() {
      public boolean act(File tmpFile) {
        return replaceEntry(zip, new FileSource(path, file), tmpFile);
      }
    });
  }
  public static boolean replaceEntry(File zip, String path, byte[] bytes, File destZip) {
    return replaceEntry(zip, new ByteSource(path, bytes), destZip);
  }
  public static boolean replaceEntry(final File zip, final String path, final byte[] bytes) {
    return operateInPlace(zip, new InPlaceAction() {
      public boolean act(File tmpFile) {
        return replaceEntry(zip, new ByteSource(path, bytes), tmpFile);
      }
    });
  }
  public static boolean replaceEntry(final File zip, final String path, final byte[] bytes,
      final int compressionMethod) {
    return operateInPlace(zip, new InPlaceAction() {
      public boolean act(File tmpFile) {
        return replaceEntry(zip, new ByteSource(path, bytes, compressionMethod), tmpFile);
      }
    });
  }
  public static boolean replaceEntry(File zip, ZipEntrySource entry, File destZip) {
    return replaceEntries(zip, new ZipEntrySource[] { entry }, destZip);
  }
  public static boolean replaceEntry(final File zip, final ZipEntrySource entry) {
    return operateInPlace(zip, new InPlaceAction() {
      public boolean act(File tmpFile) {
        return replaceEntry(zip, entry, tmpFile);
      }
    });
  }
  public static boolean replaceEntries(File zip, ZipEntrySource[] entries, File destZip) {
    if (log.isDebugEnabled()) {
      log.debug("Copying '" + zip + "' to '" + destZip + "' and replacing entries " + Arrays.asList(entries) + ".");
    }
    final Map<String, ZipEntrySource> entryByPath = entriesByPath(entries);
    final int entryCount = entryByPath.size();
    try {
      final ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destZip)));
      try {
        final Set<String> names = new HashSet<String>();
        iterate(zip, new ZipEntryCallback() {
          public void process(InputStream in, ZipEntry zipEntry) throws IOException {
            if (names.add(zipEntry.getName())) {
              ZipEntrySource entry = (ZipEntrySource) entryByPath.remove(zipEntry.getName());
              if (entry != null) {
                addEntry(entry, out);
              }
              else {
                ZipEntryUtil.copyEntry(zipEntry, in, out);
              }
            }
            else if (log.isDebugEnabled()) {
              log.debug("Duplicate entry: {}", zipEntry.getName());
            }
          }
        });
      }
      finally {
        IOUtils.closeQuietly(out);
      }
    }
    catch (IOException e) {
      ZipExceptionUtil.rethrow(e);
    }
    return entryByPath.size() < entryCount;
  }
  public static boolean replaceEntries(final File zip, final ZipEntrySource[] entries) {
    return operateInPlace(zip, new InPlaceAction() {
      public boolean act(File tmpFile) {
        return replaceEntries(zip, entries, tmpFile);
      }
    });
  }
  public static void addOrReplaceEntries(File zip, ZipEntrySource[] entries, File destZip) {
    if (log.isDebugEnabled()) {
      log.debug("Copying '" + zip + "' to '" + destZip + "' and adding/replacing entries " + Arrays.asList(entries)
          + ".");
    }
    final Map<String, ZipEntrySource> entryByPath = entriesByPath(entries);
    try {
      final ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destZip)));
      try {
        final Set<String> names = new HashSet<String>();
        iterate(zip, new ZipEntryCallback() {
          public void process(InputStream in, ZipEntry zipEntry) throws IOException {
            if (names.add(zipEntry.getName())) {
              ZipEntrySource entry = (ZipEntrySource) entryByPath.remove(zipEntry.getName());
              if (entry != null) {
                addEntry(entry, out);
              }
              else {
                ZipEntryUtil.copyEntry(zipEntry, in, out);
              }
            }
            else if (log.isDebugEnabled()) {
              log.debug("Duplicate entry: {}", zipEntry.getName());
            }
          }
        });
        for (ZipEntrySource zipEntrySource : entryByPath.values()) {
          addEntry(zipEntrySource, out);
        }
      }
      finally {
        IOUtils.closeQuietly(out);
      }
    }
    catch (IOException e) {
      ZipExceptionUtil.rethrow(e);
    }
  }
  public static void addOrReplaceEntries(final File zip, final ZipEntrySource[] entries) {
    operateInPlace(zip, new InPlaceAction() {
      public boolean act(File tmpFile) {
        addOrReplaceEntries(zip, entries, tmpFile);
        return true;
      }
    });
  }
  static Map<String, ZipEntrySource> entriesByPath(ZipEntrySource... entries) {
    Map<String, ZipEntrySource> result = new HashMap<String, ZipEntrySource>();
    for (int i = 0; i < entries.length; i++) {
      ZipEntrySource source = entries[i];
      result.put(source.getPath(), source);
    }
    return result;
  }
  public static boolean transformEntry(File zip, String path, ZipEntryTransformer transformer, File destZip) {
    if(zip.equals(destZip)){throw new IllegalArgumentException("Input (" +zip.getAbsolutePath()+ ") is the same as the destination!" +
            "Please use the transformEntry method without destination for in-place transformation." );}
    return transformEntry(zip, new ZipEntryTransformerEntry(path, transformer), destZip);
  }
  public static boolean transformEntry(final File zip, final String path, final ZipEntryTransformer transformer) {
    return operateInPlace(zip, new InPlaceAction() {
      public boolean act(File tmpFile) {
        return transformEntry(zip, path, transformer, tmpFile);
      }
    });
  }
  public static boolean transformEntry(File zip, ZipEntryTransformerEntry entry, File destZip) {
    return transformEntries(zip, new ZipEntryTransformerEntry[] { entry }, destZip);
  }
  public static boolean transformEntry(final File zip, final ZipEntryTransformerEntry entry) {
    return operateInPlace(zip, new InPlaceAction() {
      public boolean act(File tmpFile) {
        return transformEntry(zip, entry, tmpFile);
      }
    });
  }
  public static boolean transformEntries(File zip, ZipEntryTransformerEntry[] entries, File destZip) {
    if (log.isDebugEnabled())
      log.debug("Copying '" + zip + "' to '" + destZip + "' and transforming entries " + Arrays.asList(entries) + ".");
    try {
      ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destZip)));
      try {
        TransformerZipEntryCallback action = new TransformerZipEntryCallback(Arrays.asList(entries), out);
        iterate(zip, action);
        return action.found();
      }
      finally {
        IOUtils.closeQuietly(out);
      }
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
  }
  public static boolean transformEntries(final File zip, final ZipEntryTransformerEntry[] entries) {
    return operateInPlace(zip, new InPlaceAction() {
      public boolean act(File tmpFile) {
        return transformEntries(zip, entries, tmpFile);
      }
    });
  }
  public static boolean transformEntry(InputStream is, String path, ZipEntryTransformer transformer, OutputStream os) {
    return transformEntry(is, new ZipEntryTransformerEntry(path, transformer), os);
  }
  public static boolean transformEntry(InputStream is, ZipEntryTransformerEntry entry, OutputStream os) {
    return transformEntries(is, new ZipEntryTransformerEntry[] { entry }, os);
  }
  public static boolean transformEntries(InputStream is, ZipEntryTransformerEntry[] entries, OutputStream os) {
    if (log.isDebugEnabled())
      log.debug("Copying '" + is + "' to '" + os + "' and transforming entries " + Arrays.asList(entries) + ".");
    try {
      ZipOutputStream out = new ZipOutputStream(os);
      TransformerZipEntryCallback action = new TransformerZipEntryCallback(Arrays.asList(entries), out);
      iterate(is, action);
      out.finish();
      return action.found();
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
  }
  private static class TransformerZipEntryCallback implements ZipEntryCallback {
    private final Map<String, ZipEntryTransformer> entryByPath;
    private final int entryCount;
    private final ZipOutputStream out;
    private final Set<String> names = new HashSet<String>();
    public TransformerZipEntryCallback(List<ZipEntryTransformerEntry> entries, ZipOutputStream out) {
      entryByPath = transformersByPath(entries);
      entryCount = entryByPath.size();
      this.out = out;
    }
    public void process(InputStream in, ZipEntry zipEntry) throws IOException {
      if (names.add(zipEntry.getName())) {
        ZipEntryTransformer entry = (ZipEntryTransformer) entryByPath.remove(zipEntry.getName());
        if (entry != null) {
          entry.transform(in, zipEntry, out);
        }
        else {
          ZipEntryUtil.copyEntry(zipEntry, in, out);
        }
      }
      else if (log.isDebugEnabled()) {
        log.debug("Duplicate entry: {}", zipEntry.getName());
      }
    }
    public boolean found() {
      return entryByPath.size() < entryCount;
    }
  }
  static Map<String, ZipEntryTransformer> transformersByPath(List<ZipEntryTransformerEntry> entries) {
    Map<String, ZipEntryTransformer> result = new HashMap<String, ZipEntryTransformer>();
    for (ZipEntryTransformerEntry entry : entries) {
      result.put(entry.getPath(), entry.getTransformer());
    }
    return result;
  }
  private static void addEntry(ZipEntrySource entry, ZipOutputStream out) throws IOException {
    out.putNextEntry(entry.getEntry());
    InputStream in = entry.getInputStream();
    if (in != null) {
      try {
        IOUtils.copy(in, out);
      }
      finally {
        IOUtils.closeQuietly(in);
      }
    }
    out.closeEntry();
  }
  public static boolean archiveEquals(File f1, File f2) {
    try {
      if (FileUtils.contentEquals(f1, f2)) {
        return true;
      }
      log.debug("Comparing archives '{}' and '{}'...", f1, f2);
      long start = System.currentTimeMillis();
      boolean result = archiveEqualsInternal(f1, f2);
      long time = System.currentTimeMillis() - start;
      if (time > 0) {
        log.debug("Archives compared in " + time + " ms.");
      }
      return result;
    }
    catch (Exception e) {
      log.debug("Could not compare '" + f1 + "' and '" + f2 + "':", e);
      return false;
    }
  }
  private static boolean archiveEqualsInternal(File f1, File f2) throws IOException {
    ZipFile zf1 = null;
    ZipFile zf2 = null;
    try {
      zf1 = new ZipFile(f1);
      zf2 = new ZipFile(f2);
      if (zf1.size() != zf2.size()) {
        log.debug("Number of entries changed (" + zf1.size() + " vs " + zf2.size() + ").");
        return false;
      }
      Enumeration<? extends ZipEntry> en = zf1.entries();
      while (en.hasMoreElements()) {
        ZipEntry e1 = (ZipEntry) en.nextElement();
        String path = e1.getName();
        ZipEntry e2 = zf2.getEntry(path);
        if (!metaDataEquals(path, e1, e2)) {
          return false;
        }
        InputStream is1 = null;
        InputStream is2 = null;
        try {
          is1 = zf1.getInputStream(e1);
          is2 = zf2.getInputStream(e2);
          if (!IOUtils.contentEquals(is1, is2)) {
            log.debug("Entry '{}' content changed.", path);
            return false;
          }
        }
        finally {
          IOUtils.closeQuietly(is1);
          IOUtils.closeQuietly(is2);
        }
      }
    }
    finally {
      closeQuietly(zf1);
      closeQuietly(zf2);
    }
    log.debug("Archives are the same.");
    return true;
  }
  private static boolean metaDataEquals(String path, ZipEntry e1, ZipEntry e2) throws IOException {
    if (e2 == null) {
      log.debug("Entry '{}' removed.", path);
      return false;
    }
    if (e1.isDirectory()) {
      if (e2.isDirectory()) {
        return true; 
      }
      else {
        log.debug("Entry '{}' not a directory any more.", path);
        return false;
      }
    }
    else if (e2.isDirectory()) {
      log.debug("Entry '{}' now a directory.", path);
      return false;
    }
    long size1 = e1.getSize();
    long size2 = e2.getSize();
    if (size1 != -1 && size2 != -1 && size1 != size2) {
      log.debug("Entry '" + path + "' size changed (" + size1 + " vs " + size2 + ").");
      return false;
    }
    long crc1 = e1.getCrc();
    long crc2 = e2.getCrc();
    if (crc1 != -1 && crc2 != -1 && crc1 != crc2) {
      log.debug("Entry '" + path + "' CRC changed (" + crc1 + " vs " + crc2 + ").");
      return false;
    }
    if (log.isTraceEnabled()) {
      long time1 = e1.getTime();
      long time2 = e2.getTime();
      if (time1 != -1 && time2 != -1 && time1 != time2) {
        log.trace("Entry '" + path + "' time changed (" + new Date(time1) + " vs " + new Date(time2) + ").");
      }
    }
    return true;
  }
  public static boolean entryEquals(File f1, File f2, String path) {
    return entryEquals(f1, f2, path, path);
  }
  public static boolean entryEquals(File f1, File f2, String path1, String path2) {
    ZipFile zf1 = null;
    ZipFile zf2 = null;
    try {
      zf1 = new ZipFile(f1);
      zf2 = new ZipFile(f2);
      return doEntryEquals(zf1, zf2, path1, path2);
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
    finally {
      closeQuietly(zf1);
      closeQuietly(zf2);
    }
  }
  public static boolean entryEquals(ZipFile zf1, ZipFile zf2, String path1, String path2) {
    try {
      return doEntryEquals(zf1, zf2, path1, path2);
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
  }
  private static boolean doEntryEquals(ZipFile zf1, ZipFile zf2, String path1, String path2) throws IOException {
    InputStream is1 = null;
    InputStream is2 = null;
    try {
      ZipEntry e1 = zf1.getEntry(path1);
      ZipEntry e2 = zf2.getEntry(path2);
      if (e1 == null && e2 == null) {
        return true;
      }
      if (e1 == null || e2 == null) {
        return false;
      }
      is1 = zf1.getInputStream(e1);
      is2 = zf2.getInputStream(e2);
      if (is1 == null && is2 == null) {
        return true;
      }
      if (is1 == null || is2 == null) {
        return false;
      }
      return IOUtils.contentEquals(is1, is2);
    }
    finally {
      IOUtils.closeQuietly(is1);
      IOUtils.closeQuietly(is2);
    }
  }
  public static void closeQuietly(ZipFile zf) {
    try {
      if (zf != null) {
        zf.close();
      }
    }
    catch (IOException e) {
    }
  }
  private abstract static class InPlaceAction {
    abstract boolean act(File tmpFile);
  }
  private static boolean operateInPlace(File src, InPlaceAction action) {
    File tmp = null;
    try {
      tmp = File.createTempFile("zt-zip-tmp", ".zip");
      boolean result = action.act(tmp);
      if (result) { 
        FileUtils.forceDelete(src);
        FileUtils.moveFile(tmp, src);
      }
      return result;
    }
    catch (IOException e) {
      throw ZipExceptionUtil.rethrow(e);
    }
    finally {
      FileUtils.deleteQuietly(tmp);
    }
  }
}
