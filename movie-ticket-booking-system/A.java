[INFO] --- clean:3.3.2:clean (default-clean) @ movie-ticket-booking-system ---
[INFO] Deleting C:\Users\LibraryUser\git\movie_backend\movie-ticket-booking-system\target
[INFO]
[INFO] --- resources:3.3.1:resources (default-resources) @ movie-ticket-booking-system ---
[INFO] Copying 1 resource from src\main\resources to target\classes
[INFO] Copying 0 resource from src\main\resources to target\classes
[INFO]
[INFO] --- compiler:3.13.0:compile (default-compile) @ movie-ticket-booking-system ---
[INFO] Recompiling the module because of changed source code.
[INFO] Compiling 63 source files with javac [debug parameters release 17] to target\classes
[INFO] Annotation processing is enabled because one or more processors were found
  on the class path. A future release of javac may disable annotation processing
  unless at least one processor is specified by name (-processor), or a search
  path is specified (--processor-path, --processor-module-path), or annotation
  processing is enabled explicitly (-proc:only, -proc:full).
  Use -Xlint:-options to suppress this message.
  Use -proc:none to disable annotation processing.
[INFO] -------------------------------------------------------------
[WARNING] COMPILATION WARNING :
[INFO] -------------------------------------------------------------
[WARNING] /C:/Users/LibraryUser/git/movie_backend/movie-ticket-booking-system/src/main/java/com/jts/movie/entities/Movie.java:[64,24] @Builder will 
ignore the initializing expression entirely. If you want the initializing expression to serve as default, add @Builder.Default. If it is not supposed to be settable during building, make the field final.
[WARNING] /C:/Users/LibraryUser/git/movie_backend/movie-ticket-booking-system/src/main/java/com/jts/movie/entities/Show.java:[47,28] @Builder will i
gnore the initializing expression entirely. If you want the initializing expression to serve as default, add @Builder.Default. If it is not supposed to be settable during building, make the field final.
[WARNING] /C:/Users/LibraryUser/git/movie_backend/movie-ticket-booking-system/src/main/java/com/jts/movie/entities/Show.java:[50,26] @Builder will i
gnore the initializing expression entirely. If you want the initializing expression to serve as default, add @Builder.Default. If it is not supposed to be settable during building, make the field final.
[WARNING] /C:/Users/LibraryUser/git/movie_backend/movie-ticket-booking-system/src/main/java/com/jts/movie/entities/Theater.java:[30,31] @Builder wil
l ignore the initializing expression entirely. If you want the initializing expression to serve as default, add @Builder.Default. If it is not supposed to be settable during building, make the field final.
[WARNING] /C:/Users/LibraryUser/git/movie_backend/movie-ticket-booking-system/src/main/java/com/jts/movie/entities/Theater.java:[33,24] @Builder wil
l ignore the initializing expression entirely. If you want the initializing expression to serve as default, add @Builder.Default. If it is not supposed to be settable during building, make the field final.
[INFO] 5 warnings
[INFO] -------------------------------------------------------------
[INFO] -------------------------------------------------------------
[ERROR] COMPILATION ERROR :
[INFO] -------------------------------------------------------------
[ERROR] /C:/Users/LibraryUser/git/movie_backend/movie-ticket-booking-system/src/main/java/com/jts/movie/entities/User.java:[19,1] no suitable constr
uctor found for User(java.lang.Integer,java.lang.String,java.lang.Integer,java.lang.String,com.jts.movie.enums.gender,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.Boolean,java.lang.String)
    constructor com.jts.movie.entities.User.User() is not applicable
      (actual and formal argument lists differ in length)
    constructor com.jts.movie.entities.User.User(java.lang.Integer,java.lang.String,java.lang.Integer,java.lang.String,com.jts.movie.enums.gender,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.Boolean) is not applicable
      (actual and formal argument lists differ in length)
[ERROR] /C:/Users/LibraryUser/git/movie_backend/movie-ticket-booking-system/src/main/java/com/jts/movie/controller/UserController.java:[58,61] cannot find symbol
  symbol:   method findByConfirmationToken(java.lang.String)
  location: variable userRepository of type com.jts.movie.repositories.UserRepository
[ERROR] /C:/Users/LibraryUser/git/movie_backend/movie-ticket-booking-system/src/main/java/com/jts/movie/services/UserService.java:[59,33] cannot find symbol
  symbol:   method isActive(boolean)
  location: class com.jts.movie.entities.User.UserBuilder
[ERROR] /C:/Users/LibraryUser/git/movie_backend/movie-ticket-booking-system/src/main/java/com/jts/movie/services/UserService.java:[112,21] cannot find symbol
  symbol:   method setIsActive(boolean)
  location: variable user of type com.jts.movie.entities.User
[INFO] 4 errors
[INFO] -------------------------------------------------------------
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  6.352 s
[INFO] Finished at: 2024-10-06T15:44:27-04:00
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.13.0:compile (default-compile) on project movie-ticket-booking-system: Compilation failure: Compilation failure:
[ERROR] /C:/Users/LibraryUser/git/movie_backend/movie-ticket-booking-system/src/main/java/com/jts/movie/entities/User.java:[19,1] no suitable constr
uctor found for User(java.lang.Integer,java.lang.String,java.lang.Integer,java.lang.String,com.jts.movie.enums.gender,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.Boolean,java.lang.String)
[ERROR]     constructor com.jts.movie.entities.User.User() is not applicable
[ERROR]       (actual and formal argument lists differ in length)
[ERROR]     constructor com.jts.movie.entities.User.User(java.lang.Integer,java.lang.String,java.lang.Integer,java.lang.String,com.jts.movie.enums.gender,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.Boolean) is not applicable
[ERROR]       (actual and formal argument lists differ in length)
[ERROR] /C:/Users/LibraryUser/git/movie_backend/movie-ticket-booking-system/src/main/java/com/jts/movie/controller/UserController.java:[58,61] cannot find symbol
[ERROR]   symbol:   method findByConfirmationToken(java.lang.String)
[ERROR]   location: variable userRepository of type com.jts.movie.repositories.UserRepository
[ERROR] /C:/Users/LibraryUser/git/movie_backend/movie-ticket-booking-system/src/main/java/com/jts/movie/services/UserService.java:[59,33] cannot find symbol
[ERROR]   symbol:   method isActive(boolean)
[ERROR]   location: class com.jts.movie.entities.User.UserBuilder
[ERROR] /C:/Users/LibraryUser/git/movie_backend/movie-ticket-booking-system/src/main/java/com/jts/movie/services/UserService.java:[112,21] cannot find symbol
[ERROR]   symbol:   method setIsActive(boolean)
[ERROR]   location: variable user of type com.jts.movie.entities.User
[ERROR] -> [Help 1]
[ERROR]
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR]
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoFailureException
