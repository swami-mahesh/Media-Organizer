# Media-Organizer

[![Build Status](https://travis-ci.org/swami-mahesh/Media-Organizer.svg?branch=master)](https://travis-ci.org/swami-mahesh/Media-Organizer)

 Media-Organizer is a library to organise your media ( Photos and videos ) better.

Have you ever felt frustrated with managing your media like millions of photos and media scattered across different storage mediums/ hard disks?
Wouldn't it be good to have it organised automatically, grouped by date taken by inspecting the Image or Video file.

# Features!
  - Copy/Move Photos & Videos from Multiple Source folders at once.
  - Copy/Move Photos & Videos to Multiple detsination folders to store & organize the media.
  - Configure Types of media files to look for ( Ex : JPEG, PNG, MP4 ....)
  - Configure whether to MOVE or COPY the images & videos from source to destination
  - Five different Overwrite options available in case duplicate file is found in destination
  - Ability to have a dry run before actually moving/copying the media. It is very helpful in case you want to have a look at what the tool would actually do if you were to run it without moving/copying the media.

### Tech
 Media-Organizer uses a number of open source projects to work properly:

* https://www.sno.phy.queensu.ca/~phil/exiftool/ - The Amazing Exif Tool by Phil Harvey
* https://github.com/mjeanroy/exiftool
* https://github.com/google/guava

### Dependencies
Following softwares are required to be installed on your machine.

* Java - (Minimum JDK 8)
* Exif Tool - https://www.sno.phy.queensu.ca/~phil/exiftool/

### Installation
This library is available on maven repository:

    <dependency>
        <groupId>com.mediaorganizer</groupId>
        <artifactId>organizer</artifactId>
        <version>1.0</version>
    </dependency>

### Usage
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

### Configurable parameters
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



### Sample application built using library
A sample application using a Yaml file to specify the configuration is also included in the repo. It could be run as

    java com.mediaorganizer.organizer.app.staticyaml.Application config.yaml
A sample config.yaml is also available under resources in the github repo.

### What's next
  - Unit tests!!!!
  - More refactoring
  - Full Fledged Command line tool
  - A simple GUI tool
  - More organization options. Currently the files are arranged in folders with YYYY-MM-DD format only.
  - More flexibility in concurrency. Currently the number of threads are hardcoded in the code. Ideally the library, tools would let users specify the level of concurrency or supply an executor to the library instead.

