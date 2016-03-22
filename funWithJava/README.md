# funWithJava

Project Description:
-----
Fun is a simple project where a URL is crawled for email addresses, with the goal of implementing
fun in several languages. In this case, this implementation is in java, making use of Maven and
java 1.8.


Project Requirements:
-----
    + maven: The build tool used for dependency management and building.
    + java 1.8: The project makes use of Lambda's which is a new feature of java 1.8
    + Firefox should be preinstalled. The browser is required for executing the crawl

    README.md : Readme for this project


Notes:
-----
Originally I had intended to leverage Apache's httpcomponents library to Retrieve the content and scrape it manually.
However, this type of manual crawling does not actually handle javascript, which is a major component of any modern
webpage. This gave me the opportunity to work with various web page testing frameworks. I opted to use crawljax since
its API was simple to use. Crawljax is a wrapper around selenium, a popular web page testing framework.

I also tried using PhantomJS, a popular Headless Browser to test the page. However, I found that it was not fault tolerant
against javascript issues or bad formatting problems with JSON.

Logging:
-----
The project uses logback to manage all output by the application. Logback is a successor to the popular log4j framework.
See <a href="http://logback.qos.ch/index.html">Logback home</a>

The logback.xml configuration is located under src/main/resources folder.

To change the reporting of email addresses, change the "REPORT" appender to the desired logging format.

Other logging is sent to the appender "FILE" which logs output to the file "crawlerResponse.txt";

Executing the Project:
-----
After forking the project locally, run the following

    $ mvn clean install
    $ cd target
    $ java -cp funWithJava-1.0-SNAPSHOT-jar-with-dependencies.jar com.leonkay.fun.EmailFinderConsole <URL>

Where \<URL> is the url you want to crawl for content.
