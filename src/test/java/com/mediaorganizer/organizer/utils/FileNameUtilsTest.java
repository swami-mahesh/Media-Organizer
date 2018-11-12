package com.mediaorganizer.organizer.utils;

import org.junit.Test;

import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FileNameUtilsTest {

    @Test
    public void GIVEN_valid_filename_WHEN_extension_queried_THEN_correct_extension_will_be_returned() {
        String filename = "cats.JPG";
        assertThat(FileNameUtils.getExtension(filename), is(equalTo("JPG")));
    }

    @Test
    public void GIVEN_filename_with_two_dots_WHEN_extension_queried_THEN_extension_after_last_dot_will_be_returned() {
        String filename = "cats.2011.JPG";
        assertThat(FileNameUtils.getExtension(filename), is(equalTo("JPG")));
    }

    @Test
    public void GIVEN_null_filename_WHEN_extension_queried_THEN_empty_extension_will_be_returned() {
        String filename = null;
        assertThat(FileNameUtils.getExtension(filename), is(equalTo("")));
    }

    @Test
    public void GIVEN_valid_filename_WHEN_filename_wo_extension_queried_THEN_correct_filename_will_be_returned() {
        String filename = "cats.JPG";
        assertThat(FileNameUtils.getFileNameWithoutExtension(filename), is(equalTo("cats")));
    }

    @Test
    public void GIVEN_filename_with_two_dots_WHEN_filename_wo_extension_queried_THEN_filename_before_last_dot_will_be_returned() {
        String filename = "cats.2011.JPG";
        assertThat(FileNameUtils.getFileNameWithoutExtension(filename), is(equalTo("cats.2011")));
    }

    @Test
    public void GIVEN_null_filename_WHEN_filename_wo_extension_queried_THEN_empty_filename_will_be_returned() {
        String filename = null;
        assertThat(FileNameUtils.getFileNameWithoutExtension(filename), is(equalTo("")));
    }

    @Test
    public void GIVEN_valid_filename_WHEN_copy_name_required_THEN_correct_filename_will_be_returned() {
        String filename = "cats.JPG";
        assertThat(FileNameUtils.getNameForFileCopy(Paths.get(filename)).startsWith("cats_COPY_"), is(true));
        assertThat(FileNameUtils.getNameForFileCopy(Paths.get(filename)).endsWith(".JPG"), is(true));
    }
}
