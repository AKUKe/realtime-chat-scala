# RealTimeChatScala

RealTimeChatScala is a simple chat application that allows users to communicate using websockets in real time. Presently it only supports global chat and uses an in-memory H2 databse to track chat history.

The project is written in Scala and attempts to leverage the following frameworks/technologies:
<br/>
<br/>
[Play](https://www.playframework.com/) / [Akka](http://akka.io/) / [Slick](http://slick.typesafe.com/)

===================
Running locally
===================
<p>
You can run the application by using the Play console (https://www.playframework.com/documentation/2.5.x/PlayConsole)
</p>
```$ activator```

Followed by:

```$ run``` 

```
[info] p.c.s.NettyServer - Listening for HTTP on /0:0:0:0:0:0:0:0:9000

(Server started, use Ctrl+D to stop and go back to the console...)

[info] play.api.Play - Application started (Dev)
```
