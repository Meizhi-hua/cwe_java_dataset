package org.codehaus.plexus.util.cli.shell;
import org.codehaus.plexus.util.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class Shell
    implements Cloneable
{
    private static final char[] DEFAULT_QUOTING_TRIGGER_CHARS = { ' ' };
    private String shellCommand;
    private List<String> shellArgs = new ArrayList<String>();
    private boolean quotedArgumentsEnabled = true;
    private boolean unconditionallyQuote = false;
    private String executable;
    private String workingDir;
    private boolean quotedExecutableEnabled = true;
    private boolean doubleQuotedArgumentEscaped = false;
    private boolean singleQuotedArgumentEscaped = false;
    private boolean doubleQuotedExecutableEscaped = false;
    private boolean singleQuotedExecutableEscaped = false;
    private char argQuoteDelimiter = '\"';
    private char exeQuoteDelimiter = '\"';
    private String argumentEscapePattern = "\\%s";
    public void setUnconditionalQuoting(boolean unconditionallyQuote)
    {
        this.unconditionallyQuote = unconditionallyQuote;
    }
    public void setShellCommand( String shellCommand )
    {
        this.shellCommand = shellCommand;
    }
    public String getShellCommand()
    {
        return shellCommand;
    }
    public void setShellArgs( String[] shellArgs )
    {
        this.shellArgs.clear();
        this.shellArgs.addAll( Arrays.asList( shellArgs ) );
    }
    public String[] getShellArgs()
    {
        if ( ( shellArgs == null ) || shellArgs.isEmpty() )
        {
            return null;
        }
        else
        {
            return (String[]) shellArgs.toArray( new String[shellArgs.size()] );
        }
    }
    public List<String> getCommandLine( String executable, String[] arguments )
    {
        return getRawCommandLine( executable, arguments );
    }
    protected String quoteOneItem(String inputString, boolean isExecutable)
    {
        char[] escapeChars = getEscapeChars( isSingleQuotedExecutableEscaped(), isDoubleQuotedExecutableEscaped() );
        return StringUtils.quoteAndEscape(
            inputString,
            isExecutable ? getExecutableQuoteDelimiter() : getArgumentQuoteDelimiter(),
            escapeChars,
            getQuotingTriggerChars(),
            '\\',
            unconditionallyQuote
        );
    }
    protected List<String> getRawCommandLine( String executable, String[] arguments )
    {
        List<String> commandLine = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        if ( executable != null )
        {
            String preamble = getExecutionPreamble();
            if ( preamble != null )
            {
                sb.append( preamble );
            }
            if ( isQuotedExecutableEnabled() )
            {
                sb.append( quoteOneItem( getOriginalExecutable(), true ) );
            }
            else
            {
                sb.append( getExecutable() );
            }
        }
        for ( int i = 0; i < arguments.length; i++ )
        {
            if ( sb.length() > 0 )
            {
                sb.append( " " );
            }
            if ( isQuotedArgumentsEnabled() )
            {
                sb.append( quoteOneItem( arguments[i], false ) );
            }
            else
            {
                sb.append( arguments[i] );
            }
        }
        commandLine.add( sb.toString() );
        return commandLine;
    }
    protected char[] getQuotingTriggerChars()
    {
        return DEFAULT_QUOTING_TRIGGER_CHARS;
    }
    protected String getExecutionPreamble()
    {
        return null;
    }
    protected char[] getEscapeChars( boolean includeSingleQuote, boolean includeDoubleQuote )
    {
        StringBuilder buf = new StringBuilder( 2 );
        if ( includeSingleQuote )
        {
            buf.append( '\'' );
        }
        if ( includeDoubleQuote )
        {
            buf.append( '\"' );
        }
        char[] result = new char[buf.length()];
        buf.getChars( 0, buf.length(), result, 0 );
        return result;
    }
    protected boolean isDoubleQuotedArgumentEscaped()
    {
        return doubleQuotedArgumentEscaped;
    }
    protected boolean isSingleQuotedArgumentEscaped()
    {
        return singleQuotedArgumentEscaped;
    }
    protected boolean isDoubleQuotedExecutableEscaped()
    {
        return doubleQuotedExecutableEscaped;
    }
    protected boolean isSingleQuotedExecutableEscaped()
    {
        return singleQuotedExecutableEscaped;
    }
    protected void setArgumentQuoteDelimiter( char argQuoteDelimiter )
    {
        this.argQuoteDelimiter = argQuoteDelimiter;
    }
    protected char getArgumentQuoteDelimiter()
    {
        return argQuoteDelimiter;
    }
    protected void setExecutableQuoteDelimiter( char exeQuoteDelimiter )
    {
        this.exeQuoteDelimiter = exeQuoteDelimiter;
    }
    protected char getExecutableQuoteDelimiter()
    {
        return exeQuoteDelimiter;
    }
    protected void setArgumentEscapePattern(String argumentEscapePattern)
    {
        this.argumentEscapePattern = argumentEscapePattern;
    }
    protected String getArgumentEscapePattern() {
        return argumentEscapePattern;
    }
    public List<String> getShellCommandLine( String[] arguments )
    {
        List<String> commandLine = new ArrayList<String>();
        if ( getShellCommand() != null )
        {
            commandLine.add( getShellCommand() );
        }
        if ( getShellArgs() != null )
        {
            commandLine.addAll( getShellArgsList() );
        }
        commandLine.addAll( getCommandLine( getOriginalExecutable(), arguments ) );
        return commandLine;
    }
    public List<String> getShellArgsList()
    {
        return shellArgs;
    }
    public void addShellArg( String arg )
    {
        shellArgs.add( arg );
    }
    public void setQuotedArgumentsEnabled( boolean quotedArgumentsEnabled )
    {
        this.quotedArgumentsEnabled = quotedArgumentsEnabled;
    }
    public boolean isQuotedArgumentsEnabled()
    {
        return quotedArgumentsEnabled;
    }
    public void setQuotedExecutableEnabled( boolean quotedExecutableEnabled )
    {
        this.quotedExecutableEnabled = quotedExecutableEnabled;
    }
    public boolean isQuotedExecutableEnabled()
    {
        return quotedExecutableEnabled;
    }
    public void setExecutable( String executable )
    {
        if ( ( executable == null ) || ( executable.length() == 0 ) )
        {
            return;
        }
        this.executable = executable.replace( '/', File.separatorChar ).replace( '\\', File.separatorChar );
    }
    public String getExecutable()
    {
        return executable;
    }
    public void setWorkingDirectory( String path )
    {
        if ( path != null )
        {
            workingDir = path;
        }
    }
    public void setWorkingDirectory( File workingDir )
    {
        if ( workingDir != null )
        {
            this.workingDir = workingDir.getAbsolutePath();
        }
    }
    public File getWorkingDirectory()
    {
        return workingDir == null ? null : new File( workingDir );
    }
    public String getWorkingDirectoryAsString()
    {
        return workingDir;
    }
    public void clearArguments()
    {
        shellArgs.clear();
    }
    public Object clone()
    {
        Shell shell = new Shell();
        shell.setExecutable( getExecutable() );
        shell.setWorkingDirectory( getWorkingDirectory() );
        shell.setShellArgs( getShellArgs() );
        return shell;
    }
    public String getOriginalExecutable()
    {
        return executable;
    }
    public List<String> getOriginalCommandLine( String executable, String[] arguments )
    {
        return getRawCommandLine( executable, arguments );
    }
    protected void setDoubleQuotedArgumentEscaped( boolean doubleQuotedArgumentEscaped )
    {
        this.doubleQuotedArgumentEscaped = doubleQuotedArgumentEscaped;
    }
    protected void setDoubleQuotedExecutableEscaped( boolean doubleQuotedExecutableEscaped )
    {
        this.doubleQuotedExecutableEscaped = doubleQuotedExecutableEscaped;
    }
    protected void setSingleQuotedArgumentEscaped( boolean singleQuotedArgumentEscaped )
    {
        this.singleQuotedArgumentEscaped = singleQuotedArgumentEscaped;
    }
    protected void setSingleQuotedExecutableEscaped( boolean singleQuotedExecutableEscaped )
    {
        this.singleQuotedExecutableEscaped = singleQuotedExecutableEscaped;
    }
}
