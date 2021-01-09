# Hello

This tiny service can say *Hello*.

`http://localhost:8080/hello`

If you pass a text as the body and use `POST` instead of `GET`, the text is printed, too.

Finally you can add a delay in milliseconds like this:

`http://localhost:8080/hello/2000`

If you have Postman installed, you can get a collection [here](https://www.getpostman.com/collections/6cc80662ce1c2adf9a47)

To run the service get its [jar](https://github.com/tkuenneth/begleitmaterialien-zu-android-11/blob/master/kapitel_07/hello/target/hello-1.0-SNAPSHOT.jar) and execute it like this:

`java -jar hello-1.0-SNAPSHOT.jar` provided you changed to the directory that contains the downloaded file.