Experior
===========================================

_Create tests the way you already know, only better_

Experior is an editor for [FitNesse](http://fitnesse.org) with richer
features than the one already integrated in the FitNesse web interface. 

**This project can be considered _abandoned_.**



Another tool. Just what I needed...
-----------------------------------

Experior does not aim to provide a whole new UI paradigm to learn for creating tests. FitNesse'
own wiki syntax in plain text is just fine. Instead, the authors of Experior wished to provide
testers with some of the tool support that programmers take for granted and enjoy when they
write and read code, e.g:

  * syntax highliting
  * various font enhancing to visualize structure
  * automatic formatting
  * available vocabulary

Experior is thus still about plain text editing. This, and that it integrates into the
existing FitNesse interface, means that _it will work with existing tests_, and there
is virtually _no learning curve_ and no change in work process for existing FitNesse
users to start using Experior.



FitNesse versions
-------------------------

Experior has been tested to be compatible with the version 20080702 of FitNesse available in
the [http://repo1.maven.org/maven2/org/fitnesse/fitnesse/20080702/ Maven Central Repository],
and also version 20090214 available for download from the
[http://fitnesse.org/FrontPage.FitNesseDevelopment.DownLoad FitNesse homepage]. The compatibility
was however broken with version 20090321 because of a class in FitNesse, which Experior depends on,
changing its package. It is however an easy fix if you download the source code and build the
project with the FitNesse version of your choice. In most IDEs which support automatic organization
of imports, the fix is pretty much done for you (Ctrl+Shift+O in Eclipse).

**Note: The plugin requires Mozilla Firefox.**
