### 1) Project ###
  * With maven:<br>
Add this to your dependencies:<br>
<pre><code>&lt;dependency&gt;<br>
	&lt;groupId&gt;com.am&lt;/groupId&gt;<br>
	&lt;artifactId&gt;atmosphere-gwt-domain&lt;/artifactId&gt;<br>
	&lt;version&gt;0.1.2&lt;/version&gt;<br>
&lt;/dependency&gt;<br>
</code></pre>
Add this to your repositories:<br>
<pre><code>&lt;repository&gt;<br>
	&lt;id&gt;atmosphere gwt domain repository&lt;/id&gt;<br>
	&lt;url&gt;https://atmosphere-gwt-domain.googlecode.com/svn/mvnrepo&lt;/url&gt;<br>
&lt;/repository&gt;<br>
</code></pre>
<ul><li>Without maven:<br>
download the jar in <a href='http://code.google.com/p/atmosphere-gwt-domain/downloads'>downloads</a><br>
add it to your classpath and in the web-inf/lib directory of your war<br>
(do the same for the dependencies of the project)<br><br>
<h3>2) Module</h3>
Inherit from the module in your .gwt.xml:<br>
<pre><code>&lt;inherits name="com.am.atmospheregwtdomain.Atmosphere_gwt_domain" /&gt;<br>
</code></pre>
<h3>3) Web configuration</h3>
</li><li>atmosphere-gwt-domain rpc<br>
in your WEB-INF/web.xml<br>
<pre><code>&lt;servlet&gt;<br>
        &lt;servlet-name&gt;AtmosphereGwtDomainRPC&lt;/servlet-name&gt;<br>
        &lt;servlet-class&gt;com.am.atmospheregwtdomain.server.AtmosphereGwtServiceImpl&lt;/servlet-class&gt;<br>
&lt;/servlet&gt;<br>
&lt;servlet-mapping&gt;<br>
	&lt;servlet-name&gt;AtmosphereGwtDomainRPC&lt;/servlet-name&gt;<br>
	&lt;url-pattern&gt;/your_module_name/atmospheregwtservice&lt;/url-pattern&gt;<br>
&lt;/servlet-mapping&gt;<br>
</code></pre>
</li><li>atmosphere<br>
in your WEB-INF/web.xml<br>
<pre><code>&lt;servlet&gt;<br>
	&lt;servlet-name&gt;atmosphere-servlet&lt;/servlet-name&gt;<br>
	&lt;servlet-class&gt;org.atmosphere.cpr.AtmosphereServlet&lt;/servlet-class&gt;<br>
&lt;/servlet&gt;<br>
&lt;servlet-mapping&gt;<br>
	&lt;servlet-name&gt;atmosphere-servlet&lt;/servlet-name&gt;<br>
	&lt;url-pattern&gt;/your_module_name/atmosphere&lt;/url-pattern&gt;<br>
&lt;/servlet-mapping&gt;<br>
</code></pre>
in the META-INF/atmosphere.xml<br>
<pre><code>&lt;atmosphere-handlers&gt;<br>
	&lt;atmosphere-handler context-root="/your_module_name/atmosphere" class-name="org.atmosphere.handler.ReflectorServletProcessor"&gt;<br>
		&lt;property name="servletClass" value="com.am.atmospheregwtdomain.server.AtmosphereManagerLink" /&gt;<br>
	&lt;/atmosphere-handler&gt;<br>
&lt;/atmosphere-handlers&gt;<br>
</code></pre>
<i>you can check the details about the servlet configuration of <a href='https://atmosphere.dev.java.net/'>atmosphere</a> on the <a href='https://atmosphere.dev.java.net/atmosphere_whitepaper.pdf'>atmosphere whitepaper</a></i>