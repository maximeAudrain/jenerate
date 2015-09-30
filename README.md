# Jenerate - Java Generation plugin for Eclipse

* [Introduction](#Introduction)
* [Installation](#Installation)
* [Changelog](#Changelog)
* [Copyright](#copyright)
* [Contributions](#Contributions)

## <a name="Introduction"/>Introduction

Jenerate is a Java Generation plugin for Eclipse that provides customizable code generation for Java classes.

Jenerate provides code generation for methods like:

* hashCode() and equals() (ALT+SHIFT+G then H)
* toString() (ALT+SHIFT+G then S)
* compareTo() (ALT+SHIFT+G then C)

The generated methods can use internal java.util.Objects class or external libraries such as Apache commons-lang[3] Builders and Google Guava.

A certain number of parameters can be customized in the Jenerate preference page in eclipse (Window > Preferences > Java > Jenerate).

## <a name="Installation"/>Installation

You can install the Jenerate plugin via the [Eclipse Marketplace](http://marketplace.eclipse.org/content/jenerate) or using the Jenerate update site from within Eclipse:
```
http://maximeaudrain.github.io/update/
```
You can access previous versions of the plugin at ```http://maximeaudrain.github.io/update/X.X.X```  where X.X.X is the version you want to retrieve 

## <a name="Changelog"/>Changelog

Version 1.0.1:
- Added generation strategies for hashCode() and equals() using the internal java.util.Objects class.
- Allow use of two new ToString styles provided with the version 3.4 of Apache Commons lang.

Version 1.0.0:
- Added Google Guava support for all methods content generation.
- Add tooltip on the toString dialog that shows how the output of the toString() would look like (based on the javadoc). (Contribution by mwensveen-nl)
- Add the option to use getClass instead of instanceof in the equals method. (Contribution by mwensveen-nl)
- Changed javadoc to use @inheritdoc instead of the old style of javadoc. (Contribution by melloware)

## <a name="Copyright"/>Copyright

Jenerate is a fork of [commons4e](https://github.com/jiayun/commons4e). It was forked to make it alive again and respond to users need for new functionalities. 

Jenerate is under the EPL v1.0.

## <a name="Contributions"/>Contributions

Any contributions are welcome!

The plugin is currently built using Gradle.

Please read the eclipse-config.txt in org.jenerate/setup folder : this is the current configuration I use for eclipse, so if you apply the same configuration, you should be able to commit without changes in terms of formatting/warnings/etc...

