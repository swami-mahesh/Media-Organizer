# Media-Organizer

[![Build Status](https://travis-ci.org/swami-mahesh/Media-Organizer.svg?branch=master)](https://travis-ci.org/swami-mahesh/Media-Organizer)

 Media-Organizer is a library and a command line tool to organise your media ( Photos and videos ) better.

Have you ever felt frustrated with managing your media like millions of photos and media scattered across different storage mediums/ hard disks?
Wouldn't it be good to have it organised automatically, grouped by date taken by inspecting the Image or Video file.

# Features!
  - Copy/Move Photos & Videos from Multiple Source folders at once.
  - Copy/Move Photos & Videos to Multiple detsination folders to store & organize the media.
  - Configure Types of media files to look for ( Ex : JPEG, PNG, MP4 ....)
  - Configure whether to MOVE or COPY the images & videos from source to destination
  - Five different Overwrite options available in case duplicate file is found in destination
  - Ability to have a dry run before actually moving/copying the media. It is very helpful in case you want to have a look at what the tool would actually do if you were to run it without moving/copying the media.
  - Ability to specify concurrency level (Parallelism) required to process files. Very effective if you are running on machine with more than one core.

### Tech
 Media-Organizer uses a number of open source projects to work properly:

* https://www.sno.phy.queensu.ca/~phil/exiftool/ - The Amazing Exif Tool by Phil Harvey
* https://github.com/mjeanroy/exiftool
* https://github.com/google/guava
* http://jcommander.org/
* https://bitbucket.org/asomov/snakeyaml


### Dependencies
Following softwares are required to be installed on your machine.

* Java - (Minimum JDK 8)
* Exif Tool - https://www.sno.phy.queensu.ca/~phil/exiftool/

### Installation
This library is available on maven repository:

    <dependency>
        <groupId>com.github.swami-mahesh</groupId>
        <artifactId>mediaorganizer</artifactId>
        <version>1.0</version>
    </dependency>

### Library Usage
Media-Organizer could be used as a library by your java application. It is very easy to use.
Example :

         final MediaOrganizerBuilder builder = new MediaOrganizerBuilder().
                usingExifTool("/usr/local/exiftool").
                forImageFileExtensions("JPG", "PNG").
                forVideoFileExtensions("MP4", "MOV", "MKV").
                fromSource("/home/user/pictures1", "/tmp/pics_videos").
                saveImagesTo("/home/user/masterphotos","/tmp/anothermediabackup").
                saveVideosTo("/home/user/mastervideos", "/tmp/anothermediabackup").
                usingOverwriteMode("OVERWRITE_ONLY_IF_EXACT_SAME_FILE_ELSE_COPY").
                usingCopyMode("MOVE").build();
        mediaOrganizer.run();
        //Dont forget to add a shutdown hook for a clean exit
                Runtime.getRuntime().addShutdownHook(new NamedThreadFactory("Shutdown Hook").newThread(mediaOrganizer::shutdown));


### Command line tool Usage
Media-Organizer could be run as a command line tool. It is very easy to use.
Example :

Short options

    java MediaOrganizerTool -exif /usr/local/exiftool -from /home/user/pictures1 -from /tmp/pics_videos -imagesto /home/user/masterphotos -imagesto /tmp/anothermediabackup -videosto /home/user/mastervideos -videosto /tmp/anothermediabackup -c COPY_DRY_RUN -o OVERWRITE_ONLY_IF_EXACT_SAME_FILE_ELSE_COPY
 

Long Options

    java MediaOrganizerTool --exiftoolpath /usr/local/exiftool --sourcedirectory /home/user/pictures1 --sourcedirectory /tmp/pics_videos --targetimagesdirectory /home/user/masterphotos --targetimagesdirectory /tmp/anothermediabackup --targetvideosdirectory /home/user/mastervideos --targetvideosdirectory /tmp/anothermediabackup --copymode COPY_DRY_RUN --overwrite OVERWRITE_ONLY_IF_EXACT_SAME_FILE_ELSE_COPY
        
### Library : Configurable parameters
When building the MediaOrganizer object as mentioned in the example above, Following parameters could be configured as below

| Builder Method  | Description | Required ? |Default Value |
| ------------- | ------------- | ------------- |------------- |
| usingExifTool  | Path to the Exif Tool Installation   | Yes  |Empty |
| fromSource  | Absolute path to multiple source directories containing media which needs to be organized.   | Yes  |Empty |
| saveImagesTo  | Absolute path to multiple destination directories which will have images organized.   | Yes  |Empty |
| saveVideosTo  | Absolute path to multiple destination directories which will have videos organized.   | Yes  |Empty |
| usingCopyMode  | "COPY" to copy media from source to destination folder.<br/> "MOVE" to move media from source to destination folder.<br/> "COPY_DRY_RUN" to dry run copy (No actual copy) media from source to destination folder.<br/> "MOVE_DRY_RUN" to dry run move (No actual move) media from source to destination folder. | No  |COPY_DRY_RUN |
| usingOverwriteMode  | In case if there is a file name conflict while copying/moving. Following are the stratgies to chose from.<br/> "DO_NOT_OVERWRITE" if you dont want to overwrite files in destination directory.<br/> "DO_NOT_OVERWRITE_COPY"  if you dont want to overwrite files in destination directory but instead make a copy with unique name.<br/> "OVERWRITE_ONLY_IF_FILE_SIZE_MATCH_ELSE_COPY" if you want to overwrite the file if source & destination files have exactly the same SIZE else make a copy with unique name.<br/> "OVERWRITE_ONLY_IF_EXACT_SAME_FILE_ELSE_COPY" if you want to overwrite the file if source & destination files have exactly the same CONTENTS else make a copy with unique name.<br/> ALWAYS_OVERWRITE if you want to overwrite the file always. (Dangerous. Use with care !!!)| No  |DO_NOT_OVERWRITE |
| forImageFileExtensions  | Image File types in the source folders. Case Insensitive File extensions.  | No  | "png", "gif", "jpg","jpeg", "tiff"|
| forVideoFileExtensions  | Video File types in the source folders. Case Insensitive File extensions.  | No  | "vob", "webm","mkv", "wmv", "mpeg", "mpg", "flv", "mp4", "mts", "mov", "3gp", "avi"|
| usingExecutorService  | An Executor Service to process files in parallel. <br/>Your application has responsibility to manage the lifecycle of the passed Executor service  | No  | null. If this parameter is not specified, library would create an internal executor based on parameter passed to withConcurrencyLevel|
| withConcurrencyLevel  | Concurrency level to process files in parallel. <br/> Only respected if  usingExecutorService is NOT used. Use a value 0 if you do not want the library to create any threads. | No  | Number of cores + 1|
### Command line tool : Configurable parameters
When running the CLI, Following parameters could be specified as below

| Short Option | Long Option  | Description | Required ? |Default Value |
| ------------- | ------------- | ------------- | ------------- |------------- |
| -h  | --help  | Prints Usage   | N/A  |N/A |
| -exif  | --exiftoolpath  | Path to the Exif Tool Installation   | Yes  |Empty |
| -from  | --sourcedirectory  | (Multiple) Absolute path to source directories containing media which needs to be organized.   | Yes  |Empty |
| -imagesto  | --targetimagesdirectory  | (Multiple) Absolute path to destination directories which will have images organized.   | Yes  |Empty |
| -videosto  | --targetvideosdirectory  | (Multiple) Absolute path to destination directories which will have videos organized.   | Yes  |Empty |
| -c  | --copymode  | "COPY" to copy media from source to destination folder.<br/> "MOVE" to move media from source to destination folder.<br/> "COPY_DRY_RUN" to dry run copy (No actual copy) media from source to destination folder.<br/> "MOVE_DRY_RUN" to dry run move (No actual move) media from source to destination folder. | No  |COPY_DRY_RUN |
| -o  | --overwrite  | In case if there is a file name conflict while copying/moving. Following are the stratgies to chose from.<br/> "DO_NOT_OVERWRITE" if you dont want to overwrite files in destination directory.<br/> "DO_NOT_OVERWRITE_COPY"  if you dont want to overwrite files in destination directory but instead make a copy with unique name.<br/> "OVERWRITE_ONLY_IF_FILE_SIZE_MATCH_ELSE_COPY" if you want to overwrite the file if source & destination files have exactly the same SIZE else make a copy with unique name.<br/> "OVERWRITE_ONLY_IF_EXACT_SAME_FILE_ELSE_COPY" if you want to overwrite the file if source & destination files have exactly the same CONTENTS else make a copy with unique name.<br/> ALWAYS_OVERWRITE if you want to overwrite the file always. (Dangerous. Use with care !!!)| No  |DO_NOT_OVERWRITE |
| -it  | --imagetype  | Image File types in the source folders. Case Insensitive File extensions.  | No  | "png", "gif", "jpg","jpeg", "tiff"|
| -vt  | --videotype  | Video File types in the source folders. Case Insensitive File extensions.  | No  | "vob", "webm","mkv", "wmv", "mpeg", "mpg", "flv", "mp4", "mts", "mov", "3gp", "avi"|
 | --concurrencyLevel  | -p | Concurrency level to process files in parallel. <br/> Use a value 0 if you do not want the library to create any threads. | No  | Number of cores + 1|


### Concurrency/ Threading model
When using the library, for faster processing, it is advisable to run using a thread pool.
Users have two options. First is to specify a Thread pool (ExecutorService) to the library. It would then be used to process files in parallel. Users have the responsibility for life cycle management of this pool though. The library would not shut down the pool after processing finishes.
Second options is to specify a concurrency level. If no concurrency level is specified, the library creates an internal thread pool of size (Number of cores + 1). It takes care of shutting down the pool after processing finishes. If you dont want the library to create any thread pool , specify a concurrency level of 0 explicitly.(BEWARE: this would be slow if you have large number of files to be organized)
If both the Executor Service & a concurrency level is specified, the Executor Service would be used & concurrency level is ignored.

When using the command line tool, users can specify a concurrency level. If no concurrency level is specified, the tool creates an internal thread pool of size (Number of cores + 1). It takes care of shutting down the pool after processing finishes. If you dont want the tool to create any thread pool , specify a concurrency level of 0 explicitly. (BEWARE: this would be slow if you have large number of files to be organized)

### Sample application built using library
A sample application using a Yaml file to specify the configuration is also included in the repo. It could be run as

    java com.github.swamim.media.examples.app.staticyaml.Application config.yaml
A sample config.yaml is also available under resources in the github repo.

### What's next
  - A simple GUI tool
  - More organization options. Currently the files are arranged in folders with YYYY-MM-DD format only.

