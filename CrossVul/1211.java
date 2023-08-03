
package hudson;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import hudson.cli.CLICommand;
import hudson.console.ConsoleAnnotationDescriptor;
import hudson.console.ConsoleAnnotatorFactory;
import hudson.model.*;
import hudson.model.ParameterDefinition.ParameterDescriptor;
import hudson.search.SearchableModelObject;
import hudson.security.AccessControlled;
import hudson.security.AuthorizationStrategy;
import hudson.security.GlobalSecurityConfiguration;
import hudson.security.Permission;
import hudson.security.SecurityRealm;
import hudson.security.captcha.CaptchaSupport;
import hudson.security.csrf.CrumbIssuer;
import hudson.slaves.Cloud;
import hudson.slaves.ComputerLauncher;
import hudson.slaves.NodeProperty;
import hudson.slaves.NodePropertyDescriptor;
import hudson.slaves.RetentionStrategy;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrappers;
import hudson.tasks.Builder;
import hudson.tasks.Publisher;
import hudson.tasks.UserAvatarResolver;
import hudson.util.Area;
import hudson.util.Iterators;
import hudson.scm.SCM;
import hudson.scm.SCMDescriptor;
import hudson.util.Secret;
import hudson.views.MyViewsTabBar;
import hudson.views.ViewsTabBar;
import hudson.widgets.RenderOnDemandClosure;
import jenkins.model.GlobalConfiguration;
import jenkins.model.GlobalConfigurationCategory;
import jenkins.model.GlobalConfigurationCategory.Unclassified;
import jenkins.model.Jenkins;
import jenkins.model.ModelObjectWithContextMenu;
import org.acegisecurity.providers.anonymous.AnonymousAuthenticationToken;
import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jexl.parser.ASTSizeFunction;
import org.apache.commons.jexl.util.Introspector;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;
import org.jvnet.tiger_types.Types;
import org.kohsuke.stapler.Ancestor;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.jelly.InternationalizedStringExpression.RawHtmlArgument;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
public class Functions {
    private static volatile int globalIota = 0;
    private int iota;
    public Functions() {
        iota = globalIota;
        globalIota+=1000;
    }
    public String generateId() {
        return "id"+iota++;
    }
    public static boolean isModel(Object o) {
        return o instanceof ModelObject;
    }
    public static boolean isModelWithContextMenu(Object o) {
        return o instanceof ModelObjectWithContextMenu;
    }
    public static String xsDate(Calendar cal) {
        return Util.XS_DATETIME_FORMATTER.format(cal.getTime());
    }
    public static String rfc822Date(Calendar cal) {
        return Util.RFC822_DATETIME_FORMATTER.format(cal.getTime());
    }
    public static void initPageVariables(JellyContext context) {
        String rootURL = Stapler.getCurrentRequest().getContextPath();
        Functions h = new Functions();
        context.setVariable("h", h);
        context.setVariable("rootURL", rootURL);
        context.setVariable("resURL",rootURL+getResourcePath());
        context.setVariable("imagesURL",rootURL+getResourcePath()+"/images");
    }
    public static <B> Class getTypeParameter(Class<? extends B> c, Class<B> base, int n) {
        Type parameterization = Types.getBaseClass(c,base);
        if (parameterization instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) parameterization;
            return Types.erasure(Types.getTypeArgument(pt,n));
        } else {
            throw new AssertionError(c+" doesn't properly parameterize "+base);
        }
    }
    public JDK.DescriptorImpl getJDKDescriptor() {
        return Jenkins.getInstance().getDescriptorByType(JDK.DescriptorImpl.class);
    }
    public static String getDiffString(int i) {
        if(i==0)    return "\u00B10";   
        String s = Integer.toString(i);
        if(i>0)     return "+"+s;
        else        return s;
    }
    public static String getDiffString2(int i) {
        if(i==0)    return "";
        String s = Integer.toString(i);
        if(i>0)     return "+"+s;
        else        return s;
    }
    public static String getDiffString2(String prefix, int i, String suffix) {
        if(i==0)    return "";
        String s = Integer.toString(i);
        if(i>0)     return prefix+"+"+s+suffix;
        else        return prefix+s+suffix;
    }
    public static String addSuffix(int n, String singular, String plural) {
        StringBuilder buf = new StringBuilder();
        buf.append(n).append(' ');
        if(n==1)
            buf.append(singular);
        else
            buf.append(plural);
        return buf.toString();
    }
    public static RunUrl decompose(StaplerRequest req) {
        List<Ancestor> ancestors = req.getAncestors();
        Ancestor f=null,l=null;
        for (Ancestor anc : ancestors) {
            if(anc.getObject() instanceof Run) {
                if(f==null) f=anc;
                l=anc;
            }
        }
        if(l==null) return null;    
        String head = f.getPrev().getUrl()+'/';
        String base = l.getUrl();
        String reqUri = req.getOriginalRequestURI();
        String furl = f.getUrl();
        int slashCount = 0;
        for (int i = furl.indexOf('/'); i >= 0; i = furl.indexOf('/', i + 1)) slashCount++;
        String rest = reqUri.replaceFirst("(?:/+[^/]*){" + slashCount + "}", "");
        return new RunUrl( (Run) f.getObject(), head, base, rest);
    }
    public static Area getScreenResolution() {
        Cookie res = Functions.getCookie(Stapler.getCurrentRequest(),"screenResolution");
        if(res!=null)
            return Area.parse(res.getValue());
        return null;
    }
    public static final class RunUrl {
        private final String head, base, rest;
        private final Run run;
        public RunUrl(Run run, String head, String base, String rest) {
            this.run = run;
            this.head = head;
            this.base = base;
            this.rest = rest;
        }
        public String getBaseUrl() {
            return base;
        }
        public String getNextBuildUrl() {
            return getUrl(run.getNextBuild());
        }
        public String getPreviousBuildUrl() {
            return getUrl(run.getPreviousBuild());
        }
        private String getUrl(Run n) {
            if(n ==null)
                return null;
            else {
                return head+n.getNumber()+rest;
            }
        }
    }
    public static Node.Mode[] getNodeModes() {
        return Node.Mode.values();
    }
    public static String getProjectListString(List<Project> projects) {
        return Items.toNameList(projects);
    }
    public static Object ifThenElse(boolean cond, Object thenValue, Object elseValue) {
        return cond ? thenValue : elseValue;
    }
    public static String appendIfNotNull(String text, String suffix, String nullText) {
        return text == null ? nullText : text + suffix;
    }
    public static Map getSystemProperties() {
        return new TreeMap<Object,Object>(System.getProperties());
    }
    public static Map getEnvVars() {
        return new TreeMap<String,String>(EnvVars.masterEnvVars);
    }
    public static boolean isWindows() {
        return File.pathSeparatorChar==';';
    }
    public static List<LogRecord> getLogRecords() {
        return Jenkins.logRecords;
    }
    public static String printLogRecord(LogRecord r) {
        return formatter.format(r);
    }
    public static Cookie getCookie(HttpServletRequest req,String name) {
        Cookie[] cookies = req.getCookies();
        if(cookies!=null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }
    public static String getCookie(HttpServletRequest req,String name, String defaultValue) {
        Cookie c = getCookie(req, name);
        if(c==null || c.getValue()==null) return defaultValue;
        return c.getValue();
    }
    private static final Pattern ICON_SIZE = Pattern.compile("\\d+x\\d+");
    @Restricted(NoExternalUse.class)
    public static String validateIconSize(String iconSize) throws SecurityException {
        if (!ICON_SIZE.matcher(iconSize).matches()) {
            throw new SecurityException("invalid iconSize");
        }
        return iconSize;
    }
    public static String getYuiSuffix() {
        return DEBUG_YUI ? "debug" : "min";
    }
    public static boolean DEBUG_YUI = Boolean.getBoolean("debug.YUI");
    public static <V> SortedMap<Integer,V> filter(SortedMap<Integer,V> map, String from, String to) {
        if(from==null && to==null)      return map;
        if(to==null)
            return map.headMap(Integer.parseInt(from)-1);
        if(from==null)
            return map.tailMap(Integer.parseInt(to));
        return map.subMap(Integer.parseInt(to),Integer.parseInt(from)-1);
    }
    private static final SimpleFormatter formatter = new SimpleFormatter();
    public static void configureAutoRefresh(HttpServletRequest request, HttpServletResponse response, boolean noAutoRefresh) {
        if(noAutoRefresh)
            return;
        String param = request.getParameter("auto_refresh");
        boolean refresh = isAutoRefresh(request);
        if (param != null) {
            refresh = Boolean.parseBoolean(param);
            Cookie c = new Cookie("hudson_auto_refresh", Boolean.toString(refresh));
            c.setPath("/");
            c.setMaxAge(60*60*24*30); 
            response.addCookie(c);
        }
        if (refresh) {
            response.addHeader("Refresh", System.getProperty("hudson.Functions.autoRefreshSeconds", "10"));
        }
    }
    public static boolean isAutoRefresh(HttpServletRequest request) {
        String param = request.getParameter("auto_refresh");
        if (param != null) {
            return Boolean.parseBoolean(param);
        }
        Cookie[] cookies = request.getCookies();
        if(cookies==null)
            return false; 
        for (Cookie c : cookies) {
            if (c.getName().equals("hudson_auto_refresh")) {
                return Boolean.parseBoolean(c.getValue());
            }
        }
        return false;
    }
    public static String getNearestAncestorUrl(StaplerRequest req,Object it) {
        List list = req.getAncestors();
        for( int i=list.size()-1; i>=0; i-- ) {
            Ancestor anc = (Ancestor) list.get(i);
            if(anc.getObject()==it)
                return anc.getUrl();
        }
        return null;
    }
    public static String getSearchURL() {
        List list = Stapler.getCurrentRequest().getAncestors();
        for( int i=list.size()-1; i>=0; i-- ) {
            Ancestor anc = (Ancestor) list.get(i);
            if(anc.getObject() instanceof SearchableModelObject)
                return anc.getUrl()+"/search/";
        }
        return null;
    }
    public static String appendSpaceIfNotNull(String n) {
        if(n==null) return null;
        else        return n+' ';
    }
    public static String nbspIndent(String size) {
        int i = size.indexOf('x');
        i = Integer.parseInt(i > 0 ? size.substring(0, i) : size) / 10;
        StringBuilder buf = new StringBuilder(30);
        for (int j = 0; j < i; j++)
            buf.append("&nbsp;");
        return buf.toString();
    }
    public static String getWin32ErrorMessage(IOException e) {
        return Util.getWin32ErrorMessage(e);
    }
    public static boolean isMultiline(String s) {
        if(s==null)     return false;
        return s.indexOf('\r')>=0 || s.indexOf('\n')>=0;
    }
    public static String encode(String s) {
        return Util.encode(s);
    }
    public static String escape(String s) {
        return Util.escape(s);
    }
    public static String xmlEscape(String s) {
        return Util.xmlEscape(s);
    }
    public static String xmlUnescape(String s) {
        return s.replace("&lt;","<").replace("&gt;",">").replace("&amp;","&");
    }
    public static String htmlAttributeEscape(String text) {
        StringBuilder buf = new StringBuilder(text.length()+64);
        for( int i=0; i<text.length(); i++ ) {
            char ch = text.charAt(i);
            if(ch=='<')
                buf.append("&lt;");
            else
            if(ch=='>')
                buf.append("&gt;");
            else
            if(ch=='&')
                buf.append("&amp;");
            else
            if(ch=='"')
                buf.append("&quot;");
            else
            if(ch=='\'')
                buf.append("&#39;");
            else
                buf.append(ch);
        }
        return buf.toString();
    }
    public static void checkPermission(Permission permission) throws IOException, ServletException {
        checkPermission(Jenkins.getInstance(),permission);
    }
    public static void checkPermission(AccessControlled object, Permission permission) throws IOException, ServletException {
        if (permission != null) {
            object.checkPermission(permission);
        }
    }
    public static void checkPermission(Object object, Permission permission) throws IOException, ServletException {
        if (permission == null)
            return;
        if (object instanceof AccessControlled)
            checkPermission((AccessControlled) object,permission);
        else {
            List<Ancestor> ancs = Stapler.getCurrentRequest().getAncestors();
            for(Ancestor anc : Iterators.reverse(ancs)) {
                Object o = anc.getObject();
                if (o instanceof AccessControlled) {
                    checkPermission((AccessControlled) o,permission);
                    return;
                }
            }
            checkPermission(Jenkins.getInstance(),permission);
        }
    }
    public static boolean hasPermission(Permission permission) throws IOException, ServletException {
        return hasPermission(Jenkins.getInstance(),permission);
    }
    public static boolean hasPermission(Object object, Permission permission) throws IOException, ServletException {
        if (permission == null)
            return true;
        if (object instanceof AccessControlled)
            return ((AccessControlled)object).hasPermission(permission);
        else {
            List<Ancestor> ancs = Stapler.getCurrentRequest().getAncestors();
            for(Ancestor anc : Iterators.reverse(ancs)) {
                Object o = anc.getObject();
                if (o instanceof AccessControlled) {
                    return ((AccessControlled)o).hasPermission(permission);
                }
            }
            return Jenkins.getInstance().hasPermission(permission);
        }
    }
    public static void adminCheck(StaplerRequest req, StaplerResponse rsp, Object required, Permission permission) throws IOException, ServletException {
        if(required!=null && !Hudson.adminCheck(req, rsp)) {
            rsp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            rsp.getOutputStream().close();
            throw new ServletException("Unauthorized access");
        }
        if(permission!=null)
            checkPermission(permission);
    }
    public static String inferHudsonURL(StaplerRequest req) {
        String rootUrl = Jenkins.getInstance().getRootUrl();
        if(rootUrl !=null)
            return rootUrl;
        StringBuilder buf = new StringBuilder();
        buf.append(req.getScheme()).append(":
        buf.append(req.getServerName());
        if(! (req.getScheme().equals("http") && req.getLocalPort()==80 || req.getScheme().equals("https") && req.getLocalPort()==443))
            buf.append(':').append(req.getLocalPort());
        buf.append(req.getContextPath()).append('/');
        return buf.toString();
    }
    public static String getFooterURL() {
        if(footerURL == null) {
            footerURL = System.getProperty("hudson.footerURL");
            if(StringUtils.isBlank(footerURL)) {
                footerURL = "http:
            }
        }
        return footerURL;
    }
    private static String footerURL = null;
    public static List<JobPropertyDescriptor> getJobPropertyDescriptors(Class<? extends Job> clazz) {
        return JobPropertyDescriptor.getPropertyDescriptors(clazz);
    }
    public static List<Descriptor<BuildWrapper>> getBuildWrapperDescriptors(AbstractProject<?,?> project) {
        return BuildWrappers.getFor(project);
    }
    public static List<Descriptor<SecurityRealm>> getSecurityRealmDescriptors() {
        return SecurityRealm.all();
    }
    public static List<Descriptor<AuthorizationStrategy>> getAuthorizationStrategyDescriptors() {
        return AuthorizationStrategy.all();
    }
    public static List<Descriptor<Builder>> getBuilderDescriptors(AbstractProject<?,?> project) {
        return BuildStepDescriptor.filter(Builder.all(), project.getClass());
    }
    public static List<Descriptor<Publisher>> getPublisherDescriptors(AbstractProject<?,?> project) {
        return BuildStepDescriptor.filter(Publisher.all(), project.getClass());
    }
    public static List<SCMDescriptor<?>> getSCMDescriptors(AbstractProject<?,?> project) {
        return SCM._for(project);
    }
    public static List<Descriptor<ComputerLauncher>> getComputerLauncherDescriptors() {
        return Jenkins.getInstance().<ComputerLauncher,Descriptor<ComputerLauncher>>getDescriptorList(ComputerLauncher.class);
    }
    public static List<Descriptor<RetentionStrategy<?>>> getRetentionStrategyDescriptors() {
        return RetentionStrategy.all();
    }
    public static List<ParameterDescriptor> getParameterDescriptors() {
        return ParameterDefinition.all();
    }
    public static List<Descriptor<CaptchaSupport>> getCaptchaSupportDescriptors() {
        return CaptchaSupport.all();
    }
    public static List<Descriptor<ViewsTabBar>> getViewsTabBarDescriptors() {
        return ViewsTabBar.all();
    }
    public static List<Descriptor<MyViewsTabBar>> getMyViewsTabBarDescriptors() {
        return MyViewsTabBar.all();
    }
    public static List<NodePropertyDescriptor> getNodePropertyDescriptors(Class<? extends Node> clazz) {
        List<NodePropertyDescriptor> result = new ArrayList<NodePropertyDescriptor>();
        Collection<NodePropertyDescriptor> list = (Collection) Jenkins.getInstance().getDescriptorList(NodeProperty.class);
        for (NodePropertyDescriptor npd : list) {
            if (npd.isApplicable(clazz)) {
                result.add(npd);
            }
        }
        return result;
    }
    public static Collection<Descriptor> getSortedDescriptorsForGlobalConfig(Predicate<GlobalConfigurationCategory> predicate) {
        ExtensionList<Descriptor> exts = Jenkins.getInstance().getExtensionList(Descriptor.class);
        List<Tag> r = new ArrayList<Tag>(exts.size());
        for (ExtensionComponent<Descriptor> c : exts.getComponents()) {
            Descriptor d = c.getInstance();
            if (d.getGlobalConfigPage()==null)  continue;
            if (d instanceof GlobalConfiguration) {
                if (predicate.apply(((GlobalConfiguration)d).getCategory()))
                    r.add(new Tag(c.ordinal(), d));
            } else {
                if (predicate.apply(GlobalConfigurationCategory.get(Unclassified.class)))
                    r.add(new Tag(0, d));
            }
        }
        Collections.sort(r);
        List<Descriptor> answer = new ArrayList<Descriptor>(r.size());
        for (Tag d : r) answer.add(d.d);
        return DescriptorVisibilityFilter.apply(Jenkins.getInstance(),answer);
    }
    public static Collection<Descriptor> getSortedDescriptorsForGlobalConfig() {
        return getSortedDescriptorsForGlobalConfig(Predicates.<GlobalConfigurationCategory>alwaysTrue());
    }
    @Deprecated
    public static Collection<Descriptor> getSortedDescriptorsForGlobalConfigNoSecurity() {
        return getSortedDescriptorsForGlobalConfig(Predicates.not(GlobalSecurityConfiguration.FILTER));
    }
    public static Collection<Descriptor> getSortedDescriptorsForGlobalConfigUnclassified() {
        return getSortedDescriptorsForGlobalConfig(new Predicate<GlobalConfigurationCategory>() {
            public boolean apply(GlobalConfigurationCategory cat) {
                return cat instanceof GlobalConfigurationCategory.Unclassified;
            }
        });
    }
    private static class Tag implements Comparable<Tag> {
        double ordinal;
        String hierarchy;
        Descriptor d;
        Tag(double ordinal, Descriptor d) {
            this.ordinal = ordinal;
            this.d = d;
            this.hierarchy = buildSuperclassHierarchy(d.clazz, new StringBuilder()).toString();
        }
        private StringBuilder buildSuperclassHierarchy(Class c, StringBuilder buf) {
            Class sc = c.getSuperclass();
            if (sc!=null)   buildSuperclassHierarchy(sc,buf).append(':');
            return buf.append(c.getName());
        }
        public int compareTo(Tag that) {
            int r = Double.compare(this.ordinal, that.ordinal);
            if (r!=0)   return -r; 
            return this.hierarchy.compareTo(that.hierarchy);
        }
    }
    public static String getIconFilePath(Action a) {
        String name = a.getIconFileName();
        if (name==null)     return null;
        if (name.startsWith("/"))
            return name.substring(1);
        else
            return "images/24x24/"+name;
    }
    public static int size2(Object o) throws Exception {
        if(o==null) return 0;
        return ASTSizeFunction.sizeOf(o,Introspector.getUberspect());
    }
    public static String getRelativeLinkTo(Item p) {
        Map<Object,String> ancestors = new HashMap<Object,String>();
        View view=null;
        StaplerRequest request = Stapler.getCurrentRequest();
        for( Ancestor a : request.getAncestors() ) {
            ancestors.put(a.getObject(),a.getRelativePath());
            if(a.getObject() instanceof View)
                view = (View) a.getObject();
        }
        String path = ancestors.get(p);
        if(path!=null)  return path;
        Item i=p;
        String url = "";
        while(true) {
            ItemGroup ig = i.getParent();
            url = i.getShortUrl()+url;
            if(ig== Jenkins.getInstance()) {
                assert i instanceof TopLevelItem;
                if(view!=null && view.contains((TopLevelItem)i)) {
                    return ancestors.get(view)+'/'+url;
                } else {
                    return request.getContextPath()+'/'+p.getUrl();
                }
            }
            path = ancestors.get(ig);
            if(path!=null)  return path+'/'+url;
            assert ig instanceof Item; 
            i = (Item) ig;
        }
    }
    public static Map<Thread,StackTraceElement[]> dumpAllThreads() {
        Map<Thread,StackTraceElement[]> sorted = new TreeMap<Thread,StackTraceElement[]>(new ThreadSorter());
        sorted.putAll(Thread.getAllStackTraces());
        return sorted;
    }
    @IgnoreJRERequirement
    public static ThreadInfo[] getThreadInfos() {
        ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
        return mbean.dumpAllThreads(mbean.isObjectMonitorUsageSupported(),mbean.isSynchronizerUsageSupported());
    }
    public static ThreadGroupMap sortThreadsAndGetGroupMap(ThreadInfo[] list) {
        ThreadGroupMap sorter = new ThreadGroupMap();
        Arrays.sort(list, sorter);
        return sorter;
    }
    private static class ThreadSorterBase {
        protected Map<Long,String> map = new HashMap<Long,String>();
        private ThreadSorterBase() {
            ThreadGroup tg = Thread.currentThread().getThreadGroup();
            while (tg.getParent() != null) tg = tg.getParent();
            Thread[] threads = new Thread[tg.activeCount()*2];
            int threadsLen = tg.enumerate(threads, true);
            for (int i = 0; i < threadsLen; i++)
                map.put(threads[i].getId(), threads[i].getThreadGroup().getName());
        }
        protected int compare(long idA, long idB) {
            String tga = map.get(idA), tgb = map.get(idB);
            int result = (tga!=null?-1:0) + (tgb!=null?1:0);  
            if (result==0 && tga!=null)
                result = tga.compareToIgnoreCase(tgb);
            return result;
        }
    }
    public static class ThreadGroupMap extends ThreadSorterBase implements Comparator<ThreadInfo> {
        public String getThreadGroup(ThreadInfo ti) {
            return map.get(ti.getThreadId());
        }
        public int compare(ThreadInfo a, ThreadInfo b) {
            int result = compare(a.getThreadId(), b.getThreadId());
            if (result == 0)
                result = a.getThreadName().compareToIgnoreCase(b.getThreadName());
            return result;
        }
    }
    private static class ThreadSorter extends ThreadSorterBase implements Comparator<Thread> {
        public int compare(Thread a, Thread b) {
            int result = compare(a.getId(), b.getId());
            if (result == 0)
                result = a.getName().compareToIgnoreCase(b.getName());
            return result;
        }
    }
    @IgnoreJRERequirement
    public static boolean isMustangOrAbove() {
        try {
            System.console();
            return true;
        } catch(LinkageError e) {
            return false;
        }
    }
    @IgnoreJRERequirement
    public static String dumpThreadInfo(ThreadInfo ti, ThreadGroupMap map) {
        String grp = map.getThreadGroup(ti);
        StringBuilder sb = new StringBuilder("\"" + ti.getThreadName() + "\"" +
                                             " Id=" + ti.getThreadId() + " Group=" +
                                             (grp != null ? grp : "?") + " " +
                                             ti.getThreadState());
        if (ti.getLockName() != null) {
            sb.append(" on " + ti.getLockName());
        }
        if (ti.getLockOwnerName() != null) {
            sb.append(" owned by \"" + ti.getLockOwnerName() +
                      "\" Id=" + ti.getLockOwnerId());
        }
        if (ti.isSuspended()) {
            sb.append(" (suspended)");
        }
        if (ti.isInNative()) {
            sb.append(" (in native)");
        }
        sb.append('\n');
        StackTraceElement[] stackTrace = ti.getStackTrace();
        for (int i=0; i < stackTrace.length; i++) {
            StackTraceElement ste = stackTrace[i];
            sb.append("\tat " + ste.toString());
            sb.append('\n');
            if (i == 0 && ti.getLockInfo() != null) {
                Thread.State ts = ti.getThreadState();
                switch (ts) {
                    case BLOCKED:
                        sb.append("\t-  blocked on " + ti.getLockInfo());
                        sb.append('\n');
                        break;
                    case WAITING:
                        sb.append("\t-  waiting on " + ti.getLockInfo());
                        sb.append('\n');
                        break;
                    case TIMED_WAITING:
                        sb.append("\t-  waiting on " + ti.getLockInfo());
                        sb.append('\n');
                        break;
                    default:
                }
            }
            for (MonitorInfo mi : ti.getLockedMonitors()) {
                if (mi.getLockedStackDepth() == i) {
                    sb.append("\t-  locked " + mi);
                    sb.append('\n');
                }
            }
       }
       LockInfo[] locks = ti.getLockedSynchronizers();
       if (locks.length > 0) {
           sb.append("\n\tNumber of locked synchronizers = " + locks.length);
           sb.append('\n');
           for (LockInfo li : locks) {
               sb.append("\t- " + li);
               sb.append('\n');
           }
       }
       sb.append('\n');
       return sb.toString();
    }
    public static <T> Collection<T> emptyList() {
        return Collections.emptyList();
    }
    public static String jsStringEscape(String s) {
        StringBuilder buf = new StringBuilder();
        for( int i=0; i<s.length(); i++ ) {
            char ch = s.charAt(i);
            switch(ch) {
            case '\'':
                buf.append("\\'");
                break;
            case '\\':
                buf.append("\\\\");
                break;
            case '"':
                buf.append("\\\"");
                break;
            default:
                buf.append(ch);
            }
        }
        return buf.toString();
    }
    public static String capitalize(String s) {
        if(s==null || s.length()==0) return s;
        return Character.toUpperCase(s.charAt(0))+s.substring(1);
    }
    public static String getVersion() {
        return Jenkins.VERSION;
    }
    public static String getResourcePath() {
        return Jenkins.RESOURCE_PATH;
    }
    public static String getViewResource(Object it, String path) {
        Class clazz = it.getClass();
        if(it instanceof Class)
            clazz = (Class)it;
        if(it instanceof Descriptor)
            clazz = ((Descriptor)it).clazz;
        StringBuilder buf = new StringBuilder(Stapler.getCurrentRequest().getContextPath());
        buf.append(Jenkins.VIEW_RESOURCE_PATH).append('/');
        buf.append(clazz.getName().replace('.','/').replace('$','/'));
        buf.append('/').append(path);
        return buf.toString();
    }
    public static boolean hasView(Object it, String path) throws IOException {
        if(it==null)    return false;
        return Stapler.getCurrentRequest().getView(it,path)!=null;
    }
    public static boolean defaultToTrue(Boolean b) {
        if(b==null) return true;
        return b;
    }
    public static <T> T defaulted(T value, T defaultValue) {
        return value!=null ? value : defaultValue;
    }
    public static String printThrowable(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
    public static int determineRows(String s) {
        if(s==null)     return 5;
        return Math.max(5,LINE_END.split(s).length);
    }
    public static String toCCStatus(Item i) {
        if (i instanceof Job) {
            Job j = (Job) i;
            switch (j.getIconColor().noAnime()) {
            case ABORTED:
            case RED:
            case YELLOW:
                return "Failure";
            case BLUE:
                return "Success";
            case DISABLED:
            case GREY:
                return "Unknown";
            }
        }
        return "Unknown";
    }
    private static final Pattern LINE_END = Pattern.compile("\r?\n");
    public static boolean isAnonymous() {
        return Jenkins.getAuthentication() instanceof AnonymousAuthenticationToken;
    }
    public static JellyContext getCurrentJellyContext() {
        JellyContext context = ExpressionFactory2.CURRENT_CONTEXT.get();
        assert context!=null;
        return context;
    }
    public static String runScript(Script script) throws JellyTagException {
        StringWriter out = new StringWriter();
        script.run(getCurrentJellyContext(), XMLOutput.createXMLOutput(out));
        return out.toString();
    }
    public static <T> List<T> subList(List<T> base, int maxSize) {
        if(maxSize<base.size())
            return base.subList(0,maxSize);
        else
            return base;
    }
    public static String joinPath(String... components) {
        StringBuilder buf = new StringBuilder();
        for (String s : components) {
            if (s.length()==0)  continue;
            if (buf.length()>0) {
                if (buf.charAt(buf.length()-1)!='/')
                    buf.append('/');
                if (s.charAt(0)=='/')   s=s.substring(1);
            }
            buf.append(s);
        }
        return buf.toString();
    }
    public static String getActionUrl(String itUrl,Action action) {
        String urlName = action.getUrlName();
        if(urlName==null)   return null;    
        try {
            if (new URI(urlName).isAbsolute()) {
                return urlName;
            }
        } catch (URISyntaxException x) {
            Logger.getLogger(Functions.class.getName()).log(Level.WARNING, "Failed to parse URL for {0}: {1}", new Object[] {action, x});
            return null;
        }
        if(urlName.startsWith("/"))
            return joinPath(Stapler.getCurrentRequest().getContextPath(),urlName);
        else
            return joinPath(Stapler.getCurrentRequest().getContextPath()+'/'+itUrl,urlName);
    }
    public static String toEmailSafeString(String projectName) {
        StringBuilder buf = new StringBuilder(projectName.length());
        for( int i=0; i<projectName.length(); i++ ) {
            char ch = projectName.charAt(i);
            if(('a'<=ch && ch<='z')
            || ('z'<=ch && ch<='Z')
            || ('0'<=ch && ch<='9')
            || "-_.".indexOf(ch)>=0)
                buf.append(ch);
            else
                buf.append('_');    
        }
        return projectName;
    }
    public String getSystemProperty(String key) {
        return System.getProperty(key);
    }
    public String getServerName() {
        String url = Jenkins.getInstance().getRootUrl();
        try {
            if(url!=null) {
                String host = new URL(url).getHost();
                if(host!=null)
                    return host;
            }
        } catch (MalformedURLException e) {
        }
        return Stapler.getCurrentRequest().getServerName();
    }
    public String getCheckUrl(String userDefined, Object descriptor, String field) {
        if(userDefined!=null || field==null)   return userDefined;
        if (descriptor instanceof Descriptor) {
            Descriptor d = (Descriptor) descriptor;
            return d.getCheckUrl(field);
        }
        return null;
    }
    public boolean hyperlinkMatchesCurrentPage(String href) throws UnsupportedEncodingException {
        String url = Stapler.getCurrentRequest().getRequestURL().toString();
        if (href == null || href.length() <= 1) return ".".equals(href) && url.endsWith("/");
        url = URLDecoder.decode(url,"UTF-8");
        href = URLDecoder.decode(href,"UTF-8");
        if (url.endsWith("/")) url = url.substring(0, url.length() - 1);
        if (href.endsWith("/")) href = href.substring(0, href.length() - 1);
        return url.endsWith(href);
    }
    public <T> List<T> singletonList(T t) {
        return Collections.singletonList(t);
    }
    public static List<PageDecorator> getPageDecorators() {
        if(Jenkins.getInstance()==null)  return Collections.emptyList();
        return PageDecorator.all();
    }
    public static List<Descriptor<Cloud>> getCloudDescriptors() {
        return Cloud.all();
    }
    public String prepend(String prefix, String body) {
        if(body!=null && body.length()>0)
            return prefix+body;
        return body;
    }
    public static List<Descriptor<CrumbIssuer>> getCrumbIssuerDescriptors() {
        return CrumbIssuer.all();
    }
    public static String getCrumb(StaplerRequest req) {
        Jenkins h = Jenkins.getInstance();
        CrumbIssuer issuer = h != null ? h.getCrumbIssuer() : null;
        return issuer != null ? issuer.getCrumb(req) : "";
    }
    public static String getCrumbRequestField() {
        Jenkins h = Jenkins.getInstance();
        CrumbIssuer issuer = h != null ? h.getCrumbIssuer() : null;
        return issuer != null ? issuer.getDescriptor().getCrumbRequestField() : "";
    }
    public static Date getCurrentTime() {
        return new Date();
    }
    public static Locale getCurrentLocale() {
        Locale locale=null;
        StaplerRequest req = Stapler.getCurrentRequest();
        if(req!=null)
            locale = req.getLocale();
        if(locale==null)
            locale = Locale.getDefault();
        return locale;
    }
    public static String generateConsoleAnnotationScriptAndStylesheet() {
        String cp = Stapler.getCurrentRequest().getContextPath();
        StringBuilder buf = new StringBuilder();
        for (ConsoleAnnotatorFactory f : ConsoleAnnotatorFactory.all()) {
            String path = cp + "/extensionList/" + ConsoleAnnotatorFactory.class.getName() + "/" + f.getClass().getName();
            if (f.hasScript())
                buf.append("<script src='"+path+"/script.js'></script>");
            if (f.hasStylesheet())
                buf.append("<link rel='stylesheet' type='text/css' href='"+path+"/style.css' />");
        }
        for (ConsoleAnnotationDescriptor d : ConsoleAnnotationDescriptor.all()) {
            String path = cp+"/descriptor/"+d.clazz.getName();
            if (d.hasScript())
                buf.append("<script src='"+path+"/script.js'></script>");
            if (d.hasStylesheet())
                buf.append("<link rel='stylesheet' type='text/css' href='"+path+"/style.css' />");
        }
        return buf.toString();
    }
    public List<String> getLoggerNames() {
        while (true) {
            try {
                List<String> r = new ArrayList<String>();
                Enumeration<String> e = LogManager.getLogManager().getLoggerNames();
                while (e.hasMoreElements())
                    r.add(e.nextElement());
                return r;
            } catch (ConcurrentModificationException e) {
            }
        }
    }
    public String getPasswordValue(Object o) {
        if (o==null)    return null;
        if (o instanceof Secret)    return ((Secret)o).getEncryptedValue();
        return o.toString();
    }
    public List filterDescriptors(Object context, Iterable descriptors) {
        return DescriptorVisibilityFilter.apply(context,descriptors);
    }
    public static boolean getIsUnitTest() {
        return Main.isUnitTest;
    }
    public static boolean isArtifactsPermissionEnabled() {
        return Boolean.getBoolean("hudson.security.ArtifactsPermission");
    }
    public static boolean isWipeOutPermissionEnabled() {
        return Boolean.getBoolean("hudson.security.WipeOutPermission");
    }
    public static String createRenderOnDemandProxy(JellyContext context, String attributesToCapture) {
        return Stapler.getCurrentRequest().createJavaScriptProxy(new RenderOnDemandClosure(context,attributesToCapture));
    }
    public static String getCurrentDescriptorByNameUrl() {
        return Descriptor.getCurrentDescriptorByNameUrl();
    }
    public static String setCurrentDescriptorByNameUrl(String value) {
        String o = getCurrentDescriptorByNameUrl();
        Stapler.getCurrentRequest().setAttribute("currentDescriptorByNameUrl", value);
        return o;
    }
    public static void restoreCurrentDescriptorByNameUrl(String old) {
        Stapler.getCurrentRequest().setAttribute("currentDescriptorByNameUrl", old);
    }
    public static List<String> getRequestHeaders(String name) {
        List<String> r = new ArrayList<String>();
        Enumeration e = Stapler.getCurrentRequest().getHeaders(name);
        while (e.hasMoreElements()) {
            r.add(e.nextElement().toString());
        }
        return r;
    }
    public static Object rawHtml(Object o) {
        return o==null ? null : new RawHtmlArgument(o);
    }
    public static ArrayList<CLICommand> getCLICommands() {
        ArrayList<CLICommand> all = new ArrayList<CLICommand>(CLICommand.all());
        Collections.sort(all, new Comparator<CLICommand>() {
            public int compare(CLICommand cliCommand, CLICommand cliCommand1) {
                return cliCommand.getName().compareTo(cliCommand1.getName());
            }
        });
        return all;
    }
    public static String getAvatar(User user, String avatarSize) {
        return UserAvatarResolver.resolve(user, avatarSize);
    }
    public String getUserAvatar(User user, String avatarSize) {
        return getAvatar(user,avatarSize);
    }
    public static String humanReadableByteSize(long size){
        String measure = "B";
        if(size < 1024){
            return size + " " + measure;
        }
        Double number = new Double(size);
        if(number>=1024){
            number = number/1024;
            measure = "KB";
            if(number>=1024){
                number = number/1024;
                measure = "MB";
                if(number>=1024){
                    number=number/1024;
                    measure = "GB";
                }
            }
        }
        DecimalFormat format = new DecimalFormat("#0.00");
        return format.format(number) + " " + measure;
    }
}
