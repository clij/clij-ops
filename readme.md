## CLIJ plugin template

This repository contains a simple CLIJ plugin. Clone it to make your own [CLIJ](https://github.com/haesleinhuepf/ClearCLIJ) 
plugin and run your [OpenCL](https://www.khronos.org/opencl/) code from ImageJ macro.

Just open pom.xml and enter your name, domain, the name of your plugin etc. Afterwards, navigate to PluginTemplate.java 
to inspect the API of a basic CLIJ plugin. Furthermore, you find example OpenCL code in ClearCLs dialect in template.cl.

In order to deploy your plugin to your Fiji installation, enter the correct path of your Fiji to the pom file:

```xml
<imagej.app.directory>C:/programs/fiji-win64/Fiji.app/</imagej.app.directory>
```

Afterwards, run

```
mvn install
```

Restart Fiji and check using this macro if your plugin was installed successfully:

```java
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ_help("pluginTemplate");
```

The ImageJ log window should then output something like:

```java
Found 1 method(s) containing the pattern "pluginTemplate":
Ext.CLIJ_pluginTemplate(Image source, Image destination, Number scalar);
```

In case of any issues, just leave a github issue or drop a mail to rhaase@mpi-cbg.de
