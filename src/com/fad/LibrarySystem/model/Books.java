/**
 * @author      masjohncook
 * @version     0.0.1
 * @copyright   (C) Copyright 2026
 * @license     None
 * @maintainer  masjohncook
 * @email       mas.john.cook@gmail.com
 * @status      Deprecated
 */
package com.fad.LibrarySystem.model;

/**
 * Deprecated compatibility alias for {@link Book}.
 *
 * This class existed in an earlier version of the project where the class
 * representing a single book was incorrectly named in the plural form.
 * It has been replaced by {@link Book} (singular), which follows standard
 * Java naming conventions where a class represents one instance of a concept.
 *
 * This stub is kept only to avoid breaking any external references that
 * may still use the old name. All new code must use {@link Book} instead.
 *
 * @deprecated Use {@link Book} instead.
 */
@Deprecated
public class Books extends Book {

    public Books(String bookId, String title, String author, String genre) {
        super(bookId, title, author, genre);
    }
}
