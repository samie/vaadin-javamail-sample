vaadin-error-mailer-sample
===========

Simple Vaadin application. This project demonstrates:
 * How to catch all uncaught exception in a Vaadin application
 * Send email from an Vaadin application using JavaMail (JSR 919) included in Java EE 7 (JSR342)

### Running the example:

Make sure you have installed [Maven](http://maven.apache.org/) and [Git](http://git-scm.com/) and WildFly application server (or some other Java EE 7 server) running on localhost.

    git clone https://github.com/samie/vaadin-error-mailer-sample.git
    cd vaadin-error-mailer-sample
    mvn wildfly:deploy
    
After these steps you have the application up and running at http://localhost:8080/vaadin-error-mailer-sample-1.0-SNAPSHOT

You can find the code for the application in [HelloWorldUI.java](src/main/java/org/vaadin/samples/errormailer/ErrorHandlingUI.java).

### License

This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to <[http://unlicense.org/](http://unlicense.org/)>
