FROM openjdk:17
EXPOSE 8080
ADD ./Unsolo-rockets/target/Unsolo-rockets.jar Unsolo-rockets.jar
ENTRYPOINT ["java","-jar","Unsolo-rockets.jar"]

CMD java -cp target com.interswitch.Unsolorockets.UserServiceApplication