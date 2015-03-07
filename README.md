# Jenerate - Java Generation plugin for Eclipse

Jenerate provides customizable code generation for Java classes.

* [Features](#features)
* [Jenerate todo list](#todo)
* [Copyright and contributions](#copyright)

## <a name="features"/>Features

Jenerate provides code generation for methods like:
* hashCode() and equals()
* toString()
* compareTo()

The generated methods are using apache commons-lang[3] Builders. 

A certain number of parameters can be customized in the Jenerate preference page in eclipse (Window > Preferences > Java > Jenerate).

## <a name="todo"/>Jenerate todo list

* Test the plugin core code (code generation)
* Provide additional libraries to be used in the generated code (Guava Objects and MoreObjects, etc...)
* Extend code generation to other common methods or needs.

## <a name="copyright"/>Copyright and contributions

Jenerate is a fork of [commons4e](https://github.com/jiayun/commons4e). It was forked to make it alive again and respond to users need for new functionalities. 

Jenerate is under the EPL v1.0.

Any contributions are welcome!

